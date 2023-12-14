package kosta.main.exchangehistories.repository;

import kosta.main.chatrooms.entity.ChatRoom;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeHistoriesRepository extends JpaRepository<ExchangeHistory, Integer> {
  Page<ExchangeHistory> findByExchangeInitiator_UserIdOrExchangePartner_UserId(Integer initiatorUserId, Integer partnerUserId, Pageable pageable);
  ExchangeHistory findExchangeHistoryByExchangePost_ExchangePostId(Integer postId);
}

