package net.sunniwell.georgeconversion.recyclerview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.sunniwell.georgeconversion.MainActivity;
import net.sunniwell.georgeconversion.MainApplication;
import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.util.CalculateUtil;
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
    private CustomEditText.OnEditTouchListener mETistener;
    /**
     * 全局静态SharedPreferences
     */
    private static SharedPreferences mPrefs;
    /**
     * 货币数量默认数字标志位 true-没有输入过数字 false-输入过数字
     */
    private boolean isDefaultState = true;
    /**
     * 网络刷新成功标志位，数据刷新成功后，置位true
     */
    public boolean needToRefresh = false;
    private StringBuilder mBuilder;
    /**
     * 标记处理数据接口的默认进入方式 0-处理数据前不进行任何操作 1-切换Item 2-点击数字按钮
     */
    private static final int TYPE_DO_NOTHING = 0;
    private static final int TYPE_CHANGE_ITEM = 1;
    private static final int TYPE_INPUT_NUMBER = 2;

    private static int mRefreshItemCount = 0;

    public CustomAdapter(Context context, List<Money> countryList) {
        Log.d(TAG, "CustomAdapter: ");
        this.mMoneyList = countryList;

        mETistener = new CustomEditText.OnEditTouchListener() {
            @Override
            public void onEditTouch(CustomEditText cet, int position) {
                Log.d(TAG, "onEditTouch: position:" + position + ",text:" + cet.getText());
                onClick(mViewList.get(position));
            }
        };
        mBuilder = new StringBuilder();
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
        mRefreshItemCount++;
        Log.d(TAG, "onBindViewHolder: refreshCount:" + mRefreshItemCount);
        Money money = mMoneyList.get(position);
        holder.moneyName.setText(money.getName());
        holder.moneyCode.setText(money.getCode());
        holder.itemView.setTag(position);
        holder.moneyCount.setTag(position);
        holder.moneyCount.setOnEditTouchListener(mETistener);
        holder.itemView.setOnClickListener(this);
        Log.d(TAG, "onBindViewHolder: position:" + position);
        if (mCusEditList.size() < 4) {
            mCusEditList.add(holder.moneyCount);
        } else {
            mCusEditList.set(position, holder.moneyCount);
        }
        if (mViewList.size() < 4) {
            mViewList.add(holder.itemView);
        } else {
            mViewList.set(position, holder.itemView);
        }

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
            Log.d(TAG, "onBindViewHolder: isSelected.");
            holder.itemView.setBackgroundResource(R.drawable.item_bg_select);
            holder.moneyCount.obtainFocus();
        } else {
            Log.d(TAG, "onBindViewHolder: not selected.");
            holder.itemView.setBackgroundResource(R.drawable.item_bg);
        }

        if (mRefreshItemCount == 4) {
            dealEditData(TYPE_DO_NOTHING);
            mCurrentEdit.moveCursorToEnd();
            mRefreshItemCount = 0;
        }
    }

    private void dealEditData(int enterType) {
//        Log.d(TAG, "dealEditData: isDefaultState:" + isDefaultState);
        Log.d(TAG, "dealEditData: mCurPo:" + mCurrentItemPosition);
        double[] rates = new double[4];
        double baseRate = mMoneyList.get(mCurrentItemPosition).getBase1CNYToCurrent();
        for (int i = 0; i < rates.length; i++) {
            rates[i] = mMoneyList.get(i).getBase1CNYToCurrent();
        }
        if (isDefaultState) {
            rates = CalculateUtil.calculate(rates, baseRate, 100);
        } else {
            if (enterType == TYPE_CHANGE_ITEM) {
                rates = CalculateUtil.calculate(rates, baseRate, Double.parseDouble(mCurrentEdit.getText().toString()));
            } else {
                rates = CalculateUtil.calculate(rates, baseRate, Double.parseDouble(mBuilder.toString()));
            }
        }
        for (int i = 0; i < rates.length; i++) {
//            Log.d(TAG, "dealEditData: before optimize:" + String.valueOf(rates[i]));
            String number = optimizeNumber(String.valueOf(rates[i]));
            Log.d(TAG, "dealEditData: number:" + number + ",text:" + mCusEditList.get(i).getText());
            // TODO: 2017/11/7 添加刷新后的数字动画
            if (needToRefresh) {
                if (!number.equals(mCusEditList.get(i).getText())) {
                    CustomEditText text = mCusEditList.get(i);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(text, "rotation"
                            , 0, 360);
                    animator.setDuration(500);
                    animator.start();
                }
            }
            mCusEditList.get(i).setText(number);
            if (isDefaultState) {
                mCusEditList.get(i).setTextColor(Color.BLACK);
            } else {
                mCusEditList.get(i).setTextColor(Color.WHITE);
            }
            if (i == mCurrentItemPosition) {
                mCusEditList.get(i).moveCursorToEnd();
            }
        }
        needToRefresh = false;
        printCusEditList(mCusEditList);
    }

    private void printCusEditList(List<CustomEditText> list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "printCusEditList: po:" + i + ",value:" + list.get(i).getText().toString() + ",tag:" + list.get(i).getTag());
        }
    }

    /**
     * 优化最终显示到界面上的数字（去除末尾多余的0)
     * @param number 要优化的数字字符串
     */
    private String optimizeNumber(String number) {
        String str = (number.length() > 10) ? number.substring(0, 10) : number;
        if (str.contains(".")) {
            // 1 123456789.
//            Log.d(TAG, "change: char:" + str.charAt(str.length() - 1));
            if (str.charAt(str.length() - 1) == '.') {
                str = str.substring(0, str.length() - 1);
            } else {
                int dotPosition = str.indexOf(".");
//                Log.d(TAG, "change: dotPo:" + dotPosition);
                String sub = str.substring(dotPosition + 1);
                // 2 123.0 123.00 123.000
                if (Integer.parseInt(sub) == 0) {
                    str = str.substring(0, dotPosition);
                } else {
                    int zeroCount = 0;
                    // 最后一位为0 123.230 123.230000
//                    Log.d(TAG, "change: sub:" + sub);
                    if (sub.charAt(sub.length() - 1) == '0') {
                        for (int i = sub.length() - 1; i >= 0; i--) {
                            if (sub.charAt(i) == '0') {
                                zeroCount++;
                            } else {
                                break;
                            }
                        }
//                        Log.d(TAG, "change: zeroCount:" + zeroCount);
                        str = str.substring(0, str.length() - zeroCount);
                    }
                }
            }
        }

//        Log.d(TAG, "optimizeNumber: after str:" + str);
        return str;
    }

    /**
     * 数字键盘数字被按下的处理
     * @param number 按下的数字
     */
    public void numberChanged(int number) {
        Log.d(TAG, "numberChanged: number:" + number);
        if (number >= 0 && number <= 9) { // 数字0~9
            if (isDefaultState) {
                if (number != 0) {
                    isDefaultState = false;
                    mBuilder.append(String.valueOf(number));
                    Log.d(TAG, "numberChanged: build:" + mBuilder.toString());
                    dealEditData(TYPE_INPUT_NUMBER);
                }
            } else {
                isDefaultState = false;
                mBuilder.append(String.valueOf(number));
                Log.d(TAG, "numberChanged: build:" + mBuilder.toString());
                dealEditData(TYPE_INPUT_NUMBER);
            }
        }
        if (number == 14) { // 删除键
            if (mBuilder.length() > 0) {
                mBuilder.deleteCharAt(mBuilder.length() - 1);
                Log.d(TAG, "numberChanged: mBuild:" + mBuilder.toString());
            }
            if (mBuilder.length() == 0) {
                isDefaultState = true;
            }
            dealEditData(TYPE_INPUT_NUMBER);
        }
    }

    /**
     * 长按删除键处理接口
     */
    public void longPressDeleteButton() {
        isDefaultState = true;
        mBuilder.delete(0, mBuilder.length());
        dealEditData(TYPE_INPUT_NUMBER);
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
        Log.d(TAG, "onClick: ........................");
        Log.d(TAG, "onClick: clickTag:" + v.getTag() + ",oldItemTag:" + mCurrentItem.getTag()
            + ", oldCusEditTag:" + mCurrentEdit.getTag() + ",text:" + mCurrentEdit.getText());
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
        mCurrentEdit.obtainFocus();
        mMoneyList.get(mCurrentItemPosition).setSelected(true);
        setPreFlag(mCurrentItemPosition);
        if (!isDefaultState) {
            // 清除数字存储器内容
            mBuilder.delete(0, mBuilder.length());
            mBuilder.append(mCurrentEdit.getText().toString());
            Log.d(TAG, "onClick: builder:" + mBuilder.toString());
        }
        dealEditData(TYPE_CHANGE_ITEM);
        Log.d(TAG, "onClick: curPos:" + mCurrentItemPosition + ",curItemTag:" + mCurrentItem.getTag()
            + ",curCusEditTag:" + mCurrentEdit.getId() + ",text:" + mCurrentEdit.getText().toString());
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
