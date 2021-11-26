package com.johnny.chorus.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongsList {

    private final int fillingStringSongTitleId;
    private final List<Song> list;

    public SongsList(int fillingStringSongTitleId, Song... songs) {
        list = new ArrayList<>();
        this.fillingStringSongTitleId = fillingStringSongTitleId;
        Collections.addAll(list, songs);
    }

    public int getFillingStringSongTitleId() {
        return fillingStringSongTitleId;
    }

    public List<Song> getSongs() {
        return list;
    }

    public Song getSong(int id) {
        for (Song song: list) {
            if (song.getNumber() == id)
                return song;
        }
        return null;
    }
}