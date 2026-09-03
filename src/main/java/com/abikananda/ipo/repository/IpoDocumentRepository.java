package com.abikananda.ipo.repository; import com.abikananda.ipo.domain.IpoDocument; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface IpoDocumentRepository extends JpaRepository<IpoDocument,Long>{List<IpoDocument> findByIpoIdOrderByRetrievedAtDesc(Long ipoId);}
