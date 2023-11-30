package kosta.main.bids.controller;

import kosta.main.bids.dto.*;
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

    @PostMapping("/{exchangePostId}/bids") // 23.11.29 동작확인
    public ResponseEntity<?> createBid(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody BidsDto bidDTO) {
        return ResponseEntity.ok(bidService.createBid(exchangePostId, bidDTO));
    }

    @GetMapping("/{exchangePostId}/bids") // 23.11.29 동작확인
    public ResponseEntity<List<BidListDTO>> getAllBidsForPost(@PathVariable("exchangePostId") Integer exchangePostId) {
        return ResponseEntity.ok(bidService.findAllBidsForPost(exchangePostId));
    }

    @GetMapping("/bids/{bidId}") // 23.11.29 동작확인
    public ResponseEntity<BidDetailResponseDTO> getBidById(@PathVariable("bidId") Integer bidId) {
        return ResponseEntity.ok(bidService.findBidById(bidId));
    }

    // 입찰 정보 수정 API
    @PutMapping("/bids/{bidId}")// 23.11.29 동작확인
    public ResponseEntity<BidUpdateResponseDTO> updateBid(@PathVariable("bidId") Integer bidId,
                                                          @RequestBody BidUpdateDTO bidUpdateDto) {
        return ResponseEntity.ok(bidService.updateBid(bidId, bidUpdateDto));
    }

    @DeleteMapping("/bids/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable("bidId") Integer bidId) {
        bidService.deleteBid(bidId);
        return ResponseEntity.ok().build();
    }

    // 거래 완료 API
    // 생각해보니 채팅에서도 거래완료가 가능하니까 PathVariable대신 DTO에 담는 방법도 다시 생각 해봐야할 듯합니다.
    @PutMapping("/{exchangePostId}/bids/{bidId}/complete")
    public ResponseEntity<?> completeExchange(@PathVariable Integer exchangePostId,
                                              @PathVariable Integer bidId,
                                              @RequestBody BidCompleteDTO bidCompleteDto) {
        bidService.completeExchange(exchangePostId, bidId, bidCompleteDto.getUserId());
        return ResponseEntity.ok().build();
    }

}
