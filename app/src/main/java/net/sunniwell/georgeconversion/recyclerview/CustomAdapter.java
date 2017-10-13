package net.sunniwell.georgeconversion.recyclerview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sunniwell.georgeconversion.MainApplication;
import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/12.
 */


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<Country> mCountryList = new ArrayList<>();
    public static final String TAG = "jpd-CustomAdapter";
    private View mCurrentItem;
    private int mCurrentItemPosition;
    /**
     * 全局静态SharedPreferences
     */
    private static SharedPreferences mPrefs;

    public CustomAdapter(List<Country> countryList) {
        Log.d(TAG, "CustomAdapter: ");
        this.mCountryList = countryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: ");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: po:" + position);
        Country country = mCountryList.get(position);
        holder.countryName.setText(country.getCountryName());
        holder.countryCode.setText(country.getCountryCode());
        holder.moneyNumber.setText(country.getMoneyNumber());
        holder.moneySymbol.setText(country.getMoneySymbol());
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);

        int cuPos = getPreFlag();
        if (cuPos == -1) {
            if (position == 0) {
                mCountryList.get(position).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentItemPosition = position;
            }
        } else {
            if (cuPos == position) {
                mCountryList.get(cuPos).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentItemPosition = position;
            }
        }
        holder.itemView.setTag(position);
        if (mCountryList.get(position).isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.item_bg_select);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    /**
     * 获取全局静态SharedPreInstance
     * @return 全局静态SharedPreInstance
     */
    private SharedPreferences getSharedPreInstance() {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        }
        return mPrefs;
    }

    /**
     * 获取item选中偏好设置
     * @return
     */
    private int getPreFlag() {
        return getSharedPreInstance().getInt("itemPosition", -1);
    }

    /**
     * 设置item选中偏好
     * @param position 选中的位置
     */
    private void setPreFlag(int position) {
        SharedPreferences.Editor editor = getSharedPreInstance().edit();
        editor.putInt("itemPosition", position);
        editor.apply();
    }

    /**
     * item点击事件
     * @param v 点击的item
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        if (mCurrentItem != null) {
            mCurrentItem.setBackgroundResource(R.drawable.item_bg);
            mCountryList.get(mCurrentItemPosition).setSelected(false);
        }
        v.setBackgroundResource(R.drawable.item_bg_select);
        mCurrentItem = v;
        mCurrentItemPosition = (int)v.getTag();
        mCountryList.get(mCurrentItemPosition).setSelected(true);
        setPreFlag(mCurrentItemPosition);
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
