package com.example.aiot.service;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aiot.config.DateConverter;
import com.example.aiot.model.Feed;
import com.example.aiot.repository.FeedRepository;

@Database(entities = {Feed.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FeedRepository feedRepository();
}
