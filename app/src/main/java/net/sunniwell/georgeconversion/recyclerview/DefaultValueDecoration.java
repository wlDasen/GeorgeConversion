package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import net.sunniwell.georgeconversion.R;

/**
 * Created by admin on 2017/11/8.
 */

public class DefaultValueDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "jpd-NavID";
    private int mDividerHeight;
    private Paint mPaint;

    public DefaultValueDecoration(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mDividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDividerHeight;
        Log.d(TAG, "getItemOffsets: view:" + view);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int left = parent.getPaddingLeft();
            Log.d(TAG, "onDraw: left:" + left);
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = view.getBottom();
            int bottom = view.getBottom() + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
