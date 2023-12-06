package kosta.main.users.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "token", timeToLive = 604800000)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Token {

    @Id
    private Long id;
    private String refreshToken;

}
