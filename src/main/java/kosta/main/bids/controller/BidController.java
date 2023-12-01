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

    // 입찰을 수행하는 기능
    @PostMapping("/{exchangePostId}/bids") // 23.11.30 동작확인
    public ResponseEntity<Integer> createBid(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody BidsDto bidDTO) {
        return ResponseEntity.ok(bidService.createBid(exchangePostId, bidDTO));
    }

    // 한 교환게시글에 있는 입찰 목록을 모두 불러오는 기능
    @GetMapping("/{exchangePostId}/bids/{currentUserId}") // 23.11.30 동작확인
    public ResponseEntity<List<BidListResponseDTO>> getAllBidsForPost(@PathVariable("exchangePostId") Integer exchangePostId, @PathVariable("currentUserId")  Integer currentUserId) {
        return ResponseEntity.ok(bidService.findAllBidsForPost(exchangePostId, currentUserId));
    }

    // 한 입찰에 대한 상세 정보를 제공하는 기능
    @GetMapping("/bids/{bidId}") // 23.11.30 동작확인
    public ResponseEntity<BidDetailResponseDTO> getBidById(@PathVariable("bidId") Integer bidId) {
        return ResponseEntity.ok(bidService.findBidById(bidId));
    }

    // 입찰을 수정하는 기능
    @PutMapping("/bids/{bidId}")// 23.11.30 동작확인
    public ResponseEntity<BidUpdateResponseDTO> updateBid(@PathVariable("bidId") Integer bidId,
                                                          @RequestBody BidUpdateDTO bidUpdateDto) {
        return ResponseEntity.ok(bidService.updateBid(bidId, bidUpdateDto));
    }

    // 입찰을 삭제하는 기능
    @DeleteMapping("/bids/{bidId}") // 23.11.30 동작확인
    public ResponseEntity<Void> deleteBid(@PathVariable("bidId") Integer bidId, @RequestBody BidDeleteDTO bidDeleteDTO) {
        bidService.deleteBid(bidId, bidDeleteDTO);
        return ResponseEntity.ok().build();
    }

    // 거래를 완료하는 기능
    @PutMapping("/{exchangePostId}/bids/{bidId}/complete") // 23.11.30 동작확인
    public ResponseEntity<?> completeExchange(@PathVariable Integer exchangePostId,
                                              @PathVariable Integer bidId,
                                              @RequestBody BidCompleteDTO bidCompleteDto) {
        bidService.completeExchange(exchangePostId, bidId, bidCompleteDto.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{exchangePostId}/bids/{bidId}/reserve") // 23.11.30 Bid에 Status 추가 필요
    public ResponseEntity<?> reserveExchange(@PathVariable Integer exchangePostId,
                                             @PathVariable Integer bidId,
                                             @RequestBody BidReserveDTO bidReserveDto) {
        bidService.reserveExchange(exchangePostId, bidId, bidReserveDto.getUserId());
        return ResponseEntity.ok().build();
    }



}
