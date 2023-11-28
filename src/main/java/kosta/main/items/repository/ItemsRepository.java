package kosta.main.items.repository;

import kosta.main.items.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Item,Integer> {

}
