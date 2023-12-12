package kosta.main.exchangehistories.repository;

import kosta.main.exchangehistories.entity.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeHistoriesRepository extends JpaRepository<ExchangeHistory, Integer> {
}

