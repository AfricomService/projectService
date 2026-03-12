package com.gpm.project.repository;

import com.gpm.project.domain.MatriceJourFerie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MatriceJourFerie entity.
 */
@Repository
public interface MatriceJourFerieRepository extends JpaRepository<MatriceJourFerie, Long> {
    default Optional<MatriceJourFerie> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MatriceJourFerie> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MatriceJourFerie> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matriceJourFerie from MatriceJourFerie matriceJourFerie left join fetch matriceJourFerie.jourFerie",
        countQuery = "select count(distinct matriceJourFerie) from MatriceJourFerie matriceJourFerie"
    )
    Page<MatriceJourFerie> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct matriceJourFerie from MatriceJourFerie matriceJourFerie left join fetch matriceJourFerie.jourFerie")
    List<MatriceJourFerie> findAllWithToOneRelationships();

    @Query(
        "select matriceJourFerie from MatriceJourFerie matriceJourFerie left join fetch matriceJourFerie.jourFerie where matriceJourFerie.id =:id"
    )
    Optional<MatriceJourFerie> findOneWithToOneRelationships(@Param("id") Long id);
}
