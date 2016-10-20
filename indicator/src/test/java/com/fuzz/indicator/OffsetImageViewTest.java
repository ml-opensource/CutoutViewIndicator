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
package com.fuzz.indicator;

import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests logical assumptions in
 * {@link OffSetters#offsetImageBy(ImageView, int, float, Matrix)}.
 * Visual regressions are not checked, and neither is
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
@RunWith(Parameterized.class)
public class OffsetImageViewTest {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    @Parameterized.Parameters(name = "Test{index} : {0}x{1} at {3}")
    public static Iterable<? extends Object[]> data() {
        return Arrays.asList(new Number[][]{
                {0, 0, HORIZONTAL, 0},
                {5, 5, HORIZONTAL, -.77f},
                {100, 100, HORIZONTAL, 0},
                {50, 0, HORIZONTAL, .999f},
                {0, 50, HORIZONTAL, 0},
                {25, 75, HORIZONTAL, 0.3f},
                {-20, 0, HORIZONTAL, 0},
                {7, 7, VERTICAL, 0},
                {30, 0, VERTICAL, 0},
                {60, 60, VERTICAL, -1f},
                {100, 100, VERTICAL, -.2f},
                {0, 0, VERTICAL, 0}
        });
    }

    private final int width;
    private final int height;
    private final int orientation;
    private final float percentage;

    public OffsetImageViewTest(int width, int height, int orientation, float percentage) {
        this.width = width;
        this.height = height;
        this.orientation = orientation;
        this.percentage = percentage;
    }

    @Test
    public void offsetImageBy() throws Exception {
        ImageView imageView = mock(ImageView.class);
        Matrix matrix = mock(Matrix.class);

        when(imageView.getWidth()).thenReturn(width);
        when(imageView.getHeight()).thenReturn(height);

        OffSetters.offsetImageBy(imageView, orientation, percentage, matrix);

        // How do we test the method is setting the right values without copying its logic to here?
        verify(matrix).setTranslate(anyFloat(), anyFloat());
        verify(imageView).setImageMatrix(matrix);
    }

}