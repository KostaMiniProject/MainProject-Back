package kosta.main.chatrooms.dto;

import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomEnterResponseDTO {
  private Integer userId; // 상대방 유저의 ID
  private String userName; // 상대방 유저의 닉네임
  private String userProfileImage; // 상대방 유저의 이미지
  private List<ChatMessageResponseDTO> messages; // 채팅 메시지 목록

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class ChatMessageResponseDTO {
    private Integer senderId; // 보낸 사람의 ID 본인 ID 일지도, 타인일수 도 있음
    private Optional<String> content;
    private Optional<String> imageUrl;
    private String createAt;
    private Boolean isRead;
  }
}