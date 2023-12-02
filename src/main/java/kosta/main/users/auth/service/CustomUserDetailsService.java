package kosta.main.users.auth.service;

import kosta.main.users.entity.User;
import kosta.main.users.entity.UserAdapter;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = usersRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Email입니다."));
        return new UserAdapter(user);
    }
}