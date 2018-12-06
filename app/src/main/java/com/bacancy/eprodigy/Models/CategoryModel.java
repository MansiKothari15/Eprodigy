package com.bacancy.eprodigy.Models;

import android.arch.persistence.room.Entity;

/**
 * Created by samir on 7/3/18.
 */

@Entity
public class CategoryModel {

    int id;
    String catname;

    public void setId(int id) {
        this.id = id;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public int getId() {
        return id;
    }
    public String getCatname() {
        return catname;
    }
}
