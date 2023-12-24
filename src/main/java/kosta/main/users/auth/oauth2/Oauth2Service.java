package kosta.main.users.auth.oauth2;

import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.auth.oauth2.dto.Oauth2DTO;
import kosta.main.users.auth.oauth2.dto.Oauth2ResponseDTO;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final UsersRepository usersRepository;
    private final TokenProvider tokenProvider;

    public Oauth2ResponseDTO checkAuth(String jws){
        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        Map<String,Object> claims = tokenProvider.getClaims(jws, base64EncodedSecretKey).getBody();
        String email = (String) claims.get("email");
        Optional<User> userByEmail =
                usersRepository.findUserByEmail(email);
        User user = userByEmail.orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));
        return new Oauth2ResponseDTO(user.getUserId());
    }
}
