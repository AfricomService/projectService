package com.gpm.project.service;

import com.gpm.project.domain.AclEntryProjet;
import com.gpm.project.domain.AclSid;
import com.gpm.project.repository.AclEntryProjetRepository;
import com.gpm.project.repository.AclSidRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjetAclService {

    private final AclSidRepository aclSidRepository;
    private final AclEntryProjetRepository aclEntryProjetRepository;

    public ProjetAclService(AclSidRepository aclSidRepository, AclEntryProjetRepository aclEntryProjetRepository) {
        this.aclSidRepository = aclSidRepository;
        this.aclEntryProjetRepository = aclEntryProjetRepository;
    }

    // ==========================================
    // 1. GRANT OR UPDATE PERMISSIONS
    // ==========================================

    /**
     * Grant or update permissions for a specific SID (User or Role) on a Project.
     */
    public AclEntryProjet grantOrUpdatePermission(
        Long projectId,
        String sidType,
        String sidValue,
        String nomDescriptif,
        boolean canRead,
        boolean canWrite
    ) {
        // Ensure the SID exists in acl_sid, or create it dynamically
        AclSid sid = aclSidRepository
            .findBySidTypeAndSidValue(sidType, sidValue)
            .orElseGet(() -> {
                AclSid newSid = new AclSid();
                newSid.setSidType(sidType);
                newSid.setSidValue(sidValue);
                newSid.setNomDescriptif(nomDescriptif);
                return aclSidRepository.save(newSid);
            });

        // Find existing ACL entry or create a new one
        AclEntryProjet entry = aclEntryProjetRepository
            .findByProject_idAndSid_id(projectId, sid.getId())
            .orElseGet(() -> {
                AclEntryProjet newEntry = new AclEntryProjet();
                newEntry.setProject_id(projectId);
                newEntry.setSid_id(sid.getId());
                newEntry.setSid(sidValue);
                return newEntry;
            });

        entry.setCan_read(canRead);
        entry.setCan_write(canWrite);

        return aclEntryProjetRepository.save(entry);
    }

    /**
     * Helper shortcut to grant read permission only.
     */
    public AclEntryProjet grantReadPermission(Long projectId, String sidType, String sidValue) {
        return grantOrUpdatePermission(projectId, sidType, sidValue, null, true, false);
    }

    /**
     * Helper shortcut to grant read and write permissions.
     */
    public AclEntryProjet grantReadWritePermission(Long projectId, String sidType, String sidValue) {
        return grantOrUpdatePermission(projectId, sidType, sidValue, null, true, true);
    }

    // ==========================================
    // 2. CHECK PERMISSIONS
    // ==========================================

    /**
     * Check if any of the provided SIDs (e.g., username + active authorities) have READ access.
     */
    @Transactional(readOnly = true)
    public boolean canRead(Long projectId, Collection<String> userSids) {
        if (userSids == null || userSids.isEmpty()) return false;
        return aclEntryProjetRepository.hasReadPermission(projectId, userSids);
    }

    /**
     * Check if any of the provided SIDs have WRITE access.
     */
    @Transactional(readOnly = true)
    public boolean canWrite(Long projectId, Collection<String> userSids) {
        if (userSids == null || userSids.isEmpty()) return false;
        return aclEntryProjetRepository.hasWritePermission(projectId, userSids);
    }

    /**
     * Single SID read check shortcut.
     */
    @Transactional(readOnly = true)
    public boolean canRead(Long projectId, String sidValue) {
        return canRead(projectId, Collections.singletonList(sidValue));
    }

    /**
     * Single SID write check shortcut.
     */
    @Transactional(readOnly = true)
    public boolean canWrite(Long projectId, String sidValue) {
        return canWrite(projectId, Collections.singletonList(sidValue));
    }

    // ==========================================
    // 3. RETRIEVE ACCESSIBLE PROJECTS (FOR LISTING/SEARCH)
    // ==========================================

    /**
     * Returns all Project IDs the given user/role SIDs are allowed to read.
     * Use this in your ProjetRepository to filter results: `WHERE p.id IN (:accessibleIds)`
     */
    @Transactional(readOnly = true)
    public List<Long> getReadableProjectIds(Collection<String> userSids) {
        if (userSids == null || userSids.isEmpty()) return Collections.emptyList();
        return aclEntryProjetRepository.findReadableProjectIdsBySids(userSids);
    }

    /**
     * Returns all Project IDs the given user/role SIDs are allowed to write.
     */
    @Transactional(readOnly = true)
    public List<Long> getWritableProjectIds(Collection<String> userSids) {
        if (userSids == null || userSids.isEmpty()) return Collections.emptyList();
        return aclEntryProjetRepository.findWritableProjectIdsBySids(userSids);
    }

    // ==========================================
    // 4. REVOKE & CLEANUP PERMISSIONS
    // ==========================================

    /**
     * Revoke access for a single SID on a project.
     */
    public void revokePermission(Long projectId, Long sidId) {
        aclEntryProjetRepository.deleteByProject_idAndSid_id(projectId, sidId);
    }

    /**
     * Revoke access for a SID by value (e.g. username or role name).
     */
    public void revokePermissionForSidValue(Long projectId, String sidValue) {
        aclEntryProjetRepository.findByProject_idAndSid(projectId, sidValue).ifPresent(aclEntryProjetRepository::delete);
    }

    /**
     * Delete all permissions for a project (useful when a Project entity is deleted).
     */
    public void deleteAclForProject(Long projectId) {
        aclEntryProjetRepository.deleteByProject_id(projectId);
    }

    // ==========================================
    // 5. INSPECT / AUDIT
    // ==========================================

    /**
     * Get all ACL entries associated with a specific project.
     */
    @Transactional(readOnly = true)
    public List<AclEntryProjet> getAclEntriesForProject(Long projectId) {
        return aclEntryProjetRepository.findByProject_id(projectId);
    }
}
