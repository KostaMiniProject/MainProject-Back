package kosta.main.users.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.users.auth.dto.TokenDTO;
import kosta.main.users.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenDTO tokenDTO, HttpServletResponse response, HttpServletRequest request) {
        //리프레쉬토큰을 DTO로 받음
        tokenService.reissue(tokenDTO,response,request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
