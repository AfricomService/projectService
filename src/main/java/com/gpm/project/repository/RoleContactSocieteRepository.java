package com.gpm.project.repository;

import com.gpm.project.domain.RoleContactSociete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoleContactSociete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleContactSocieteRepository extends JpaRepository<RoleContactSociete, Long> {}
