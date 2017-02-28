/*
 * Copyright 2016 Philip Cohn-Cort
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuzz.emptyhusk.choosegenerator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutCellGenerator;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * A specialised dialog to bring up a {@link GeneratorChoiceAdapter}, allowing the user
 * to select among the demo implementations of {@link CutoutCellGenerator} included
 * in this app.
 * <p>
 *     Please create instances via the static {@link #newInstance(Class)} method - this
 *     ensures that the previously-selected Generator appears selected when this dialog
 *     appears on screen.
 * </p>
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class GeneratorChoiceFragment extends DialogFragment {

    public static final String TAG = GeneratorChoiceFragment.class.getSimpleName();

    private static final String ARG_PRESELECT = TAG + "-preselect";

    @NonNull
    protected GeneratorChoiceAdapter adapter = new GeneratorChoiceAdapter();

    @Nullable
    protected OnSelectedListener<Class<? extends CutoutCellGenerator>> onSelectedListener;

    public static GeneratorChoiceFragment newInstance(@NonNull Class<? extends CutoutCellGenerator> present) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRESELECT, present);

        GeneratorChoiceFragment fragment = new GeneratorChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnSelectedListener(@Nullable OnSelectedListener<Class<? extends CutoutCellGenerator>> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        OnClickListener positiveListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onSelectedListener != null) {
                    onSelectedListener.onSelected(adapter.getChosen());
                }
                dismiss();
            }
        };

        adapter.setChosen(getChosenFrom(getArguments()));

        return createRecyclerViewBasedDialog(adapter, positiveListener);
    }

    protected Dialog createRecyclerViewBasedDialog(GeneratorChoiceAdapter adapter, OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());

        LayoutInflater inflater = LayoutInflater.from(builder.getContext());

        // We're calling it 'decoy' because this is a throwaway View
        ViewGroup decoy = new FrameLayout(builder.getContext());

        // Satiate the lint check by inflating against this decoy
        RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.dialog_generator, decoy, false);
        rootView.setAdapter(adapter);
        rootView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));

        return builder
                .setPositiveButton("Change", positiveListener)
                .setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setView(rootView)
                .create();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private Class<? extends CutoutCellGenerator> getChosenFrom(@Nullable Bundle bundle) {
        Class<? extends CutoutCellGenerator> chosen = null;
        if (bundle != null) {
            chosen = (Class<? extends CutoutCellGenerator>) bundle.getSerializable(ARG_PRESELECT);
        }
        if (chosen == null) {
            throw new IllegalStateException("This class must be seeded with a non-null preselection.");
        }
        return chosen;
    }
}
