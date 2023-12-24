package kosta.main.users.auth.oauth2;

import jakarta.servlet.http.HttpServletResponse;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.auth.oauth2.dto.Oauth2DTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuth2Controller {
    private final Oauth2Service oauth2Service;

//        @GetMapping("/callback")
//        public ResponseEntity<?> oauthCallback(@RequestParam String code) {
//            // OAuth 인증 코드를 사용하여 토큰을 요청
//
//            // 클라이언트에 토큰을 전달하는 HTML 페이지 생성
//            String html = "<!DOCTYPE html>"
//                    + "<html>"
//                    + "<head>"
//                    + "<script type= text/javascript>"
//                    + "localStorage.setItem('token', '" + code + "');"
//                    + "window.location.href = '/';" // 메인 페이지 또는 대시보드로 리디렉션
//                    + "</script>"
//                    + "</head>"
//                    + "<body>"
//                    + "</body>"
//                    + "</html>";
//
//            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
//        }
        @PostMapping("/check-token")
        public ResponseEntity<?> checkToken(@RequestBody() Oauth2DTO oauth2DTO, HttpServletResponse response) {
            // OAuth 인증 코드를 사용하여 토큰을 요청
            String jws = oauth2DTO.getToken();
            response.setHeader("Authorization","Bearer " + jws);
            return new ResponseEntity<>(oauth2Service.checkAuth(jws),HttpStatus.OK);
        }
}
