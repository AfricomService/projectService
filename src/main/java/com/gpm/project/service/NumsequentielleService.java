package com.gpm.project.service;

import com.gpm.project.domain.Numsequentielle;
import com.gpm.project.repository.NumsequentielleRepository;
import com.gpm.project.service.dto.NumsequentielleDTO;
import com.gpm.project.service.mapper.NumsequentielleMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Numsequentielle}.
 */
@Service
@Transactional
public class NumsequentielleService {

    private final Logger log = LoggerFactory.getLogger(NumsequentielleService.class);

    private final NumsequentielleRepository numsequentielleRepository;

    private final NumsequentielleMapper numsequentielleMapper;

    private final Configuration freemarkerConfig;

    public NumsequentielleService(NumsequentielleRepository numsequentielleRepository, NumsequentielleMapper numsequentielleMapper) {
        this.numsequentielleRepository = numsequentielleRepository;
        this.numsequentielleMapper = numsequentielleMapper;
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        this.freemarkerConfig.setDefaultEncoding("UTF-8");
        this.freemarkerConfig.setNumberFormat("computer");
    }

    /**
     * Save a numsequentielle.
     *
     * @param numsequentielleDTO the entity to save.
     * @return the persisted entity.
     */
    public NumsequentielleDTO save(NumsequentielleDTO numsequentielleDTO) {
        log.debug("Request to save Numsequentielle : {}", numsequentielleDTO);
        Numsequentielle numsequentielle = numsequentielleMapper.toEntity(numsequentielleDTO);
        numsequentielle = numsequentielleRepository.save(numsequentielle);
        return numsequentielleMapper.toDto(numsequentielle);
    }

    /**
     * Partially update a numsequentielle.
     *
     * @param numsequentielleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NumsequentielleDTO> partialUpdate(NumsequentielleDTO numsequentielleDTO) {
        log.debug("Request to partially update Numsequentielle : {}", numsequentielleDTO);

        return numsequentielleRepository
            .findById(numsequentielleDTO.getId())
            .map(existingNumsequentielle -> {
                numsequentielleMapper.partialUpdate(existingNumsequentielle, numsequentielleDTO);

                return existingNumsequentielle;
            })
            .map(numsequentielleRepository::save)
            .map(numsequentielleMapper::toDto);
    }

    /**
     * Get all the numsequentielles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NumsequentielleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Numsequentielles");
        return numsequentielleRepository.findAll(pageable).map(numsequentielleMapper::toDto);
    }

    /**
     * Get one numsequentielle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NumsequentielleDTO> findOne(Long id) {
        log.debug("Request to get Numsequentielle : {}", id);
        return numsequentielleRepository.findById(id).map(numsequentielleMapper::toDto);
    }

    /**
     * Delete the numsequentielle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Numsequentielle : {}", id);
        numsequentielleRepository.deleteById(id);
    }

    /**
     * Génère et incrémente l'identifiant unique client.
     * Format attendu : C-00001-26
     *
     * @return l'identifiant généré.
     */
    public String genererIdentifiantClient() {
        log.debug("Request to generate identifiant for Client");
        return genererIdentifiant("CLIENT");
    }

    public String genererIdentifiantSociete() {
        log.debug("Request to generate identifiant for Societe");
        return genererIdentifiant("SOCIETE");
    }

    /**
     * Génère et incrémente l'identifiant unique agence.
     * Format attendu : A-00001-26
     *
     * @return l'identifiant généré.
     */
    public String genererIdentifiantAgence() {
        log.debug("Request to generate identifiant for Agence");
        return genererIdentifiant("AGENCE");
    }

    /**
     * Génère et incrémente l'identifiant unique bon de commande.
     * Format attendu : BC-0001-26
     *
     * @return l'identifiant généré.
     */
    public String genererIdentifiantBonCommande() {
        log.debug("Request to generate identifiant for BonCommande");
        return genererIdentifiant("BONCOMMANDE");
    }

    /**
     * Génère et incrémente l'identifiant unique OT Externe.
     * Format attendu : OT-0001-26
     *
     * @return l'identifiant généré.
     */
    public String genererIdentifiantOtExterne() {
        log.debug("Request to generate identifiant for OtExterne");
        return genererIdentifiant("OT_EXTERNE");
    }

    /**
     * Génère et incrémente l'identifiant unique WorkOrder.
     * Format attendu : WO-0001-26
     *
     * @return l'identifiant généré.
     */
    public String genererIdentifiantWorkOrder() {
        log.debug("Request to generate identifiant for WorkOrder");
        return genererIdentifiant("WORK_ORDER");
    }

