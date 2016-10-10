package com.fuzz.emptyhusk.choosegenerator;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fuzz.emptyhusk.R;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
class GeneratorViewHolder extends RecyclerView.ViewHolder {
    protected TextView title;
    protected TextView description;

    public GeneratorViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
    }

    public void setSelected(boolean selected) {
        PorterDuffColorFilter filter;
        if (selected) {
            filter = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        } else {
            filter = null;
        }
        itemView.getBackground().setColorFilter(filter);
    }
}
