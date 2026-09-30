package com.gpm.project.repository;

import com.gpm.project.domain.AclEntryProjet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AclEntryProjet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclEntryProjetRepository extends JpaRepository<AclEntryProjet, Long> {}
