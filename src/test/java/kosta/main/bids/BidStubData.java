package kosta.main.bids;

import kosta.main.bids.dto.BidsDTO;

import java.util.ArrayList;
import java.util.List;

public class BidStubData {
    public static final int ITEM_ID = 1;
    public static final int ANOTHER_ITEM_ID = 2;

    //itemStubData에 bid가 존재

    public BidsDTO getBidsDTO(){
        ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
        integerArrayList.add(ITEM_ID);
        integerArrayList.add(ANOTHER_ITEM_ID);
        return BidsDTO.builder()
                .itemIds(integerArrayList
                ).userId(1).build();
    }
}
