package com.os.popularmoviesstage2.connectivity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by Omar on 17-Feb-18 1:15 AM
 */

public class NetworkStateReceiver extends BroadcastReceiver implements LifecycleObserver {
    private static final String TAG = NetworkStateReceiver.class.getSimpleName();
    private Context context;
    private Lifecycle lifecycle;
    private OnConnectivityChangedListener listener;

    public NetworkStateReceiver(Context context, Lifecycle lifecycle, OnConnectivityChangedListener listener) {
        this.context = context;
        this.lifecycle = lifecycle;
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: connectivity changed!");
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            listener.onConnectivityChanged(NetworkMonitor.getNetworkStatus(context));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void register() {
        Log.d(TAG, "registered network state receiver to " + context.getClass().getSimpleName());
        context.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void unregister() {
        Log.d(TAG, "unregistered network state receiver to " + context.getClass().getSimpleName());
        context.unregisterReceiver(this);
    }
}
