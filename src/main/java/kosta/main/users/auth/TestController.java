package kosta.main.users.auth;

import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TokenProvider tokenProvider;
    private final UsersService usersService;

    @PostMapping("/expire/{userId}")
    public ResponseEntity<?> findMyProfile(@PathVariable Integer userId){
        User user = usersService.findUserByUserId(userId);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();

        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String accessToken = tokenProvider.generateExpiredAccessToken(claims, subject, base64EncodedSecretKey);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
    @GetMapping("/1")
    public ResponseEntity<?> throwBusinessExceptionExchangePostNotFoundError(){
        throw new BusinessException(CommonErrorCode.EXCHANGE_POST_NOT_FOUND);
    }
    @GetMapping("/2")
    public ResponseEntity<?> throwBusinessExceptionNotExchangePostOwnerError(){
        throw new BusinessException(CommonErrorCode.NOT_EXCHANGE_POST_OWNER);
    }
}
