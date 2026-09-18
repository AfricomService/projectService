package com.gpm.project.repository;

import com.gpm.project.domain.Ressource;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ressource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    Page<Ressource> findAllByAgenceIdIn(List<Long> agenceIds, Pageable pageable);

    List<Ressource> findAllByAgenceIdIn(List<Long> agenceIds);
}
