package kosta.main.email.repository;

import kosta.main.email.entity.Emails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailsRepository extends JpaRepository<Emails, String> {
}
