package kosta.main.items.repository;

import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemsRepository extends JpaRepository<Item, Integer> {
  Page<Item> findByUser_UserId(Integer userId, Pageable pageable);

  Page<Item> findItemByItemStatusNotContainingAndUser_UserId(Item.ItemStatus status1, Integer userId, Pageable pageable);
  Page<Item> findByUser_UserIdAndIsBiding(Integer userId, Item.IsBiding isBiding, Pageable pageable);

//  @Query("SELECT i FROM Item i " +
//          "WHERE (LOWER(i.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND i.itemStatus = 'PUBLIC') " +
//          "OR (i.itemStatus = 'PRIVATE' AND i.user.userId = :userId) " +
//          "ORDER BY i.itemId DESC")
//  Page<Item> findByTitleContainingAndItemStatusOrUserId(@Param("searchTerm") String searchTerm, @Param("userId") Integer userId, Pageable pageable);

  @Query("SELECT i FROM Item i " +
      "WHERE ((LOWER(i.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
      "      AND i.itemStatus = 0 AND i.isBiding != 1 AND i.user.userId = :userId) " +
      "ORDER BY i.itemId DESC")
  Page<Item> findByTitleOrDescriptionContainingAndItemStatusOrUserId(@Param("searchTerm") String searchTerm, @Param("userId") Integer userId, Pageable pageable);


  @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.items WHERE u = :user")
  User findUserWithItems(@Param("user") User user);
}
