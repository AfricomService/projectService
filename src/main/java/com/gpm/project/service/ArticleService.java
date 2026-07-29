package com.gpm.project.service;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.AffaireArticle;
import com.gpm.project.domain.AffaireSocieteAdj;
import com.gpm.project.domain.Article;
import com.gpm.project.repository.AffaireArticleRepository;
import com.gpm.project.repository.AffaireRepository;
import com.gpm.project.repository.AffaireSocieteAdjRepository;
import com.gpm.project.repository.ArticleRepository;
import com.gpm.project.service.dto.AffaireArticleDTO;
import com.gpm.project.service.dto.ArticleDTO;
import com.gpm.project.service.dto.ArticleImportResultDTO;
import com.gpm.project.service.mapper.ArticleMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    private final AffaireArticleRepository affaireArticleRepository;

    private final AffaireRepository affaireRepository;

    public ArticleService(
        ArticleRepository articleRepository,
        ArticleMapper articleMapper,
        AffaireArticleRepository affaireArticleRepository,
        AffaireRepository affaireRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.affaireArticleRepository = affaireArticleRepository;
        this.affaireRepository = affaireRepository;
    }

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    /**
     * Update a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    public ArticleDTO update(ArticleDTO articleDTO) {
        log.debug("Request to update Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    /**
     * Partially update a article.
     *
     * @param articleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO) {
        log.debug("Request to partially update Article : {}", articleDTO);

        return articleRepository
            .findById(articleDTO.getId())
            .map(existingArticle -> {
                articleMapper.partialUpdate(existingArticle, articleDTO);

                return existingArticle;
            })
            .map(articleRepository::save)
            .map(articleMapper::toDto);
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable).map(articleMapper::toDto);
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id).map(articleMapper::toDto);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }

    public ArticleImportResultDTO importArticles(MultipartFile file, Long affaireId) throws IOException {
        log.debug("Request to import Articles");

        List<String> errors = new ArrayList<>();
        int successCount = 0;
        DataFormatter formatter = new DataFormatter();

        Affaire affaire = affaireRepository.findById(affaireId).orElseThrow(() -> new RuntimeException("Affaire not found"));

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                int excelLine = i + 1;

                try {
                    String designation = getCellString(row, 0, formatter);
                    String code = getCellString(row, 1, formatter);
                    String uniteMesure = getCellString(row, 2, formatter);
                    String codeClient = getCellString(row, 3, formatter);
                    BigDecimal prixUnitHT = getCellBigDecimal(row, 4, formatter);
                    String granularite = getCellString(row, 5, formatter);
                    BigDecimal prixAchat = getCellBigDecimal(row, 6, formatter);

                    // Required fields
                    if (isBlank(designation) || isBlank(code) || isBlank(uniteMesure)) {
                        errors.add("Ligne " + excelLine + " : champs obligatoires manquants (Label, Code, Unité)");
                        continue;
                    }

                    // Duplicate code
                    if (articleRepository.findByCode(code.trim()).isPresent()) {
                        errors.add("Ligne " + excelLine + " : le code '" + code + "' existe déjà");
                        continue;
                    }

                    Article article = new Article();
                    article.setDesignation(designation.trim());
                    article.setCode(code.trim());
                    article.setUniteMesure(uniteMesure.trim());
                    article.setCodeClient(codeClient);
                    article.setPrixUnitHT(prixUnitHT);
                    article.setGranularite(granularite);
                    article.setPrixAchat(prixAchat);

                    article.setCreatedAt(ZonedDateTime.now());

                    articleRepository.save(article);
                    successCount++;

                    AffaireArticle affaireArticle = new AffaireArticle().affaire(affaire).article(article);

                    affaireArticleRepository.save(affaireArticle);
                } catch (Exception e) {
                    log.error("Erreur lors de l'import de la ligne {}", excelLine, e);
                    errors.add("Ligne " + excelLine + " : erreur inattendue - " + e.getMessage());
                }
            }
        }

        ArticleImportResultDTO result = new ArticleImportResultDTO();
        result.setSuccessCount(successCount);
        result.setErrors(errors);

        return result;
    }

    public byte[] generateArticleImportTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Articles");

            String[] headers = { "Label", "Code", "Unité", "Code Client", "PUHT", "Granularite", "PrixAchat" };

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

    private BigDecimal getCellBigDecimal(Row row, int idx, DataFormatter formatter) {
        String value = getCellString(row, idx, formatter);

        if (value == null) {
            return null;
        }

        try {
            value = value.replace(" ", "").replace(",", ".");
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
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
