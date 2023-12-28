package kosta.main.chatrooms.dto;


import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.chats.entity.Chat;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.users.entity.User;
import lombok.*;

import java.time.Duration;
import java.time.Period;
import java.util.Comparator;

import java.time.LocalDateTime;
import java.util.Objects;
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
  private String anotherParticipantName;
  private String anotherParticipantProfileImg;
  private String exchangePostAddress;

  public static ChatRoomResponseDTO of(ChatRoom chatRoom, User currentUser) {
    ChatRoomResponseDTO dto = new ChatRoomResponseDTO();
    if(currentUser == null) throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
    dto.setChatRoomId(chatRoom.getChatRoomId());
    User participant = chatRoom.getSender();
    User anotherParticipant = chatRoom.getReceiver();
    if(participant == null) {
      dto.setParticipantName(anotherParticipant.getName());
      dto.setParticipantProfileImg(anotherParticipant.getProfileImage());
      dto.setAnotherParticipantName("익명");
      dto.setAnotherParticipantProfileImg("https://d30zoz4y43tmi6.cloudfront.net/simpleProfile.jpg");
    } else if(anotherParticipant == null){
      dto.setParticipantName(participant.getName());
      dto.setParticipantProfileImg(participant.getProfileImage());
      dto.setAnotherParticipantName("익명");
      dto.setAnotherParticipantProfileImg("https://d30zoz4y43tmi6.cloudfront.net/simpleProfile.jpg");
    } else{
      if(Objects.equals(participant.getUserId(), currentUser.getUserId())) {
        dto.setParticipantName(participant.getName());
        dto.setParticipantProfileImg(participant.getProfileImage());
        dto.setAnotherParticipantName(anotherParticipant.getName());
        dto.setAnotherParticipantProfileImg(anotherParticipant.getProfileImage());
      } else{
        dto.setParticipantName(anotherParticipant.getName());
        dto.setParticipantProfileImg(anotherParticipant.getProfileImage());
        dto.setAnotherParticipantName(participant.getName());
        dto.setAnotherParticipantProfileImg(participant.getProfileImage());
      }
    }
    // 참여 대상 식별을 위해 Optional 사용
    //일단 챗룸 안에 있는 유저값 다 꺼냄
    //현재 유저랑 일치하는값 확인
    //일치하면 그대로 아니면 반대로
    //현재 유저랑 일치하지 않는 값 확인
    //널인지 확인

    //널일경우 익명의 유저이름과 익명이미지 전송
    //널이 아닐경우 해당 이름과 이미지 전송

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

