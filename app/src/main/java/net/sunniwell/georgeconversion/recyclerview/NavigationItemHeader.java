package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.NaviSettingItem;

import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class NavigationItemHeader extends RecyclerView.ItemDecoration {
    private static final String TAG = "jpd-NavIH";
    private List<NaviSettingItem> mTitleList;
    private int mHeaderHeight;
    private Paint mRectPaint;
    private Paint mTextPaint;
    private int mHeaderLeftPadding;

    public NavigationItemHeader(Context context, List<NaviSettingItem> list) {
        mTitleList = list;
        mHeaderHeight = context.getResources().getDimensionPixelSize(R.dimen.navigation_header_height);
        mHeaderLeftPadding = context.getResources().getDimensionPixelSize(R.dimen.header_padding_left);
        mRectPaint = new Paint();
        mRectPaint.setColor(Color.WHITE);
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize(50);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                outRect.top = mHeaderHeight;
            } else {
                if (!mTitleList.get(i).getGroup().equals(mTitleList.get(i - 1).getGroup())) {
                    outRect.top = mHeaderHeight;
                } else {
                    outRect.top = 0;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom;
        int top;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int start = parent.getPaddingLeft() + mHeaderLeftPadding;
            int end = (int)(view.getTop() - mHeaderHeight + (mHeaderHeight / 2 - (mTextPaint.ascent() + mTextPaint.descent()) /2));
            if (i == 0) {
                top = view.getTop() - mHeaderHeight;
                bottom = view.getTop();
                c.drawRect(left, top, right,bottom, mRectPaint);
                c.drawText(mTitleList.get(i).getGroup(), start, end, mTextPaint);
            } else {
                if (!mTitleList.get(i).getGroup().equals(mTitleList.get(i - 1).getGroup())) {
                    top = view.getTop() - mHeaderHeight;
                    bottom = view.getTop();
                    c.drawRect(left, top, right,bottom, mRectPaint);
                    c.drawText(mTitleList.get(i).getGroup(), start, end, mTextPaint);
                }
            }
        }
    }
}