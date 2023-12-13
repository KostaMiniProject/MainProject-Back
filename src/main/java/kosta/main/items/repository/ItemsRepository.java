package kosta.main.items.repository;

import kosta.main.items.dto.ItemPageDTO;
import kosta.main.items.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Item, Integer> {
  Page<Item> findByUser_UserId(Integer userId, Pageable pageable);
  Page<Item> findByUser_UserIdAndIsBiding(Integer userId, Item.IsBiding isBiding, Pageable pageable);

//  @Query("SELECT i FROM Item i " +
//          "WHERE (LOWER(i.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND i.itemStatus = 'PUBLIC') " +
//          "OR (i.itemStatus = 'PRIVATE' AND i.user.userId = :userId) " +
//          "ORDER BY i.itemId DESC")
//  Page<Item> findByTitleContainingAndItemStatusOrUserId(@Param("searchTerm") String searchTerm, @Param("userId") Integer userId, Pageable pageable);

  @Query("SELECT i FROM Item i " +
          "WHERE (LOWER(i.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND i.itemStatus = 0) " +
          "   OR (i.itemStatus = 1 AND i.user.userId = :userId) " +
          "ORDER BY i.itemId DESC")
  Page<Item> findByTitleContainingAndItemStatusOrUserId(@Param("searchTerm") String searchTerm, @Param("userId") Integer userId, Pageable pageable);


}
