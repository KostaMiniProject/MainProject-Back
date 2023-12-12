package kosta.main.exchangehistories.repository;

import kosta.main.exchangehistories.entity.ExchangeHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeHistoriesRepository extends JpaRepository<ExchangeHistory, Integer> {
  List<ExchangeHistory> findByUser_UserId(Integer userId, Pageable pageable);
}

