package kosta.main.items.repository;

import kosta.main.items.dto.ItemPageDTO;
import kosta.main.items.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Item,Integer> {
  Page<ItemPageDTO> findByUser_UserId(Integer userId, Pageable pageable);

//  @Query("SELECT * from Item where Item.user.userId = :userId")
//  Page<Item> findItemBypagenation(Integer userId);



}
