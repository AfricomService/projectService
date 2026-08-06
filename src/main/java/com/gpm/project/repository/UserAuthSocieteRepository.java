package com.gpm.project.repository;

import com.gpm.project.domain.UserAuthSociete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface UserAuthSocieteRepository extends JpaRepository<UserAuthSociete, Long> {
    Optional<UserAuthSociete> findBySocieteIdAndContactSocieteId(Long societeId, Long contactSocieteId);

    Optional<UserAuthSociete> findBySocieteIdAndContactSocieteIdAndRoleContactSocieteId(
        Long societeId,
        Long contactSocieteId,
        Long roleContactSocieteId
    );

    List<UserAuthSociete> findBySocieteId(Long societeId);
}
