package kosta.main.items;

import kosta.main.bids.entity.Bid;
import kosta.main.categories.entity.Category;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.dto.*;
import kosta.main.items.entity.Item;
import kosta.main.users.UserStubData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemStubData {

    public static final int ITEM_ID = 1;
    public static final int BID_ID = 1;
    public static final int CATEGORY_ID = 1;
    public static final String CATEGORY_NAME = "카테고리이름";
    public static final String TITLE = "물건제목";
    public static final String DESCRIPTION = "물건에대한설명";
    public static final int ANOTHER_ITEM_ID = 2;
    public static final String ANOTHER_TITLE = "다른물건제목";
    public static final String ANOTHER_DESCRIPTION = "다른 내용";
    public static final Item.ItemStatus ITEM_STATUS = Item.ItemStatus.PUBLIC;

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
    public Item getAnotherNoBidItem(){
        UserStubData userStubData = new UserStubData();
        ArrayList<String> images = new ArrayList<>();
        images.add("주소1");
        images.add("주소2");
        return Item.builder()
                .itemId(ANOTHER_ITEM_ID)
                .user(userStubData.getUser())
                .category(Category.builder()
                        .categoryId(CATEGORY_ID)
                        .categoryName(CATEGORY_NAME)
                        .build())
                .title(ANOTHER_TITLE)
                .description(ANOTHER_DESCRIPTION)
                .images(images)
                .build();
    }
    public Bid getBid(){
        UserStubData userStubData = new UserStubData();
        ExchangePostStubData exchangePostStubData = new ExchangePostStubData();
        ExchangePost exchangePostNoBid = exchangePostStubData.getExchangePostNoBid();
        ArrayList<Item> items = new ArrayList<>();
        items.add(getNoBidItem());
        Bid bid = Bid.builder()
                .bidId(BID_ID)
                .user(userStubData.getUser())
                .exchangePost(exchangePostNoBid)
                .items(items)
                .build();
        exchangePostNoBid.addBid(bid);
        items.get(0).updateBid(bid);
        return bid;
    }

    public Item getBidItem(){
        return getBid().getItems().get(0);
    }

    public ItemSaveDTO getItemSaveDTO(){
        ArrayList<String> images = new ArrayList<>();
        images.add("주소1");
        images.add("주소2");
        return ItemSaveDTO.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .imageUrl(images)
                .build();
    }

    public ItemUpdateResponseDTO getItemUpdateResponseDto(){
        Item item = getNoBidItem();
        return ItemUpdateResponseDTO.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .itemStatus(item.getItemStatus())
                .images(item.getImages())
                .build();
    }

    public ItemUpdateDTO getItemUpdateDto(){
        ArrayList<String> images = new ArrayList<>();
        images.add("주소1");
        images.add("주소2");
        return ItemUpdateDTO.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .itemStatus(ITEM_STATUS)
                .images(images)
                .build();
    }
    public List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        items.add(getNoBidItem());
        items.add(getAnotherNoBidItem());
        return items;
    }

    public Page<ItemPageDTO> getItemPageDTOs(){
        List<ItemPageDTO> list = getItems().stream().map(ItemPageDTO::from).toList();

        PageRequest pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());

    }

    public ItemDetailResponseDTO getItemDetailResponse() {
        Item bidItem = getBidItem();
        return ItemDetailResponseDTO.of(bidItem);
    }

    public MockMultipartFile getMockMultipartFile() throws IOException {
        final String fileName = "testImage1"; //파일명
        final String contentType = "png"; //파일타입
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image1 = new MockMultipartFile(
                "file", //name
                fileName + "." + contentType, //originalFilename
                "image/png",
                fileInputStream
        );

        return image1;
    }
}
