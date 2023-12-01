package kosta.main.dibs.controller;

import kosta.main.dibs.dto.DibRequestDto;
import kosta.main.dibs.service.DibsService;
import kosta.main.exchangeposts.entity.ExchangePost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange-posts")
public class DibsController {

    private final DibsService dibsService;

    @PostMapping("/{exchangePostId}/dibs") // 23.11.30 동작확인
    public ResponseEntity<?> toggleDib(@PathVariable("exchangePostId")Integer exchangePostId, @RequestBody DibRequestDto dibRequestDto) {
        dibsService.toggleDib(dibRequestDto.getUserId(), exchangePostId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dibs/{userId}")
    public ResponseEntity<List<ExchangePost>> getUserDibs(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(dibsService.getUserDibs(userId));
    }
}
