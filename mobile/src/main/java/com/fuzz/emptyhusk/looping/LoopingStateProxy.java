/*
 * Copyright 2017 Philip Cohn-Cort
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
package com.fuzz.emptyhusk.looping;

import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.fuzz.indicator.proxy.IndicatorOffsetEvent;
import com.fuzz.indicator.proxy.ProxyReference;
import com.fuzz.indicator.proxy.StateProxy;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * This StateProxy works much like an automaton - it announces positions from 0 to
 * {@link #getCellCount()} in increments of {@link #DIFF} every
 * {@link #DELAY_MILLIS} milliseconds.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LoopingStateProxy implements StateProxy {

    public static final long DELAY_MILLIS = 100L;
    public static final float DIFF = .01f;

    /**
     * This thread is where the {@link LoopingRunnable} gets run
     */
    @NonNull
    protected final LoopingThread preferredMailThread = new LoopingThread();

    /**
     * Initialize as half of {@link #getCellCount()}.
     */
    protected float currentPosition = getCellCount() / 2.0f;

    protected final Map<Integer, Runnable> callbacks = new ArrayMap<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LoopingStateProxy() {
        preferredMailThread.start();
    }

    @Override
    public float getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCellCount() {
        return 3;
    }

    @Override
    public IndicatorOffsetEvent resendPositionInfo(ProxyReference cvi, float assumedIndicatorPosition) {
        return IndicatorOffsetEvent.from(cvi, assumedIndicatorPosition);
    }

    @Override
    public void associateWith(DataSetObserver observer) {
        Runnable runnable = new LoopingRunnable(observer);
        callbacks.put(observer.hashCode(), runnable);
        preferredMailThread.repost(runnable, DELAY_MILLIS);
    }

    @Override
    public void disassociateFrom(DataSetObserver observer) {
        Runnable removed = callbacks.remove(observer.hashCode());
        preferredMailThread.stopPosting(removed);
    }

    @Override
    public boolean canObserve(DataSetObserver observer) {
        return observer != null;
    }

    /**
     * Simple runnable that
     * <ol>
     *     <li>Updates {@link #currentPosition}</li>
     *     <li>Checks whether {@link LoopingRunnable#o} has been garbage-collected
     *         <ol>
     *             <li>If so, it sends itself into {@link LoopingThread#stopPosting(Runnable)}</li>
     *             <li>If not, it calls {@link DataSetObserver#onChanged()} and
     *             {@link LoopingThread#repost(Runnable, long) reposts} itself</li>
     *         </ol>
     *     </li>
     * </ol>
     * @see #DELAY_MILLIS
     */
    private class LoopingRunnable implements Runnable {
        private final WeakReference<DataSetObserver> o;

        public LoopingRunnable(DataSetObserver observer) {
            this.o = new WeakReference<>(observer);
        }

        @Override
        public void run() {
            currentPosition += DIFF;
            currentPosition %= (getCellCount() * 1.0f);

            final DataSetObserver ref = o.get();
            if (ref != null) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ref.onChanged();
                    }
                });
                preferredMailThread.repost(this, DELAY_MILLIS);
            } else {
                preferredMailThread.stopPosting(this);
            }
        }
    }
}
