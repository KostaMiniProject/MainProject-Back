package kosta.main.users.repository;

import kosta.main.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Integer>, UsersRepositoryCustom{

    Optional<User> findUserByEmail(String email);
}
