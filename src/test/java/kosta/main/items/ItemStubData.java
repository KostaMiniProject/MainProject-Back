package kosta.main.items;

import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.items.entity.Item;
import kosta.main.users.UserStubData;

import java.util.ArrayList;

public class ItemStubData {

    public static final int ITEM_ID = 1;
    public static final int BID_ID = 1;
    public static final int CATEGORY_ID = 1;
    public static final String CATEGORY_NAME = "카테고리이름";
    public static final String TITLE = "물건제목";
    public static final String DESCRIPTION = "물건에대한설명";

    public Item getNoBidItem(){
        UserStubData userStubData = new UserStubData();
        ArrayList<String> images = new ArrayList<>();
        images.add("주소1");
        images.add("주소2");
        return Item.builder()
                .itemId(ITEM_ID)
                .user(userStubData.getUser())
                .category(Category.builder()
                        .categoryId(CATEGORY_ID)
                        .categoryName(CATEGORY_NAME)
                        .build())
                .title(TITLE)
                .description(DESCRIPTION)
                .images(images)
                .build();
    }
    public Bid getBid(){
        UserStubData userStubData = new UserStubData();
        ExchangePostStubData exchangePostStubData = new ExchangePostStubData();

        Bid.builder()
                .bidId(BID_ID)
                .user(userStubData.getUser())
                .
    }
}
