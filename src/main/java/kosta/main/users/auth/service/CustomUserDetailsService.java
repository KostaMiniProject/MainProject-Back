package kosta.main.users.auth.service;

import kosta.main.users.auth.CustomUserDetails;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(
                usersRepository
                        .findUserByEmail(username)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 Email입니다.")));
    }
}
