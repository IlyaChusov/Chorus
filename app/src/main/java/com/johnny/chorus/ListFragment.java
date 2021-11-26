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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.johnny.chorus.model.Lab;
import com.johnny.chorus.model.Song;
import com.johnny.chorus.model.SongsList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListFragment extends Fragment {

    private static final String SONGS_LIST_ID = "songsListId";

    private int songsListId;
    private SongAdapter adapter;

    @NotNull
    public static ListFragment create(int songsListId) {
        ListFragment listFragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SONGS_LIST_ID, songsListId);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_songs, container, false);
        SongsList songsList = null;

        if (getArguments() != null) {
            songsListId = getArguments().getInt(SONGS_LIST_ID);
            songsList = Lab.getLab().getSongsList(songsListId);
        }
        RecyclerView mRecyclerView = v.findViewById(R.id.song_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (songsList != null) {
            adapter = new SongAdapter(songsList);
            mRecyclerView.setAdapter(adapter);
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
                    SongActivity.newIntent(getContext(), songId, songsListId),
                    activityOptions.toBundle());
        }
    }

    private class SongAdapter extends RecyclerView.Adapter<SongHolder> {
        private final List<Song> songsList;

        public SongAdapter(@NonNull @NotNull SongsList songsList) {
            this.songsList = songsList.getSongs();
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