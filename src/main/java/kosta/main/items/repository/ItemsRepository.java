package kosta.main.items.repository;

import kosta.main.items.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Item,Integer> {
  List<Item> findByUser_UserId(Integer userId);
}
