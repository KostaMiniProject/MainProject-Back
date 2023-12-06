package kosta.main.chats.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatMessageDTO {
  private String content; // 메시지 내용
  private Integer chatRoomId; // 채팅방
  private Integer senderId; // 전송 유저 ID
}
