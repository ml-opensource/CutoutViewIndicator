package com.fuzz.emptyhusk.prefab;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuzz.emptyhusk.R;
import com.fuzz.indicator.CutoutViewIndicator;
import com.fuzz.indicator.ImageViewGenerator;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * This fragment does not expect any arguments to be passed in.
 * All configuration and appearance logic is self-contained.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class VerticalDotsFragment extends Fragment {
    private static final String ARG_CHILD_LAYOUT_ID = "child_layout_id";
    private static final String ARG_CHILD_QUANTITY = "child_quantity";

    public static @NonNull Bundle buildArguments(int childQuantity, @LayoutRes int childLayoutId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CHILD_QUANTITY, childQuantity);
        args.putInt(ARG_CHILD_LAYOUT_ID, childLayoutId);
        return args;
    }

    public int getChildQuantity() {
        return getArguments().getInt(ARG_CHILD_QUANTITY);
    }

    public int getChildLayoutId() {
        return getArguments().getInt(ARG_CHILD_LAYOUT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vertical_dots, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initRecycler(recyclerView);

        CutoutViewIndicator cvi = (CutoutViewIndicator) view.findViewById(R.id.cutoutViewIndicator);
        initIndicator(recyclerView, cvi);
    }

    private void initRecycler(@NonNull RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(), VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MultiColoredAdapter(getChildQuantity(), getChildLayoutId()));
    }

    /**
     * Precondition: at least one call to {@link #initRecycler(RecyclerView)} with the same parameter.
     * @param recyclerView    an initialised RecyclerView
     * @param cvi             a new CutoutViewIndicator
     */
    private void initIndicator(@NonNull RecyclerView recyclerView, @NonNull CutoutViewIndicator cvi) {
        cvi.setGenerator(new ImageViewGenerator());

        int initialDx = recyclerView.getScrollX();
        int initialDy = recyclerView.getScrollY();

        CVIScrollListener listener = new CVIScrollListener(cvi, initialDx, initialDy);
        RecyclerStateProxy proxy = new RecyclerStateProxy(recyclerView, cvi, listener);
        cvi.setStateProxy(proxy);
        recyclerView.addOnScrollListener(listener);
    }
}
