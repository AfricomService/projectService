package com.gpm.project.repository;

import com.gpm.project.domain.ParentClassdef;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParentClassdef entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParentClassdefRepository extends JpaRepository<ParentClassdef, Long> {}
