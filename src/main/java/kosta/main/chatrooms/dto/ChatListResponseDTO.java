package kosta.main.chatrooms.dto;


import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chats.entity.Chat;
import kosta.main.users.entity.User;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Comparator;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatListResponseDTO {
  private Integer userId; // 채팅방의 Sender나 Receiver를 비교해서 채팅의 좌우 위치를 결정
  private String userName; // 유저의 닉네임
  private String userProfileImage; // 유저의 프로필 이미지
  private Optional<String> text; // img 대신 텍스트만 전송할수 있음
  private Optional<String> imageUrl; // 텍스트 대신 img만 전송할 수 있음
  private Boolean isRead; // 읽었는지 여부 파악
  private LocalDateTime createAt; // 언제 보냈는지
  private String messageType; // "TEXT" 또는 "IMAGE"

  public static ChatListResponseDTO from(Chat chat) {
    String messageType = chat.getChatImage() != null ? "IMAGE" : "TEXT";
    Optional<String> content = Optional.ofNullable(chat.getMessage());
    Optional<String> imageUrl = Optional.ofNullable(chat.getChatImage());

    return ChatListResponseDTO.builder()
        .userId(chat.getUser().getUserId())
        .userName(chat.getUser().getName())
        .userProfileImage(chat.getUser().getProfileImage())
        .text(content)
        .imageUrl(imageUrl)
        .isRead(chat.isRead())
        .createAt(chat.getCreatedAt())
        .messageType(messageType)
        .build();
  }

}

