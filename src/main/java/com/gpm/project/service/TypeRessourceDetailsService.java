package com.gpm.project.service;

import com.gpm.project.domain.TypeRessourceDetails;
import com.gpm.project.repository.TypeRessourceDetailsRepository;
import com.gpm.project.service.dto.TypeRessourceDetailsDTO;
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

    public TypeRessourceDetailsService(
        TypeRessourceDetailsRepository typeRessourceDetailsRepository,
        TypeRessourceDetailsMapper typeRessourceDetailsMapper
    ) {
        this.typeRessourceDetailsRepository = typeRessourceDetailsRepository;
        this.typeRessourceDetailsMapper = typeRessourceDetailsMapper;
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
}
