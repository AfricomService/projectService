package com.gpm.project.repository;

import com.gpm.project.domain.TypeRessourceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeRessourceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRessourceDetailsRepository extends JpaRepository<TypeRessourceDetails, Long> {}
