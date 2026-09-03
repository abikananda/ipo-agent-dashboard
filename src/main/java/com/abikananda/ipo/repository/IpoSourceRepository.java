package com.abikananda.ipo.repository; import com.abikananda.ipo.domain.IpoSource; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface IpoSourceRepository extends JpaRepository<IpoSource,Long>{List<IpoSource> findByIpoIdOrderByRetrievedAtDesc(Long ipoId);}
