package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.NaviSettingItem;

import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class NavigationSettingAdaptor extends RecyclerView.Adapter<NavigationSettingAdaptor.ViewHolder> {
    private static final String TAG = "jpd-NSAdaptor";
    private List<NaviSettingItem> mItemList;
    private Context mContext;

    public NavigationSettingAdaptor(Context context, List<NaviSettingItem> list) {
        mContext = context;
        mItemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.navigation_setting_main_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NaviSettingItem item = mItemList.get(position);
        String title = item.getItemTitle();
        String value = item.getItemValue();
        holder.textTitle.setText(title);
        if (!"".equals(value)) {
            holder.textValue.setText(value);
        } else {
            holder.textValue.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle;
        public TextView textValue;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView)itemView.findViewById(R.id.text_title);
            textValue = (TextView)itemView.findViewById(R.id.text_value);
        }
    }
}
