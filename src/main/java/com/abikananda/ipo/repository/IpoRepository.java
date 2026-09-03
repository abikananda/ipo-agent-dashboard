package com.abikananda.ipo.repository;
import com.abikananda.ipo.domain.Ipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface IpoRepository extends JpaRepository<Ipo,Long> { Optional<Ipo> findBySlug(String slug); }

