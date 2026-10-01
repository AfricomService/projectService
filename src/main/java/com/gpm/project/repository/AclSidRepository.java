package com.gpm.project.repository;

import com.gpm.project.domain.AclSid;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AclSidRepository extends JpaRepository<AclSid, Long> {
    Optional<AclSid> findBySidTypeAndSidValue(String sidType, String sidValue);
    Optional<AclSid> findBySidValue(String sidValue);
}
