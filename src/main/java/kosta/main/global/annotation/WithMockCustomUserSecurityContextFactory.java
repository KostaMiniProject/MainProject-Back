package kosta.main.global.annotation;

import kosta.main.users.entity.User;
import kosta.main.users.entity.UserAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        User build = User.builder()
                .userId(1)
                .name("테스트이름")
                .phone("010-1234-5678")
                .password("asdfQWER1234!")
                .address("경기도 성남시 분당구 성남대로 지하55")
                .email("hongildong@gmail.com")
                .profileImage("프로필 이미지")
                .userStatus(User.UserStatus.ACTIVATE)
                .build();
        UserDetails userDetails = new UserAdapter(build);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        securityContext.setAuthentication(authenticationToken);

        return securityContext;
    }
}
