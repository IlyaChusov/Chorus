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

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private List<SongPlayer> playerList;
    private AnimatedVectorDrawable pauseToPlayDrawable;
    private final List<AnimatedVectorDrawable> animPlayToPauseDrawablesList = new ArrayList<>();
    private boolean hasData = false;
    public LiveData<Song> songLiveData;
    private final SongRepository songRepository = SongRepository.get();

    public boolean hasData() {
        return hasData;
    }

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

    public void setPlayerList(List<SongPlayer> playerList) {
        this.playerList = playerList;
        hasData = true;
    }

    public List<SongPlayer> getPlayerList() {
        return playerList;
    }

    public void pauseAllPlayers() {
        for (SongPlayer player: playerList)
            if (player.isPlaying())
                player.pausePlayer();
    }

    @Override
    protected void onCleared() {
        for (SongPlayer player: playerList)
            player.destroyPlayer();
        SongPlayer.clearIdCount();
        super.onCleared();
    }

    public void setDrawables(AnimatedVectorDrawable pauseToPlayDrawable, Context context, @DrawableRes int imageId) {
        this.pauseToPlayDrawable = pauseToPlayDrawable;

        for (int i = 0; i< playerList.size(); i++) {
            animPlayToPauseDrawablesList.add(
                    (AnimatedVectorDrawable) AppCompatResources.getDrawable(context, imageId));
        }
    }

    public AnimatedVectorDrawable getPauseToPlayDrawable() {
        return pauseToPlayDrawable;
    }

    public AnimatedVectorDrawable getPlayToPauseDrawable(int playerId) {
        return animPlayToPauseDrawablesList.get(playerId);
    }
}