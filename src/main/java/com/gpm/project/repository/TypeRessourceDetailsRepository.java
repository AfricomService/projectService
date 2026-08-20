package com.gpm.project.repository;

import com.gpm.project.domain.TypeRessourceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the TypeRessourceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRessourceDetailsRepository extends JpaRepository<TypeRessourceDetails, Long> {

    List<TypeRessourceDetails> findByTypeRessourceId(Long typeRessourceId);

    Optional<TypeRessourceDetails> findByTypeRessourceIdAndDetailRessourceId(Long typeRessourceId, Long detailRessourceId);

    void deleteByTypeRessourceIdAndDetailRessourceId(Long typeRessourceId, Long detailRessourceId);

    void deleteByTypeRessourceId(Long typeRessourceId);

}
