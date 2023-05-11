package com.example.aiot.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.aiot.model.Feed;

import java.util.List;

@Dao
public interface FeedRepository {

    @Query("SELECT * FROM feed WHERE feed_key = :feedKey ORDER BY created_at DESC LIMIT 1")
    Feed getLast(String feedKey);

    @Query("SELECT * FROM feed WHERE feed_key = :feedKey")
    List<Feed> getAll(String feedKey);

    @Insert
    void insertAll(Feed... feeds);
}
