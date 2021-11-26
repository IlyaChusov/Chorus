package com.johnny.chorus.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.johnny.chorus.model.Song;
import com.johnny.chorus.model.SongType;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM song WHERE type=(:type)")
    LiveData<List<Song>> getSongs(SongType type);

    @Query("SELECT * FROM song WHERE id=(:id)")
    LiveData<Song> getSong(int id);
}
