package com.mobitrack.mobi.model;

import java.io.Serializable;

/**
 * Created by sohel on 5/3/2018.
 */

public class SharedUser implements Serializable {
    private String id;
    private int isEnable;

    public SharedUser() {
    }

    public SharedUser(String id, int isEnable) {
        this.id = id;
        this.isEnable = isEnable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }
}
