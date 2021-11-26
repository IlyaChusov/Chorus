package com.johnny.chorus;

import android.app.Application;

public class ChorusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SongRepository.initialize(this);
    }
}
