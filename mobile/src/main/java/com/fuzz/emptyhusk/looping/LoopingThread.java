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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;

/**
 * Send messages to this object's {@link Handler} with
 * {@link #repost(Runnable, long)}. Cancel any pending
 * messages with {@link #stopPosting(Runnable)}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class LoopingThread extends Thread {

    /**
     * ID for use with {@link Message#what}
     */
    public static final int UNIQUE_WHAT_ID = 3429;

    protected Handler mailTransport;

    private final CountDownLatch isReadyLatch = new CountDownLatch(1);

    @Override
    public void run() {
        Looper.prepare();
        mailTransport = new Handler();
        isReadyLatch.countDown();
        Looper.loop();
    }

    /**
     * Wait for {@link #mailTransport} to be ready, then send {@code runnable}
     * onto that handler. Note: {@link #mailTransport} is only usable while this
     * {@link LoopingThread} is running.
     *
     * @param runnable       any non-null runnable
     * @param delayMillis    second parameter to {@link Handler#sendMessageDelayed(Message, long)}
     */
    public void repost(Runnable runnable, long delayMillis) {
        try {
            isReadyLatch.await();
            Message m = Message.obtain(mailTransport, runnable);
            m.what = UNIQUE_WHAT_ID;
            mailTransport.sendMessageDelayed(m, delayMillis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Removes all callbacks from {@link #mailTransport} containing
     * {@code runnable}.
     * @param runnable    any runnable that was previously {@link #repost(Runnable, long) posted}
     */
    public void stopPosting(@NonNull Runnable runnable) {
        mailTransport.removeCallbacks(runnable);
    }
}
