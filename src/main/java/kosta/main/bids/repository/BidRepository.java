package kosta.main.bids.repository;

import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.entity.ExchangePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
  List<Bid> findByExchangePost(ExchangePost exchangePost);

  // ExchangePost에 해당하고 DELETED 상태가 아닌 Bid의 수를 카운트합니다.
  @Query("SELECT COUNT(b) FROM Bid b WHERE b.exchangePost = :exchangePost AND b.status <> 3")
  Integer countByExchangePostAndStatusNotDeleted(@Param("exchangePost") ExchangePost exchangePost);

}
