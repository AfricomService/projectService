package com.gpm.project.repository;

import com.gpm.project.domain.Agence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Agence entity.
 */
@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {
    default Optional<Agence> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Agence> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Agence> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct agence from Agence agence left join fetch agence.societe",
        countQuery = "select count(distinct agence) from Agence agence"
    )
    Page<Agence> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct agence from Agence agence left join fetch agence.societe")
    List<Agence> findAllWithToOneRelationships();

    @Query("select agence from Agence agence left join fetch agence.societe where agence.id =:id")
    Optional<Agence> findOneWithToOneRelationships(@Param("id") Long id);
}
