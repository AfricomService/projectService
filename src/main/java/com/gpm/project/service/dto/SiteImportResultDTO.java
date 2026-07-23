package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SiteImportResultDTO implements Serializable {

    private int successCount;
    private List<String> errors = new ArrayList<>();

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
