package com.johnny.chorus;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.johnny.chorus.model.Song;
import com.johnny.chorus.model.SongType;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ListFragment extends Fragment {

    private static final String SONGS_TYPE = "songsType";

    private SongAdapter adapter;

    @NotNull
    public static ListFragment create(SongType songType) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SONGS_TYPE, songType);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    private void setAdapter(List<Song> songsList, RecyclerView mRecyclerView) {
        if (songsList != null) {
            adapter = new SongAdapter(songsList);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        SongRepository repository = SongRepository.get();

        RecyclerView mRecyclerView = v.findViewById(R.id.song_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SongAdapter(Collections.emptyList());

        if (getArguments() != null) {
            LiveData<List<Song>> songsList = repository.getSongs((SongType) getArguments().getSerializable(SONGS_TYPE));
            songsList.observe(getViewLifecycleOwner(), songs -> setAdapter(songs, mRecyclerView));
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        adapter = null;
        super.onDestroyView();
    }

    private class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mNumberView;
        private int songId;

        public SongHolder(@NonNull @NotNull LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));

            itemView.setOnClickListener(this);
            mNumberView = itemView.findViewById(R.id.sing_number);
        }

        public void bind(@NonNull @NotNull Song song) {
            mNumberView.setText(getString(R.string.glas_s, String.valueOf(song.getNumber())));
            songId = song.getNumber();
        }

        @Override
        public void onClick(@NonNull View v) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    requireActivity(),
                    v,
                    SongActivity.getSharedElement());
            startActivity(
                    SongActivity.newIntent(getContext(), songId),
                    activityOptions.toBundle());
        }
    }

    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {
        private final List<Song> songsList;

        public SongAdapter(@NonNull @NotNull List<Song> songsList) {
            this.songsList = songsList;
        }

        @NonNull
        @NotNull
        @Override
        public SongHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new SongHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SongHolder holder, int position) {
            holder.bind(songsList.get(position));
        }

        @Override
        public int getItemCount() {
            return songsList.size();
        }
    }
}