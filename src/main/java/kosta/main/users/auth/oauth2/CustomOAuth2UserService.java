package kosta.main.users.auth.oauth2;

import kosta.main.users.entity.OAuth2CustomUser;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

        private final UsersRepository usersRepository;


        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

            OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = service.loadUser(userRequest);

            Map<String, Object> originAttributes = oAuth2User.getAttributes();

            //OAuth2 제공자 (google, kakao, naver)
            String provider = userRequest.getClientRegistration().getRegistrationId();
            //OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
            OAuthAttributes attributes = OAuthAttributes.of(provider, originAttributes);
            User user = saveOrUpdate(attributes);
            String email = user.getEmail();
            List<GrantedAuthority> authorities = authorities(user.getRoles());

            return new OAuth2CustomUser(provider, originAttributes, authorities, email);

        }
    private static List<GrantedAuthority> authorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : roles.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
    private User saveOrUpdate(OAuthAttributes authAttributes) {
        Optional<User> userByEmail = usersRepository.findUserByEmail(authAttributes.getEmail());
        if(userByEmail.isPresent())
            return userByEmail.get();
        else {
            User user = authAttributes.toEntity();
            return usersRepository.save(user);
        }
    }
}


