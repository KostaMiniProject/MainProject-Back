package kosta.main.dibs.repository;

import kosta.main.dibs.entity.Dib;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DibsRepository extends JpaRepository<Dib, Integer> {
    // 특정 사용자와 게시글에 대한 찜 조회
    Optional<Dib> findByUserUserIdAndExchangePostExchangePostId(Integer userId, Integer exchangePostId);

    // 특정 사용자의 모든 찜 조회
    List<Dib> findByUserUserId(Integer userId);
}
