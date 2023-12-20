package kosta.main.blockedusers.repository;

import kosta.main.blockedusers.entity.BlockedUser;
import kosta.main.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockedUsersRepository extends JpaRepository<BlockedUser,Integer> {

//    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.blockedUsers WHERE u = :user")
//    User findUserWithBlockUsers(@Param("user") User user);

    @Query("SELECT distinct u FROM User u join fetch u.blockedUsers")
    Optional<User> findUserWithBlockUsersAndBlockedUsers(@Param("user") User user);
}
