package kosta.main.users.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.global.dto.LoginResponse;
import kosta.main.users.auth.jwt.TokenProvider;
import kosta.main.users.entity.Role;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends  SimpleUrlAuthenticationSuccessHandler{
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String REFRESH = "Refresh";
    public static final int ONLY_BEARER_LENGTH = 8;
        private final UsersRepository usersRepository;
        private final TokenProvider tokenProvider;
        private final ObjectMapper objectMapper;
        private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String targetUrl = determineTargetUrl(request, response, authentication);

            UserOAuth2DTO userOAuth2DTO = null;
            Optional<User> userByEmail = usersRepository.findUserByEmail(oAuth2User.getAttribute("email").toString());
            if(userByEmail.isPresent())
                userOAuth2DTO = userByEmail.map(user -> UserOAuth2DTO.of(user, "temporaryUserId")).get();
            else {
                userOAuth2DTO = saveNewMember(oAuth2User);
            }
//                    .map(user -> UserOAuth2DTO.of(user,"temporaryUserId"))
//                    .orElse(saveNewMember(oAuth2User));    //orElse에 계정저장


            //소셜이 아닌 회원이 이메일로 저장했을 때
            if (!userOAuth2DTO.getSocial()) {
                response.sendError(404, "해당 이메일을 가진 회원이 존재합니다.");
                clearAuthenticationAttributes(request, response);
            } else {
                String accessToken = delegateAccessToken(userOAuth2DTO);
                String refreshToken = delegateRefreshToken(userOAuth2DTO);

                response.setHeader(AUTHORIZATION,BEARER+accessToken);
                response.setHeader(REFRESH, refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
            }

            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");
        }


        protected UserOAuth2DTO saveNewMember(OAuth2User oAuth2User) {

            //userId를 나중에 변경해야함
            String userId = oAuth2User.getAttribute("userId").toString().concat(oAuth2User.getAttribute("provider").toString());
            List<Role> roles = new ArrayList<>();
            roles.add(Role.ROLE_USER);
            String replaceRole = roles.toString().replace("[", "").replace("]", "");
            User user = User.builder()
                    .provider(oAuth2User.getAttribute("provider").toString())
                    .social(true)
                    .password(makePassword())
                    .email(oAuth2User.getAttribute("email"))
                    .name(oAuth2User.getAttribute("username"))
                    .roles(replaceRole)
                    .build();

            usersRepository.save(user);
            return UserOAuth2DTO.of(user, userId);

        }

    private String makePassword() {
        return "socialUser";
    }


    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
            super.clearAuthenticationAttributes(request);
            httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        }
    private String delegateAccessToken(UserOAuth2DTO userOAuth2DTO){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userOAuth2DTO.getEmail());
        claims.put("roles", userOAuth2DTO.getRoles());

        String subject = userOAuth2DTO.getEmail();
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String accessToken = tokenProvider.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        return accessToken;
    }
    private String delegateRefreshToken(UserOAuth2DTO userOAuth2DTO){
        String subject = userOAuth2DTO.getEmail();
        Date expiration = tokenProvider.getTokenExpiration(tokenProvider.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        String refreshToken = tokenProvider.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
        return refreshToken;
    }
}
