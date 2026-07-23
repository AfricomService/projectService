package com.gpm.project.repository;

import com.gpm.project.domain.ContactSociete;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactSociete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactSocieteRepository extends JpaRepository<ContactSociete, Long> {
    List<ContactSociete> findAllBySocieteId(Long societeId);
}
