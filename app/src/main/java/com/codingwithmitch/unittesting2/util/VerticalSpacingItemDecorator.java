package com.codingwithmitch.unittesting2.util;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration{

    private final int verticalSpaceHeight;

    public VerticalSpacingItemDecorator(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        outRect.bottom = verticalSpaceHeight;
    }
}