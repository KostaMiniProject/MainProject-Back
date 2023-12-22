package kosta.main.users.auth.oauth2;

import lombok.*;

import java.util.HashMap;
import java.util.Map;


@Getter
@AllArgsConstructor
@Builder
public class OAuth2Attribute {

    private String provider;
    private Map<String, Object> attributes;
    private String userId;
    private String username;
    private String email;
    private String picture;
    private String nickname;

    public static OAuth2Attribute of(String provider, String usernameAttributeName, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return OAuth2Attribute.ofGoogle(provider, usernameAttributeName, attributes);
            default:
                throw new RuntimeException("소셜 로그인 접근 실패");
        }

    }

    private static OAuth2Attribute ofGoogle(String provider, String usernameAttributeName, Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .provider(provider)
                .attributes(attributes)
                .username(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .userId(String.valueOf(attributes.get(usernameAttributeName)))
                .build();
    }

    public Map<String, Object> mapAttribute() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("username", username);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}