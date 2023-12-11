package kosta.main.chats.dto;

import lombok.*;

import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatMessageDTO {
  private Integer roomId; // 채팅방 ID
  private Integer senderId; // 전송 유저 ID
  private Optional<String> content; // 메시지 내용
  private Optional<String> imageUrl; // 전송받은 이미지 URL
}
