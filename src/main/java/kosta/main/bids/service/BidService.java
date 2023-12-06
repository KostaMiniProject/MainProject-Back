package kosta.main.bids.service;

import kosta.main.bids.dto.*;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.ErrorCode;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static kosta.main.global.error.exception.ErrorCode.NOT_DENIED_STATUS;
import static kosta.main.global.error.exception.ErrorCode.NOT_EXCHANGE_POST_OWNER;

@Service
@AllArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ExchangePostsRepository exchangePostsRepository;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;

    // 공통 메서드: 특정 ID를 가진 엔티티를 찾고, 없으면 예외를 발생시키는 메서드
    private <T> T findEntityById(JpaRepository<T, Integer> repository, Integer id, String errorMessage) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException(errorMessage));
    }

    // 공통 메서드 : 아이템 상태 및 bid 참조 업데이트 메서드
    private void updateItemsBidingStatus(List<Item> items, Item.IsBiding status, Bid bid) {
        for (Item item : items) {
            item.updateIsBiding(status);
            item.updateBid(bid); // Bid 참조 업데이트, bid가 null일 경우 참조 제거
            itemsRepository.save(item);
        }
    }

    // 공통 메서드: 'DELETED' 상태의 아이템이 있는지 확인 및 최소 하나의 아이템이 있는지 확인
    private void validateItemsForBidding(List<Item> items) {
        if (items.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_BID_REQUEST);
        }
        if (items.stream().anyMatch(item -> item.getItemStatus() == Item.ItemStatus.DELETED)) {
            throw new BusinessException(ErrorCode.INVALID_BIDDING_REQUEST);
        }
    }
    // 입찰 생성
    @Transactional
    public Integer createBid(User user, Integer exchangePostId, BidsDTO bidDTO) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        User bidder = user;

        // 게시글 작성자가 본인 게시글에 입찰하는 것 방지
        if (exchangePost.getUser().equals(bidder)) {
            throw new BusinessException(ErrorCode.MY_POST_BID);
        }

        List<Item> items = itemsRepository.findAllById(bidDTO.getItemIds());
        validateItemsForBidding(items); // 'DELETED' 상태의 아이템 확인 및 최소 하나의 아이템 확인

        for (Item item : items) {
            if (!item.getUser().getUserId().equals(user.getUserId())) {
                throw new BusinessException(ErrorCode.OTHER_ITEM_USE);
            }
            if (item.getIsBiding() == Item.IsBiding.BIDING) {
                throw new BusinessException(ErrorCode.ALREADY_BIDDING_ITEM);
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
    public BidDetailResponseDTO findBidById(Integer bidId, Integer exchangePostId, User currentUser) {
        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "Post Not Found");
        boolean isOwner = currentUser != null && exchangePost.getUser().getUserId().equals(currentUser.getUserId());
        // 입찰자 정보 저장
        User bidder = bid.getUser();

        // 입찰에 사용된 아이템 상세 정보
        List<BidDetailResponseDTO.ItemDetails> itemDetails = bid.getItems().stream().map(item ->
                BidDetailResponseDTO.ItemDetails.builder()
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .imageUrls(item.getImages())
                        .build()
        ).collect(Collectors.toList());

        return BidDetailResponseDTO.builder()
                .isOwner(isOwner)
                //.bidId(bid.getBidId())
                //.bidderId(bidder.getUserId())
                .bidderNickname(bidder.getName())
                .bidderAddress(bidder.getAddress())
                .items(itemDetails)
                .build();
    }

    // 입찰 서비스 클래스 내 updateBid 메서드
    // 입찰 수정
    @Transactional
    public BidUpdateResponseDTO updateBid(User user, Integer bidId, BidUpdateDTO bidUpdateDto) {
        Bid existingBid = findEntityById(bidRepository, bidId, "Bid not found");

        // 입찰자 확인과 유저 검증
        if (!existingBid.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(ErrorCode.NOT_BID_OWNER);
        }

        // DB에서 현재 요청으로 날아온 Item들의 ID 리스트를 업데이트 예정의 상태로 담아둠
        List<Item> itemsToUpdate = itemsRepository.findAllById(bidUpdateDto.getItemIds());

        // 업데이트 예정에 담긴 item들을 하나씩 검증을 진행함
        validateItemsForBidding(itemsToUpdate);

        // 우선 기존에 있던 모든 Item들의 상태를 Not_Biding으로 바꾸고 bid의 참조도 모두 해제해줌
        updateItemsBidingStatus(existingBid.getItems(), Item.IsBiding.NOT_BIDING, null);

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
            throw new BusinessException(ErrorCode.NOT_BID_OWNER);
        }

        updateItemsBidingStatus(bid.getItems(), Item.IsBiding.NOT_BIDING, null); // 아이템 상태 변경 및 bid 참조 제거
        bid.updateStatus(Bid.BidStatus.DELETED);
        bidRepository.save(bid);
    }


    // 거래 완료 로직
    @Transactional
    public void completeExchange(Integer exchangePostId, Integer selectedBidId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");

        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
        }

        List<Bid> bids = bidRepository.findByExchangePost(exchangePost);
        for (Bid bid : bids) {
            if (bid.getBidId().equals(selectedBidId)) {
                bid.updateStatus(Bid.BidStatus.SELECTED);
                transferItemOwnership(bid.getItems(), exchangePost.getUser()); // 선택된 입찰의 아이템 소유권 변경
            } else {
                bid.updateStatus(Bid.BidStatus.DENIED);
            }
            updateItemsBidingStatus(bid.getItems(), Item.IsBiding.NOT_BIDING, null);
            bidRepository.save(bid);
        }
        Bid selectedBid = bidRepository.findById(selectedBidId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BID_NOT_FOUND));
        transferItemOwnership(List.of(exchangePost.getItem()), selectedBid.getUser()); // 게시글의 아이템 소유권 변경
        exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.COMPLETED);
        exchangePostsRepository.save(exchangePost);
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
    public List<BidListResponseDTO> findAllBidsForPost(Integer exchangePostId, Integer currentUserId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        Integer postOwnerId = exchangePost.getUser().getUserId();

        return bidRepository.findByExchangePost(exchangePost).stream()
                .filter(bid -> bid.getStatus() != Bid.BidStatus.DELETED)
                .map(bid -> {
                    List<BidListResponseDTO.ItemDetails> itemDetails = bid.getItems().stream()
                            .map(item -> BidListResponseDTO.ItemDetails.builder()
                                    .title(item.getTitle())
                                    .description(item.getDescription())
                                    .imgUrl(!item.getImages().isEmpty() ? item.getImages().get(0) : null)
                                    .created_at(item.getCreatedAt())
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
            .orElseThrow(() -> new BusinessException(ErrorCode.BID_NOT_FOUND));

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
            throw new BusinessException(ErrorCode.FINISHED_EXCHANGE);
        } else {
            throw new BusinessException(ErrorCode.EXCHANGE_POST_NOT_FOUND);
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
