package kosta.main.items.repository;

import kosta.main.items.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ItemsRepository extends JpaRepository<Item,Integer> {

}
