package net.sunniwell.georgeconversion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by admin on 2017/10/12.
 */

public class DragItemHelperCallback extends ItemTouchHelper.Callback {
    public static final String TAG = "jpd-DragItemHelper";

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
        Log.d(TAG, "onSwiped: direction:" + direction);
        Context context = MainApplication.getContext();
        // TODO: 2017/10/12  
        if (direction == ItemTouchHelper.START) {
            Log.d(TAG, "onSwiped: 往左滑动");
        } else {
            Log.d(TAG, "onSwiped: 往右滑动");
            Intent intent = new Intent(context, TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
