package com.johnny.chorus.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Song {

    @PrimaryKey private final int id;
    private int number;
    private String text;

    public String getGeneralSongId() {
        return generalSongId;
    }

    public void setGeneralSongId(String generalSongId) {
        this.generalSongId = generalSongId;
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public SongType getType() {
        return type;
    }

    public void setType(SongType type) {
        this.type = type;
    }

    public List<String> getToneSongsArray() {
        return toneSongsArray;
    }

    public void setToneSongsArray(List<String> list) {
        toneSongsArray = list;
    }

    public int getToneSongsId(int id) {
        return Integer.parseInt(toneSongsArray.get(id));
    }

    public void addToneSongsId(@NonNull int... ids) {
        for (int id: ids) {
            toneSongsArray.add(id + "");
        }

    }

    private String generalSongId;
    private String notesId;
    private SongType type;
    private List<String> toneSongsArray = new ArrayList<>();

    public Song(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public int getToneSongsId(@NonNull Context context, int arrayId) {
        return context.getResources().getIdentifier(toneSongsArray.get(arrayId), "raw", context.getPackageName());
    }

    public void addToneSongsId(@NonNull Context context, @NotNull int... toneSongsId) {
        for (int songId: toneSongsId)
            toneSongsArray.add(context.getResources().getResourceEntryName(songId));
    }

    public int getNotesId(@NonNull Context context) {
        return context.getResources().getIdentifier(notesId, "drawable", context.getPackageName());
    }

    public void setNotesId(@NonNull Context context, int notesId) {
        this.notesId = context.getResources().getResourceEntryName(notesId);
    }

    public int getGeneralSongId(@NonNull Context context) {
        return context.getResources().getIdentifier(generalSongId, "raw", context.getPackageName());
    }

    public void setGeneralSongId(@NonNull Context context, int generalSongId) {
        this.generalSongId = context.getResources().getResourceEntryName(generalSongId);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}