    /**
     * Génère et incrémente un identifiant unique à partir d'un codeNumSeq donné.
     * Si un format FreeMarker est renseigné sur la séquence, il est utilisé pour
     * construire l'identifiant. Sinon, on retombe sur le format historique :
     * {prefix}-{numero sur 4 chiffres}-{2 derniers chiffres de l'année}.
     *
     * @param codeNumSeq le code de la séquence à utiliser (ex: "CLIENT", "AGENCE").
     * @return l'identifiant généré.
     */
    private String genererIdentifiant(String codeNumSeq) {
        Numsequentielle seq = numsequentielleRepository
            .findByCodeNumSeq(codeNumSeq)
            .orElseThrow(() -> new RuntimeException("Numsequentielle introuvable pour codeNumSeq : " + codeNumSeq));

        Long currentNumber = seq.getNextNumber();
        String prefix = seq.getPrefix() != null ? seq.getPrefix() : "";
        int shortYear = LocalDate.now().getYear() % 100;
        int fullYear = LocalDate.now().getYear();

        String identifiant = null;

        if (seq.getFormat() != null && !seq.getFormat().isBlank()) {
            identifiant = genererIdentifiantAvecFormat(seq.getFormat(), codeNumSeq, prefix, currentNumber, shortYear, fullYear);
        }

        if (identifiant == null) {
            String numeroFormatte = String.format("%04d", currentNumber);
            identifiant = prefix + "-" + numeroFormatte + "-" + String.format("%02d", shortYear);
        }

        seq.setNextNumber(currentNumber + 1);
        numsequentielleRepository.save(seq);

        log.debug("Identifiant généré pour {} : {}", codeNumSeq, identifiant);
        return identifiant;
    }

    /**
     * Rend l'identifiant à partir d'un template FreeMarker stocké sur la séquence.
     * Variables disponibles dans le template :
     *   prefix         -> préfixe de la séquence (ex: "WO")
     *   numero         -> numéro brut, non formaté (ex: 12)
     *   annee          -> 2 derniers chiffres de l'année (ex: 26)
     *   anneeComplete  -> année sur 4 chiffres (ex: 2026)
     *   codeNumSeq     -> code de la séquence (ex: "WORK_ORDER")
     *
     * Exemple de format stocké en base :
     *   {@code ${prefix}-${numero?string("00000")}-${annee}}
     *   -> "WO-00012-26"
     *
     * @return l'identifiant rendu, ou {@code null} si le rendu échoue (le format
     *         est alors ignoré et on retombe sur la logique historique).
     */
    private String genererIdentifiantAvecFormat(
        String format,
        String codeNumSeq,
        String prefix,
        Long numero,
        int shortYear,
        int fullYear
    ) {
        try {
            Template template = new Template("numseq-" + codeNumSeq, new StringReader(format), freemarkerConfig);

            Map<String, Object> model = new HashMap<>();
            model.put("prefix", prefix);
            model.put("numero", numero);
            model.put("annee", shortYear);
            model.put("anneeComplete", fullYear);
            model.put("codeNumSeq", codeNumSeq);

            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("Erreur lors du rendu du format FreeMarker pour codeNumSeq={} : {}", codeNumSeq, e.getMessage());
            return null;
        }
    }

    /**
     * Prévisualise le rendu d'un format FreeMarker sans consommer le compteur
     * (utilise nextNumber actuel à titre d'exemple, sans l'incrémenter).
     *
     * @param format le template FreeMarker à tester.
     * @param codeNumSeq la séquence dont on veut utiliser prefix/nextNumber comme exemple.
     * @return l'identifiant simulé.
     */
    @Transactional(readOnly = true)
    public String previewFormat(String format, String codeNumSeq) {
        Numsequentielle seq = numsequentielleRepository
            .findByCodeNumSeq(codeNumSeq)
            .orElseThrow(() -> new RuntimeException("Numsequentielle introuvable pour codeNumSeq : " + codeNumSeq));

        String prefix = seq.getPrefix() != null ? seq.getPrefix() : "";
        int shortYear = LocalDate.now().getYear() % 100;
        int fullYear = LocalDate.now().getYear();

        String result = genererIdentifiantAvecFormat(format, codeNumSeq, prefix, seq.getNextNumber(), shortYear, fullYear);

        if (result == null) {
            throw new RuntimeException("Format FreeMarker invalide");
        }

        return result;
    }

    String genererIdentifiantAffaire(String codeNumSeq) {
        Numsequentielle seq = numsequentielleRepository
            .findByCodeNumSeq(codeNumSeq)
            .orElseThrow(() -> new RuntimeException("Numsequentielle introuvable pour codeNumSeq : " + codeNumSeq));

        Long currentNumber = seq.getNextNumber();

        String numeroFormatte = String.format("%04d", currentNumber);
        String annee = String.format("%02d", LocalDate.now().getYear() % 100);
        String prefix = seq.getPrefix() != null ? seq.getPrefix() : "";

        // Exemple : A260001
        String identifiant = prefix + annee + numeroFormatte;

        seq.setNextNumber(currentNumber + 1);
        numsequentielleRepository.save(seq);

        log.debug("Identifiant généré pour {} : {}", codeNumSeq, identifiant);
        return identifiant;
    }
}
