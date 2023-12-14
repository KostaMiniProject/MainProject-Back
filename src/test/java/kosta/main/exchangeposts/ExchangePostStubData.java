package kosta.main.exchangeposts;

import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.dto.*;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.ItemStubData;
import kosta.main.items.entity.Item;
import kosta.main.users.UserStubData;
import kosta.main.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExchangePostStubData {


    public static final int EXCHANGE_POST_ID = 1;
    public static final String PREFER_ITEMS = "선호 물건에 대한 설명";
    public static final String ADDRESS = "만날 위치";
    public static final String CONTENT = "물물교환 게시글 내용";
    public static final String TITLE = "교환 게시글 제목";
    public static final int ITEM_ID = 1;
    public static final int ANOTHER_EXCHANGE_POST_ID = 2;
    public static final String ANOTHER_PREFERRED_ITEM = "다른 선호 물건";
    public static final String ANOTHER_ADDRESS = "다른 주소";
    public static final String ANOTHER_CONTENT = "다른 내용";
    public static final String ANOTHER_TITLE = "다른제목";
    public static final String LATITUDE = "Y좌표";
    public static final String LONGITUDE = "X좌표";

    public ExchangePost getExchangePostNoBid(){
        UserStubData userStubData = new UserStubData();
        ItemStubData itemStubData = new ItemStubData();
        return ExchangePost.builder()
                .exchangePostId(EXCHANGE_POST_ID)
                .title(TITLE)
                .user(userStubData.getUser())
                .item(itemStubData.getNoBidItem())
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .preferItems(PREFER_ITEMS)
                .address(ADDRESS)
                .content(CONTENT)
                .build();

    }

    public ExchangePost getExchangePostBid(){
        UserStubData userStubData = new UserStubData();
        ItemStubData itemStubData = new ItemStubData();
        Bid bid = itemStubData.getBid();
        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(bid);
        return ExchangePost.builder()
                .exchangePostId(EXCHANGE_POST_ID)
                .title(TITLE)
                .user(userStubData.getUser())
                .item(itemStubData.getBidItem())
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .preferItems(PREFER_ITEMS)
                .address(ADDRESS)
                .content(CONTENT)
                .bids(bids)
                .build();

    }
    public ExchangePost getAnotherExchangePostNoBid(){
        UserStubData userStubData = new UserStubData();
        ItemStubData itemStubData = new ItemStubData();
        return ExchangePost.builder()
                .exchangePostId(ANOTHER_EXCHANGE_POST_ID)
                .user(userStubData.getUser())
                .title(ANOTHER_TITLE)
                .item(itemStubData.getNoBidItem())
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .preferItems(ANOTHER_PREFERRED_ITEM)
                .address(ANOTHER_ADDRESS)
                .content(ANOTHER_CONTENT)
                .build();

    }
    public ExchangePostDTO getExchangePostDTO() {
        return ExchangePostDTO.builder()
                .title(TITLE)
                .preferItems(PREFER_ITEMS)
                .address(ADDRESS)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .content(CONTENT)
                .itemId(ITEM_ID)
                .build();
    }
    public List<ExchangePostListDTO> getExchangePostListDTO(){
        List<ExchangePostListDTO> exchangePostListDTOS = new ArrayList<ExchangePostListDTO>();
        exchangePostListDTOS.add(ExchangePostListDTO.testOf(getExchangePostNoBid(), "대표이미지1", 1));
        exchangePostListDTOS.add(ExchangePostListDTO.testOf(getAnotherExchangePostNoBid(), "대표이미지2", 2));
        return exchangePostListDTOS;
    }

    public List<ExchangePost> getExchangePosts(){
        List<ExchangePost> exchangePosts = new ArrayList<ExchangePost>();
        exchangePosts.add(getExchangePostNoBid());
        exchangePosts.add(getExchangePostBid());
        return exchangePosts;
    }
    public Page<ExchangePost> getExchangePostPages(){
        List<ExchangePost> exchangePosts = getExchangePosts();
        Pageable pageable = getPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), exchangePosts.size());
        return new PageImpl<>(exchangePosts.subList(start, end), pageable, exchangePosts.size());
    }

    public ExchangePostUpdateResponseDTO getExchangePostUpdateResponseDTO(){
        ExchangePost exchangePostBid = getExchangePostBid();
        return ExchangePostUpdateResponseDTO.from(exchangePostBid);
    }

    public Page<ExchangePostListDTO> getExchangePostListDTOPage() {
        List<ExchangePostListDTO> exchangePostListDTO = getExchangePostListDTO();
        // 요청으로 들어온 page와 한 page당 원하는 데이터의 갯수
        PageRequest pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), exchangePostListDTO.size());
        return new PageImpl<>(exchangePostListDTO.subList(start, end), pageRequest, exchangePostListDTO.size());
    }

    public ResponseDto getResponseDto() {
        return ResponseDto.of(EXCHANGE_POST_ID);
    }


    public Pageable getPageable() {
        return PageRequest.of(0, 10);

    }

    public ExchangePostDetailDTO getExchangePostDetailDTO() {
        ExchangePost exchangePostBid = getExchangePostBid();
        User user = exchangePostBid.getUser();
        Item item = exchangePostBid.getItem();
        List<Bid> bids = exchangePostBid.getBids();
        return ExchangePostDetailDTO.builder()
                .postOwner(true)
                .title(exchangePostBid.getTitle())
                .preferItems(exchangePostBid.getPreferItems())
                .address(exchangePostBid.getAddress())
                .content(exchangePostBid.getContent())
                .profile(ExchangePostDetailDTO.UserProfile.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .address(user.getAddress())
                        .imageUrl(user.getProfileImage())
                        .rating(user.getRating()).build())
                .item(ExchangePostDetailDTO.ItemDetails.builder()
                        .itemId(item.getItemId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .imageUrls(item.getImages()).build())
                .bidList(bids.stream()
                        .map(bid ->
                                ExchangePostDetailDTO.BidDetails.builder()
                                        .bidId(bid.getBidId())
                                        .name(bid.getUser().getName())
                                        .imageUrl(bid.getUser().getProfileImage())
                                        .items(Arrays.toString(bid.getItems().stream().map(Item::getTitle).toArray()).replace("[","").replace("]",""))
                                        .build()
                        ).toList())
                .build();
    }
}
