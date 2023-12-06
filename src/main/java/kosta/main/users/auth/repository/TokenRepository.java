package kosta.main.users.auth.repository;

import kosta.main.users.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
