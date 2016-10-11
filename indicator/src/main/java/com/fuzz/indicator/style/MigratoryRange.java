/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Modifications Copyright 2016 Philip Cohn-Cort
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
package com.fuzz.indicator.style;

import android.support.annotation.NonNull;

/**
 * Backward-compatible version of Lollipop's {@link android.util.Range}.
 * <p>
 * Handy for representing a range of values without saying anything
 * about whether terminating values are included therein.
 * </p>
 * This is here specifically for making {@link MigratorySpan} a lot
 * easier to work with, but that doesn't mean it mightn't be useful
 * for other purposes.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public final class MigratoryRange<T extends Number & Comparable<T>> {

    public static abstract class Operator<T> {
        @NonNull
        public abstract T add(T lower, T offset);
    }

    public static final Operator<Float> FLOAT_OPERATOR
            = new Operator<Float>() {
        @NonNull
        @Override
        public Float add(Float lower, Float offset) {
            return lower + offset;
        }
    };

    public static MigratoryRange<Float> from(float lower, float upper) {
        return new MigratoryRange<>(lower, upper, FLOAT_OPERATOR);
    }

    @NonNull
    private final T lower;
    @NonNull
    private final T upper;
    @NonNull
    private final Operator<T> operator;

    public MigratoryRange(@NonNull T lower, @NonNull T upper, @NonNull Operator<T> operator) {
        this.lower = lower;
        this.upper = upper;
        this.operator = operator;

        if (lower.compareTo(upper) > 0) {
            throw new IllegalArgumentException("Migratory ranges may not have an upper bound below their lower bound.");
        }
    }

    @NonNull
    public T getLower() {
        return lower;
    }

    @NonNull
    public T getUpper() {
        return upper;
    }

    /**
     * If the parameter is within this range it will be
     * returned unaltered. If it isn't, the closest value
     * within the range (e.g. {@link #getLower()} or
     * {@link #getUpper()}) will be returned.
     *
     * @param value any non-null value of type T
     * @return a value guaranteed to be within this range
     */
    @NonNull
    public T clamp(@NonNull T value) {
        if (value.compareTo(lower) < 0) {
            return lower;
        } else if (value.compareTo(upper) > 0) {
            return upper;
        } else {
            return value;
        }
    }

    public double diff() {
        return upper.doubleValue() - lower.doubleValue();
    }

    public MigratoryRange<T> translate(@NonNull T offset) {
        T newLower = operator.add(lower, offset);
        T newUpper = operator.add(upper, offset);
        return new MigratoryRange<>(newLower, newUpper, operator);
    }
}
