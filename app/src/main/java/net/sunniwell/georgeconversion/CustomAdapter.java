package net.sunniwell.georgeconversion;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/12.
 */


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<Country> countryList = new ArrayList<>();
    public static final String TAG = "jpd-CustomAdapter";
    private RelativeLayout selectedItem;

    public CustomAdapter(List<Country> countryList) {
        Log.d(TAG, "CustomAdapter: ");
        this.countryList = countryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getCountryName());
        holder.countryCode.setText(country.getCountryCode());
        holder.moneyNumber.setText(country.getMoneyNumber());
        holder.moneySymbol.setText(country.getMoneySymbol());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
//        if (selectedItem == null) {
//            Log.d(TAG, "onBindViewHolder: null.");
//            if (position == 0) {
//                Log.d(TAG, "onBindViewHolder: set position 0.");
//                selectedItem = (RelativeLayout)holder.itemView;
//                selectedItem.setBackgroundColor(Color.parseColor("#27408B"));
//            }
//        } else {
//            Log.d(TAG, "onBindViewHolder: not null.");
//            selectedItem.setBackgroundColor(Color.parseColor("#27408B"));
//        }
//        Log.d(TAG, "onBindViewHolder: tag:" + selectedItem.getTag());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        if (selectedItem != null) {
            Log.d(TAG, "onClick: not null");
            selectedItem.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        RelativeLayout layout = (RelativeLayout)v;
        layout.setBackgroundColor(Color.parseColor("#27408B"));
        selectedItem = layout;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView countryName;
        public TextView countryCode;
        public TextView moneyNumber;
        public TextView moneySymbol;

        public ViewHolder(View itemView) {
            super(itemView);
            countryName = (TextView)itemView.findViewById(R.id.item_country_name);
            countryCode = (TextView)itemView.findViewById(R.id.item_country_code);
            moneyNumber = (TextView)itemView.findViewById(R.id.item_money_number);
            moneySymbol = (TextView)itemView.findViewById(R.id.item_money_symbol);
        }
    }
}
