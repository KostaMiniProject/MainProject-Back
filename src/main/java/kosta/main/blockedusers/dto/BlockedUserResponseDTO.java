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
    private String profileImage;
    private String name;
    public static BlockedUserResponseDTO from(User user, User blockedUser, LocalDateTime createdAt, LocalDateTime updatedAt){
        BlockedUserResponseDTO dto = new BlockedUserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setBlockingUserId(blockedUser.getUserId());
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        dto.setProfileImage(blockedUser.getProfileImage());
        dto.setName(blockedUser.getName());
        return dto;
    }
}
