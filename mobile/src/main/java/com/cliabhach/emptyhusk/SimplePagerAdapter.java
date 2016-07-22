package com.cliabhach.emptyhusk;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
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

    private final LruCache<String, View> viewCache;

    public SimplePagerAdapter(@NonNull String[] values) {
        this.values = values;
        viewCache = new LruCache<>(values.length);
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

        viewCache.put(value, inflated);

        container.addView(inflated);

        return value;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = viewCache.get(object.toString());
        if (view != null) {
            container.removeView(view);
        }
    }
}
