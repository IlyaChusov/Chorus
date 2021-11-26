package com.johnny.chorus;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class ToneDialogFragment extends DialogFragment {

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        String[] tones = getResources().getStringArray(R.array.tones_array);
        int selectedToneId = PreferenceWork.getDefaultToneId();

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.default_tone_title)
                .setSingleChoiceItems(tones, selectedToneId, (dialog, which) -> {
                    if (which != selectedToneId) {
                        PreferenceWork.setDefaultTone(which);
                        dismiss();
                    }
                }).create();
    }
}