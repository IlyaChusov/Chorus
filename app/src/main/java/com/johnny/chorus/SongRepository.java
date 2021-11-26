package com.johnny.chorus;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.johnny.chorus.database.SongDao;
import com.johnny.chorus.database.SongDatabase;
import com.johnny.chorus.model.Song;
import com.johnny.chorus.model.SongType;

import java.util.List;

public class SongRepository {
    private static final String DATABASE_NAME = "chorus_db";
    private static SongDatabase database;

    private static SongRepository repository;

    private SongRepository(Context context) {
         database = Room.databaseBuilder(context, SongDatabase.class, DATABASE_NAME).build();
    }

    public static void initialize(Context context) {
        if (repository == null)
            repository = new SongRepository(context);
    }

    public static SongRepository get() {
        return repository;
    }

    private SongDao songDao() {
        return database.songDao();
    }

    public LiveData<List<Song>> getSongs(SongType songType) {
        return songDao().getSongs(songType);
    }

    public LiveData<Song> getSong(int id) {
        return songDao().getSong(id);
    }
}
