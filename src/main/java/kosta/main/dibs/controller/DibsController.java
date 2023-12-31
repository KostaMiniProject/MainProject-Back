package kosta.main.dibs.controller;

import kosta.main.dibs.dto.DibbedExchangePostDTO;
import kosta.main.dibs.service.DibsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
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
    public ResponseEntity<?> toggleDib(@PathVariable("exchangePostId")Integer exchangePostId, @LoginUser User user) {
        dibsService.toggleDib(user.getUserId(), exchangePostId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dibs")
    public ResponseEntity<List<DibbedExchangePostDTO>> getUserDibs(@LoginUser User user) {
        return ResponseEntity.ok(dibsService.getUserDibs(user));
    }

}
