package kosta.main.chatrooms.controller;

import kosta.main.bids.entity.Bid;
import kosta.main.chatrooms.dto.ChatRoomEnterResponseDTO;
import kosta.main.chatrooms.dto.ChatRoomResponseDTO;
import kosta.main.chatrooms.dto.CreateChatRoomDTO;
import kosta.main.chatrooms.dto.CreateChatRoomResponseDTO;
import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.exchangeposts.ExchangePostStubData;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.UserStubData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatRoomStubData {



    ChatRoom getChatRoom() {
        ExchangePostStubData exchangePostStubData = new ExchangePostStubData();
        ExchangePost exchangePostBid = exchangePostStubData.getExchangePostBid();
        Bid bid = exchangePostBid.getBids().get(0);
        UserStubData userStubData = new UserStubData();
        return ChatRoom.builder()
                .chatRoomId(1)
                .exchangePost(exchangePostBid)
                .bid(bid)
                .sender(userStubData.getUser())
                .receiver(userStubData.getUpdateUser())
                .build();
    }
    CreateChatRoomDTO getCreateChatRoomDTO(){
        return new CreateChatRoomDTO(1);
    }

    CreateChatRoomResponseDTO getCreateChatRoomResponseDTO() {
        return CreateChatRoomResponseDTO.from(getChatRoom());
    }
    ChatRoomResponseDTO getChatRoomResponseDTO() {
        return ChatRoomResponseDTO.builder()
                .chatRoomId(1)
                .participantName("참가자")
                .lastMessageTimeDifference(LocalDateTime.now())
                .lastMessageDateTime(LocalDateTime.now())
                .lastMessageContent("마지막 메세지 콘텐츠")
                .participantProfileImg("참여자 프로필 이미지주소")
                .anotherParticipantName("다른 참가자 이름")
                .anotherParticipantProfileImg("다른 참가자 프로필 이미지")
                .exchangePostAddress("교환게시글에 적혀있는 주소")
                .build();
    }

    ChatRoomResponseDTO getAnotherChatRoomResponseDTO() {
        return ChatRoomResponseDTO.builder()
                .chatRoomId(2)
                .participantName("다른 참가자")
                .lastMessageTimeDifference(LocalDateTime.now())
                .lastMessageDateTime(LocalDateTime.now())
                .lastMessageContent("마지막 메세지 콘텐츠2")
                .participantProfileImg("다른 참가자 프로필 이미지주소2")
                .anotherParticipantName("참여자 이름")
                .anotherParticipantProfileImg("참여자 프로필 이미지")
                .exchangePostAddress("교환게시글에 적혀있는 주소")
                .build();
    }

    List<ChatRoomResponseDTO> getChatRoomResponseDTOs(){
        List<ChatRoomResponseDTO> chatRoomResponseDTOs = new ArrayList<>();
        chatRoomResponseDTOs.add(getChatRoomResponseDTO());
        chatRoomResponseDTOs.add(getAnotherChatRoomResponseDTO());
        return chatRoomResponseDTOs;
    }

    ChatRoomEnterResponseDTO getChatRoomEnterResponseDTO(){
       return ChatRoomEnterResponseDTO.builder()
                .isOwner(true)
                .exchangePostId(1)
                .exchangePostTittle("대화중인 교환 게시글의 제목")
                .exchangePostAddress("대화중인 교환 게시글의 거래 희망 주소")
                .exchangePostCategory("카테고리")
                .exchangePostImage("대표이미지")
                .status(Bid.BidStatus.BIDDING)
                .bidId(1)
                .userId(1)
                .userName("상대방유저이름")
                .userProfileImage("유저프로필이미지주소")
                .messages(getChatMessageResponseDTOs())
                .build();
    }

    ChatRoomEnterResponseDTO.ChatMessageResponseDTO getChatMessageResponseDTO(){
        return ChatRoomEnterResponseDTO.ChatMessageResponseDTO.builder()
                .chatId(1)
                .senderId(1)
                .content(Optional.of("일딴 내용"))
                .imageUrl(Optional.of("이미지url"))
                .createAt("날짜적혀있음")
                .isRead(false).build();
    }

    ChatRoomEnterResponseDTO.ChatMessageResponseDTO getAnotherChatMessageResponseDTO(){
        return ChatRoomEnterResponseDTO.ChatMessageResponseDTO.builder()
                .chatId(2)
                .senderId(2)
                .content(Optional.of("일딴 내용2"))
                .imageUrl(Optional.of("이미지url2"))
                .createAt("날짜적혀있음2")
                .isRead(false).build();
    }
    List<ChatRoomEnterResponseDTO.ChatMessageResponseDTO> getChatMessageResponseDTOs(){
        List<ChatRoomEnterResponseDTO.ChatMessageResponseDTO> chatMessageResponseDTOList
                = new ArrayList<>();
        chatMessageResponseDTOList.add(getChatMessageResponseDTO());
        chatMessageResponseDTOList.add(getAnotherChatMessageResponseDTO());
        return chatMessageResponseDTOList;
    }
}
