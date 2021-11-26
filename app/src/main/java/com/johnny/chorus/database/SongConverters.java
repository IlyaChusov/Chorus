package com.johnny.chorus.database;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class SongConverters {

    @TypeConverter
    public List<String> toListString(@NonNull String allToneSongsPath) {
        return Arrays.asList(allToneSongsPath.split("%"));
    }

    @TypeConverter
    public String toString(@NonNull List<String> array) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            out.append(array.get(i));
            if (i != array.size() - 1)
                out.append("%");
        }
        return out.toString();
    }
}