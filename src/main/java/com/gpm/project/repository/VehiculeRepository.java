package com.gpm.project.repository;

import com.gpm.project.domain.Vehicule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicule entity.
 */
@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    default Optional<Vehicule> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vehicule> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vehicule> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vehicule from Vehicule vehicule left join fetch vehicule.agence",
        countQuery = "select count(distinct vehicule) from Vehicule vehicule"
    )
    Page<Vehicule> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vehicule from Vehicule vehicule left join fetch vehicule.agence")
    List<Vehicule> findAllWithToOneRelationships();

    @Query("select vehicule from Vehicule vehicule left join fetch vehicule.agence where vehicule.id =:id")
    Optional<Vehicule> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "select distinct vehicule from Vehicule vehicule left join fetch vehicule.agence agence where agence.societe.id = :societeId",
        countQuery = "select count(distinct vehicule) from Vehicule vehicule where vehicule.agence.societe.id = :societeId"
    )
    Page<Vehicule> findAllBySocieteId(@Param("societeId") Long societeId, Pageable pageable);

    @Query("select distinct vehicule from Vehicule vehicule left join fetch vehicule.agence agence where agence.societe.id = :societeId")
    List<Vehicule> findAllBySocieteId(@Param("societeId") Long societeId);
}
