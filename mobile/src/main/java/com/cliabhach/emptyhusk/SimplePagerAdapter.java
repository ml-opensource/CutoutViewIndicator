package com.cliabhach.emptyhusk;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Philip Cohn-Cort (Fuzz)
 */
public class SimplePagerAdapter extends PagerAdapter {

    @NonNull
    private final String[] values;

    public SimplePagerAdapter(@NonNull String[] values) {
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        TextView textView = (TextView) view.findViewById(R.id.cell_text);
        CharSequence text = null;

        if (textView != null) {
            text = textView.getText();
        }

        return text != null && text.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String value = values[position];

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View inflated = inflater.inflate(R.layout.pager_cell_text, container, false);
        TextView item = (TextView) inflated.findViewById(R.id.cell_text);

        item.setText(value);

        return value;
    }
}
