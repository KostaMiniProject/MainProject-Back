package kosta.main.global.dto;

import lombok.Getter;

@Getter
public class LoginResponse  {
    private final Integer userId;

    private LoginResponse(Integer userId){
        this.userId = userId;
    }

    public static LoginResponse of(Integer data){
        return new LoginResponse(data);
    }

}
