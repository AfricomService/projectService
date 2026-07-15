package com.gpm.project.repository;

import com.gpm.project.domain.AffaireSocieteAdj;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AffaireSocieteAdj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffaireSocieteAdjRepository extends JpaRepository<AffaireSocieteAdj, Long> {}
