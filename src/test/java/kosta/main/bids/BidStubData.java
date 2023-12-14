package kosta.main.bids;

import kosta.main.bids.dto.*;
import kosta.main.bids.entity.Bid;
import kosta.main.items.ItemStubData;
import kosta.main.items.entity.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BidStubData {
    public static final int ITEM_ID = 1;
    public static final int ANOTHER_ITEM_ID = 2;
    public static final int USER_ID = 1;

    //itemStubData에 bid가 존재

    public BidsDTO getBidsDTO(){
        ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
        integerArrayList.add(ITEM_ID);
        integerArrayList.add(ANOTHER_ITEM_ID);
        return BidsDTO.builder()
                .itemIds(integerArrayList
                ).userId(USER_ID).build();
    }

    public BidListResponseDTO getBidListResponseDTO() {
        ItemStubData itemStubData = new ItemStubData();
        ArrayList<BidListResponseDTO.ItemDetails> itemDetails = new ArrayList<>();
        Item bidItem = itemStubData.getBidItem();
        Item noBidItem = itemStubData.getNoBidItem();

        BidListResponseDTO.ItemDetails item1 = BidListResponseDTO.ItemDetails.builder()
                .title(bidItem.getTitle())
                .description(bidItem.getDescription())
                .imgUrl(bidItem.getImages().get(0))
                .createdAt(LocalDateTime.now())
                .build();

        BidListResponseDTO.ItemDetails item2 = BidListResponseDTO.ItemDetails.builder()
                .title(noBidItem.getTitle())
                .description(noBidItem.getDescription())
                .imgUrl(noBidItem.getImages().get(0))
                .createdAt(LocalDateTime.now())
                .build();


        itemDetails.add(item1);
        itemDetails.add(item2);
        return BidListResponseDTO.builder()
                .items(itemDetails)
                .isOwner(true)
                .build();
    }

    public BidDetailResponseDTO getBidDetailResponseDTO() {
        ItemStubData itemStubData = new ItemStubData();
        Bid bid = itemStubData.getBid();
        return BidDetailResponseDTO.of(bid, true);
    }

    public BidUpdateResponseDTO getBidUpdateResponseDTO() {
        ItemStubData itemStubData = new ItemStubData();
        Item bidItem = itemStubData.getBidItem();
        Item noBidItem = itemStubData.getNoBidItem();

        BidUpdateResponseDTO.ItemDetails item1 = BidUpdateResponseDTO.ItemDetails.builder()
                .itemId(bidItem.getItemId())
                .title(bidItem.getTitle())
                .description(bidItem.getDescription())
                .imageUrls(bidItem.getImages())
                .build();

        BidUpdateResponseDTO.ItemDetails item2 = BidUpdateResponseDTO.ItemDetails.builder()
                .itemId(noBidItem.getItemId())
                .title(noBidItem.getTitle())
                .description(noBidItem.getDescription())
                .imageUrls(noBidItem.getImages())
                .build();

        ArrayList<BidUpdateResponseDTO.ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(item1);
        itemDetails.add(item2);
        Bid bid = itemStubData.getBid();
        return BidUpdateResponseDTO.builder()
                .bidId(bid.getBidId())
                .userId(bid.getUser().getUserId())
                .exchangePostId(bid.getExchangePost().getExchangePostId())
                .status(bid.getStatus().toString())
                .itemDetails(itemDetails)
                .build();
    }

    public BidUpdateDTO getBidUpdateDTO() {
        List<Integer> itemIds = new ArrayList<>();
        itemIds.add(1);
        itemIds.add(2);
        return BidUpdateDTO.builder()
                .itemIds(itemIds)
                .build();
    }
}
