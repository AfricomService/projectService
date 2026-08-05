package com.gpm.project.service.dto;

public class ContactSocieteKeycloakUserCreationResultDTO {

    private ContactSocieteDTO contactSociete;
    private String generatedPassword;

    public ContactSocieteKeycloakUserCreationResultDTO(ContactSocieteDTO contactSociete, String generatedPassword) {
        this.contactSociete = contactSociete;
        this.generatedPassword = generatedPassword;
    }

    public ContactSocieteDTO getContactSociete() {
        return contactSociete;
    }

    public void setContactSociete(ContactSocieteDTO contactSociete) {
        this.contactSociete = contactSociete;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }
}
