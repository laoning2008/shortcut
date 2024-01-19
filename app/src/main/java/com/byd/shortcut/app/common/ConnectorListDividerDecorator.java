package com.byd.shortcut.app.common;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectorListDividerDecorator extends RecyclerView.ItemDecoration {
    private final Drawable mDrawable;
    private final int mDividerHeight;
    private final int mDividerWidth;

    public ConnectorListDividerDecorator(Drawable divider) {
        mDrawable = divider;
        mDividerHeight = mDrawable.getIntrinsicHeight();
        mDividerWidth = mDrawable.getIntrinsicWidth();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int childCount = parent.getChildCount();


        if (childCount == 0) {
            return;
        }

        final float xPositionThreshold = mDividerHeight + 1.0f; // [px]
        final float yPositionThreshold = mDividerWidth + 1.0f; // [px]
        final float zPositionThreshold = 1.0f; // [px]

        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final View nextChild = parent.getChildAt(i + 1);

            if ((child.getVisibility() != View.VISIBLE) ||
                    (nextChild.getVisibility() != View.VISIBLE)) {
                continue;
            }

            // check if the next item is placed at the bottom or right
//            final float childBottom = child.getBottom() + child.getTranslationY();
//            final float nextChildTop = nextChild.getTop() + nextChild.getTranslationY();
//            final float childRight = child.getRight() + child.getTranslationX();
//            final float nextChildLeft = nextChild.getLeft() + nextChild.getTranslationX();
//
//            if (!(((mHorizontalDividerHeight != 0) && (Math.abs(nextChildTop - childBottom) < yPositionThreshold)) ||
//                    ((mVerticalDividerWidth != 0) && (Math.abs(nextChildLeft - childRight) < xPositionThreshold)))) {
//                continue;
//            }

            // check if the next item is placed on the same plane
            final float childZ = ViewCompat.getTranslationZ(child) + ViewCompat.getElevation(child);
            final float nextChildZ = ViewCompat.getTranslationZ(nextChild) + ViewCompat.getElevation(nextChild);

            if (!(Math.abs(nextChildZ - childZ) < zPositionThreshold)) {
                continue;
            }

            final float childAlpha = child.getAlpha();
            final float nextChildAlpha = nextChild.getAlpha();

            final int tx = (int) (child.getTranslationX() + 0.5f);
            final int ty = (int) (child.getTranslationY() + 0.5f);

            final int left = (child.getLeft() + child.getRight() - mDividerWidth) / 2;
            final int right = (child.getLeft() + child.getRight() + mDividerWidth) / 2;
            final int top = child.getBottom();
            final int bottom = top + mDividerHeight;

            mDrawable.setAlpha((int) ((0.5f * 255) * (childAlpha + nextChildAlpha) + 0.5f));
            mDrawable.setBounds(left + tx, top + ty, right + tx, bottom + ty);
            mDrawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, mDividerWidth, mDividerHeight);
    }
}
