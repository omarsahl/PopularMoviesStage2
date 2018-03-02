package com.os.popularmoviesstage2.connectivity;

import java.io.IOException;

/**
 * Created by Omar on 28-Feb-18 6:59 AM
 */

public class NoNetworkException extends IOException {
    public NoNetworkException(String message) {
        super(message);
    }
}
