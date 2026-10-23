package com.gpm.project.repository;

import com.gpm.project.domain.Ville;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ville entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    Optional<Ville> findByNomIgnoreCase(String nom);

    List<Ville> findAllByZoneId(Long zoneId);
}
