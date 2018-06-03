package com.automation.baidu.domain.base;

import java.io.Serializable;

public class PageParam implements Serializable {
    private static final long serialVersionUID = 780441301778777408L;
    private int page = 1;
    private int size = 10;
    private int total = 0;
    private int totalPage;

    public PageParam() {
    }

    public PageParam(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageParam(int page, int size, int total) {
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static long getSerialVersionUID() {
        return 780441301778777408L;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
        if (this.size > 0) {
            this.totalPage = 1 + (total - 1) / this.size;
        } else {
            this.totalPage = 1;
        }

    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getOffset() {
        return (this.page - 1) * this.size;
    }

    public boolean checkValid() {
        return this.page > 0 && this.size > 0;
    }
}
