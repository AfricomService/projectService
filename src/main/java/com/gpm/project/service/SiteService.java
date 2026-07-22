package com.gpm.project.service;

import com.gpm.project.domain.Client;
import com.gpm.project.domain.Site;
import com.gpm.project.domain.Ville;
import com.gpm.project.domain.Zone;
import com.gpm.project.repository.ClientRepository;
import com.gpm.project.repository.SiteRepository;
import com.gpm.project.repository.VilleRepository;
import com.gpm.project.repository.ZoneRepository;
import com.gpm.project.service.dto.SiteDTO;
import com.gpm.project.service.dto.SiteImportResultDTO;
import com.gpm.project.service.mapper.SiteMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    private final VilleRepository villeRepository;

    private final ZoneRepository zoneRepository;

    private final ClientRepository clientRepository;

    public SiteService(
        SiteRepository siteRepository,
        SiteMapper siteMapper,
        VilleRepository villeRepository,
        ZoneRepository zoneRepository,
        ClientRepository clientRepository
    ) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
        this.villeRepository = villeRepository;
        this.zoneRepository = zoneRepository;
        this.clientRepository = clientRepository;
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

    /**
     * Importe une liste de sites à partir d'un fichier Excel, pour un client donné.
     * Résout les colonnes "Gouvernorat" (ville) et "Zone" par leur nom, et vérifie
     * l'unicité du code avant insertion.
     *
     * @param file le fichier Excel (.xlsx) à importer.
     * @param clientId l'id du client auquel rattacher tous les sites importés.
     * @return le résultat de l'import (nombre de succès + liste des erreurs par ligne).
     */
    public SiteImportResultDTO importSites(MultipartFile file, Long clientId) throws IOException {
        log.debug("Request to import Sites for client : {}", clientId);

        Client client = clientRepository
            .findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client introuvable pour id : " + clientId));

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                int excelLine = i + 1;

                try {
                    String code = getCellString(row, 0, formatter);
                    String designation = getCellString(row, 1, formatter);
                    String villeNom = getCellString(row, 2, formatter);
                    Float longitude = getCellFloat(row, 3, formatter);
                    Float latitude = getCellFloat(row, 4, formatter);
                    String nodaleGpm = getCellString(row, 5, formatter);
                    String sitePriority = getCellString(row, 6, formatter);
                    String typeSite = getCellString(row, 7, formatter);
                    String regionSite = getCellString(row, 8, formatter);
                    String zoneNom = getCellString(row, 9, formatter);

                    if (isBlank(code) || isBlank(designation) || isBlank(villeNom)) {
                        errors.add("Ligne " + excelLine + " : champs obligatoires manquants (code, désignation, gouvernorat)");
                        continue;
                    }

                    if (siteRepository.findByCode(code.trim()).isPresent()) {
                        errors.add("Ligne " + excelLine + " : le code '" + code + "' existe déjà");
                        continue;
                    }

                    Ville ville = villeRepository.findByNomIgnoreCase(villeNom.trim()).orElse(null);
                    if (ville == null) {
                        errors.add("Ligne " + excelLine + " : gouvernorat introuvable '" + villeNom + "'");
                        continue;
                    }

                    Zone zone = null;
                    if (!isBlank(zoneNom)) {
                        zone = zoneRepository.findByNomIgnoreCaseAndVilleId(zoneNom.trim(), ville.getId()).orElse(null);
                        if (zone == null) {
                            errors.add("Ligne " + excelLine + " : zone introuvable '" + zoneNom + "' pour le gouvernorat '" + villeNom + "'");
                            continue;
                        }
                    }

                    Site site = new Site();
                    site.setCode(code.trim());
                    site.setDesignation(designation.trim());
                    site.setGpsX(longitude);
                    site.setGpsY(latitude);
                    site.setNodaleGpm(nodaleGpm);
                    site.setSitePriority(sitePriority);
                    site.setTypeSite(typeSite);
                    site.setRegionSite(regionSite);
                    site.setVille(ville);
                    if (zone != null) {
                        site.setZoneId(zone.getId());
                    }
                    site.setClient(client);

                    siteRepository.save(site);
                    successCount++;
                } catch (Exception e) {
                    log.error("Erreur lors de l'import de la ligne {}", excelLine, e);
                    errors.add("Ligne " + excelLine + " : erreur inattendue - " + e.getMessage());
                }
            }
        }

        SiteImportResultDTO result = new SiteImportResultDTO();
        result.setSuccessCount(successCount);
        result.setErrors(errors);
        return result;
    }

    /**
     * Génère un fichier Excel modèle vide pour l'import de sites,
     * avec les en-têtes de colonnes attendues.
     *
     * @return le contenu du fichier Excel en bytes.
     */
    public byte[] generateImportTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sites");

            String[] headers = {
                "Code Site GPM",
                "Désignation",
                "Gouvernorat",
                "Longitude",
                "Latitude",
                "Nodale GPM",
                "Site Priority Label",
                "Type",
                "Region",
                "Zone",
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 22 * 256);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String getCellString(Row row, int idx, DataFormatter formatter) {
        Cell cell = row.getCell(idx);
        if (cell == null) {
            return null;
        }
        String value = formatter.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }

    private Float getCellFloat(Row row, int idx, DataFormatter formatter) {
        String value = getCellString(row, idx, formatter);
        if (value == null) {
            return null;
        }
        try {
            return Float.parseFloat(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
