package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import net.sunniwell.georgeconversion.MainApplication;
import net.sunniwell.georgeconversion.interfaces.ItemSwipeListener;

/**
 * 实现RecyclerView的左右滑动
 */

public class DragItemHelperCallback extends ItemTouchHelper.Callback {
    public static final String TAG = "jpd-DragItemHelper";
    private ItemSwipeListener listener;

    public DragItemHelperCallback(ItemSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.END;
        return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d(TAG, "onSwiped: 往右滑动");
            if (listener != null) {
                listener.onItemSwipe(viewHolder.getAdapterPosition());
            }
    }
}
