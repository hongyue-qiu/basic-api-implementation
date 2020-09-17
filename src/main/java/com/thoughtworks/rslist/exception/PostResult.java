package com.thoughtworks.rslist.exception;

import lombok.Data;

@Data
public class PostResult {
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    Integer index;
}
