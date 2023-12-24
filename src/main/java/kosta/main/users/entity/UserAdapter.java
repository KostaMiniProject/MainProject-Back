package kosta.main.users.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private User user;

    public UserAdapter(User user){
        super(user.getEmail(), user.getPassword(), authorities(user.getRoles()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    private static List<GrantedAuthority> authorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : roles.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
