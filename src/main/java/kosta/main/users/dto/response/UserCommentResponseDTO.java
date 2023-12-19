package kosta.main.users.dto.response;


import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCommentResponseDTO {

    private Integer userId;

    private String name;

    private String imageUrl;

    public static UserCommentResponseDTO from(User user){
        return new UserCommentResponseDTO(user.getUserId(), user.getName(), user.getProfileImage());
    }


}
