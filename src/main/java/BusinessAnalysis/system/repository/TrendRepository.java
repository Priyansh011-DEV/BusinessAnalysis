package BusinessAnalysis.system.repository;

import BusinessAnalysis.system.model.TrendData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendRepository extends JpaRepository<TrendData, Long> {
}