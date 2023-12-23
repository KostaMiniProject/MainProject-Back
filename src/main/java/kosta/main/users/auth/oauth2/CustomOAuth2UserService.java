package kosta.main.users.auth.oauth2;

import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

        private final UsersRepository usersRepository;


        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

            OAuth2User oAuth2User = super.loadUser(userRequest);

            String provider = userRequest.getClientRegistration().getRegistrationId();
            String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, usernameAttributeName, oAuth2User.getAttributes());

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    oAuth2Attribute.mapAttribute(),
                    "email"
            );

        }
    }
