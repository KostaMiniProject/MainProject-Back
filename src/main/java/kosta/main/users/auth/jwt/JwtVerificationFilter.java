package kosta.main.users.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.users.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final int ONLY_BEARER_LENGTH = 8;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader(AUTHORIZATION);

        return authorization == null || !authorization.startsWith(BEARER) || authorization.length() < ONLY_BEARER_LENGTH;
    }

    private Map<String,Object> verifyJws(HttpServletRequest request){
        Optional<Cookie> first = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getValue().startsWith(BEARER)).findFirst();
        String jws = "";
//        if(first.isPresent()) {
//            jws = first.get().getValue().replace(BEARER, "");
//        }else jws = request.getHeader(AUTHORIZATION).replace(BEARER, "");
        jws = first.map(cookie -> cookie.getValue().replace(BEARER, "")).orElseGet(() -> request.getHeader(AUTHORIZATION).replace(BEARER, ""));
        String base64EncodedSecretKey = tokenProvider.encodeBase64SecretKey(tokenProvider.getSecretKey());
        Map<String,Object> claims = tokenProvider.getClaims(jws, base64EncodedSecretKey).getBody();
        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims){
        String email = (String) claims.get("email");
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = (String) claims.get("roles");
        authorities.add(new SimpleGrantedAuthority(role));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
