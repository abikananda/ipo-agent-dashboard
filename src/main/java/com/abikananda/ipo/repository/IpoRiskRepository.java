package com.abikananda.ipo.repository; import com.abikananda.ipo.domain.IpoRisk; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface IpoRiskRepository extends JpaRepository<IpoRisk,Long>{List<IpoRisk> findByIpoIdOrderBySeverityAsc(Long ipoId);}
