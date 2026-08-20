package com.gpm.project.service;

import com.gpm.project.domain.TypeRessource;
import com.gpm.project.repository.TypeRessourceRepository;
import com.gpm.project.service.dto.TypeRessourceDTO;
import com.gpm.project.service.mapper.TypeRessourceMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeRessource}.
 */
@Service
@Transactional
public class TypeRessourceService {

    private final Logger log = LoggerFactory.getLogger(TypeRessourceService.class);

    private final TypeRessourceRepository typeRessourceRepository;

    private final TypeRessourceMapper typeRessourceMapper;

    public TypeRessourceService(TypeRessourceRepository typeRessourceRepository, TypeRessourceMapper typeRessourceMapper) {
        this.typeRessourceRepository = typeRessourceRepository;
        this.typeRessourceMapper = typeRessourceMapper;
    }

    /**
     * Save a typeRessource.
     *
     * @param typeRessourceDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeRessourceDTO save(TypeRessourceDTO typeRessourceDTO) {
        log.debug("Request to save TypeRessource : {}", typeRessourceDTO);
        TypeRessource typeRessource = typeRessourceMapper.toEntity(typeRessourceDTO);
        typeRessource = typeRessourceRepository.save(typeRessource);
        return typeRessourceMapper.toDto(typeRessource);
    }

    /**
     * Partially update a typeRessource.
     *
     * @param typeRessourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeRessourceDTO> partialUpdate(TypeRessourceDTO typeRessourceDTO) {
        log.debug("Request to partially update TypeRessource : {}", typeRessourceDTO);

        return typeRessourceRepository
            .findById(typeRessourceDTO.getId())
            .map(existingTypeRessource -> {
                typeRessourceMapper.partialUpdate(existingTypeRessource, typeRessourceDTO);

                return existingTypeRessource;
            })
            .map(typeRessourceRepository::save)
            .map(typeRessourceMapper::toDto);
    }

    /**
     * Get all the typeRessources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeRessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeRessources");
        return typeRessourceRepository.findAll(pageable).map(typeRessourceMapper::toDto);
    }

    /**
     * Get one typeRessource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeRessourceDTO> findOne(Long id) {
        log.debug("Request to get TypeRessource : {}", id);
        return typeRessourceRepository.findById(id).map(typeRessourceMapper::toDto);
    }

    /**
     * Get all the typeRessources as a simple list (id, type, code), without pagination.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeRessourceDTO> findAllList() {
        log.debug("Request to get all TypeRessources as list");
        return typeRessourceRepository.findAll().stream().map(typeRessourceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Delete the typeRessource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeRessource : {}", id);
        typeRessourceRepository.deleteById(id);
    }
}
