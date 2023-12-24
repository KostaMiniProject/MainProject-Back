package kosta.main.users.auth.oauth2;

import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.users.dto.response.UsersResponseDTO;
import kosta.main.users.entity.OAuth2CustomUser;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

//Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

        private final UsersRepository usersRepository;
        @Value("${profile}")
        private String profileImage;


        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

            OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = service.loadUser(userRequest);

            Map<String, Object> originAttributes = oAuth2User.getAttributes();

            //OAuth2 제공자 (google, kakao, naver)
            String provider = userRequest.getClientRegistration().getRegistrationId();
            //OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
            OAuthAttributes attributes = OAuthAttributes.of(provider, originAttributes);
            User user = saveOrUpdate(attributes,provider);
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
    private User saveOrUpdate(OAuthAttributes authAttributes, String provider) {
        Optional<User> userByEmail = usersRepository.findUserByEmail(authAttributes.getEmail());
        if(userByEmail.isPresent()){
            User user = userByEmail.get();
            Boolean social = user.getSocial();
            if(!social) throw new BusinessException(CommonErrorCode.ALREADY_EXIST_LOCAL_USER);
            if(social && !Objects.equals(user.getProvider(), provider))
                throw new BusinessException(CommonErrorCode.ALREADY_EXIST_OAUTH2_USER);
            return user;
        }
        else {
            User user = authAttributes.toEntity(nickNameCreate());
            user.updateProfileImage(profileImage);
            return usersRepository.save(user);
        }
    }
    private String nickNameCreate() {
        String createNickName = "";
        while(true) {
            createNickName = "임시_" + UUID.randomUUID().toString().substring(0, 10);
            Optional<UsersResponseDTO> userByUserName = usersRepository.findUserByUserName(createNickName);
            if(userByUserName.isEmpty()) break;
        }
        return createNickName;
    }
}


