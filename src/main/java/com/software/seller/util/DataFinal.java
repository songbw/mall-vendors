package com.software.seller.util;

public enum DataFinal {
    freeze(1, "不可修改"),
    readWrite(2, "可修改");
    private int code;
    private String msg;

    DataFinal(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
