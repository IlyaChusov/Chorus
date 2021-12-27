package com.johnny.chorus;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.johnny.chorus.model.Song;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class SongActivity extends AppCompatActivity {

    private static final String SONG_ID = "songId";
    private static final String SHARED_ELEMENT = "sharedElement";
    private static final String PLAYER_POS = "generalPlayerPosition";
    private SongViewModel songViewModel;
    private SongPlayer songPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(android.R.id.content).setTransitionName(SHARED_ELEMENT);

        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        MaterialContainerTransform materialContainer = (MaterialContainerTransform) new MaterialContainerTransform().addTarget(findViewById(android.R.id.content));

        getWindow().setSharedElementEnterTransition(materialContainer);
        getWindow().setSharedElementExitTransition(materialContainer);
        getWindow().setSharedElementReturnTransition(materialContainer);

        setContentView(R.layout.activity_song);

        setSupportActionBar(findViewById(R.id.sing_toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(5);

        int songId = getIntent().getIntExtra(SONG_ID, 0);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.setSong(songId);
        songViewModel.songLiveData.observe(
                this,
                song -> workWithSong(savedInstanceState, song));
    }

    private void workWithSong(@Nullable Bundle savedInstanceState, @NonNull Song song) {
        CollapsingToolbarLayout collBar = findViewById(R.id.sing_coll_toolbar);
        collBar.setTitle(song.getType().getRussian() + ", глас " + song.getNumber());

        TextView textView = findViewById(R.id.song_text);
        textView.setText(song.getText());

        ImageView notesView = findViewById(R.id.notes_image);
        notesView.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(song.getNotesId(this))));
        notesView.setOnClickListener(v -> {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    SongActivity.this,
                    v,
                    ImageActivity.getSharedElement());
            startActivity(
                    ImageActivity.newIntent(SongActivity.this, song.getNotesId(this)),
                    activityOptions.toBundle());
        });

        final FloatingActionButton singFAB = findViewById(R.id.sing_fab);
        singFAB.setOnClickListener((v) -> startActivity(SingActivity.newIntent(
                SongActivity.this,
                song.getId(),
                song.getGeneralSongId(this),
                song.getSrtId(this),
                song.getSrtId())));

        SeekBar songBar = findViewById(R.id.song_bar);
        ImageButton playButton = findViewById(R.id.play_button);

        try {
            if (songViewModel.getPlayer() == null) {
                songPlayer = new SongPlayer(songViewModel, song.getGeneralSongId(this));
                songViewModel.setPlayer(songPlayer);
                songViewModel.setDrawables(
                        (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.pause_to_play_animate),
                        this,
                        R.drawable.play_to_pause_animate);
            } else
                songPlayer = songViewModel.getPlayer();

            songPlayer.setPlayButton(playButton);
            songPlayer.setSongBar(songBar);

            if (savedInstanceState != null) {
                if (!songPlayer.isPlaying())
                    songPlayer.seekToWithBar(savedInstanceState.getInt(PLAYER_POS));
            }
        }
        catch (IOException e) {
            showSoundException();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYER_POS, songPlayer.getCurrentPosition());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }

    private void showSoundException() {
        Toast.makeText(this, "Ошибка поиска звуковых ресурсов", Toast.LENGTH_LONG).show();
    }

    @NotNull
    public static Intent newIntent(Context context, int songId) {
        Intent intent = new Intent(context, SongActivity.class);
        intent.putExtra(SONG_ID, songId);
        return intent;
    }

    public static String getSharedElement() {
        return SHARED_ELEMENT;
    }
}