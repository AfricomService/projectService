package com.gpm.project.service;

import com.gpm.project.domain.ParentClassdef;
import com.gpm.project.repository.ParentClassdefRepository;
import com.gpm.project.service.dto.ParentClassdefDTO;
import com.gpm.project.service.mapper.ParentClassdefMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParentClassdef}.
 */
@Service
@Transactional
public class ParentClassdefService {

    private final Logger log = LoggerFactory.getLogger(ParentClassdefService.class);

    private final ParentClassdefRepository parentClassdefRepository;

    private final ParentClassdefMapper parentClassdefMapper;

    public ParentClassdefService(ParentClassdefRepository parentClassdefRepository, ParentClassdefMapper parentClassdefMapper) {
        this.parentClassdefRepository = parentClassdefRepository;
        this.parentClassdefMapper = parentClassdefMapper;
    }

    /**
     * Save a parentClassdef.
     *
     * @param parentClassdefDTO the entity to save.
     * @return the persisted entity.
     */
    public ParentClassdefDTO save(ParentClassdefDTO parentClassdefDTO) {
        log.debug("Request to save ParentClassdef : {}", parentClassdefDTO);
        ParentClassdef parentClassdef = parentClassdefMapper.toEntity(parentClassdefDTO);
        parentClassdef = parentClassdefRepository.save(parentClassdef);
        return parentClassdefMapper.toDto(parentClassdef);
    }

    /**
     * Partially update a parentClassdef.
     *
     * @param parentClassdefDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParentClassdefDTO> partialUpdate(ParentClassdefDTO parentClassdefDTO) {
        log.debug("Request to partially update ParentClassdef : {}", parentClassdefDTO);

        return parentClassdefRepository
            .findById(parentClassdefDTO.getId())
            .map(existingParentClassdef -> {
                parentClassdefMapper.partialUpdate(existingParentClassdef, parentClassdefDTO);

                return existingParentClassdef;
            })
            .map(parentClassdefRepository::save)
            .map(parentClassdefMapper::toDto);
    }

    /**
     * Get all the parentClassdefs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParentClassdefDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ParentClassdefs");
        return parentClassdefRepository.findAll(pageable).map(parentClassdefMapper::toDto);
    }

    /**
     * Get one parentClassdef by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParentClassdefDTO> findOne(Long id) {
        log.debug("Request to get ParentClassdef : {}", id);
        return parentClassdefRepository.findById(id).map(parentClassdefMapper::toDto);
    }

    /**
     * Delete the parentClassdef by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParentClassdef : {}", id);
        parentClassdefRepository.deleteById(id);
    }
}
