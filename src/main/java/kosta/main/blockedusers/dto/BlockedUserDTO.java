package kosta.main.blockedusers.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedUserDTO {

    private Integer userId;
    private Integer blockingUserId;

    public static BlockedUserDTO from(User user,User blockedUser){
        return new BlockedUserDTO(user.getUserId(), blockedUser.getUserId());
    }
}
