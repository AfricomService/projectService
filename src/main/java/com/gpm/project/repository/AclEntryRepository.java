package com.gpm.project.repository;

import com.gpm.project.domain.AclEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AclEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {}
