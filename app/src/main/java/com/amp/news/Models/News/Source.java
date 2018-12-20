package com.amp.news.Models.News;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by amal on 17/12/18.
 */

public class Source {
    @ColumnInfo(name = "source_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
