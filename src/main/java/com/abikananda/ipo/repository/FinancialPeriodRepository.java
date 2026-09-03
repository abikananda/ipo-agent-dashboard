package com.abikananda.ipo.repository;
import com.abikananda.ipo.domain.FinancialPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FinancialPeriodRepository extends JpaRepository<FinancialPeriod,Long> { List<FinancialPeriod> findByIpoIdOrderByPeriodEndAsc(Long ipoId); }

