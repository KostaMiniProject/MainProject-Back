package kosta.main.exchangehistories.repository;

import kosta.main.exchangehistories.entity.ItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInfoRepository extends JpaRepository<ItemInfo,Integer> {
}
