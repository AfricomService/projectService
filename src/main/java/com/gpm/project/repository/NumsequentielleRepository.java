package com.gpm.project.repository;

import com.gpm.project.domain.Numsequentielle;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Numsequentielle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NumsequentielleRepository extends JpaRepository<Numsequentielle, Long> {
    @Query("select n from Numsequentielle n where trim(n.codeNumSeq) = trim(:codeNumSeq)")
    Optional<Numsequentielle> findByCodeNumSeq(@Param("codeNumSeq") String codeNumSeq);

}
