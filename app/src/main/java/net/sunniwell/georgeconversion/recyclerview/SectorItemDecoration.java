package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.SortData;

import java.util.List;

/**
 * Created by admin on 2017/10/24.
 */

public class SectorItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "jpd-SID";
    private List<SortData> mDataList;
    private int headerHeight;
    private Paint mPaint;
    private TextPaint mTextPaint;
    Paint.FontMetrics mFontMetrics;
    private int headerPaddingLeft;

    public SectorItemDecoration(Context context, List<SortData> list) {
//        Log.d(TAG, "SectorItemDecoration: ");
        mDataList = list;
        headerHeight = context.getResources().getDimensionPixelSize(R.dimen.header_height);
        headerPaddingLeft = context.getResources().getDimensionPixelSize(R.dimen.header_padding_left);
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(40);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        mFontMetrics = new Paint.FontMetrics();
//        mTextPaint.getFontMetrics(mFontMetrics);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        Log.d(TAG, "getItemOffsets: ");
        int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        if (position == 0) {
            outRect.top = headerHeight;
        } else {
            if (mDataList.get(position).getFirstLetter() != null &&
                    !mDataList.get(position).getFirstLetter().equalsIgnoreCase(mDataList.get(position -1).getFirstLetter())) {
                outRect.top = headerHeight;
            } else {
                outRect.top = 0;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.d(TAG, "onDraw: ");
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
            int position = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
//            Log.d(TAG, "onDraw: position:" + position +",marginTop:" + layoutParams.topMargin);
            int top = view.getTop() - headerHeight - layoutParams.topMargin;
            int bottom = view.getTop() - layoutParams.topMargin;
//            int start = (int)(view.getWidth() / 2 - mTextPaint.measureText(mDataList.get(position).getFirstLetter()) / 2);
            int start = parent.getPaddingLeft() + headerPaddingLeft;
            int end = (int)(view.getTop() - headerHeight + (headerHeight / 2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2));
            if (position == 0) {
                c.drawRect(left, top, right, bottom, mPaint);
                c.drawText(mDataList.get(position).getFirstLetter(), start, end, mTextPaint);
            } else {
                if (mDataList.get(position).getFirstLetter() != null
                        && !mDataList.get(position).getFirstLetter().equalsIgnoreCase(mDataList.get(position -1).getFirstLetter())) {
                    c.drawRect(left, top, right, bottom, mPaint);
                    c.drawText(mDataList.get(position).getFirstLetter(), start, end, mTextPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
//        Log.d(TAG, "onDrawOver: ");
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
//            Log.d(TAG, "onDrawOver: i:" + i);
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)view.getLayoutParams();
            int index = parent.getChildAdapterPosition(view);
//            Log.d(TAG, "onDrawOver: index:" + index);

            if (i == 0) { // 屏幕第一个或者每个组的最后一个出现在第一个位置
                int top = parent.getPaddingTop();
                if (mDataList.size() > 1 && mDataList.get(index + 1) != null &&
                        !mDataList.get(index).getFirstLetter().equalsIgnoreCase(mDataList.get(index + 1).getFirstLetter())) {
                    int suggestTop = view.getBottom() - headerHeight;
                    if (suggestTop < top) {
                        top = suggestTop;
                    }
                }
                int bottom = top + headerHeight;
                c.drawRect(left, top, right, bottom, mPaint);
                int startX = left + headerPaddingLeft;
                int startY = top + (int)(headerHeight /2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2);
                c.drawText(mDataList.get(index).getFirstLetter(), startX, startY, mTextPaint);
            } else {
                // 非组内第一个Item不需要做任何事情
                if (mDataList.get(index).getFirstLetter().equalsIgnoreCase(mDataList.get(index -1).getFirstLetter())) {
                    continue;
                } else { // 组内第一个Item需要绘制Header
                    int top = view.getTop() - headerHeight;
                    int bottom = view.getTop();
                    c.drawRect(left, top, right, bottom, mPaint);
                    int startX = left + headerPaddingLeft;
                    int startY = top + (int)(headerHeight /2 - (mTextPaint.ascent() + mTextPaint.descent()) / 2);
                    c.drawText(mDataList.get(index).getFirstLetter(), startX, startY, mTextPaint);
                }
            }
        }
    }
}
