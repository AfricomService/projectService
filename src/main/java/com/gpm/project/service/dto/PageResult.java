package com.gpm.project.service.dto;

import java.util.List;

public class PageResult<T> {

    private final List<T> content;
    private final long total;

    public PageResult(List<T> content, long total) {
        this.content = content;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotal() {
        return total;
    }
}
