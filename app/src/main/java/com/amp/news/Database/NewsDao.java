package com.amp.news.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.amp.news.Models.NewsDetail;

import java.util.List;

/**
 * Created by amal on 19/12/18.
 */

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NewsDetail newsDetail);

    @Query("SELECT * from news_table")
    LiveData<List<NewsDetail>> getAllSavedNews();

}