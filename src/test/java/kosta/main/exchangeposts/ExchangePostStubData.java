package kosta.main.exchangeposts;

import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostDetailDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.ItemStubData;
import kosta.main.users.UserStubData;

import java.util.ArrayList;
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

    public ExchangePost getExchangePostNoBid(){
        UserStubData userStubData = new UserStubData();
        ItemStubData itemStubData = new ItemStubData();
        return ExchangePost.builder()
                .exchangePostId(EXCHANGE_POST_ID)
                .user(userStubData.getUser())
                .item(itemStubData.getNoBidItem())
                .preferItems(PREFER_ITEMS)
                .address(ADDRESS)
                .content(CONTENT)
                .build();

    }
    public ExchangePost getAnotherExchangePostNoBid(){
        UserStubData userStubData = new UserStubData();
        ItemStubData itemStubData = new ItemStubData();
        return ExchangePost.builder()
                .exchangePostId(ANOTHER_EXCHANGE_POST_ID)
                .user(userStubData.getUser())
                .item(itemStubData.getNoBidItem())
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
                .content(CONTENT)
                .itemId(ITEM_ID)
                .build();
    }
    public List<ExchangePostListDTO> getExchangePostListDTO(){
        List<ExchangePostListDTO> exchangePostListDTOS = new ArrayList<ExchangePostListDTO>();
        exchangePostListDTOS.add(ExchangePostListDTO.of(getExchangePostNoBid(), "대표이미지1", 1));
        exchangePostListDTOS.add(ExchangePostListDTO.of(getAnotherExchangePostNoBid(), "대표이미지2", 2));
        return exchangePostListDTOS;
    }

}
