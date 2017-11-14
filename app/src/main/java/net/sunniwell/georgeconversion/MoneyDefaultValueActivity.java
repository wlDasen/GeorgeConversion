package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.sunniwell.georgeconversion.recyclerview.DefaultValueDecoration;
import net.sunniwell.georgeconversion.util.ColorDBUtil;
import net.sunniwell.georgeconversion.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class MoneyDefaultValueActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "jpd-MDVAc";
    private List<MoneyBean> mList;
    private RecyclerView mRecycler;
    private MyAdaptor mMyAdapter;
    private LinearLayoutManager mManager;
    private Button mBackButton;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_default_value);

        initData();
        initView();
        registerListener();
    }

    private void initData() {
        mList = new ArrayList<>();
        String[] numberList = getResources().getStringArray(R.array.default_number_list);
        Log.d(TAG, "initData: numberlist:" + numberList);
        for (String i : numberList) {
            MoneyBean mb = new MoneyBean();
            mb.setValue(i);
            if (i.equals(SharedPreferenceUtil.getString(this, "default_money_number", ""))) {
                mb.setSelected(true);
            } else {
                mb.setSelected(false);
            }
            mList.add(mb);
        }
    }
    private void initView() {
        mToolBar = (Toolbar)findViewById(R.id.default_value_toolbar);
        mToolBar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        mRecycler = (RecyclerView)findViewById(R.id.setting_recycler_layout);
        mMyAdapter = new MyAdaptor();
        mManager = new LinearLayoutManager(this);
        mRecycler.setAdapter(mMyAdapter);
        mRecycler.setLayoutManager(mManager);
        mRecycler.addItemDecoration(new DefaultValueDecoration(this));
        mBackButton = (Button)findViewById(R.id.back_button);
    }
    private void registerListener() {
        mBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.ViewHolder> {
        private int curPos;
        private List<ImageView> imgList;

        public MyAdaptor() {
            imgList = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MoneyDefaultValueActivity.this).inflate(R.layout.default_value_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.text.setText(mList.get(position).getValue());
            if (mList.get(position).isSelected) {
                holder.image.setVisibility(View.VISIBLE);
                curPos = position;
            } else {
                holder.image.setVisibility(View.GONE);
            }
            imgList.add(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgList.get(curPos).setVisibility(View.GONE);
                    curPos = position;
                    imgList.get(position).setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.putExtra("select_value", mList.get(position).getValue());
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
