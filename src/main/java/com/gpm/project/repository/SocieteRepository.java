package com.gpm.project.repository;

import com.gpm.project.domain.Societe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Societe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocieteRepository extends JpaRepository<Societe, Long> {}
