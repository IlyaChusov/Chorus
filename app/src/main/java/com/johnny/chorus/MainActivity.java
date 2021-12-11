package com.johnny.chorus;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.johnny.chorus.model.SongType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Chorus);
        super.onCreate(savedInstanceState);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        ViewPager2 viewPager = findViewById(R.id.fragment_pager);
        viewPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            int i = 0;

            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                return ListFragment.create(SongType.values()[i++]);
            }

            @Override
            public int getItemCount() {
                return SongType.values().length;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText(R.string.title_tropary_caps);
            else
                tab.setText(R.string.title_stikhiry_caps);
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.default_tone) {
            new ToneDialogFragment().show(getSupportFragmentManager(), "dialogTone");
            return true;
        } else if (itemId == R.id.repeat) {
            new RepModeDialogFragment().show(getSupportFragmentManager(), "dialogRepeatMode");
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}