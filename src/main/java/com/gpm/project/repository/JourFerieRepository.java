package com.gpm.project.repository;

import com.gpm.project.domain.JourFerie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JourFerie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {}
