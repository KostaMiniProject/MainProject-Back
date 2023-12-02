package kosta.main.exchangeposts;

import kosta.main.bids.entity.Bid;
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
}
