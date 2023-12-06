package kosta.main.chats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChatMessageDTO {
  private String content; // 메시지 내용
  private Integer chatRoomId; // 채팅방
  private Integer senderId; // 전송 유저 ID
}
