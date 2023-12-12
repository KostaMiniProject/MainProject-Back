package kosta.main.exchangehistories.service;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangehistories.dto.ExchangeHistoryCreateDTO;
import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.dto.ExchangeHistoryCreateResponseDTO;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangehistories.repository.ExchangeHistoriesRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExchangeHistoriesService {
  private final ExchangeHistoriesRepository exchangeHistoriesRepository;
  private final ExchangePostsRepository exchangePostsRepository;
  private final BidRepository bidRepository;


  // 공통 메서드: 특정 ID를 가진 엔티티를 찾고, 없으면 예외를 발생시키는 메서드
  private <T> T findEntityById(JpaRepository<T, Integer> repository, Integer id, String errorMessage) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException(errorMessage));
  }

  // 교환 내역 생성 = 거래완료 후의 로직
  @Transactional
  public ExchangeHistoryCreateResponseDTO createExchangeHistory(ExchangeHistoryCreateDTO exchangeHistoryCreateDTO, User user) {
    ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangeHistoryCreateDTO.getExchangePostId(), "ExchangePost not found");
    Bid selectedBid = findEntityById(bidRepository, exchangeHistoryCreateDTO.getSelectedBidId(), "Bid not found");

    User postUser = exchangePost.getUser(); // 게시글 작성자

    Item postItem = exchangePost.getItem(); // 게시글의 아이템

    User bidUser = selectedBid.getUser(); // 입찰자
    List<Item> bidItems = selectedBid.getItems(); // 입찰에 포함된 아이템들

    // ExchangeHistory 객체 생성
    ExchangeHistory exchangeHistory = ExchangeHistory.builder()
        //.exchangeDate(exchangeHistoryCreateDTO.getExchangeDate()) // createAt으로 대체함
        .user(postUser) // 게시글 작성자를 거래 내역의 유저로 설정
        .exchangePost(exchangePost)
        .item(postItem) // 게시글의 아이템을 거래 내역의 아이템으로 설정
        .build();
    ExchangeHistory savedExchangeHistory = exchangeHistoriesRepository.save(exchangeHistory);

    return new ExchangeHistoryCreateResponseDTO(savedExchangeHistory.getExchangeHistoryId());
  }

  @Transactional(readOnly = true)
  public List<ExchangeHistoriesResponseDTO> getExchangeHistories(User user, Pageable pageable) {
    List<ExchangeHistory> histories = exchangeHistoriesRepository.findByUser_UserId(user.getUserId(), pageable);

    return histories.stream().map(history -> {
      ExchangePost exchangePost = history.getExchangePost();
      Bid selectedBid = history.getExchangePost().getBids().stream()
          .filter(bid -> bid.getStatus() == Bid.BidStatus.SELECTED)
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException("Selected bid not found"));

      List<ExchangeHistoriesResponseDTO.ItemDetailsDTO> myItems = Collections.singletonList(
          new ExchangeHistoriesResponseDTO.ItemDetailsDTO(
              exchangePost.getItem().getItemId(),
              exchangePost.getItem().getTitle(),
              exchangePost.getItem().getDescription(),
              exchangePost.getItem().getImages().isEmpty() ? null : exchangePost.getItem().getImages().get(0)
          )
      );

      List<ExchangeHistoriesResponseDTO.ItemDetailsDTO> otherUserItems = selectedBid.getItems().stream()
          .map(item -> new ExchangeHistoriesResponseDTO.ItemDetailsDTO(
              item.getItemId(),
              item.getTitle(),
              item.getDescription(),
              item.getImages().isEmpty() ? null : item.getImages().get(0)
          ))
          .collect(Collectors.toList());

      return new ExchangeHistoriesResponseDTO(
          history.getCreatedAt(),
          exchangePost.getUser().getName(),
          exchangePost.getUser().getAddress(),
          exchangePost.getUser().getProfileImage(),
          myItems,
          selectedBid.getUser().getName(),
          selectedBid.getUser().getAddress(),
          selectedBid.getUser().getProfileImage(),
          otherUserItems
      );
    }).collect(Collectors.toList());
  }


}
