package com.software.seller.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PageInfo<T> {
    private int total;
    private int pageSize;
    private List<T> rows;

    public PageInfo(int total, int pageSize,List<T> rows) {
        this.total = total;
        this.rows = rows;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "total=" + total +
                "pageSize=" + pageSize +
                ", rows=" + rows +
                '}';
    }

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return rows;
    }
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
