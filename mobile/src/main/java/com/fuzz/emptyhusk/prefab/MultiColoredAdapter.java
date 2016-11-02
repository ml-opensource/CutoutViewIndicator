package com.fuzz.emptyhusk.prefab;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class MultiColoredAdapter extends RecyclerView.Adapter {

    private final int pageCount;
    private SparseIntArray colors;
    private int childLayoutId;

    public MultiColoredAdapter(int pageCount, int childLayoutId) {
        this.pageCount = pageCount;
        colors = new SparseIntArray(pageCount);
        this.childLayoutId = childLayoutId;
        Random rand = new Random();
        for (int i = 0; i < pageCount; i++) {
            int red = rand.nextInt(256);
            int green = rand.nextInt(256);
            int blue = rand.nextInt(256);
            colors.put(i, Color.rgb(red, green, blue));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View inflated = inflater.inflate(childLayoutId, parent, false);

        return new RecyclerView.ViewHolder(inflated) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Set a new background color.
        holder.itemView.setBackgroundColor(colors.get(position));
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }
}
