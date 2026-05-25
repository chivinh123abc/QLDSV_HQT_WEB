package com.ptithcm.shared.dtos;

import java.util.List;

public class PaginationResult<T> {
    private List<T> rows;
    private long total;
    private int limit;
    private int offset;

    public PaginationResult(List<T> rows, long total, int limit, int offset) {
        this.rows = rows;
        this.total = total;
        this.limit = limit;
        this.offset = offset;
    }

    public List<T> getRows() {
        return rows;
    }

    public long getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
