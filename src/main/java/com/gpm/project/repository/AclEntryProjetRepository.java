package com.gpm.project.repository;

import com.gpm.project.domain.AclEntryProjet;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AclEntryProjetRepository extends JpaRepository<AclEntryProjet, Long> {
    Optional<AclEntryProjet> findByProject_idAndSid_id(Long projectId, Long sidId);

    Optional<AclEntryProjet> findByProject_idAndSid(Long projectId, String sid);

    List<AclEntryProjet> findByProject_id(Long projectId);

    List<AclEntryProjet> findBySid_id(Long sidId);

    void deleteByProject_id(Long projectId);

    void deleteByProject_idAndSid_id(Long projectId, Long sidId);

    // Retrieve readable project IDs for a given list of SIDs (e.g., username + user roles)
    @Query("SELECT DISTINCT e.project_id FROM AclEntryProjet e WHERE e.sid IN :sids AND e.can_read = true")
    List<Long> findReadableProjectIdsBySids(@Param("sids") Collection<String> sids);

    // Retrieve writable project IDs for a given list of SIDs
    @Query("SELECT DISTINCT e.project_id FROM AclEntryProjet e WHERE e.sid IN :sids AND e.can_write = true")
    List<Long> findWritableProjectIdsBySids(@Param("sids") Collection<String> sids);

    // Check read access across multiple SIDs for a single project
    @Query(
        "SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM AclEntryProjet e " +
        "WHERE e.project_id = :projectId AND e.sid IN :sids AND e.can_read = true"
    )
    boolean hasReadPermission(@Param("projectId") Long projectId, @Param("sids") Collection<String> sids);

    // Check write access across multiple SIDs for a single project
    @Query(
        "SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM AclEntryProjet e " +
        "WHERE e.project_id = :projectId AND e.sid IN :sids AND e.can_write = true"
    )
    boolean hasWritePermission(@Param("projectId") Long projectId, @Param("sids") Collection<String> sids);
}
