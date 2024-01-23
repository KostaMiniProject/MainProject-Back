package kosta.main.users.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.global.error.exception.AuthErrorCode;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.global.error.exception.UnauthorizedException;
import kosta.main.global.utils.Helper;
import kosta.main.users.auth.dto.TokenDTO;
import kosta.main.users.auth.entity.Token;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.auth.repository.TokenRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static kosta.main.users.auth.jwt.JwtAuthenticationFilter.REFRESH;
import static kosta.main.users.auth.jwt.JwtVerificationFilter.AUTHORIZATION;
import static kosta.main.users.auth.jwt.JwtVerificationFilter.BEARER;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final UsersRepository usersRepository;

    @Transactional
    public void saveTokenInfo(Integer userId, String ip, String refreshToken){
        tokenRepository.save(Token.of(String.valueOf(userId), ip, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(Integer id) {
        tokenRepository.findById(String.valueOf(id))
                .ifPresent(tokenRepository::delete);
    }

    public void reissue(TokenDTO tokenDTO, HttpServletResponse response, HttpServletRequest request) {
        //요청받은 User가 존재하는지 확인
        User user = usersRepository.findById(Integer.parseInt(tokenDTO.getId()))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));
        //UserId를 키값으로 레디스에서 value꺼내기
        Iterable<Token> all = tokenRepository.findAll();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Token token : all) {
            System.out.println(token.getId());
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Token token = tokenRepository.findById(tokenDTO.getId())
                .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.INVALID_REFRESH_TOKEN));
        //레디스에서 꺼낸 value에서 ip와 리프레시 토큰 값이 일치하는지 확인
        String clientIp = Helper.getClientIp(request);
        //일치하지 않을 경우 에러던짐
        if(!Objects.equals(token.getIp(), clientIp)) throw new BusinessException(AuthErrorCode.INVALID_IP);
        //일치할 경우 새로운 액세스 토큰과 새로운 리프레시 토큰을 생성
        String accessToken = delegateAccessToken(user);
        String refreshToken = delegateRefreshToken();
        tokenRepository.delete(token);
        //레디스 내부 리포지토리에 해당 UserId를 키값으로 다시 IP주소와 RefreshToken값 저장
        tokenRepository.save(Token.of(tokenDTO.getId(), clientIp, refreshToken));
        //응답 헤더에 액세스 토큰과 리프레시 토큰 값담아서 보내기
        response.setHeader(AUTHORIZATION,BEARER+" "+accessToken);
        response.setHeader(REFRESH, refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
    }

    public String delegateAccessToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String accessToken = tokenProvider.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }
    public String delegateAccessToken(String email, List<String> authorities){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("roles", authorities);

        String subject = email;
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String accessToken = tokenProvider.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }

    public String delegateRefreshToken(){
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String refreshToken = tokenProvider.generateRefreshToken(expiration, base64EncodedSecretKey);
        return refreshToken;
    }
}
