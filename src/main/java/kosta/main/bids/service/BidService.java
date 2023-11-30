package kosta.main.bids.service;

import kosta.main.bids.dto.*;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kosta.main.images.entity.Image;

import java.util.List;
import java.util.stream.Collectors;

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

    // 공통 메서드 : 아이템 상태를 업데이트하는 메서드
    private void updateItemsBidingStatus(List<Item> items, Item.IsBiding status) {
        for (Item item : items) {
            item.updateIsBiding(status);
            itemsRepository.save(item);
        }
    }

    // 입찰 생성
    @Transactional
    public Integer createBid(Integer exchangePostId, BidsDto bidDTO) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");
        User bidder = findEntityById(usersRepository, bidDTO.getUserId(), "User not found");

        if (exchangePost.getUser().equals(bidder)) {
            throw new RuntimeException("Post owner cannot bid on their own post");
        }

        List<Item> items = itemsRepository.findAllById(bidDTO.getItemIds());
        for (Item item : items) {
            if (!item.getUser().getUserId().equals(bidDTO.getUserId())) {
                throw new RuntimeException("You can only bid with your own items");
            }
            if (item.getIsBiding() == Item.IsBiding.BIDING) {
                throw new RuntimeException("Item is already in bidding");
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

        Bid savedBid = bidRepository.save(bid);

        items.forEach(item -> {
            item.updateBid(savedBid);
            itemsRepository.save(item);
        });

        return savedBid.getBidId();
    }


    // 특정 입찰에 대한 상세정보를 제공하는 메서드
    @Transactional(readOnly = true)
    public BidDetailResponseDTO findBidById(Integer bidId) {
        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");

        // 입찰자의 정보를 bidder에 담아줌
        User bidder = bid.getUser();

        // 입찰에 사용된 Item에서 필요한 정보를 빌더로 생성
        List<BidDetailResponseDTO.ItemDetails> itemDetails = bid.getItems().stream().map(item ->
            BidDetailResponseDTO.ItemDetails.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .imageUrls(item.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

        // DTO 생성 및 반환
        return BidDetailResponseDTO.builder()
            .bidId(bid.getBidId())
            .bidderId(bidder.getUserId())
            .bidderNickname(bidder.getName())
            .bidderProfileImageUrl(bidder.getProfileImage() != null ? bidder.getProfileImage().getImageUrl() : null)
            .bidderAddress(bidder.getAddress())
            .items(itemDetails)
            .build();
    }

    // 입찰 서비스 클래스 내 updateBid 메서드
    // 입찰 수정
    @Transactional
    public BidUpdateResponseDTO updateBid(Integer bidId, BidUpdateDTO bidUpdateDto) {
        Bid existingBid = findEntityById(bidRepository, bidId, "Bid not found");

        if (!existingBid.getUser().getUserId().equals(bidUpdateDto.getUserId())) {
            throw new RuntimeException("Only the bid creator can update the bid");
        }

        List<Item> itemsToUpdate = itemsRepository.findAllById(bidUpdateDto.getItemIds());
        for (Item item : itemsToUpdate) {
            if (!item.getUser().getUserId().equals(bidUpdateDto.getUserId())) {
                throw new RuntimeException("You can only update bid with your own items");
            }
        }

        existingBid.getItems().forEach(item -> {
            item.updateIsBiding(Item.IsBiding.NOT_BIDING);
            item.updateBid(null);
            itemsRepository.save(item);
        });

        itemsToUpdate.forEach(item -> {
            item.updateIsBiding(Item.IsBiding.BIDING);
            item.updateBid(existingBid);
            itemsRepository.save(item);
        });

        existingBid.updateItems(itemsToUpdate);

        bidRepository.save(existingBid);

        return createBidUpdateResponseDTO(existingBid);
    }
    //입찰 수정이 완료된 뒤 값을 반환하는 메서드
    private BidUpdateResponseDTO createBidUpdateResponseDTO(Bid bid) {
        List<BidUpdateResponseDTO.ItemDetails> itemDetails = bid.getItems().stream().map(item ->
            BidUpdateResponseDTO.ItemDetails.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .description(item.getDescription())
                .imageUrls(item.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList()))
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


    // 입찰 삭제
    @Transactional
    public void deleteBid(Integer bidId) {
        Bid bid = findEntityById(bidRepository, bidId, "Bid not found");
        // 기존 아이템들의 상태를 NOT_BIDING으로 변경하고 bid 참조를 제거합니다.
        bid.getItems().forEach(item -> {
            item.updateIsBiding(Item.IsBiding.NOT_BIDING);
            item.updateBid(null); // bid 참조 제거
            itemsRepository.save(item);
        });

        bid.updateStatus(Bid.BidStatus.DELETED);
        bidRepository.save(bid);
    }

    // 거래 완료 처리
    @Transactional
    public void completeExchange(Integer exchangePostId, Integer selectedBidId, Integer userId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");

        // 게시글 작성자만 거래를 완료할 수 있도록 확인
        if (!exchangePost.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Only the post owner can complete the exchange");
        }

        // 관련된 모든 입찰을 조회
        List<Bid> bids = bidRepository.findByExchangePost(exchangePost);

        // 입찰들의 상태 업데이트
        for (Bid bid : bids) {
            if (bid.getBidId().equals(selectedBidId)) {
                bid.updateStatus(Bid.BidStatus.SELECTED);
                updateItemsBidingStatus(bid.getItems(), Item.IsBiding.NOT_BIDING);
            } else {
                bid.updateStatus(Bid.BidStatus.DENIED);
                updateItemsBidingStatus(bid.getItems(), Item.IsBiding.NOT_BIDING);
            }
            bidRepository.save(bid);
        }

        // 게시글의 상태를 '완료됨'으로 업데이트
        exchangePost.updateExchangePostStatus(ExchangePost.ExchangePostStatus.COMPLETED);
        exchangePostsRepository.save(exchangePost);
    }


    // 특정 게시글에 대한 모든 입찰 조회
    @Transactional(readOnly = true)
    public List<BidListDTO> findAllBidsForPost(Integer exchangePostId) {
        ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");

        return bidRepository.findByExchangePost(exchangePost).stream()
            .map(bid -> BidListDTO.builder()
                .bidId(bid.getBidId())
                .userId(bid.getUser().getUserId())
                .exchangePostId(bid.getExchangePost().getExchangePostId())
                .status(bid.getStatus())
                .itemIds(bid.getItems().stream().map(Item::getItemId).collect(Collectors.toList()))
                .build())
            .collect(Collectors.toList());
    }
}
