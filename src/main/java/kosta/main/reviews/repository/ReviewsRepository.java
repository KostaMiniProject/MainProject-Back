package kosta.main.reviews.repository;

import kosta.main.reviews.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Review,Integer> {
}
