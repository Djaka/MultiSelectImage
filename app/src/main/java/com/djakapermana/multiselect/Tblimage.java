package com.djakapermana.multiselect;

import com.orm.SugarRecord;

/**
 * Created by Djaka on 10/03/2018.
 */

public class Tblimage extends SugarRecord {
    String path;

    public Tblimage() {
    }

    public Tblimage(String path) {
        this.path = path;
    }

    public String getRes() {
        return path;
    }

    public void setRes(String path) {
        this.path = path;
    }
}
