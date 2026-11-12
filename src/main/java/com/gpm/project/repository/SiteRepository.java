package com.gpm.project.repository;

import com.gpm.project.domain.Site;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gpm.project.service.dto.SiteListDTO;

/**
 * Spring Data JPA repository for the Site entity.
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    default Optional<Site> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Site> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Site> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct site from Site site left join fetch site.ville left join fetch site.client",
        countQuery = "select count(distinct site) from Site site"
    )
    Page<Site> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct site from Site site left join fetch site.ville left join fetch site.client")
    List<Site> findAllWithToOneRelationships();

    @Query("select site from Site site left join fetch site.ville left join fetch site.client where site.id =:id")
    Optional<Site> findOneWithToOneRelationships(@Param("id") Long id);

    Page<Site> findByClientId(Long clientId, Pageable pageable);

    Page<Site> findByClientIdAndDesignationContainingIgnoreCase(Long clientId, String designation, Pageable pageable);

    @Query(
        value = "select new com.gpm.project.service.dto.SiteListDTO(s.id, s.code, s.designation) " +
            "from Site s where s.client.id = :clientId",
        countQuery = "select count(s) from Site s where s.client.id = :clientId"
    )
    Page<SiteListDTO> findListByClientId(@Param("clientId") Long clientId, Pageable pageable);

    @Query(
        value = "select new com.gpm.project.service.dto.SiteListDTO(s.id, s.code, s.designation) " +
            "from Site s where s.client.id = :clientId " +
            "and lower(s.designation) like lower(concat('%', :designation, '%'))",
        countQuery = "select count(s) from Site s where s.client.id = :clientId " +
            "and lower(s.designation) like lower(concat('%', :designation, '%'))"
    )
    Page<SiteListDTO> searchListByClientId(
        @Param("clientId") Long clientId,
        @Param("designation") String designation,
        Pageable pageable
    );

    boolean existsByClientIdAndCode(Long clientId, String code);

    Optional<Site> findFirstByClientIdAndCode(Long clientId, String code);

    boolean existsByClientIdAndCodeAndIdNot(Long clientId, String code, Long id);
}
