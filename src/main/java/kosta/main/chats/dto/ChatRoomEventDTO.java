package kosta.main.chats.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatRoomEventDTO {
  private Integer chatRoomId;
  private Integer userId;
  private String userName;
}
