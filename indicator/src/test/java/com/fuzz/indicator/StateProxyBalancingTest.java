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
package com.fuzz.indicator;

import android.database.DataSetObserver;
import android.view.ViewTreeObserver;

import com.fuzz.indicator.proxy.StateProxy;
import com.fuzz.indicator.proxy.UnavailableProxy;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests of whether calls to {@link StateProxy#associateWith} are always mirrored by
 * calls to {@link StateProxy#disassociateFrom}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class StateProxyBalancingTest {

    protected CutoutViewIndicator indicator = mock(CutoutViewIndicator.class);
    protected StateProxy proxyA = mock(StateProxy.class);
    protected StateProxy proxyB = mock(StateProxy.class);

    @Before
    public void setUp() throws Exception {

        ViewTreeObserver vtoMock = mock(ViewTreeObserver.class);

        indicator.dataSetObserver = mock(DataSetObserver.class);
        indicator.stateProxy = new UnavailableProxy();

        when(indicator.getViewTreeObserver()).thenReturn(vtoMock);
        doCallRealMethod().when(indicator).setStateProxy(any(StateProxy.class));

        when(proxyA.canObserve(any(DataSetObserver.class))).thenReturn(false);

        when(proxyB.canObserve(any(DataSetObserver.class))).thenReturn(true);
    }

    @Test
    public void associateThenDisassociateWithNoChanges() throws Exception {
        when(proxyA.canObserve(any(DataSetObserver.class))).thenReturn(false);

        indicator.setStateProxy(proxyA);

        verify(proxyA).canObserve(any(DataSetObserver.class));
        verify(proxyA, never()).associateWith(any(DataSetObserver.class));

        indicator.setStateProxy(proxyB);

        verify(proxyA, never()).disassociateFrom(any(DataSetObserver.class));

        verify(proxyB).canObserve(any(DataSetObserver.class));
        verify(proxyB).associateWith(any(DataSetObserver.class));
    }

    @Test
    public void associateThenDisassociateWithChangedObservability() throws Exception {
        when(proxyA.canObserve(any(DataSetObserver.class))).thenReturn(false);

        indicator.setStateProxy(proxyA);

        verify(proxyA).canObserve(any(DataSetObserver.class));
        verify(proxyA, never()).associateWith(any(DataSetObserver.class));

        // Change whether this proxy can be observed
        when(proxyA.canObserve(any(DataSetObserver.class))).thenReturn(true);

        indicator.setStateProxy(proxyB);

        verify(proxyA, never()).disassociateFrom(any(DataSetObserver.class));

        verify(proxyB).canObserve(any(DataSetObserver.class));
        verify(proxyB).associateWith(any(DataSetObserver.class));
    }
}
