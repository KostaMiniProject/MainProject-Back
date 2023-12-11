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
  private Integer senderId;
  private Optional<String> content;
  private Optional<String> imageUrl;
  private String createAt;
  private Boolean isRead;
}
