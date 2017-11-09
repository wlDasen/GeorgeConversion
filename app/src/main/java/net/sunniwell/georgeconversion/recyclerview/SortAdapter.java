package net.sunniwell.georgeconversion.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.util.MoneyDBUtil;
import net.sunniwell.georgeconversion.util.SharedPreferenceUtil;

import java.util.List;

/**
 * Created by admin on 2017/10/24.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private static final String TAG = "jpd-SortAdapter";
    private List<Money> mSortData;
    private String selectMoney;
    private int mSwipePosition;
    private Context mContext;
    private OnItemClickedListener listener;

    public SortAdapter(Context context, List<Money> list, String selectMoney, int position) {
        mContext = context;
        mSortData = list;
        this.selectMoney = selectMoney;
        mSwipePosition = position;
        Log.d(TAG, "SortAdapter: size:" + mSortData.size());
        Log.d(TAG, "SortAdapter: selectMoney:" + selectMoney);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Money data = mSortData.get(position);
        String moneyCode = data.getName();
        holder.mText.setText(moneyCode);
        Log.d(TAG, "onBindViewHolder: position:" + position);
        Log.d(TAG, "onBindViewHolder: data:" + data);
        if (data.isMain4Money()) {
            holder.mImage.setVisibility(View.VISIBLE);
            if (selectMoney.equals(data.getName())) {
                holder.mImage.setBackgroundResource(R.drawable.checkmark_selected);
                holder.mText.setTextColor(Color.BLUE);
            } else {
                holder.mImage.setBackgroundResource(R.drawable.checkmark_normal);
                holder.mText.setTextColor(Color.BLACK);
            }
        } else {
            holder.mImage.setVisibility(View.GONE);
            holder.mText.setTextColor(Color.BLACK);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: position:" + position + ",name:" + mSortData.get(position).getName());
                if (mSortData.get(position).isMain4Money()) {
                    Toast.makeText(mContext, "重复的货币选择，请重新选择其他货币", Toast.LENGTH_LONG).show();
                } else {
                    MoneyDBUtil.setMain4Money(selectMoney, mSortData.get(position).getName(), mSwipePosition);F
                    SharedPreferenceUtil.setString(mContext, String.valueOf(mSwipePosition), mSortData.get(position).getCode());
                    if (listener != null) {
                        listener.onItemClick();
                    }
                }
            }
        });
    }

    public void  notifyChangeDataList() {
        mSortData.clear();
        mSortData.addAll(MoneyDBUtil.getAllMoney());
    }

    public void setOnItemClickListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickedListener {
        public void onItemClick();
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
