package com.os.popularmoviesstage2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.popularmoviesstage2.models.Review;
import com.os.popularmoviesstage2.utils.gson.SkipSerializationStrategy;

import org.junit.Test;

/**
 * Created by Omar on 02-Mar-18 1:06 AM
 */

public class GsonTest {

    /* test whether the SkipSerializationStrategy works or not */
    @Test
    public void testGson() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new SkipSerializationStrategy())
                .create();

        String s = gson.toJson(new Review(12, "sdfdf", "Omar sahl", "content content", "os.com"));
        System.out.println(s);
    }
}
