package kosta.main.bids.service;

import kosta.main.bids.dto.*;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangehistories.service.ExchangeHistoriesService;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static kosta.main.global.error.exception.CommonErrorCode.*;
import static kosta.main.items.entity.Item.IsBiding.NOT_BIDING;

@Service
@AllArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ExchangePostsRepository exchangePostsRepository;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;
    private final ExchangeHistoriesService exchangeHistoriesService; // 추가

    // 공통 메서드: 특정 ID를 가진 엔티티를 찾고, 없으면 예외를 발생시키는 메서드
    private <T> T findEntityById(JpaRepository<T, Integer> repository, Integer id, String errorMessage) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException(errorMessage));
    }

    // 공통 메서드 : 아이템 상태 및 bid 참조 업데이트 메서드
    public void updateItemsBidingStatus(List<Item> items, Item.IsBiding status, Bid bid) {

        for (Item item : items) {
            item.updateIsBiding(status);
            item.updateBid(bid); // Bid 참조 업데이트, bid가 null일 경우 참조 제거
            itemsRepository.save(item);
        }
    }

    // 공통 메서드: 'DELETED' 상태의 아이템이 있는지 확인 및 최소 하나의 아이템이 있는지 확인
    private void validateItemsForBidding(List<Item> items) {
        if (items.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_BID_REQUEST);
        }
        if (items.stream().anyMatch(item -> item.getItemStatus() == Item.ItemStatus.DELETED)) {
            throw new BusinessException(CommonErrorCode.INVALID_BIDDING_REQUEST);
        }
    }
    // 입찰 생성
    @Transactional
    public Integer createBid(User user, Integer exchangePostId, BidsDTO bidDTO) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        User bidder = user;

        // 게시글 작성자가 본인 게시글에 입찰하는 것 방지
        if (exchangePost.getUser().equals(bidder)) {
            throw new BusinessException(CommonErrorCode.MY_POST_BID);
        }

        List<Item> items = itemsRepository.findAllById(bidDTO.getItemIds());

        // 아이템 개수가 5개를 초과하는지 검증
        if (items.size() > 5) {
            throw new BusinessException(CommonErrorCode.TOO_MANY_ITEMS);
        }

        validateItemsForBidding(items); // 'DELETED' 상태의 아이템 확인 및 최소 하나의 아이템 확인

        for (Item item : items) {
            if (!item.getUser().getUserId().equals(user.getUserId())) {
                throw new BusinessException(CommonErrorCode.OTHER_ITEM_USE);
            }
            if (item.getIsBiding() == Item.IsBiding.BIDING) {
                throw new BusinessException(CommonErrorCode.ALREADY_BIDDING_ITEM);
            }
            item.updateIsBiding(Item.IsBiding.BIDING);
            itemsRepository.save(item);
        }

        Bid bid = Bid.builder()
                .user(bidder)
                .exchangePost(exchangePost)
                .status(Bid.BidStatus.BIDDING)
                .items(items)
                .build();

        // 먼저 bid를 저장하는 이유 : 현재 Item 엔터티는 bid 엔터티의 id를 참조하고 있다.
        // 만약 Bid가 생성되기전에 Item 엔터티가 Bid_id를 참조할면 Null 값이 들어가게된다. 따라서 먼저 저장을 하고 Item의 상태를 저장한다.
        Bid savedBid = bidRepository.save(bid);
        updateItemsBidingStatus(items, Item.IsBiding.BIDING, savedBid); // 미리 저장된 Bid를 이용해서 연관된 Item들의 Status를 변경

        return savedBid.getBidId();
    }


    // 특정 입찰에 대한 상세 정보 조회
    @Transactional(readOnly = true)
    public BidDetailResponseDTO findBidById(Integer bidId, User currentUser) {
        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");
        boolean isOwner = currentUser != null && bid.getUser().getUserId().equals(currentUser.getUserId());
        return BidDetailResponseDTO.of(bid, isOwner);
    }

    // 입찰 서비스 클래스 내 updateBid 메서드
    // 입찰 수정
    @Transactional
    public BidUpdateResponseDTO updateBid(User user, Integer bidId, BidUpdateDTO bidUpdateDto) {
        Bid existingBid = findEntityById(bidRepository, bidId, "Bid not found");

        // 입찰자 확인과 유저 검증
        if (!existingBid.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(CommonErrorCode.NOT_BID_OWNER);
        }

        // DB에서 현재 요청으로 날아온 Item들의 ID 리스트를 업데이트 예정의 상태로 담아둠
        List<Item> itemsToUpdate = itemsRepository.findAllById(bidUpdateDto.getItemIds());

        // 업데이트 예정에 담긴 item들을 하나씩 검증을 진행함
        validateItemsForBidding(itemsToUpdate);

        // 우선 기존에 있던 모든 Item들의 상태를 Not_Biding으로 바꾸고 bid의 참조도 모두 해제해줌
        updateItemsBidingStatus(existingBid.getItems(), NOT_BIDING, null);

        // 그 다음 업데이트 예정에 담겨있던 Item들은 확정적으로 업데이트 될것이기 때문에 BIDING으로 바꿔주고 existingBid와의 참조도 맺어줌
        updateItemsBidingStatus(itemsToUpdate, Item.IsBiding.BIDING, existingBid); // 새 아이템 상태 변경 및 bid 참조 설정

        // 실제로 현재 입찰에 Item들을 모두 업데이트 해줌
        existingBid.updateItems(itemsToUpdate);
        bidRepository.save(existingBid);

        // 모두 완수 되면 existingBid를 Builder를 통해 수정을 진행함
        return createBidUpdateResponseDTO(existingBid);
    }

    // 입찰 수정 후 반환할 DTO 생성
    private BidUpdateResponseDTO createBidUpdateResponseDTO(Bid bid) {
        List<BidUpdateResponseDTO.ItemDetails> itemDetails = bid.getItems().stream().map(item ->
                BidUpdateResponseDTO.ItemDetails.builder()
                        .itemId(item.getItemId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .imageUrls(item.getImages())
                        .build()
        ).collect(Collectors.toList());

        return BidUpdateResponseDTO.builder()
                .bidId(bid.getBidId())
                .userId(bid.getUser().getUserId())
                .exchangePostId(bid.getExchangePost().getExchangePostId())
                .status(bid.getStatus().toString())
                .itemDetails(itemDetails)
                .build();
    }


    @Transactional
    public void deleteBid(Integer bidId, User user) {
        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");

        // 입찰자 확인: 요청한 사용자가 입찰을 생성한 사용자와 동일한지 확인
        if (!bid.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(CommonErrorCode.NOT_BID_OWNER);
        }

        updateItemsBidingStatus(bid.getItems(), NOT_BIDING, null); // 아이템 상태 변경 및 bid 참조 제거
        bid.updateStatus(Bid.BidStatus.DELETED);
        bidRepository.save(bid);
    }


    // 거래 완료 로직
    @Transactional
    public void completeExchange(Integer exchangePostId, Integer selectedBidId, User user) {
//
//        //순서
//        //exchangePost가 존재하는지 확인
//        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
//        //입력받은 입찰번호와 유저가 존재하는지 확인
//        user = usersRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
//        Integer userId = user.getUserId();
//        //거래가 가능한지 권한 확인
//        if (!exchangePost.getUser().getUserId().equals(userId)) {
//            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
//        }
//        Bid selectedBid = bidRepository.findById(selectedBidId)
//                .orElseThrow(() -> new BusinessException(BID_NOT_FOUND));
//        // 입찰정보와게시글 정보를 통해 교환 내역 생성함 -> 모든 itemInfo 생성
//        ExchangeHistory exchangeHistory
//                = exchangeHistoriesService.createExchangeHistory(user, selectedBidId, exchangePostId);
//        //입찰 아이템 소유권 변경
//        transferItemOwnership(selectedBid.getItems(), exchangePost.getUser());
//        //게시글 아이템 소유권 변경
//        transferItemOwnership(List.of(exchangePost.getItem()), selectedBid.getUser());
//        // 게시글 상태 업데이트
//        exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.COMPLETED);
//        //모든 입찰의 상태 변경 -> 선택된 것은 SELECTED, 나머지는 DENIED
//        List<Bid> bids = exchangePost.getBids();
//        for (Bid bid : bids) {
//            if (bid.getBidId().equals(selectedBidId)) {
//                bid.updateStatus(Bid.BidStatus.SELECTED);
//            } else {
//                bid.updateStatus(Bid.BidStatus.DENIED);
//            }
//            //내부의 ITEM 연관관계는 전부 삭제, FINISHED ITEM으로 기존의 정보는 이관
//            List<Item> items = bid.getItems();
//            //입찰의 물건 데이터 이관
//            List<ItemInfo> list = items.stream()
//                    .map(item -> ItemInfo.from(item,exchangeHistory))
//                    .map(itemInfoRepository::save)
//                    .toList();
//            bid.exchangeFinishedItems(list);// 연관 관계가 끊어질 아이템들을 finishedItems에 저장
//            //ITEM의 ISBIDING는 다시 NOT_BIDING으로 변경
//            List<Item> updatedItems = makeItemBidIsNotBiding(items);
//            //BID 참조는 삭제
//            List<Item> updateItems = deleteItemBidreference(updatedItems);
//            updateItems.stream().forEach(bid::removeItem);
//            bidRepository.save(bid);
//        }
//        //모든 과정을 끝내고 전부 저장
//        exchangePostsRepository.save(exchangePost);
/////////////////////////////////////////////////////////////////////////////////////////////
        //우선 연관있는 테이블
        //User -> 수정사항 없음
        //ItemStatus -> FinishedExchange 추가 -> 거래내역에서사용되는 스테이터스 (쓸일있나싶긴한데일단)
        //그냥 아이템은 그대로 두고 ItemInfo 제거, 새로운 아이템을 각각 만들어서 상대방에게 각각 전달해주고
        //아이템 필드에 BeforeItem 필드 추가 -> 이전에 거래했던 Item에 대한 정보를 가지고 있음
        //입찰도 Finished 하고 땡치기
        //거래도 땡치고 -> 거래내역은 그냥 간단하게 생성
        //ExchangeHistory
        //Item
        //Bid
        //1. completeExchange(Integer exchangePostId, Integer selectedBidId, User user)

        //exchangePostId로 exchangePost 찾아오기 -> 존재 여부 확인, 예약중인지확인
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        if(!exchangePost.getExchangePostStatus().equals(ExchangePost.ExchangePostStatus.RESERVATION))
            throw new BusinessException(NOT_RESERVATED);
        // selectedBidId 찾아오기 -> exchangePostId와 selectedBid.getExchangePost().getExchangePostId()와 일치하는지 확인,
        Optional<Bid> first = exchangePost.getBids().stream().filter(b -> b.getStatus().equals(Bid.BidStatus.RESERVATION)).findFirst();
        if(first.isEmpty())
            throw new BusinessException(NOT_RESERVATED);
        Bid bid = first.get();
        if(!Objects.equals(selectedBidId, bid.getBidId()))
            throw new BusinessException(NOT_RESERVATED);

        if(!Objects.equals(bid.getExchangePost().getExchangePostId(), exchangePostId))
            throw new BusinessException(NOT_EQUAL_COMMUNITY_POST);
        // User Repository에서 새로 찾아오기
        if(user != null) {
            user = usersRepository.findById(user.getUserId()).get();
        } else throw new BusinessException(USER_NOT_FOUND);
        // exchangePost를 완료상태로 변경
        exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.COMPLETED);
        // selectedBid를 완료 상태로 변경
        bid.updateStatus(Bid.BidStatus.COMPLETED);
        // exchangePost.getItem 해서 새로운 물건 bid.getUser() 껄로 생성
        Item expItem = exchangePost.getItem();
        expItem.itemStatusUpdate(Item.ItemStatus.COMPLETED); // expUser의 Item Completed
        List<Item> bidItems = bid.getItems();
        List<Item> list = bidItems.stream().peek(i -> i.itemStatusUpdate(Item.ItemStatus.COMPLETED)).toList(); // bidItemCopleted
        User bidUser = bid.getUser();
        User expUser = exchangePost.getUser();
        ArrayList<String> expItemImages = new ArrayList<>(expItem.getImages());
        Item bidderNewItems = Item.builder()
                .user(bidUser)
                .category(expItem.getCategory())
                .title(expItem.getTitle())
                .description(expItem.getDescription())
                .images(expItemImages)
                .beforeItem(expItem) // 이전 아이템 설정
                .build();
        itemsRepository.save(bidderNewItems);
        bidderNewItems.setCreatedAt(expItem.getCreatedAt());

        // selectedBid.getItem 해서 새로운 물건 exchangePost.getUser() 껄로 생성
        List<Item> posterNewItems = bidItems.stream().map(item -> {
            ArrayList<String> bidItemImages = new ArrayList<>(item.getImages());
            Item posterNewItem = Item.builder()
                    .user(expUser)
                    .category(item.getCategory())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .images(bidItemImages)
                    .beforeItem(item) //이전 아이템 설정
                    .build();
            itemsRepository.save(posterNewItem);
            posterNewItem.setCreatedAt(item.getCreatedAt());
            return posterNewItem;
        }).toList();
        //입찰 기록을 남기기 위해 나머지 입찰의 경우 Item을 새로 생성해 줄 것인지? 아니면 그냥 기존 bid와의 연관관계를 끊어줄 것인지?
        //히스토리를 좀더 제대로 남기기 위해서 그대로 두고 새로 만들자
        exchangePost
                .getBids()
                .stream()
                .filter(b -> !b.getStatus().equals(Bid.BidStatus.COMPLETED))
                .peek(this::createBidderItems) // 입찰자들의 물건을 완료상태로 변경하고 새로운 물건을 만들어서 저장해준다.
                .peek(Bid::updateCompleteStatus) // 입찰상태를 완료상태로 변경
                .toList();
        // -> 동일한 정보의 아이템을 그냥 그대로 꽂아두고 Item의 Status는 그냥 COMPLETED로 변경처리하는게 깔끔할듯

        // exchangeHistory 생성 ->굳이 안해도 될듯

        // 거래끗
    }
    private void createBidderItems(Bid bid){
        ArrayList<Item> items = new ArrayList<>(bid.getItems());
        ArrayList<Item> newItems = new ArrayList<>();
        if(items.isEmpty()) throw new BusinessException(ITEM_NOT_FOUND);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.itemStatusUpdate(Item.ItemStatus.COMPLETED);
            ArrayList<String> images = new ArrayList<>(item.getImages());
            Item save = itemsRepository.save(Item.builder()
                    .user(bid.getUser())
                    .category(item.getCategory())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .images(images)
                    .build());
            save.setCreatedAt(item.getCreatedAt());
        }
    }


    private List<Item> deleteItemBidreference(List<Item> items) {
        List<Item> newItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.updateBid(null);
            newItems.add(item);
        }
        return newItems;
    }

    private List<Item> makeItemBidIsNotBiding(List<Item> items) {
        List<Item> newItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.updateIsBiding(NOT_BIDING);
            newItems.add(item);
        }
        return newItems;
    }


    // 거래 완료시 실제 물건의 소유주를 바꾸는 로직
    private void transferItemOwnership(List<Item> items, User newOwner) {
        for (Item item : items) {
            item.updateUser(newOwner);
            itemsRepository.save(item);
        }
    }


    // 특정 게시글에 대한 모든 입찰 조회 (DELETED 상태 제외)
    @Transactional(readOnly = true)
    public List<BidListResponseDTO> findAllBidsForPost(Integer exchangePostId, User user) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        Integer postOwnerId = exchangePost.getUser().getUserId();
        Integer currentUserId;
        if(user != null) currentUserId = user.getUserId();
        else {
            currentUserId = 0;
        }

        return bidRepository.findByExchangePost(exchangePost).stream()
