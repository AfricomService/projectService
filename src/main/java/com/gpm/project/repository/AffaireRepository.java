package com.gpm.project.repository;

import com.gpm.project.domain.Affaire;
import java.util.List;
import java.util.Optional;

import com.gpm.project.domain.enumeration.StatutAffaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Affaire entity.
 */
@Repository
public interface AffaireRepository extends JpaRepository<Affaire, Long> {
    default Optional<Affaire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Affaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Affaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct affaire from Affaire affaire left join fetch affaire.client",
        countQuery = "select count(distinct affaire) from Affaire affaire"
    )
    Page<Affaire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct affaire from Affaire affaire left join fetch affaire.client")
    List<Affaire> findAllWithToOneRelationships();

    @Query("select affaire from Affaire affaire left join fetch affaire.client where affaire.id =:id")
    Optional<Affaire> findOneWithToOneRelationships(@Param("id") Long id);

    List<Affaire> findByClientId(Long clientId);

    List<Affaire> findByClientIdAndDesignationAffaireContainingIgnoreCase(Long clientId, String designationAffaire);

    @Query(
        value = "select distinct affaire from Affaire affaire left join fetch affaire.client " +
            "where affaire.statut = :statut " +
            "and (:search is null or :search = '' " +
            "     or lower(affaire.designationAffaire) like lower(concat('%', :search, '%')) " +
            "     or lower(affaire.identifiantUnique) like lower(concat('%', :search, '%')) " +
            "     or lower(affaire.client.raisonSociale) like lower(concat('%', :search, '%')) " +
            "     or str(affaire.numAffaire) like concat('%', :search, '%'))",
        countQuery = "select count(distinct affaire) from Affaire affaire " +
            "where affaire.statut = :statut " +
            "and (:search is null or :search = '' " +
            "     or lower(affaire.designationAffaire) like lower(concat('%', :search, '%')) " +
            "     or lower(affaire.identifiantUnique) like lower(concat('%', :search, '%')) " +
            "     or lower(affaire.client.raisonSociale) like lower(concat('%', :search, '%')) " +
            "     or str(affaire.numAffaire) like concat('%', :search, '%'))"
    )
    Page<Affaire> findByStatutAndSearch(@Param("statut") StatutAffaire statut, @Param("search") String search, Pageable pageable);

}
