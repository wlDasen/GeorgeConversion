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
    private List<Money> mMoneyList = new ArrayList<>();
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
        this.mMoneyList = countryList;
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
        Money money = mMoneyList.get(position);
        holder.moneyName.setText(money.getName());
        holder.moneyCode.setText(money.getCode());
        holder.moneyCount.setText(String.valueOf(money.getCount()));
        holder.itemView.setTag(position);
        holder.moneyCount.setTag(position);
        holder.moneyCount.setOnEditTouchListener(listener);
        holder.itemView.setOnClickListener(this);
        mCusEditList.add(holder.moneyCount);
        mViewList.add(holder.itemView);

        int cuPos = getPreFlag();
        if (cuPos == -1) {
            if (position == 0) {
                mMoneyList.get(position).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentEdit = holder.moneyCount;
                mCurrentItemPosition = position;
            }
        } else {
            if (cuPos == position) {
                mMoneyList.get(cuPos).setSelected(true);
                mCurrentItem = holder.itemView;
                mCurrentEdit = holder.moneyCount;
                mCurrentItemPosition = position;
            }
        }
        holder.itemView.setTag(position);
        if (mMoneyList.get(position).isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.item_bg_select);
            holder.moneyCount.obtainFocus();
        } else {
            holder.itemView.setBackgroundResource(R.drawable.item_bg);
        }
    }

    @Override
    public int getItemCount() {
        return mMoneyList.size();
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
            mMoneyList.get(mCurrentItemPosition).setSelected(false);
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
        mMoneyList.get(mCurrentItemPosition).setSelected(true);
        setPreFlag(mCurrentItemPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView moneyName;
        public TextView moneyCode;
        public CustomEditText moneyCount;

        public ViewHolder(View itemView) {
            super(itemView);
            moneyName = (TextView)itemView.findViewById(R.id.item_name);
            moneyCode = (TextView)itemView.findViewById(R.id.item_code);
            moneyCount = (CustomEditText) itemView.findViewById(R.id.item_count);
        }
    }
}
