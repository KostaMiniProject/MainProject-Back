package kosta.main.users.auth.service;

import kosta.main.users.auth.entity.Token;
import kosta.main.users.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveTokenInfo(Integer userId, String refreshToken, String accessToken){
        tokenRepository.save(Token.of(String.valueOf(userId), refreshToken, accessToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        tokenRepository.findByAccessToken(accessToken)
                .ifPresent(tokenRepository::delete);
    }
}
