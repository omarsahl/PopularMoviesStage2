package com.os.popularmoviesstage2.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Omar on 17-Feb-18 1:12 AM
 */

public class NetworkMonitor {
    public enum State {
        CONNECTED(1), DISCONNECTED(0);

        private final int i;

        State(int i) {
            this.i = i;
        }

        public boolean isConnected() {
            return this.i == CONNECTED.i;
        }
    }

    public static State getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected ? State.CONNECTED : State.DISCONNECTED;
    }
}
