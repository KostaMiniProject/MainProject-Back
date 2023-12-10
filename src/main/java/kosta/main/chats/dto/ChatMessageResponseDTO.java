package kosta.main.chats.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatMessageResponseDTO{
  private String content; // 메시지 내용
}
