package net.sunniwell.georgeconversion.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.SortData;

import java.util.List;

/**
 * Created by admin on 2017/10/24.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private static final String TAG = "jpd-SortAdapter";
    private List<SortData> mSortData;

    public SortAdapter(List<SortData> list) {
        mSortData = list;
        Log.d(TAG, "SortAdapter: size:" + mSortData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SortData data = mSortData.get(position);
        holder.mText.setText(data.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: position:" + position + ",name:" + mSortData.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSortData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.item);
            mImage = (ImageView)itemView.findViewById(R.id.check_mark);
        }
    }
}
