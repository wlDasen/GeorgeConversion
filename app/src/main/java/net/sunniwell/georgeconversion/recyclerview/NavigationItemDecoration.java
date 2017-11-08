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

import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class NavigationItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "jpd-NavID";
    private int mDividerHeight;
    private Paint mPaint;

    public NavigationItemDecoration(Context context) {
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
        Log.d(TAG, "onDraw: pL:" + parent.getPaddingLeft() + ",pR:" + parent.getPaddingRight());
        Log.d(TAG, "onDraw: parent:" + parent);
        Log.d(TAG, "onDraw: child:" + parent.getChildAt(0));
        for (int i = 0; i < childCount - 1; i++) {
            LinearLayout ll = (LinearLayout)parent.getChildAt(i);
            View view = ll.getChildAt(0);
            int left = view.getPaddingLeft();
            Log.d(TAG, "onDraw: left:" + left);
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = ll.getBottom();
            int bottom = ll.getBottom() + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
