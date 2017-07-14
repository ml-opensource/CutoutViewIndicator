package com.fuzz.indicator;

import android.database.DataSetObserver;
import android.view.ViewTreeObserver;

import com.fuzz.indicator.proxy.StateProxy;
import com.fuzz.indicator.proxy.UnavailableProxy;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test of whether calls to {@link StateProxy#associateWith} are always mirrored by
 * calls to {@link StateProxy#disassociateFrom}.
 *
 * @author Philip Cohn-Cort (Fuzz)
 */
public class StateProxyBalancingTest {

    @Test
    public void associateThenDisassociateWithNoChanges() throws Exception {
        CutoutViewIndicator indicator = mock(CutoutViewIndicator.class);


        // Setup

        indicator.dataSetObserver = mock(DataSetObserver.class);
        indicator.stateProxy = new UnavailableProxy();
        ViewTreeObserver vtoMock = mock(ViewTreeObserver.class);

        when(indicator.getViewTreeObserver()).thenReturn(vtoMock);
        doCallRealMethod().when(indicator).setStateProxy(any(StateProxy.class));

        StateProxy proxyA = mock(StateProxy.class);
        when(proxyA.canObserve(any(DataSetObserver.class))).thenReturn(false);

        StateProxy proxyB = mock(StateProxy.class);
        when(proxyB.canObserve(any(DataSetObserver.class))).thenReturn(true);


        // The test

        indicator.setStateProxy(proxyA);

        verify(proxyA).canObserve(any(DataSetObserver.class));
        verify(proxyA, never()).associateWith(any(DataSetObserver.class));

        indicator.setStateProxy(proxyB);

        verify(proxyA, never()).disassociateFrom(any(DataSetObserver.class));

        verify(proxyB).canObserve(any(DataSetObserver.class));
        verify(proxyB).associateWith(any(DataSetObserver.class));
    }
}
