package kosta.main.blockedusers.repository;

import kosta.main.blockedusers.entity.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedUsersRepository extends JpaRepository<BlockedUser,Integer> {
}
