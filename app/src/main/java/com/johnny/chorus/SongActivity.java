package com.johnny.chorus;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.johnny.chorus.model.Lab;
import com.johnny.chorus.model.Song;
import com.johnny.chorus.model.SongType;
import com.johnny.chorus.model.SongsList;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SongActivity extends AppCompatActivity {

    private static final String SONG_ID = "songId";
    private static final String SONGS_LIST_ID = "songsListId";
    private static final String SHARED_ELEMENT = "sharedElement";
    private static final String GENERAL_PLAYER_POS = "generalPlayerPosition";
    private static final String TONE_PLAYER_POS = "tonePlayerPosition";
    private List<SongPlayer> playerList;
    private boolean userTouched = false;
    private SongViewModel songViewModel;

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

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        songViewModel.songLiveData.observe(
                this,
                song -> workWithSong(savedInstanceState, song));
        songViewModel.setSongId(getIntent().getIntExtra(SONG_ID, 0));
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

        SeekBar songBarGeneral = findViewById(R.id.song_bar_general);
        ImageButton playButtonGeneral = findViewById(R.id.play_button_general);

        SeekBar songBarTone = findViewById(R.id.song_bar_tone);
        ImageButton playButtonTone = findViewById(R.id.play_button_tone);

        try {
            int defaultToneId = PreferenceWork.getDefaultToneId();


            if (!songViewModel.hasData()) {
                playerList = new ArrayList<>();
                playerList.add(new SongPlayer(songViewModel, song.getGeneralSongId(this))); // GENERAL -- INDEX 0
                playerList.add(new SongPlayer(songViewModel, song.getToneSongsId(this, defaultToneId))); // TONE -- INDEX 1

                songViewModel.setPlayerList(playerList);
                songViewModel.setDrawables(
                        (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.pause_to_play_animate),
                        this,
                        R.drawable.play_to_pause_animate);
            } else
                playerList = songViewModel.getPlayerList();

            SongPlayer songPlayerGeneral = playerList.get(0);
            SongPlayer songPlayerTone = playerList.get(1);

            songPlayerGeneral.setPlayButton(playButtonGeneral);
            songPlayerGeneral.setSongBar(songBarGeneral);

            songPlayerTone.setPlayButton(playButtonTone);
            songPlayerTone.setSongBar(songBarTone);

            int tonePos = 0;
            if (savedInstanceState != null) {
                if (!songPlayerGeneral.isPlaying())
                    songPlayerGeneral.seekToWithBar(savedInstanceState.getInt(GENERAL_PLAYER_POS));
                tonePos = savedInstanceState.getInt(TONE_PLAYER_POS);
            }

            Spinner toneSpinner = findViewById(R.id.tone_selector);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tones_array, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            toneSpinner.setAdapter(adapter);
            toneSpinner.setSelection(defaultToneId);
            final int finalTonePos = tonePos;
            toneSpinner.setOnTouchListener((v, event) -> {
                userTouched = true;
                v.performClick();
                return false;
            });
            toneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int newSongId = song.getToneSongsId(SongActivity.this, position);
                    try {
                        songPlayerTone.setNewSong(newSongId);
                        if (!userTouched)
                            if (!songPlayerTone.isPlaying())
                                songPlayerTone.seekToWithBar(finalTonePos);
                    } catch (IOException e) {
                        showSoundException();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        catch (IOException e) {
            showSoundException();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GENERAL_PLAYER_POS, playerList.get(0).getCurrentPosition());
        outState.putInt(TONE_PLAYER_POS, playerList.get(1).getCurrentPosition());
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
    public static Intent newIntent(Context context, int songId, int songListId) {
        Intent intent = new Intent(context, SongActivity.class);
        intent.putExtra(SONG_ID, songId);
        intent.putExtra(SONGS_LIST_ID, songListId);
        return intent;
    }

    public static String getSharedElement() {
        return SHARED_ELEMENT;
    }
}