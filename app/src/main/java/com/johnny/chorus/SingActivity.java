package com.johnny.chorus;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingActivity extends AppCompatActivity {

    private static final String SONG_ID = "songId";
    private static final String AUDIO_ID = "audioId";
    private static final String SRT_ID = "srtId";
    private static final String SRT_NAME = "srtName";
    private SongPlayer singPlayer;
    private View layout;
    private RecyclerView recyclerView;
    private SrtAdapter adapter;
    private static final Handler handler = new Handler();
    private final List<Pair<String, String>> timeList = new ArrayList<>();
    private SongViewModel singViewModel;
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_sing);

        recyclerView = findViewById(R.id.srt_recycler);
        layout = findViewById(R.id.layout);
        layout.setVisibility(View.INVISIBLE);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        // TODO: Хрень какая-то тут с высотой RecyclerView
        float sss = getResources().getDisplayMetrics().density - 1;
        int ss = (int) (size.y / sss);
        recyclerView.getLayoutParams().height = ss;
        recyclerView.requestLayout();

        ViewTreeObserver observer = layout.getViewTreeObserver();
        if (observer.isAlive())
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    circularReveal();
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        if (getIntent() != null) {
            getIntent().getIntExtra(SONG_ID, 0);
            int audioId = getIntent().getIntExtra(AUDIO_ID, 0);
            int srtId = getIntent().getIntExtra(SRT_ID, 0);
            String srtName = getIntent().getStringExtra(SRT_NAME);

            try {
                String srtFilePath = getFileStreamPath(srtName + ".srt").getAbsolutePath();
                File file = new File(srtFilePath);
                if (!file.exists()) {
                    InputStream inputStream = getResources().openRawResource(srtId);
                    OutputStream outputStream = new FileOutputStream(srtFilePath);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0)
                        outputStream.write(buffer, 0, length);

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }

                MediaPlayer.OnTimedTextListener listener = (mp, text) -> {
                    if (text != null) {
                        handler.post(() -> {
                            for (int i = 0; i < timeList.size(); i++) {
                                Pair<String, String> timeLine = timeList.get(i);
                                int milFirst = millisecondsFromSrtLine(timeLine.first);
                                int milSecond = millisecondsFromSrtLine(timeLine.second);
                                int milCur = mp.getCurrentPosition();
                                if (milCur >= milFirst && milCur <= milSecond) {
                                    unhighlightAllLines();
                                    workWithSrtLine(i, true);
                                }
                            }
                        });
                    }
                };

                singViewModel = new ViewModelProvider(this).get(SongViewModel.class);
                if (singViewModel.getPlayer() == null) {

                    singPlayer = new SongPlayer(singViewModel, audioId, srtName, listener, (mp) -> {singPlayer.pausePlayer(); unhighlightAllLines();});
                    singViewModel.setPlayer(singPlayer);
                    singViewModel.setDrawables(
                            (AnimatedVectorDrawable) ContextCompat.getDrawable(this, R.drawable.pause_to_play_animate),
                            this,
                            R.drawable.play_to_pause_animate);
                } else
                    singPlayer = singViewModel.getPlayer();

                singPlayer.setPlayButton(findViewById(R.id.sing_button));
                singPlayer.setSongBar(findViewById(R.id.sing_bar));

                // Parsing srt
                String lineNumberPattern = "(\\d+\\s)";
                String timeStampPattern = "([\\d:,]+)";
                String contentPattern = "(.*\\s.*)";

                File srtFile = new File(srtFilePath);
                FileInputStream fis = new FileInputStream(srtFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder srtString = new StringBuilder();
                String srtLine;
                while ((srtLine = reader.readLine()) != null)
                    srtString.append(srtLine).append("\n");
                fis.close();
                in.close();
                reader.close();

                Matcher matcher = Pattern.compile(lineNumberPattern + timeStampPattern + "( --> )" + timeStampPattern + "(\\s)" + contentPattern).matcher(srtString);

                List<String> srtList = new ArrayList<>();
                while(matcher.find()) {
                    String start = matcher.group(2);
                    String end = matcher.group(4);
                    String content = matcher.group(6);

                    timeList.add(new Pair<>(start, end));
                    srtList.add(content);
                }
                for (int i = 0; i < srtList.size(); i++) {
                    String line = srtList.get(i);
                    if (line.endsWith("\n"))
                        srtList.set(i, (line.substring(0, line.length() - 1)).trim());
                }

                adapter = new SrtAdapter(srtList);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e) {
                Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private class SrtAdapter extends RecyclerView.Adapter<SrtHolder> {
        private final List<String> srtList;
        private final List<SrtHolder> holders = new ArrayList<>();

        public SrtAdapter(@NonNull List<String> srtList) {
            this.srtList = srtList;
        }

        public void highlightLine(int position) {
            Log.d("TAG", "scrolling to position: " + position);
            for (SrtHolder holder: holders)
                holder.unHighlight();

            holders.get(position).highlight();
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }

        @NonNull
        @Override
        public SrtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SrtHolder holder = new SrtHolder(LayoutInflater.from(SingActivity.this), parent);
            holders.add(holder);
            Log.d("TAG", "holder size: " + holders.size());
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SrtHolder holder, int position) {
            Log.d("TAG", "binding position: " + position);
            holder.bind(srtList.get(position));
        }

        @Override
        public int getItemCount() {
            return srtList.size();
        }
    }

    private class SrtHolder extends RecyclerView.ViewHolder {
        private final TextView srtView;

        public SrtHolder(@NonNull @NotNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.srt_item, parent, false));

            srtView = itemView.findViewById(R.id.srt_place);
        }

        public void bind(String srtLine) {
            srtView.setText(srtLine);
        }

        public void highlight() {
            srtView.setTextSize(25);
            srtView.setTextColor(ContextCompat.getColor(SingActivity.this, R.color.pink_200));
        }
        public void unHighlight() {
            srtView.setTextSize(20);
            srtView.setTextColor(ContextCompat.getColor(SingActivity.this, R.color.white));
        }
    }

    @Override
    public void onBackPressed() {
        int cx = layout.getWidth();
        int cy = layout.getHeight();

        float finalRadius = Math.max(layout.getWidth(), layout.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(layout, cx, cy, finalRadius, 0);

        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout.setVisibility(View.INVISIBLE);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circularReveal.setDuration(300);
        circularReveal.start();
    }

    private void circularReveal() {
        int cx = layout.getWidth() / 2;
        int cy = layout.getHeight() / 2;

        float radius = (float) Math.hypot(cx, cy);

        Animator animator = ViewAnimationUtils.createCircularReveal(
                layout,
                cx,
                cy,
                0,
                radius);

        animator.setDuration(300);
        layout.setVisibility(View.VISIBLE);
        animator.start();
    }

    @NonNull
    public static Intent newIntent(Context context, int songId, int audioId, int srtId, String srtName) {
        Intent intent = new Intent(context, SingActivity.class);
        intent.putExtra(SONG_ID, songId);
        intent.putExtra(AUDIO_ID, audioId);
        intent.putExtra(SRT_ID, srtId);
        intent.putExtra(SRT_NAME, srtName);
        return intent;
    }

    private void workWithSrtLine(int position, boolean highlight) {
        linearLayoutManager.scrollToPositionWithOffset(position, 0);
        View view = linearLayoutManager.findViewByPosition(position);
        if (view == null)
            return;
        TextView textView = view.findViewById(R.id.srt_place);
        if (highlight) {
            textView.setTextSize(25);
            textView.setTextColor(ContextCompat.getColor(SingActivity.this, R.color.pink_200));
        }
        else {
            textView.setTextSize(20);
            textView.setTextColor(ContextCompat.getColor(SingActivity.this, R.color.white));
        }
    }

    private void unhighlightAllLines() {
        for (int j = 0; j < timeList.size(); j++)
            workWithSrtLine(j, false);
    }

    public int millisecondsFromSrtLine(@NonNull String srtLine) {
        String[] timeParts = srtLine.split(":");
        String[] secondsParts = timeParts[2].split(",");
        int milliseconds = 0;
        milliseconds += Integer.parseInt(timeParts[0]) * 3600000; // hours
        milliseconds += Integer.parseInt(timeParts[1]) * 60000; // minutes
        milliseconds += Integer.parseInt(secondsParts[0]) * 1000; // seconds
        milliseconds += Integer.parseInt(secondsParts[1]); // milliseconds
        return milliseconds;
    }


}