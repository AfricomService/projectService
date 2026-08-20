package com.gpm.project.repository;

import com.gpm.project.domain.DetailRessource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailRessource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailRessourceRepository extends JpaRepository<DetailRessource, Long> {}
