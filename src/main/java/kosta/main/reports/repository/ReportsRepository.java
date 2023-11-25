package kosta.main.reports.repository;

import kosta.main.reports.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportsRepository extends JpaRepository<Report,Integer> {
}
