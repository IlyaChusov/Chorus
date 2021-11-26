package com.johnny.chorus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.johnny.chorus.model.Song;

@Database(entities = Song.class, version = 1)
@TypeConverters(SongConverters.class)
public abstract class SongDatabase extends RoomDatabase {
    public abstract SongDao songDao();
}
