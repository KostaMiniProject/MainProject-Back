package kosta.main.bids.controller;

import kosta.main.bids.dto.BidResponseDTO;
import kosta.main.bids.dto.BidsDto;
import kosta.main.bids.dto.BidListDTO;
import kosta.main.bids.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-posts")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/{exchangePostId}/bids")
    public ResponseEntity<BidResponseDTO> createBid(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody BidsDto bidDTO) {
        return ResponseEntity.ok(bidService.createBid(bidDTO));
    }

    @GetMapping("/{exchangePostId}/bids")
    public ResponseEntity<List<BidListDTO>> getAllBidsForPost(@PathVariable("exchangePostId") Integer exchangePostId) {
        return ResponseEntity.ok(bidService.findAllBidsForPost(exchangePostId));
    }

    @GetMapping("/bids/{bidId}")
    public ResponseEntity<BidResponseDTO> getBidById(@PathVariable("bidId") Integer bidId) {
        return ResponseEntity.ok(bidService.findBidById(bidId));
    }

    @PutMapping("/bids/{bidId}")
    public ResponseEntity<BidResponseDTO> updateBid(@PathVariable("bidId") Integer bidId, @RequestBody BidsDto bidDTO) {
        return ResponseEntity.ok(bidService.updateBid(bidId, bidDTO));
    }

    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable("bidId") Integer bidId) {
        bidService.deleteBid(bidId);
        return ResponseEntity.ok().build();
    }
}
