package kosta.main.bids.repository;

import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.entity.ExchangePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
  List<Bid> findByExchangePost(ExchangePost exchangePost);

}
