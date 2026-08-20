package com.gpm.project.service;

import com.gpm.project.domain.TypeRessourceDetails;
import com.gpm.project.repository.DetailRessourceRepository;
import com.gpm.project.repository.TypeRessourceDetailsRepository;
import com.gpm.project.service.dto.DetailRessourceDTO;
import com.gpm.project.service.dto.TypeRessourceDetailsDTO;
import com.gpm.project.service.mapper.DetailRessourceMapper;
import com.gpm.project.service.mapper.TypeRessourceDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeRessourceDetails}.
 */
@Service
@Transactional
public class TypeRessourceDetailsService {

    private final Logger log = LoggerFactory.getLogger(TypeRessourceDetailsService.class);

    private final TypeRessourceDetailsRepository typeRessourceDetailsRepository;

    private final TypeRessourceDetailsMapper typeRessourceDetailsMapper;

    private final DetailRessourceRepository detailRessourceRepository;

    private final DetailRessourceMapper detailRessourceMapper;

    public TypeRessourceDetailsService(
        TypeRessourceDetailsRepository typeRessourceDetailsRepository,
        TypeRessourceDetailsMapper typeRessourceDetailsMapper,
        DetailRessourceRepository detailRessourceRepository,
        DetailRessourceMapper detailRessourceMapper
    ) {
        this.typeRessourceDetailsRepository = typeRessourceDetailsRepository;
        this.typeRessourceDetailsMapper = typeRessourceDetailsMapper;
        this.detailRessourceRepository = detailRessourceRepository;
        this.detailRessourceMapper = detailRessourceMapper;
    }

    /**
     * Save a typeRessourceDetails.
     *
     * @param typeRessourceDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeRessourceDetailsDTO save(TypeRessourceDetailsDTO typeRessourceDetailsDTO) {
        log.debug("Request to save TypeRessourceDetails : {}", typeRessourceDetailsDTO);
        TypeRessourceDetails typeRessourceDetails = typeRessourceDetailsMapper.toEntity(typeRessourceDetailsDTO);
        typeRessourceDetails = typeRessourceDetailsRepository.save(typeRessourceDetails);
        return typeRessourceDetailsMapper.toDto(typeRessourceDetails);
    }

    /**
     * Partially update a typeRessourceDetails.
     *
     * @param typeRessourceDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeRessourceDetailsDTO> partialUpdate(TypeRessourceDetailsDTO typeRessourceDetailsDTO) {
        log.debug("Request to partially update TypeRessourceDetails : {}", typeRessourceDetailsDTO);

        return typeRessourceDetailsRepository
            .findById(typeRessourceDetailsDTO.getId())
            .map(existingTypeRessourceDetails -> {
                typeRessourceDetailsMapper.partialUpdate(existingTypeRessourceDetails, typeRessourceDetailsDTO);

                return existingTypeRessourceDetails;
            })
            .map(typeRessourceDetailsRepository::save)
            .map(typeRessourceDetailsMapper::toDto);
    }

    /**
     * Get all the typeRessourceDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeRessourceDetailsDTO> findAll() {
        log.debug("Request to get all TypeRessourceDetails");
        return typeRessourceDetailsRepository
            .findAll()
            .stream()
            .map(typeRessourceDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one typeRessourceDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeRessourceDetailsDTO> findOne(Long id) {
        log.debug("Request to get TypeRessourceDetails : {}", id);
        return typeRessourceDetailsRepository.findById(id).map(typeRessourceDetailsMapper::toDto);
    }

    /**
     * Delete the typeRessourceDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeRessourceDetails : {}", id);
        typeRessourceDetailsRepository.deleteById(id);
    }

    /**
     * Récupère les DetailRessource affectés à un TypeRessource donné.
     *
     * @param typeRessourceId l'id du TypeRessource.
     * @return la liste des DetailRessourceDTO affectés.
     */
    @Transactional(readOnly = true)
    public List<DetailRessourceDTO> getDetailsForType(Long typeRessourceId) {
        log.debug("Request to get details for TypeRessource : {}", typeRessourceId);
        List<Long> detailIds = typeRessourceDetailsRepository
            .findByTypeRessourceId(typeRessourceId)
            .stream()
            .map(TypeRessourceDetails::getDetailRessourceId)
            .collect(Collectors.toList());
        return detailRessourceMapper.toDto(detailRessourceRepository.findAllById(detailIds));
    }

    /**
     * Ajoute une affectation unique (idempotent : ne duplique pas si déjà existante).
     *
     * @param typeRessourceId   l'id du TypeRessource.
     * @param detailRessourceId l'id du DetailRessource à affecter.
     */
    public void addDetail(Long typeRessourceId, Long detailRessourceId) {
        log.debug("Request to add detail {} to TypeRessource {}", detailRessourceId, typeRessourceId);
        typeRessourceDetailsRepository
            .findByTypeRessourceIdAndDetailRessourceId(typeRessourceId, detailRessourceId)
            .orElseGet(() -> {
                TypeRessourceDetails link = new TypeRessourceDetails();
                link.setTypeRessourceId(typeRessourceId);
                link.setDetailRessourceId(detailRessourceId);
                return typeRessourceDetailsRepository.save(link);
            });
    }

    /**
     * Retire une affectation unique.
     *
     * @param typeRessourceId   l'id du TypeRessource.
     * @param detailRessourceId l'id du DetailRessource à retirer.
     */
    public void removeDetail(Long typeRessourceId, Long detailRessourceId) {
        log.debug("Request to remove detail {} from TypeRessource {}", detailRessourceId, typeRessourceId);
        typeRessourceDetailsRepository.deleteByTypeRessourceIdAndDetailRessourceId(typeRessourceId, detailRessourceId);
    }

    /**
     * Remplace toute la liste des affectations d'un TypeRessource par la nouvelle liste fournie.
     *
     * @param typeRessourceId    l'id du TypeRessource.
     * @param detailRessourceIds la nouvelle liste d'ids de DetailRessource à affecter.
     * @return la liste des DetailRessourceDTO affectés après remplacement.
     */
    public List<DetailRessourceDTO> replaceDetails(Long typeRessourceId, List<Long> detailRessourceIds) {
        log.debug("Request to replace details for TypeRessource {} with {}", typeRessourceId, detailRessourceIds);
        typeRessourceDetailsRepository.deleteByTypeRessourceId(typeRessourceId);

        detailRessourceIds.forEach(detailId -> {
            TypeRessourceDetails link = new TypeRessourceDetails();
            link.setTypeRessourceId(typeRessourceId);
            link.setDetailRessourceId(detailId);
            typeRessourceDetailsRepository.save(link);
        });

        return getDetailsForType(typeRessourceId);
    }
}
