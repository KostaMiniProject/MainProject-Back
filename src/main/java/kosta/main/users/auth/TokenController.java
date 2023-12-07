package kosta.main.users.auth;

import jakarta.servlet.http.HttpServletRequest;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

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
}
