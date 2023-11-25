package kosta.main.bids.service;

import kosta.main.bids.dto.BidsDto;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.bids.entity.Bid.BidStatus;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final UsersRepository usersRepository;
    private final ExchangePostsRepository exchangePostsRepository;
    private final ItemsRepository itemsRepository;

    @Autowired
    public BidService(BidRepository bidRepository, UsersRepository usersRepository,
                      ExchangePostsRepository exchangePostsRepository, ItemsRepository itemsRepository) {
        this.bidRepository = bidRepository;
        this.usersRepository = usersRepository;
        this.exchangePostsRepository = exchangePostsRepository;
        this.itemsRepository = itemsRepository;
    }

    public Bid createBid(BidsDto bidDTO) {
        User user = usersRepository.findById(bidDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ExchangePost exchangePost = exchangePostsRepository.findById(bidDTO.getExchangePostId())
                .orElseThrow(() -> new RuntimeException("ExchangePost not found"));

        List<Item> items = bidDTO.getItemIds().stream()
                .map(itemId -> itemsRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Item not found")))
                .collect(Collectors.toList());

        Bid bid = Bid.builder()
                .user(user)
                .exchangePost(exchangePost)
                .status(bidDTO.getStatus())
                .items(items)
                .build();
        // Item 상태 업데이트
        items.forEach(item -> item.setIsBiding(Item.IsBiding.BIDING));
        return bidRepository.save(bid);
    }

    public List<Bid> findAllBids() {
        return bidRepository.findAll();
    }

    public Bid findBidById(Integer bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new RuntimeException("Bid not found"));
    }

    public Bid updateBid(Integer bidId, BidsDto bidDTO) {
        Bid existingBid = findBidById(bidId);
        if (existingBid == null) {
            throw new RuntimeException("Bid not found");
        }

        // User 객체 가져오기 (필요한 경우)
        User user = null;
        if (bidDTO.getUserId() != null) {
            user = usersRepository.findById(bidDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            user = existingBid.getUser(); // 기존 user 유지
        }

        // ExchangePost 객체 가져오기 (필요한 경우)
        ExchangePost exchangePost = null;
        if (bidDTO.getExchangePostId() != null) {
            exchangePost = exchangePostsRepository.findById(bidDTO.getExchangePostId())
                    .orElseThrow(() -> new RuntimeException("ExchangePost not found"));
        } else {
            exchangePost = existingBid.getExchangePost(); // 기존 exchangePost 유지
        }

        // Item 리스트 업데이트 (필요한 경우)
        List<Item> items = new ArrayList<>();
        if (bidDTO.getItemIds() != null && !bidDTO.getItemIds().isEmpty()) {
            items = bidDTO.getItemIds().stream()
                    .map(itemId -> itemsRepository.findById(itemId)
                            .orElseThrow(() -> new RuntimeException("Item not found")))
                    .collect(Collectors.toList());
        } else {
            items = existingBid.getItems(); // 기존 items 유지
        }

        // BidStatus 업데이트
        BidStatus status = bidDTO.getStatus() != null ? bidDTO.getStatus() : existingBid.getStatus();

        // Bid 엔터티 업데이트
        existingBid.updateBid(user, exchangePost, status, items);

        return bidRepository.save(existingBid);
    }

    public void deleteBid(Integer bidId) {
        bidRepository.deleteById(bidId);
    }
}
