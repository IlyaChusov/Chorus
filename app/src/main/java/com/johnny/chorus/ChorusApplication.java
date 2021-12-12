package com.johnny.chorus;

import android.app.Application;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChorusApplication extends Application {

    private final String DB_NAME = "chorus_db";

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceWork.setContext(this);
        try {
            if (!dbExists())
                copyDB();
            else {
                if (!BuildConfig.VERSION_NAME.equals(PreferenceWork.getSavedAppVersion())) {
                    this.deleteDatabase(DB_NAME);
                    copyDB();
                }
            }
            PreferenceWork.setSavedAppVersion(BuildConfig.VERSION_NAME);
            SongRepository.initialize(this);
        }
        catch (Exception e) {
            Toast.makeText(this, "Проблема с базой данных, корректная работа приложения невозможна", Toast.LENGTH_LONG).show();
        }
    }

    private boolean dbExists() {
        return this.getDatabasePath(DB_NAME).exists();
    }

    private void copyDB() throws IOException {
        InputStream inputStream = this.getAssets().open("databases/" + DB_NAME);
        OutputStream outputStream = new FileOutputStream(this.getDatabasePath(DB_NAME).getPath());

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
            outputStream.write(buffer, 0, length);

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
