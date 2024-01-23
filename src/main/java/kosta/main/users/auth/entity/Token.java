package kosta.main.users.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "token", timeToLive = 60*60*24*3)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Token {

    @Id
    private String id;
    private String ip;

    @Indexed
    private String refreshToken;

    public static Token of(String id, String ip,String refreshToken) {
        return new Token(id, ip, refreshToken);
    }

}
