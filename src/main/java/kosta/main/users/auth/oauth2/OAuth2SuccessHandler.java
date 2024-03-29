package kosta.main.users.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.global.dto.LoginResponse;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.auth.service.TokenService;
import kosta.main.users.entity.OAuth2CustomUser;
import kosta.main.users.entity.Role;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.client.utils.URIUtils.createURI;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends  SimpleUrlAuthenticationSuccessHandler{
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String REFRESH = "Refresh";
    @Value("${domain}")
    private String domain;
    public static final int ONLY_BEARER_LENGTH = 8;
        private final UsersRepository usersRepository;
        private final TokenService tokenService;
    private final ObjectMapper objectMapper;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            log.info("authentication.getPrincipal()====================================================== {}=====================================================",authentication.getPrincipal());
            OAuth2CustomUser oAuth2User = (OAuth2CustomUser) authentication.getPrincipal();
            String email = oAuth2User.getEmail(); // OAuth2User로부터 Resource Owner의 이메일 주소를 얻음 객체로부터

            List<String> authorities = List.of("ROLE_USER");
            redirect(request, response, email, authorities);  // Access Token과 Refresh Token을 Frontend에 전달하기 위해 Redirect
        }
    private void redirect(HttpServletRequest request, HttpServletResponse response, String email, List<String> authorities) throws IOException {
        log.info("Token 생성 시작");
        String accessToken = tokenService.delegateAccessToken(email, authorities);  // Access Token 생성
        String refreshToken = tokenService.delegateRefreshToken();     // Refresh Token 생성
        User user = usersRepository.findUserByEmail(email).get();

        response.setHeader(AUTHORIZATION,BEARER+accessToken);
        response.setHeader(REFRESH, refreshToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(LoginResponse.of(user.getUserId())));   // Access Token과 Refresh Token을 포함한 URL을 생성
        getRedirectStrategy().sendRedirect(request, response,domain+"/login/authcheck?token="+accessToken);   // sendRedirect() 메서드를 이용해 Frontend 애플리케이션 쪽으로 리다이렉트
    }


}
