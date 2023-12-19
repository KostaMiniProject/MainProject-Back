package kosta.main.users.dto.response;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateResponseDTO {

    private String email;

    private String name;

    private String address;

    private String phone;


    public static UserCreateResponseDTO of(User user){
        return new UserCreateResponseDTO(
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone()
        );
    }

}
