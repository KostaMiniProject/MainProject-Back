package kosta.main.users.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.global.dto.LoginResponse;
import kosta.main.users.auth.dto.LoginDTO;
import kosta.main.users.auth.service.TokenService;
import kosta.main.users.entity.User;
import kosta.main.users.entity.UserAdapter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String REFRESH = "Refresh";
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
//    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows //Java에서 메서드 선언부에 Throws를 정의하지 않고도, 검사 된 예외를 Throw 할 수 있도록 하는 Lombok에서 제공하는 어노테이션임
    @Override //throws나 try-catch 구문을 통해서 Exception에 대해 번거롭게 명시적으로 예외 처리를 해 줘야 하는 경우에 @SneakyThrows 어노테이션을사용하여 명시적인 예외 처리를 생략할 수 있음

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO loginDto = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserAdapter userDetails = (UserAdapter) authResult.getPrincipal();
        User user = userDetails.getUser();
        String accessToken = delegateAccessToken(user);
        String refreshToken = delegateRefreshToken(user);

//        tokenService.saveTokenInfo(user.getUserId(), accessToken,refreshToken);

        Cookie cookie = new Cookie(AUTHORIZATION, accessToken);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1200);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader(REFRESH, refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(LoginResponse.of(user.getUserId())));

    }

    private String delegateAccessToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());

        String subject = user.getEmail();
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String accessToken = tokenProvider.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }

    private String delegateRefreshToken(User user){
        String subject = user.getEmail();
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String refreshToken = tokenProvider.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        return refreshToken;
    }
}
