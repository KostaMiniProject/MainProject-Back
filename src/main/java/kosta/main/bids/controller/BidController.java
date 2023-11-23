package kosta.main.bids.controller;

import kosta.main.bids.dto.BidsDto;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-posts")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/{exchangePostId}/bids")
    public ResponseEntity<Bid> createBid(@RequestBody BidsDto bidDTO) {
        Bid bid = bidService.createBid(bidDTO);
        return ResponseEntity.ok(bid);
    }

    @GetMapping("/{exchangePostId}/bids")
    public List<Bid> getAllBids() {
        return bidService.findAllBids();
    }

    @GetMapping("/bids/{bidId}")
    public ResponseEntity<Bid> getBidById(@PathVariable Integer bidId) {
        Bid bid = bidService.findBidById(bidId);
        return ResponseEntity.ok(bid);
    }

    @PutMapping("/bids/{bidId}")
    public ResponseEntity<Bid> updateBid(@PathVariable Integer bidId, @RequestBody BidsDto bidDTO) {
        Bid updatedBid = bidService.updateBid(bidId, bidDTO);
        return ResponseEntity.ok(updatedBid);
    }

    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Integer bidId) {
        bidService.deleteBid(bidId);
        return ResponseEntity.ok().build();
    }
}
