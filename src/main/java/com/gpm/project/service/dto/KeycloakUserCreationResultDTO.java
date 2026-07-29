package com.gpm.project.service.dto;

public class KeycloakUserCreationResultDTO {

    private ContactDTO contact;
    private String generatedPassword;

    public KeycloakUserCreationResultDTO(ContactDTO contact, String generatedPassword) {
        this.contact = contact;
        this.generatedPassword = generatedPassword;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }
}
