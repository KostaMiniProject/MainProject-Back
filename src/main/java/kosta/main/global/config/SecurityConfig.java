package kosta.main.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
* @EnableWebSecurity : 스프링 시큐리티를 활성화하고 웹 보안 설정을 구성하는데 사용됨
*                      이 어노테이션은 'WebSecurityConfigurerAdapter' 클래스를 상속한 구성 클래스에 사용됨
*/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());


        // 세션을 사용하지 않기 때문에 STATELESS로 설정해야함
//        http.sessionManagement((sessionManagement) ->
//                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //  ============ 추가한 부분 =============
        http.cors(Customizer.withDefaults());
        // ============= 추가한 부분 =============

        // Spring Security 6.1.0부터는 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정하게 지향
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/h2/**"),
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/error"),
                        // 일단 모든 API 허용되게 설정
                        new AntPathRequestMatcher("/**")

                ).permitAll()
                        .anyRequest().permitAll()
//                .anyRequest().authenticated()
        );

        http.headers((headers -> headers
                .frameOptions(frameOptionsConfig ->
                        frameOptionsConfig.disable()
                )
        ));


        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:8080"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Set-Cookie", "*"));
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PATCH", "DELETE", "OPTIONS","PUT"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
