package com.gpm.project.repository;

import com.gpm.project.domain.RapportTemplateAffaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RapportTemplateAffaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportTemplateAffaireRepository extends JpaRepository<RapportTemplateAffaire, Long> {}
