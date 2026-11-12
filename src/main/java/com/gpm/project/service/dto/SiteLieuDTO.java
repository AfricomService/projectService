package com.gpm.project.service.dto;

import java.io.Serializable;

/**
 * DTO léger pour la liste « Lieu » (work order) : compatible avec ISite côté Angular.
 */
public class SiteLieuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String designation;
    private VilleDTO ville;
    private Long zoneId;
    private String zoneNom;
    private Float gpsX;
    private Float gpsY;
    private String nodaleGpm;

    public SiteLieuDTO(
        Long id,
        String code,
        String designation,
        Long villeId,
        String villeNom,
        Long zoneId,
        String zoneNom,
        Float gpsX,
        Float gpsY,
        String nodaleGpm
    ) {
        this.id = id;
        this.code = code;
        this.designation = designation;
        if (villeId != null) {
            VilleDTO v = new VilleDTO();
            v.setId(villeId);
            v.setNom(villeNom);
            this.ville = v;
        }
        this.zoneId = zoneId;
        this.zoneNom = zoneNom;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.nodaleGpm = nodaleGpm;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDesignation() { return designation; }
    public VilleDTO getVille() { return ville; }
    public Long getZoneId() { return zoneId; }
    public String getZoneNom() { return zoneNom; }
    public Float getGpsX() { return gpsX; }
    public Float getGpsY() { return gpsY; }
    public String getNodaleGpm() { return nodaleGpm; }
}
