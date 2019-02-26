package com.software.seller.bean;


public class SysPermissionGroupBean {
    Long id;
    String name;
    String description;
    String code;
    Integer isFinal;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return this.id; }

    public void setIsFinal(Integer isFinal) { this.isFinal = isFinal; }
    public Integer getIsFinal() { return this.isFinal; }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return this.description; }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public void normalize() {

        if (null == this.description) { this.description = ""; }
        if (null == this.name) { this.name = ""; }

    }
}
