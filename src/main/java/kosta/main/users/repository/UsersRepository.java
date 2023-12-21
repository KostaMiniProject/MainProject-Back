package kosta.main.users.repository;

import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer>, UsersRepositoryCustom{


    Optional<User> findUserByEmail(String email);
}
