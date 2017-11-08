package net.sunniwell.georgeconversion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.recyclerview.DefaultValueDecoration;
import net.sunniwell.georgeconversion.recyclerview.NavigationItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MoneyDefaultValueActivity extends AppCompatActivity {
    private static final String TAG = "jpd-MDVAc";
    private List<MoneyBean> mList;
    private RecyclerView mRecycler;
    private MyAdaptor mMyAdapter;
    private LinearLayoutManager mManager;
    private static final String DEFAULT_MONEY_NUMBER = "default_money_number";
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_default_value);

        initData();
        initView();
    }

    private void initData() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String str = getDefaultMoney();
        if ("".equals(str)) {
            setDefaultMoney("100");
        }
        mList = new ArrayList<>();
        String[] numberList = getResources().getStringArray(R.array.default_number_list);
        Log.d(TAG, "initData: numberlist:" + numberList);
        for (String i : numberList) {
            MoneyBean mb = new MoneyBean();
            mb.setValue(i);
            if (i.equals(getDefaultMoney())) {
                mb.setSelected(true);
            } else {
                mb.setSelected(false);
            }
            mList.add(mb);
        }
    }
    private void initView() {
        mRecycler = (RecyclerView)findViewById(R.id.setting_recycler_layout);
        mMyAdapter = new MyAdaptor();
        mManager = new LinearLayoutManager(this);
        mRecycler.setAdapter(mMyAdapter);
        mRecycler.setLayoutManager(mManager);
        mRecycler.addItemDecoration(new DefaultValueDecoration(this));
    }
    private String getDefaultMoney() {
        return mPrefs.getString(DEFAULT_MONEY_NUMBER, "");
    }
    private void setDefaultMoney(String money) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(DEFAULT_MONEY_NUMBER, money);
        editor.apply();
    }

    public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MoneyDefaultValueActivity.this).inflate(R.layout.default_value_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.text.setText(mList.get(position).getValue());
            if (mList.get(position).isSelected) {
                holder.image.setVisibility(View.VISIBLE);
            } else {
                holder.image.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDefaultMoney(mList.get(position).getValue());
                    Intent intent = new Intent();
                    intent.putExtra("selecte_value", mList.get(position).getValue());
                    setResult(1, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text;
            public ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                text = (TextView)itemView.findViewById(R.id.text);
                image = (ImageView)itemView.findViewById(R.id.image);
            }
        }
    }
    public class MoneyBean {
        String value;
        boolean isSelected;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
