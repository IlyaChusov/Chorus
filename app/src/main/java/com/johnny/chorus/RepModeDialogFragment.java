package com.johnny.chorus;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

public class RepModeDialogFragment extends DialogFragment {

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        String[] repeatModes = getResources().getStringArray(R.array.repeat_modes_array);
        boolean isRepeating = PreferenceWork.getDefaultRepeatingMode();
        int selectedModeId = isRepeating ? 0 : 1;

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.repeat_title)
                .setSingleChoiceItems(repeatModes, selectedModeId, (dialog, which) -> {
                    if (which != selectedModeId) {
                        PreferenceWork.setDefaultRepeatingMode(which == 0);
                        dismiss();
                    }
                }).create();
    }
}