//                .filter(bid -> bid.getStatus() != Bid.BidStatus.DELETED)//엔티티 애너테이션 조건으로 변경될 예정
                .map(bid -> {
                    List<BidListResponseDTO.ItemDetails> itemDetails = bid.getItems().stream()
                            .map(item -> BidListResponseDTO.ItemDetails.builder()
                                    .title(item.getTitle())
                                    .description(item.getDescription())
                                    .imgUrl(!item.getImages().isEmpty() ? item.getImages().get(0) : null)
                                    .createdAt(item.getCreatedAt())
                                    .build())
                            .collect(Collectors.toList());

                    return BidListResponseDTO.builder()
                            //.userId(bid.getUser().getUserId())
                            .isOwner(currentUserId.equals(postOwnerId))
                            .items(itemDetails)
                            .build();
                })
                .collect(Collectors.toList());
    }



    // 교환 게시글 예약하기 기능인데 BidStatus에 예약중 상태도 추가해야할듯 합니다.
    @Transactional
    public void toggleReserveExchange(Integer exchangePostId, Integer bidId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");

        // 게시글 작성자 확인
        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
        }

        Bid selectedBid = bidRepository.findById(bidId)
            .orElseThrow(() -> new BusinessException(CommonErrorCode.BID_NOT_FOUND));

        // 예약 상태 토글
        if (exchangePost.getExchangePostStatus() == ExchangePost.ExchangePostStatus.RESERVATION) {
            // 이미 예약된 경우 예약 취소
            exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.EXCHANGING);
            selectedBid.updateStatus(Bid.BidStatus.BIDDING);
        } else if (exchangePost.getExchangePostStatus() == ExchangePost.ExchangePostStatus.EXCHANGING) {
            // 예약되지 않은 경우 예약 설정
            exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.RESERVATION);
            selectedBid.updateStatus(Bid.BidStatus.RESERVATION);
        } else if( exchangePost.getExchangePostStatus() == ExchangePost.ExchangePostStatus.COMPLETED){
            // 완료 또는 삭제된 게시물인 경우
            throw new BusinessException(CommonErrorCode.FINISHED_EXCHANGE);
        } else {
            throw new BusinessException(CommonErrorCode.EXCHANGE_POST_NOT_FOUND);
        }

        exchangePostsRepository.save(exchangePost);
        bidRepository.save(selectedBid);
    }

    // 입찰 거절
    @Transactional
    public void denyBid(Integer exchangePostId, Integer bidId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
        }

        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");
        bid.updateStatus(Bid.BidStatus.DENIED);
        bidRepository.save(bid);
    }

    // 입찰 거절 취소
    @Transactional
    public void undoDenyBid(Integer exchangePostId, Integer bidId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
        }

        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");
        if (bid.getStatus() != Bid.BidStatus.DENIED) {
            throw new BusinessException(NOT_DENIED_STATUS);
        }
        bid.updateStatus(Bid.BidStatus.BIDDING);
        bidRepository.save(bid);
    }

    // 거절된 입찰 목록 조회
    @Transactional(readOnly = true)
    public List<BidListResponseDTO> findDeniedBidsForPost(Integer exchangePostId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
        }

        return bidRepository.findByExchangePostAndStatus(exchangePost, Bid.BidStatus.DENIED).stream()
            .map(bid -> BidListResponseDTO.builder()
                .isOwner(exchangePost.getUser().getUserId().equals(userId))
                .items(bid.getItems().stream().map(item ->
                        BidListResponseDTO.ItemDetails.builder()
                            .title(item.getTitle())
                            .description(item.getDescription())
                            .imgUrl(!item.getImages().isEmpty() ? item.getImages().get(0) : null)
                            .build())
                    .collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList());
    }



}
