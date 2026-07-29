package com.gpm.project.repository;

import com.gpm.project.domain.Article;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByCode(String code);

    // Gets articles by affaireId. The :searchTerm filters by designation (you can expand this to code, etc.)
    @Query(
        "SELECT aa.article FROM AffaireArticle aa " +
        "WHERE aa.affaire.id = :affaireId " +
        "AND (:searchTerm IS NULL OR " +
        "LOWER(aa.article.designation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(aa.article.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
        "LOWER(aa.article.codeClient) LIKE LOWER(CONCAT('%', :searchTerm, '%')))"
    )
    Page<Article> findArticlesByAffaireIdFiltered(
        @Param("affaireId") Long affaireId,
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );
}
