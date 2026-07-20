package com.gpm.project.service;

import com.gpm.project.domain.Numsequentielle;
import com.gpm.project.repository.NumsequentielleRepository;
import com.gpm.project.service.dto.NumsequentielleDTO;
import com.gpm.project.service.mapper.NumsequentielleMapper;

import java.time.LocalDate;
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

    public NumsequentielleService(NumsequentielleRepository numsequentielleRepository, NumsequentielleMapper numsequentielleMapper) {
        this.numsequentielleRepository = numsequentielleRepository;
        this.numsequentielleMapper = numsequentielleMapper;
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

        Numsequentielle seq = numsequentielleRepository
            .findByCodeNumSeq("CLIENT")
            .orElseThrow(() -> new RuntimeException("Numsequentielle introuvable pour codeNumSeq : CLIENT"));

        Long currentNumber = seq.getNextNumber();

        String numeroFormatte = String.format("%05d", currentNumber);
        int shortYear = LocalDate.now().getYear() % 100;
        String prefix = seq.getPrefix() != null ? seq.getPrefix() : "";
        String identifiant = prefix + "-" + numeroFormatte + "-" + String.format("%02d", shortYear);

        seq.setNextNumber(currentNumber + 1);
        numsequentielleRepository.save(seq);

        log.debug("Identifiant généré : {}", identifiant);
        return identifiant;
    }
}
