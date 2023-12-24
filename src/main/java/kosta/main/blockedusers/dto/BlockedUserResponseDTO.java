package kosta.main.blockedusers.dto;

import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedUserResponseDTO extends Auditable {

    private Integer userId;
    private Integer blockingUserId;
    public static BlockedUserResponseDTO from(User user, User blockedUser, LocalDateTime createdAt, LocalDateTime updatedAt){
        BlockedUserResponseDTO dto = new BlockedUserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setBlockingUserId(blockedUser.getUserId());
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        return dto;
    }
    public static BlockedUserDTO from(User user,User blockedUser){
        return new BlockedUserDTO(user.getUserId(), blockedUser.getUserId());
    }
}
