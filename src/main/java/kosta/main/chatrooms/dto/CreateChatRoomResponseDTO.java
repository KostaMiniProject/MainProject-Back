package kosta.main.chatrooms.dto;


import kosta.main.chatrooms.entity.ChatRoom;
import lombok.*;

import java.util.Optional;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class CreateChatRoomResponseDTO {
  private Integer chatRoomId;
  private Integer exchangePostId;
  private Integer senderId;
  private Integer receiverId;
  private Integer bidId;

  public static CreateChatRoomResponseDTO from(ChatRoom chatRoom) {
    CreateChatRoomResponseDTO dto = new CreateChatRoomResponseDTO();
    dto.setChatRoomId(chatRoom.getChatRoomId());
    dto.setExchangePostId(Optional.ofNullable(chatRoom.getExchangePost())
        .map(exchangePost -> exchangePost.getExchangePostId())
        .orElse(null));
    dto.setSenderId(Optional.ofNullable(chatRoom.getSender())
        .map(sender -> sender.getUserId())
        .orElse(null));
    dto.setReceiverId(Optional.ofNullable(chatRoom.getReceiver())
        .map(receiver -> receiver.getUserId())
        .orElse(null));
    dto.setBidId(Optional.ofNullable(chatRoom.getBid())
        .map(bid -> bid.getBidId())
        .orElse(null));
    return dto;
  }
}
