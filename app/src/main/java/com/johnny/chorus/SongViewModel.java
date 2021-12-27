package com.johnny.chorus;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.johnny.chorus.model.Song;

public class SongViewModel extends AndroidViewModel {

    private AnimatedVectorDrawable pauseToPlayDrawable;
    private AnimatedVectorDrawable animPlayToPauseDrawable;
    public LiveData<Song> songLiveData;
    private SongPlayer player;
    private final SongRepository songRepository = SongRepository.get();

    public SongViewModel(@NonNull Application application) {
        super(application);
    }

    public Context getContext() {
        return getApplication().getApplicationContext();
    }

    public void setSong(int songId) {
        if (songLiveData == null)
            songLiveData = songRepository.getSong(songId);
    }

    public void setPlayer(SongPlayer player) {
        this.player = player;
    }

    public void setDrawables(AnimatedVectorDrawable pauseToPlayDrawable, Context context, @DrawableRes int imageId) {
        this.pauseToPlayDrawable = pauseToPlayDrawable;
        animPlayToPauseDrawable = (AnimatedVectorDrawable) AppCompatResources.getDrawable(context, imageId);

    }

    public SongPlayer getPlayer() {
        return player;
    }

    public AnimatedVectorDrawable getPauseToPlayDrawable() {
        return pauseToPlayDrawable;
    }

    public AnimatedVectorDrawable getPlayToPauseDrawable() {
        return animPlayToPauseDrawable;
    }

    @Override
    protected void onCleared() {
        player.destroyPlayer();
        super.onCleared();
    }
}