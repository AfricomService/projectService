package com.gpm.project.service;

import com.gpm.project.config.ApplicationProperties;
import com.gpm.project.domain.PieceJointe;
import com.gpm.project.repository.PieceJointeRepository;
import com.gpm.project.service.dto.PieceJointeDTO;
import com.gpm.project.service.mapper.PieceJointeMapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeService.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    private final ApplicationProperties applicationProperties;

    public PieceJointeService(
        PieceJointeRepository pieceJointeRepository,
        PieceJointeMapper pieceJointeMapper,
        ApplicationProperties applicationProperties
    ) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJointeDTO save(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to save PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Update a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJointeDTO update(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to update PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Partially update a pieceJointe.
     *
     * @param pieceJointeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointeDTO);

        return pieceJointeRepository
            .findById(pieceJointeDTO.getId())
            .map(existingPieceJointe -> {
                pieceJointeMapper.partialUpdate(existingPieceJointe, pieceJointeDTO);

                return existingPieceJointe;
            })
            .map(pieceJointeRepository::save)
            .map(pieceJointeMapper::toDto);
    }

    /**
     * Get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PieceJointeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll(pageable).map(pieceJointeMapper::toDto);
    }

    /**
     * Get all the pieceJointes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PieceJointeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pieceJointeRepository.findAllWithEagerRelationships(pageable).map(pieceJointeMapper::toDto);
    }

    /**
     * Get one pieceJointe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PieceJointeDTO> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findOneWithEagerRelationships(id).map(pieceJointeMapper::toDto);
    }

    /**
     * Delete the pieceJointe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }

    // Ajouts à PieceJointeService.java — imports nécessaires en plus de l'existant :
// java.io.File, java.io.IOException, java.net.MalformedURLException,
// java.nio.file.Files, java.nio.file.Path, java.nio.file.Paths, java.nio.file.StandardCopyOption,
// java.time.format.DateTimeFormatter, java.util.List, java.util.stream.Collectors,
// org.springframework.core.io.Resource, org.springframework.core.io.UrlResource,
// org.springframework.http.HttpHeaders, org.springframework.http.ResponseEntity,
// org.springframework.web.multipart.MultipartFile
// + injecter ApplicationProperties applicationProperties dans le constructeur

    /**
     * Upload physique d'un fichier lié à un BonCommande.
     * Écrit le fichier sur disque et persiste une PieceJointe avec fichierURL = chemin absolu.
     */
    public PieceJointeDTO uploadForBonCommande(MultipartFile file, Long bonCommandeId, String uniqueName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String dirPath =
            Paths.get(applicationProperties.getDirPieceJointe()) +
                File.separator +
                ZonedDateTime.now().getYear() +
                File.separator +
                ZonedDateTime.now().getMonth() +
                File.separator +
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Path finalDir = Paths.get(dirPath);
        if (!Files.exists(finalDir)) {
            Files.createDirectories(finalDir);
        }

        Path filePath = finalDir.resolve(uniqueName + "." + extension);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        PieceJointeDTO dto = new PieceJointeDTO();
        dto.setNomFichier(originalFilename.substring(0, originalFilename.lastIndexOf(".")));
        dto.setType(extension);
        dto.setFichierURL(filePath.toString());
        dto.setDateUpload(ZonedDateTime.now());
        dto.setBonCommandeId(bonCommandeId);

        return save(dto);
    }

    @Transactional(readOnly = true)
    public List<PieceJointeDTO> findByBonCommandeId(Long bonCommandeId) {
        log.debug("Request to get PieceJointes by BonCommande : {}", bonCommandeId);
        return pieceJointeRepository.findAllByBonCommandeId(bonCommandeId)
            .stream()
            .map(pieceJointeMapper::toDto)
            .collect(Collectors.toList());
    }

    public ResponseEntity<Resource> getFile(Long pieceJointeId) {
        try {
            PieceJointeDTO dto = findOne(pieceJointeId).orElseThrow();
            Path finalPath = Paths.get(dto.getFichierURL());
            Resource resource = new UrlResource(finalPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Renomme une pièce jointe (champ nomFichier uniquement — l'extension et
     * le chemin physique du fichier sur disque ne changent pas).
     *
     * @param id l'id de la PieceJointe.
     * @param newName le nouveau nom (sans extension).
     * @return la PieceJointeDTO mise à jour.
     */
    public PieceJointeDTO renamePieceJointe(Long id, String newName) {
        log.debug("Request to rename PieceJointe {} -> {}", id, newName);

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }

        PieceJointe pieceJointe = pieceJointeRepository
            .findById(id)
            .orElseThrow(() -> new javax.persistence.EntityNotFoundException("PieceJointe not found with id " + id));

        pieceJointe.setNomFichier(newName.trim());

        PieceJointe saved = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(saved);
    }
}
