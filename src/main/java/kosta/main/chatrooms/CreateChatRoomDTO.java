package kosta.main.chatrooms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateChatRoomDTO {
  //private Integer senderId; // 메시지 보낸 사람의 ID
  private Integer exchangePostId; // 교환 게시글 ID
  private Integer receiverId; // 수신자(채팅방의 다른 참여자)의 사용자 ID
  public void updateExchangePostId(Integer exchangePostId) {
    this.exchangePostId = exchangePostId;
  }
}
