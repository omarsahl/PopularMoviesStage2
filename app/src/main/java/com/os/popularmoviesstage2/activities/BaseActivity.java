package com.os.popularmoviesstage2.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.os.popularmoviesstage2.R;
import com.os.popularmoviesstage2.connectivity.NetworkStateReceiver;

import de.mateware.snacky.Snacky;

/**
 * Created by Omar on 28-Feb-18 10:25 AM
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected NetworkStateReceiver networkStateReceiver;
    protected Snackbar networkBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkStateReceiver = new NetworkStateReceiver(this, getLifecycle(), state -> {
            if (state.isConnected()) {
                if (networkBar != null && networkBar.isShownOrQueued()) {
                    networkBar.dismiss();
                }
            } else {
                if (networkBar == null) initNetworkBar();
                if (!networkBar.isShownOrQueued()) networkBar.show();
            }
        });
        getLifecycle().addObserver(networkStateReceiver);
    }

    private void initNetworkBar() {
        networkBar = Snacky.builder()
                .setView(getSnackbarParent())
                .setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                .setIcon(R.drawable.ic_cloud_off_black_24dp)
                .setText(R.string.no_internet_message)
                .centerText()
                .setDuration(Snacky.LENGTH_INDEFINITE)
                .build();
    }

    @Override
    protected void onDestroy() {
        getLifecycle().removeObserver(networkStateReceiver);
        super.onDestroy();
    }

    protected abstract View getSnackbarParent();
}
