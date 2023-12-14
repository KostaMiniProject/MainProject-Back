package kosta.main.chats.dto;

import lombok.*;

import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatIdResponseDTO {
  private Integer chatId;
  private Integer chatRoomId;
}
