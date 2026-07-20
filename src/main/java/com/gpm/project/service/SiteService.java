package com.gpm.project.service;

import com.gpm.project.domain.Site;
import com.gpm.project.repository.SiteRepository;
import com.gpm.project.service.dto.SiteDTO;
import com.gpm.project.service.mapper.SiteMapper;
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
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    public SiteService(SiteRepository siteRepository, SiteMapper siteMapper) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
    }

    /**
     * Save a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    public SiteDTO save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);
        Site site = siteMapper.toEntity(siteDTO);
        site = siteRepository.save(site);
        return siteMapper.toDto(site);
    }

    /**
     * Update a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    public SiteDTO update(SiteDTO siteDTO) {
        log.debug("Request to update Site : {}", siteDTO);
        Site site = siteMapper.toEntity(siteDTO);
        site = siteRepository.save(site);
        return siteMapper.toDto(site);
    }

    /**
     * Partially update a site.
     *
     * @param siteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SiteDTO> partialUpdate(SiteDTO siteDTO) {
        log.debug("Request to partially update Site : {}", siteDTO);

        return siteRepository
            .findById(siteDTO.getId())
            .map(existingSite -> {
                siteMapper.partialUpdate(existingSite, siteDTO);

                return existingSite;
            })
            .map(siteRepository::save)
            .map(siteMapper::toDto);
    }

    /**
     * Get all the sites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sites");
        return siteRepository.findAll(pageable).map(siteMapper::toDto);
    }

    /**
     * Get all the sites with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SiteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return siteRepository.findAllWithEagerRelationships(pageable).map(siteMapper::toDto);
    }

    /**
     * Get all sites by clientId, paginated.
     *
     * @param clientId the id of the client.
     * @param pageable the pagination information.
     * @return the page of entities.
     */
    @Transactional(readOnly = true)
    public Page<SiteDTO> findSitesByClientId(Long clientId, Pageable pageable) {
        log.debug("Request to get Sites by clientId : {}", clientId);
        return siteRepository.findByClientId(clientId, pageable).map(siteMapper::toDto);
    }

    /**
     * Search sites by clientId and designation (partial, case-insensitive), paginated.
     *
     * @param clientId the id of the client.
     * @param designation the search term for designation.
     * @param pageable the pagination information.
     * @return the page of matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SiteDTO> searchSitesByClientId(Long clientId, String designation, Pageable pageable) {
        log.debug("Request to search Sites by clientId : {} and designation : {}", clientId, designation);
        return siteRepository.findByClientIdAndDesignationContainingIgnoreCase(clientId, designation, pageable).map(siteMapper::toDto);
    }

    /**
     * Get one site by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SiteDTO> findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findOneWithEagerRelationships(id).map(siteMapper::toDto);
    }

    /**
     * Delete the site by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.deleteById(id);
    }
}
