package com.johnny.chorus;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SongPlayer extends MediaPlayer {

    private Thread updatingThread;
    private ImageButton mPlayButton;
    private SeekBar mSongBar;
    private final SongViewModel mSongViewModel;

    public SongPlayer(@NotNull SongViewModel songViewModel, int songId) throws IOException {
        this(songViewModel, songId, "", null, null);
    }
    public SongPlayer(@NotNull SongViewModel songViewModel, int songId, OnCompletionListener onCompletionListener) throws IOException {
        this(songViewModel, songId, "", null, onCompletionListener);
    }
    public SongPlayer(@NotNull SongViewModel songViewModel, int songId, String srtName, OnTimedTextListener listener, OnCompletionListener onCompletionListener) throws IOException {
        mSongViewModel = songViewModel;

        setDataSource(mSongViewModel.getContext(), Uri.parse("android.resource://" + mSongViewModel.getContext().getPackageName() + "/" + songId));
        if (listener != null) {
            addTimedTextSource(mSongViewModel.getContext().getFileStreamPath(srtName + ".srt").getAbsolutePath(), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
            setOnTimedTextListener(listener);
            selectTrack(0);
        }
        prepare();

        setLooping(PreferenceWork.getDefaultRepeatingMode());
        seekTo(0);
        setVolume(1f, 1f);
        if (onCompletionListener != null)
            setOnCompletionListener(onCompletionListener);
        else
            setOnCompletionListener(mp -> pausePlayer());

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
                pausing ? mSongViewModel.getPlayToPauseDrawable() : mSongViewModel.getPauseToPlayDrawable();

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

    @NotNull
    private Thread createUpdatingThread() {
        return new Thread(() -> {
            boolean interrupted = false;
            while (!interrupted)
                try {
                    new Handler(mSongViewModel.getContext().getMainLooper()).post(() ->
                            setBarProgress(getCurrentPosition()));

                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            // TODO: Сделать поглощение IllegalStateException
        });
    }
}