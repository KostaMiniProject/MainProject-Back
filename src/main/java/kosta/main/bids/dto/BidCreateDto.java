package kosta.main.bids.dto;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.entity.Bid.BidStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class BidCreateDto {
    private Integer userId;
    private Integer exchangePostId;
    private List<Integer> itemIds;
    private Bid.BidStatus status;
}