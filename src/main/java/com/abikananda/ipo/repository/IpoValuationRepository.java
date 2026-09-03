package com.abikananda.ipo.repository; import com.abikananda.ipo.domain.IpoValuation; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface IpoValuationRepository extends JpaRepository<IpoValuation,Long>{Optional<IpoValuation> findByIpoId(Long ipoId);}
