package com.os.popularmoviesstage2.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Omar on 01-Mar-18 7:05 PM
 */

public class ActorsListItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ActorsListItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = 0;
        outRect.top = 0;
        outRect.right = space;

        int position = parent.getChildAdapterPosition(view);

        if (position == 0) {
            outRect.left = space;
        }
    }
}
