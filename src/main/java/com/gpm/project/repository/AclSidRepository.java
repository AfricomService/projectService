package com.gpm.project.repository;

import com.gpm.project.domain.AclSid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AclSid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AclSidRepository extends JpaRepository<AclSid, Long> {
    Optional<AclSid> findBySidTypeAndSidValue(String sidType, String sidValue);

    List<AclSid> findByIdIn(List<Long> ids);
}
