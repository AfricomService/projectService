package com.gpm.project.repository;

import com.gpm.project.domain.MatriceFacturation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MatriceFacturation entity.
 */
@Repository
public interface MatriceFacturationRepository extends JpaRepository<MatriceFacturation, Long> {
    default Optional<MatriceFacturation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    List<MatriceFacturation> findAllByAffaireId(Long affaireId);

    default Page<MatriceFacturation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matriceFacturation from MatriceFacturation matriceFacturation left join fetch matriceFacturation.affaire left join fetch matriceFacturation.ville left join fetch matriceFacturation.zone",
        countQuery = "select count(distinct matriceFacturation) from MatriceFacturation matriceFacturation"
    )
    Page<MatriceFacturation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct matriceFacturation from MatriceFacturation matriceFacturation left join fetch matriceFacturation.affaire left join fetch matriceFacturation.ville left join fetch matriceFacturation.zone"
    )
    List<MatriceFacturation> findAllWithToOneRelationships();

    @Query(
        "select matriceFacturation from MatriceFacturation matriceFacturation left join fetch matriceFacturation.affaire left join fetch matriceFacturation.ville left join fetch matriceFacturation.zone where matriceFacturation.id =:id"
    )
    Optional<MatriceFacturation> findOneWithToOneRelationships(@Param("id") Long id);
}
