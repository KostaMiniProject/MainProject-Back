package kosta.main.chats.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatMessageResponseDTO{
  private Integer chatId;
  private Integer senderId;
  private Optional<String> content;
  private Optional<String> imageUrl;
  private String createAt; // 데이터형식 다시 변경
  private Boolean isRead;
}
