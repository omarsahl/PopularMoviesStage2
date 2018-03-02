package com.os.popularmoviesstage2.connectivity;

/**
 * Created by Omar on 28-Feb-18 10:10 AM
 */

public interface OnConnectivityChangedListener {
    void onConnectivityChanged(NetworkMonitor.State state);
}
