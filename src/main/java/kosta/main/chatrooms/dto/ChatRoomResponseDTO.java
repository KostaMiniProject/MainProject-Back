package kosta.main.chatrooms.dto;


import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chats.entity.Chat;
import kosta.main.users.entity.User;
import lombok.*;

import java.time.Duration;
import java.time.Period;
import java.util.Comparator;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class ChatRoomResponseDTO {
  private Integer chatRoomId;
  private String participantName;
  private LocalDateTime lastMessageTimeDifference;
  private LocalDateTime lastMessageDateTime; // 실제 마지막 메시지의 시간을 저장하는 필드
  private String lastMessageContent;
  private String participantProfileImg;
  private String exchangePostAddress;

  public static ChatRoomResponseDTO of(ChatRoom chatRoom, User currentUser) {
    ChatRoomResponseDTO dto = new ChatRoomResponseDTO();

    dto.setChatRoomId(chatRoom.getChatRoomId());

    // 참여 대상 식별을 위해 Optional 사용
    User participant = Optional.ofNullable(chatRoom.getSender())
        .filter(sender -> !sender.equals(currentUser))
        .orElse(chatRoom.getReceiver());

    if (participant != null) {
      dto.setParticipantName(participant.getName());
      dto.setParticipantProfileImg(participant.getProfileImage());
    }

    // 마지막 메시지 정보
    Chat lastChat = chatRoom.getChats().stream()
        .max(Comparator.comparing(Chat::getCreatedAt))
        .orElse(null);

    if (lastChat != null) {
      dto.setLastMessageContent(lastChat.getMessage());
      dto.setLastMessageTimeDifference(lastChat.getCreatedAt()); // 마지막 메시지 시간 문자열로 변환
      dto.setLastMessageDateTime(lastChat.getCreatedAt()); // 실제 마지막 메시지 시간 저장
    }

    // 거래 게시글 주소
    if (chatRoom.getExchangePost() != null) {
      dto.setExchangePostAddress(chatRoom.getExchangePost().getAddress());
    }

    return dto;
  }

  public String getLastMessageTimeDifference() {
    return calculateTimeDifference(this.lastMessageTimeDifference);
  }
  public static String calculateTimeDifference(LocalDateTime messageTime) {
    LocalDateTime now = LocalDateTime.now();

    Duration duration = Duration.between(messageTime, now);
    Period period = Period.between(messageTime.toLocalDate(), now.toLocalDate());

    if (period.getYears() > 0) {
      return period.getYears() + "년 전";
    } else if (period.getMonths() > 0) {
      return period.getMonths() + "개월 전";
    } else if (period.getDays() > 0) {
      return period.getDays() + "일 전";
    } else if (duration.toHours() > 0) {
      return duration.toHours() + "시간 전";
    } else if (duration.toMinutes() > 0) {
      return duration.toMinutes() + "분 전";
    } else {
      return "방금 전";
    }
  }
}

