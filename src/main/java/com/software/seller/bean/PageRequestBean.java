package com.software.seller.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.stereotype.Component;
//import lombok.AllArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Component(value="PageRequestBean")
public class PageRequestBean<T> {

    private int pageSize;
    private int pageIndex;
    private T data;


    public int getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(int pageIndex) { this.pageIndex = pageIndex; }

    public T getData() {
        return this.data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
