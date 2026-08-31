package com.abikananda.ipo.repository;
import com.abikananda.ipo.domain.MarketSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MarketSnapshotRepository extends JpaRepository<MarketSnapshot,Long> { List<MarketSnapshot> findTop30ByIpoIdOrderByObservedAtDesc(Long ipoId); }

