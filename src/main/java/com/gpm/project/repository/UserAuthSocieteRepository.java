package com.gpm.project.repository;

import com.gpm.project.domain.UserAuthSociete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAuthSociete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAuthSocieteRepository extends JpaRepository<UserAuthSociete, Long> {
    Optional<UserAuthSociete> findBySocieteIdAndContactSocieteId(Long societeId, Long contactSocieteId);

    List<UserAuthSociete> findBySocieteId(Long societeId);
}
