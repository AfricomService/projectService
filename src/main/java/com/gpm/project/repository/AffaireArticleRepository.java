package com.gpm.project.repository;

import com.gpm.project.domain.AffaireArticle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AffaireArticle entity.
 */
@Repository
public interface AffaireArticleRepository extends JpaRepository<AffaireArticle, Long> {
    default Optional<AffaireArticle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AffaireArticle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AffaireArticle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct affaireArticle from AffaireArticle affaireArticle left join fetch affaireArticle.affaire left join fetch affaireArticle.article",
        countQuery = "select count(distinct affaireArticle) from AffaireArticle affaireArticle"
    )
    Page<AffaireArticle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct affaireArticle from AffaireArticle affaireArticle left join fetch affaireArticle.affaire left join fetch affaireArticle.article"
    )
    List<AffaireArticle> findAllWithToOneRelationships();

    @Query(
        "select affaireArticle from AffaireArticle affaireArticle left join fetch affaireArticle.affaire left join fetch affaireArticle.article where affaireArticle.id =:id"
    )
    Optional<AffaireArticle> findOneWithToOneRelationships(@Param("id") Long id);

    List<AffaireArticle> findByAffaireId(Long affaireId);
}
