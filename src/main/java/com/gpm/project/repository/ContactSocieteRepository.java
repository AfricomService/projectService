package com.gpm.project.repository;

import com.gpm.project.domain.ContactSociete;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactSociete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactSocieteRepository extends JpaRepository<ContactSociete, Long> {
    List<ContactSociete> findAllBySocieteId(Long societeId);
    List<ContactSociete> findBySocieteIdAndMatriculeIn(Long societeId, List<String> matricules);

    @Query(
        "select c from ContactSociete c where " +
        "(:societeId is null or c.societeId = :societeId) and " +
        "(" +
        "  (:nomPrenom is null and :matricule is null) or " +
        "  (:nomPrenom is not null and lower(c.nomPrenom) like lower(concat('%', :nomPrenom, '%'))) or " +
        "  (:matricule is not null and lower(c.matricule) like lower(concat('%', :matricule, '%')))" +
        ")"
    )
    List<ContactSociete> search(
        @Param("societeId") Long societeId,
        @Param("nomPrenom") String nomPrenom,
        @Param("matricule") String matricule
    );
}
