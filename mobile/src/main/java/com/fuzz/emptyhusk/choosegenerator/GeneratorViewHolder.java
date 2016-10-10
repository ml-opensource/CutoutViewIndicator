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
