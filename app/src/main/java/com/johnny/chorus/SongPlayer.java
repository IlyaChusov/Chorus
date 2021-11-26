package com.johnny.chorus;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SongPlayer extends MediaPlayer {

    private Thread updatingThread;
    private ImageButton mPlayButton;
    private SeekBar mSongBar;
    private int songId;
    private final SongViewModel mSongViewModel;
    private final int playerId;
    private static int playerIdGen = 0;

    public SongPlayer(@NotNull SongViewModel songViewModel, int songId) throws IOException {
        mSongViewModel = songViewModel;
        this.songId = songId;

        setDataSource(mSongViewModel.getContext(), Uri.parse("android.resource://" + mSongViewModel.getContext().getPackageName() + "/" + songId));
        prepare();

        setLooping(PreferenceWork.getDefaultRepeatingMode());
        seekTo(0);
        setVolume(1f, 1f);
        setOnCompletionListener(mp -> pausePlayer());
        playerId = playerIdGen;
        playerIdGen++;

        updatingThread = createUpdatingThread();
    }

    public void setPlayButton(@NotNull ImageButton playButton) {
        mPlayButton = playButton;

        if (isPlaying())
            animatePlayButton(false);
        mPlayButton.setOnClickListener(v -> {
            if (isPlaying())
                pausePlayer();
            else {
                mSongViewModel.pauseAllPlayers();
                updatingThread.start();
                start();
                animatePlayButton(false);
            }
        });
        setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(mSongViewModel.getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    public void setSongBar(@NotNull SeekBar songBar) {
        mSongBar = songBar;

        mSongBar.setMax(getDuration());
        mSongBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setNewSong(int songId) throws IOException {
        if (this.songId == songId)
            return;

        if (isPlaying())
            pausePlayer();
        stop();
        reset();

        this.songId = songId;
        setDataSource(mSongViewModel.getContext(), Uri.parse("android.resource://" + mSongViewModel.getContext().getPackageName() + "/" + songId));
        prepare();
        mSongBar.setMax(getDuration());
        setBarProgress(0);
    }

    public void pausePlayer() {
        pause();
        animatePlayButton(true);
        updatingThread.interrupt();
        updatingThread = createUpdatingThread();
    }

    public void destroyPlayer() {
        updatingThread.interrupt();
        stop();
        reset();
        release();
    }

    private void animatePlayButton(boolean pausing) {
        AnimatedVectorDrawable drawable =
                pausing ? mSongViewModel.getPlayToPauseDrawable(playerId) : mSongViewModel.getPauseToPlayDrawable();

        if (drawable != null) {
            mPlayButton.setImageDrawable(drawable);
            drawable.start();
        }
    }

    private void setBarProgress(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            mSongBar.setProgress(position, true);
        else
            mSongBar.setProgress(position);
    }

    public void seekToWithBar(int position) {
        seekTo(position);
        setBarProgress(position);
    }

    public static void clearIdCount() {
        playerIdGen = 0;
    }

    @NotNull
    private Thread createUpdatingThread() {
        return new Thread(() -> {
            boolean interrupted = false;
            while (!interrupted)
                try {
                    new Handler(mSongViewModel.getContext().getMainLooper()).post(() -> {
                        setBarProgress(getCurrentPosition());
                    });

                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            // TODO: Сделать поглощение IllegalStateException
        });
    }
}