package com.gpm.project.repository;

import com.gpm.project.domain.Societe;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Societe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocieteRepository extends JpaRepository<Societe, Long> {
    @Query("select s from Societe s where lower(trim(s.raisonSociale)) = lower(trim(:raisonSociale))")
    List<Societe> findAllByRaisonSocialeNormalisee(@Param("raisonSociale") String raisonSociale);
}
