package com.gpm.project.repository;

import com.gpm.project.domain.RapportTemplateAffaire;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RapportTemplateAffaireRepository extends JpaRepository<RapportTemplateAffaire, Long> {

    List<RapportTemplateAffaire> findByAffaireId(Long affaireId);

    void deleteByAffaireId(Long affaireId);

    boolean existsByAffaireIdAndRapportTemplateId(Long affaireId, Long rapportTemplateId);
}
