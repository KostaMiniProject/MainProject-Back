package kosta.main.bids.controller;

import jakarta.validation.constraints.Max;
import kosta.main.bids.dto.*;
import kosta.main.bids.service.BidService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Integer> createBid(@LoginUser User user, @PathVariable("exchangePostId") Integer exchangePostId, @RequestBody BidsDto bidDTO) {
        return ResponseEntity.ok(bidService.createBid(user, exchangePostId, bidDTO));
    }

    // 한 교환게시글에 있는 입찰 목록을 모두 불러오는 기능
    @GetMapping("/{exchangePostId}/bids/{currentUserId}") // 23.11.30 동작확인
    public ResponseEntity<List<BidListResponseDTO>> getAllBidsForPost(@PathVariable("exchangePostId") Integer exchangePostId, @PathVariable("currentUserId")  Integer currentUserId) {
        return ResponseEntity.ok(bidService.findAllBidsForPost(exchangePostId, currentUserId));
    }

    // 한 입찰에 대한 상세 정보를 제공하는 기능
    @GetMapping("/bids/{bidId}") // 23.11.30 동작확인
    public ResponseEntity<BidDetailResponseDTO> getBidById(@PathVariable("bidId") Integer bidId, @LoginUser User user) {
        return ResponseEntity.ok(bidService.findBidById(bidId,user));
    }

    // 입찰을 수정하는 기능
    @PutMapping("/bids/{bidId}")// 23.11.30 동작확인
    public ResponseEntity<BidUpdateResponseDTO> updateBid(@LoginUser User user, @PathVariable("bidId") Integer bidId,
                                                          @RequestBody BidUpdateDTO bidUpdateDto) {
        return ResponseEntity.ok(bidService.updateBid(user, bidId, bidUpdateDto));
    }

    // 입찰을 삭제하는 기능
    @DeleteMapping("/bids/{bidId}") // 23.11.30 동작확인
    public ResponseEntity<?> deleteBid(@PathVariable("bidId") Integer bidId, @LoginUser User user) {
        bidService.deleteBid(bidId, user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    // 거래를 완료하는 기능
    @PutMapping("/{exchangePostId}/bids/{bidId}/complete") // 23.11.30 동작확인
    public ResponseEntity<?> completeExchange(@PathVariable Integer exchangePostId,
                                              @PathVariable Integer bidId,
                                              @LoginUser User user) {
        bidService.completeExchange(exchangePostId, bidId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{exchangePostId}/bids/{bidId}/reserve") // 23.12.02 동작확인
    public ResponseEntity<?> reserveExchange(@PathVariable Integer exchangePostId,
                                             @PathVariable Integer bidId,
                                             @LoginUser User user) {
        bidService.toggleReserveExchange(exchangePostId, bidId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    // 입찰 거절
    @PutMapping("/{exchangePostId}/bids/{bidId}/deny")
    public ResponseEntity<?> denyBid(@PathVariable Integer exchangePostId,
                                     @PathVariable Integer bidId,
                                     @LoginUser User user) {
        bidService.denyBid(exchangePostId, bidId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    // 입찰 거절 취소
    @PutMapping("/{exchangePostId}/bids/{bidId}/undo-deny")
    public ResponseEntity<?> undoDenyBid(@PathVariable Integer exchangePostId,
                                         @PathVariable Integer bidId,
                                         @LoginUser User user) {
        bidService.undoDenyBid(exchangePostId, bidId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    // 거절된 입찰 목록 조회
    @GetMapping("/{exchangePostId}/bids/denied")
    public ResponseEntity<List<BidListResponseDTO>> getDeniedBidsForPost(@PathVariable Integer exchangePostId,
                                                                         @LoginUser User user) {
        List<BidListResponseDTO> deniedBids = bidService.findDeniedBidsForPost(exchangePostId, user.getUserId());
        return ResponseEntity.ok(deniedBids);
    }



}
