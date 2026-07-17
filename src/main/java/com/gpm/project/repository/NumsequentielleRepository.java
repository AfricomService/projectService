package com.gpm.project.repository;

import com.gpm.project.domain.Numsequentielle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Numsequentielle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NumsequentielleRepository extends JpaRepository<Numsequentielle, Long> {}
