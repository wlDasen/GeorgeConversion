package net.sunniwell.georgeconversion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import net.sunniwell.georgeconversion.MainApplication;
import net.sunniwell.georgeconversion.TestActivity;
import net.sunniwell.georgeconversion.interfaces.ItemSwipeListener;

/**
 * Created by admin on 2017/10/12.
 */

public class DragItemHelperCallback extends ItemTouchHelper.Callback {
    public static final String TAG = "jpd-DragItemHelper";
    private ItemSwipeListener listener;

    public DragItemHelperCallback(ItemSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // TODO: 2017/10/12
        if (direction == ItemTouchHelper.START) {
            Log.d(TAG, "onSwiped: 往左滑动");
        } else {
            Log.d(TAG, "onSwiped: 往右滑动");
            if (listener != null) {
                Log.d(TAG, "onSwiped: ");
                listener.onItemSwipe(viewHolder.getAdapterPosition());
            }
        }
    }
}
