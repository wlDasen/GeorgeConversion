package net.sunniwell.georgeconversion.recyclerview;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.sunniwell.georgeconversion.MainApplication;
import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.view.CustomEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/12.
 */


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<Money> mCountryList = new ArrayList<>();
    public static final String TAG = "jpd-CustomAdapter";
    private View mCurrentItem;
    private CustomEditText mCurrentEdit;
    private int mCurrentItemPosition;
    private List<CustomEditText> mCusEditList = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();
    private CustomEditText.OnEditTouchListener listener;
    /**
     * 全局静态SharedPreferences
     */
    private static SharedPreferences mPrefs;

    public CustomAdapter(List<Money> countryList) {
        Log.d(TAG, "CustomAdapter: ");
        this.mCountryList = countryList;
        listener = new CustomEditText.OnEditTouchListener() {
            @Override
            public void onEditTouch(CustomEditText cet, int position) {
                Log.d(TAG, "onEditTouch: position:" + position + ",text:" + cet.getText());
                onClick(mViewList.get(position));
            }
        };
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
        DefaultMoney money = mCountryList.get(position);
        holder.countryName.setText(money.getName());
        holder.countryCode.setText(money.getCode());
        holder.moneyNumber.setText(String.valueOf(money.getCount()));
        holder.itemView.setTag(position);
        holder.moneyNumber.setTag(position);
        holder.moneyNumber.setOnEditTouchListener(listener);
        holder.itemView.setOnClickListener(this);
        mCusEditList.add(holder.moneyNumber);
        mViewList.add(holder.itemView);

        int cuPos = getPreFlag();
        if (cuPos == -1) {
            if (position == 0) {
                mCountryList.get(position).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentEdit = holder.moneyNumber;
                mCurrentItemPosition = position;
            }
        } else {
            if (cuPos == position) {
                mCountryList.get(cuPos).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentEdit = holder.moneyNumber;
                mCurrentItemPosition = position;
            }
        }
        holder.itemView.setTag(position);
        if (mCountryList.get(position).isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.item_bg_select);
            holder.moneyNumber.obtainFocus();
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
        if (mCurrentEdit != null) {
            mCurrentEdit.loseFocus();
        }
        v.setBackgroundResource(R.drawable.item_bg_select);
        mCurrentItem = v;
        mCurrentItemPosition = (int)v.getTag();
        mCurrentEdit = mCusEditList.get(mCurrentItemPosition);
        Log.d(TAG, "onClick: text:" + mCurrentEdit.getText());
        mCurrentEdit.obtainFocus();
        mCountryList.get(mCurrentItemPosition).setSelected(true);
        setPreFlag(mCurrentItemPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView countryName;
        public TextView countryCode;
        public CustomEditText moneyNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            countryName = (TextView)itemView.findViewById(R.id.item_country_name);
            countryCode = (TextView)itemView.findViewById(R.id.item_country_code);
            moneyNumber = (CustomEditText) itemView.findViewById(R.id.item_money_number);
        }
    }
}
