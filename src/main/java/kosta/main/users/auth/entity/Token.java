package kosta.main.users.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "token", timeToLive = 60*60*24*3)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Token {

    @Id
    private String id;
    private String refreshToken;

    @Indexed
    private String accessToken;

    public static Token of(String s, String refreshToken, String accessToken) {
        return new Token(s, refreshToken, accessToken);
    }
}
