package com.gpm.project.repository;

import com.gpm.project.domain.TypeRessource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeRessource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRessourceRepository extends JpaRepository<TypeRessource, Long> {}
