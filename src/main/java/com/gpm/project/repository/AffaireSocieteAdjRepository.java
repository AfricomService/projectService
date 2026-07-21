package com.gpm.project.repository;

import com.gpm.project.domain.AffaireSocieteAdj;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AffaireSocieteAdj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffaireSocieteAdjRepository extends JpaRepository<AffaireSocieteAdj, Long> {
    @Modifying
    @Query("DELETE FROM AffaireSocieteAdj u WHERE u.affaireId = :affaireId")
    void deleteByAffaireId(@Param("affaireId") Long affaireId);

    @Query("SELECT u FROM AffaireSocieteAdj u WHERE u.affaireId = :affaireId")
    List<AffaireSocieteAdj> findAllByAffaireId(@Param("affaireId") Long affaireId);
}
