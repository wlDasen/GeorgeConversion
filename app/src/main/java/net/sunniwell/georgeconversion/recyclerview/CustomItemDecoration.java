package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.sunniwell.georgeconversion.R;

/**
 * Created by admin on 2017/10/24.
 */

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "jpd-CusIDec";
    private int mDividerHeight;
    private Paint mPaint;

    public CustomItemDecoration(Context context) {
        mPaint = new Paint();
        mDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider);
        mPaint.setColor(Color.GRAY);
//        Log.d(TAG, "CustomItemDecoration: mD:" + mDividerHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        Log.d(TAG, "getItemOffsets: ");
        outRect.bottom = mDividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.d(TAG, "onDraw: ");
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = view.getBottom() + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
//        Log.d(TAG, "onDrawOver: ");
    }
}
