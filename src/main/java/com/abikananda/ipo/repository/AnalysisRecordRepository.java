package com.abikananda.ipo.repository; import com.abikananda.ipo.domain.AnalysisRecord; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface AnalysisRecordRepository extends JpaRepository<AnalysisRecord,Long>{List<AnalysisRecord> findTop20ByIpoIdOrderByAnalyzedAtDesc(Long ipoId);}
