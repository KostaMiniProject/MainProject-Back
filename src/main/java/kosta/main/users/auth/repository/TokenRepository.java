package kosta.main.users.auth.repository;

import kosta.main.users.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByAccessToken(String accessToken);
}
