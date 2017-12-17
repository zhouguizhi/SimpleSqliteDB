package com.simple.util;
public enum SortUtils {
    DESC("desc"), ASC("asc");
    private String orderBy;
    SortUtils(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getName() {
        return orderBy;
    }
}
