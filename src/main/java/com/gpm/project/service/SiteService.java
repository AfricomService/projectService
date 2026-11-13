package com.gpm.project.service;

import com.gpm.project.domain.Client;
import com.gpm.project.domain.Site;
import com.gpm.project.domain.Ville;
import com.gpm.project.repository.ClientRepository;
import com.gpm.project.repository.SiteRepository;
import com.gpm.project.repository.VilleRepository;
import com.gpm.project.service.dto.SiteDTO;
import com.gpm.project.service.dto.SiteImportResultDTO;
import com.gpm.project.service.mapper.SiteMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.gpm.project.service.dto.SiteLieuDTO;
import java.util.stream.Collectors;
import com.gpm.project.web.rest.errors.BadRequestAlertException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.gpm.project.service.dto.SiteListDTO;
import com.gpm.project.client.UserRestClient;
import com.gpm.project.security.SecurityUtils;
import java.time.ZonedDateTime;

import java.io.ByteArrayOutputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);

    private static final String ENTITY_NAME = "projectServiceSite";

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    private final VilleRepository villeRepository;

    private final ClientRepository clientRepository;

    private final UserRestClient userRestClient;

    private static final int MAX_LIEU_PAGE_SIZE = 100;

    public SiteService(
        SiteRepository siteRepository,
        SiteMapper siteMapper,
        VilleRepository villeRepository,
        ClientRepository clientRepository,
        UserRestClient userRestClient
    ) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
        this.villeRepository = villeRepository;
        this.clientRepository = clientRepository;
        this.userRestClient = userRestClient;
    }

    /**
     * Renseigne les champs d'audit à la création (created* et updated*).
     */
    private void fillCreationAudit(Site site) {
        ZonedDateTime now = ZonedDateTime.now();
        String login = SecurityUtils.getCurrentUserLogin().orElse("SYSTEM");
        String userId = userRestClient.getCurrentUserId();

        site.setCreatedAt(now);
        site.setCreatedBy(login);
        site.setCreatedByUserLogin(userId);
        site.setUpdatedAt(now);
        site.setUpdatedBy(login);
        site.setUpdatedByUserLogin(userId);
    }

    /**
     * Renseigne les champs d'audit à la modification (updated* uniquement).
     */
    private void fillUpdateAudit(Site site) {
        site.setUpdatedAt(ZonedDateTime.now());
        site.setUpdatedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
        site.setUpdatedByUserLogin(userRestClient.getCurrentUserId());
    }

    /**
     * Vérifie si un site avec ce code existe déjà pour ce client.
     *
     * @param excludeSiteId id du site à exclure (cas de la modification), ou null.
     */
    @Transactional(readOnly = true)
    public boolean codeExistsForClient(Long clientId, String code, Long excludeSiteId) {
        if (clientId == null || code == null) {
            return false;
        }
        String trimmed = code.trim();
        return excludeSiteId == null
            ? siteRepository.existsByClientIdAndCode(clientId, trimmed)
            : siteRepository.existsByClientIdAndCodeAndIdNot(clientId, trimmed, excludeSiteId);
    }

    /**
     * Save a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    public SiteDTO save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);

        siteDTO.setCode(siteDTO.getCode().trim());
        Long clientId = siteDTO.getClient() != null ? siteDTO.getClient().getId() : null;
        if (codeExistsForClient(clientId, siteDTO.getCode(), null)) {
            throw new BadRequestAlertException("Ce site existe déjà pour ce client", ENTITY_NAME, "codeexists");
        }

        Site site = siteMapper.toEntity(siteDTO);
        fillCreationAudit(site);
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

        siteDTO.setCode(siteDTO.getCode().trim());
        Long clientId = siteDTO.getClient() != null ? siteDTO.getClient().getId() : null;
        if (codeExistsForClient(clientId, siteDTO.getCode(), siteDTO.getId())) {
            throw new BadRequestAlertException("Ce site existe déjà pour ce client", ENTITY_NAME, "codeexists");
        }

        Site existing = siteRepository
            .findById(siteDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
        ZonedDateTime createdAt = existing.getCreatedAt();
        String createdBy = existing.getCreatedBy();
        String createdByUserLogin = existing.getCreatedByUserLogin();

        Site site = siteMapper.toEntity(siteDTO);
        // Les champs de création ne doivent jamais être modifiés (ni par le client, ni écrasés par null)
        site.setCreatedAt(createdAt);
        site.setCreatedBy(createdBy);
        site.setCreatedByUserLogin(createdByUserLogin);
        fillUpdateAudit(site);

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
                ZonedDateTime createdAt = existingSite.getCreatedAt();
                String createdBy = existingSite.getCreatedBy();
                String createdByUserLogin = existingSite.getCreatedByUserLogin();

                siteMapper.partialUpdate(existingSite, siteDTO);

                // Protège les champs de création contre une valeur envoyée par le client
                existingSite.setCreatedAt(createdAt);
                existingSite.setCreatedBy(createdBy);
                existingSite.setCreatedByUserLogin(createdByUserLogin);
                fillUpdateAudit(existingSite);

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
     * Liste légère (id, code, désignation) des sites d'un client, paginée.
     */
    @Transactional(readOnly = true)
    public Page<SiteListDTO> findSiteListByClientId(Long clientId, Pageable pageable) {
        log.debug("Request to get light Site list by clientId : {}", clientId);
        return siteRepository.findListByClientId(clientId, pageable);
    }

    /**
     * Recherche légère (id, code, désignation) des sites d'un client par désignation, paginée.
     */
    @Transactional(readOnly = true)
    public Page<SiteListDTO> searchSiteListByClientId(Long clientId, String designation, Pageable pageable) {
        log.debug("Request to search light Site list by clientId : {} and designation : {}", clientId, designation);
        return siteRepository.searchListByClientId(clientId, designation, pageable);
    }

    /**
     * Liste « Lieu » : sites d'un client, recherche par code ou désignation, paginée.
     */
    @Transactional(readOnly = true)
    public Page<SiteLieuDTO> searchLieuxByClientId(Long clientId, String search, Pageable pageable) {
        log.debug("Request to search Lieux by clientId : {} and search : {}", clientId, search);
        String term = search == null ? "" : search.trim();
        // Plafond de taille de page imposé côté serveur ; le tri vient de la requête (order by code)
        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), Math.min(pageable.getPageSize(), MAX_LIEU_PAGE_SIZE));
        return siteRepository.searchLieuxByClientId(clientId, term, safePageable);
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
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        String currentUserId = userRestClient.getCurrentUserId();

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

                    Float longitude;
                    Float latitude;
                    try {
                        longitude = getCellFloat(row, 3, formatter, "Longitude");
                        latitude = getCellFloat(row, 4, formatter, "Latitude");
                    } catch (InvalidNumericCellException e) {
                        errors.add("Ligne " + excelLine + " : " + e.getMessage());
                        continue;
                    }

                    String nodaleGpm = getCellString(row, 5, formatter);
                    String sitePriority = getCellString(row, 6, formatter);
                    String typeSite = getCellString(row, 7, formatter);
                    String regionSite = getCellString(row, 8, formatter);
                    String zoneNom = getCellString(row, 9, formatter);

                    if (isBlank(code)) {
                        errors.add("Ligne " + excelLine + " : champ obligatoire manquant (code)");
                        continue;
                    }

                    Optional<Site> existingSite = siteRepository.findFirstByClientIdAndCode(clientId, code.trim());

                    Ville ville = null;
                    if (!isBlank(villeNom)) {
                        ville = villeRepository.findByNomIgnoreCase(villeNom.trim()).orElse(null);
                        if (ville == null) {
                            errors.add("Ligne " + excelLine + " : gouvernorat introuvable '" + villeNom + "'");
                            continue;
                        }
                    }

                    boolean isUpdate = existingSite.isPresent();
                    Site site = existingSite.orElseGet(Site::new);
                    site.setCode(code.trim());
                    site.setDesignation(designation != null ? designation.trim() : null);
                    site.setGpsX(longitude);
                    site.setGpsY(latitude);
                    site.setNodaleGpm(nodaleGpm);
                    site.setSitePriority(sitePriority);
                    site.setTypeSite(typeSite);
                    site.setRegionSite(regionSite);
                    site.setVille(ville);
                    site.setZoneNom(zoneNom != null ? zoneNom.trim() : null);
                    site.setClient(client);

                    ZonedDateTime now = ZonedDateTime.now();
                    site.setUpdatedAt(now);
                    site.setUpdatedBy(currentLogin);
                    site.setUpdatedByUserLogin(currentUserId);
                    if (!isUpdate) {
                        site.setCreatedAt(now);
                        site.setCreatedBy(currentLogin);
                        site.setCreatedByUserLogin(currentUserId);
                    }

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

    /**
     * Exception interne utilisée pour signaler qu'une cellule censée contenir un nombre
     * (GPS X / GPS Y) contient une valeur non convertible en nombre. Distincte d'une cellule
     * simplement vide, qui reste autorisée (retourne null).
     */
    private static class InvalidNumericCellException extends RuntimeException {
        InvalidNumericCellException(String message) {
            super(message);
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

    /**
     * Lit une valeur numérique (GPS) depuis une cellule, en privilégiant la valeur brute
     * de la cellule (cell.getNumericCellValue()) plutôt que sa représentation textuelle formatée,
     * pour éviter les problèmes de notation scientifique, d'arrondi ou de séparateurs de milliers
     * appliqués par le format d'affichage Excel.
     *
     * @param columnLabel libellé lisible de la colonne (ex. "Longitude"), utilisé dans le message d'erreur.
     * @throws InvalidNumericCellException si la cellule contient une valeur non vide mais non convertible en nombre.
     */
    private Float getCellFloat(Row row, int idx, DataFormatter formatter, String columnLabel) {
        Cell cell = row.getCell(idx);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        // Cas 1 : la cellule est un vrai nombre Excel -> on lit la valeur brute, sans formatage
        if (cell.getCellType() == CellType.NUMERIC) {
            return (float) cell.getNumericCellValue();
        }

        // Cas 2 : formule dont le résultat est numérique
        if (cell.getCellType() == CellType.FORMULA && cell.getCachedFormulaResultType() == CellType.NUMERIC) {
            return (float) cell.getNumericCellValue();
        }

        // Cas 3 : la cellule est du texte -> on nettoie et on parse manuellement
        String value = getCellString(row, idx, formatter);
        if (value == null) {
            return null;
        }
        try {
            String normalized = value.trim().replace(" ", "").replace(",", ".");
            return Float.parseFloat(normalized);
        } catch (NumberFormatException e) {
            throw new InvalidNumericCellException(
                "valeur '" + value + "' invalide pour le champ " + columnLabel + " (un nombre est attendu)"
            );
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
