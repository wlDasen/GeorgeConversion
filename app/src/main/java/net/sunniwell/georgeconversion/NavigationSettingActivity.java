package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.sunniwell.georgeconversion.db.NaviSettingItem;
import net.sunniwell.georgeconversion.interfaces.OnSettingItemClickListener;
import net.sunniwell.georgeconversion.recyclerview.NavigationItemDecoration;
import net.sunniwell.georgeconversion.recyclerview.NavigationItemHeader;
import net.sunniwell.georgeconversion.recyclerview.NavigationSettingAdaptor;
import net.sunniwell.georgeconversion.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Navigation 设置Item的Activity
 */
public class NavigationSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "jpd-NaviSActi";
    private RecyclerView mRecyclerView;
    private NavigationSettingAdaptor mAdaptor;
    private LinearLayoutManager mManager;
    private Button mBackButton;
    private List<NaviSettingItem> mItemList;
    private OnSettingItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_setting);

        initData();
        initView();
        registerListener();
    }

    private void initData() {
        Log.d(TAG, "initData: ");
        mItemList = new ArrayList<>();
        String[] generalTitle = getResources().getStringArray(R.array.navi_setting_general_item);
        String[] generalValue = getResources().getStringArray(R.array.navi_setting_general_item_defaultval);
        for (int i = 0; i < generalTitle.length; i++) {
            NaviSettingItem nav = new NaviSettingItem();
            if (generalTitle[i] == null) {
                Log.d(TAG, "initData: item null..");
            }
            if ("".equals(generalValue[i])) {
                Log.d(TAG, "initData: item 空");
            }
            nav.setGroup(getResources().getString(R.string.navi_setting_general));
            nav.setItemTitle(generalTitle[i]);
            nav.setItemValue(generalValue[i]);
            mItemList.add(nav);
        }
        String[] advancedTitle = getResources().getStringArray(R.array.navi_setting_advanced_item);
        String[] advancedValue = getResources().getStringArray(R.array.navi_setting_advanced_item_defaultval);
        for (int i = 0; i < advancedTitle.length; i++) {
            NaviSettingItem nav = new NaviSettingItem();
            if (advancedTitle[i] == null) {
                Log.d(TAG, "initData: item null..");
            }
            if ("".equals(advancedValue[i])) {
                Log.d(TAG, "initData: item 空");
            }
            nav.setGroup(getResources().getString(R.string.navi_setting_advanced));
            nav.setItemTitle(advancedTitle[i]);
            nav.setItemValue(advancedValue[i]);
            mItemList.add(nav);
        }
        String[] otherTitle = getResources().getStringArray(R.array.navi_setting_other_item);
        String[] otherValue = getResources().getStringArray(R.array.navi_setting_other_item_defaultval);
        for (int i = 0; i < otherTitle.length; i++) {
            NaviSettingItem nav = new NaviSettingItem();
            if (otherTitle[i] == null) {
                Log.d(TAG, "initData: item null..");
            }
            if ("".equals(otherValue[i])) {
                Log.d(TAG, "initData: item 空");
            }
            nav.setGroup(getResources().getString(R.string.navi_setting_other));
            nav.setItemTitle(otherTitle[i]);
            nav.setItemValue(otherValue[i]);
            mItemList.add(nav);
        }
        printList(mItemList);
    }
    private void initView() {
        mBackButton = (Button)findViewById(R.id.back_button);
        mRecyclerView = (RecyclerView)findViewById(R.id.setting_recycler_layout);
        mAdaptor = new NavigationSettingAdaptor(this, mItemList);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdaptor);
        mRecyclerView.addItemDecoration(new NavigationItemHeader(this, mItemList));
        mRecyclerView.addItemDecoration(new NavigationItemDecoration(this));
    }
    private void registerListener() {
        mBackButton.setOnClickListener(this);
        listener = new OnSettingItemClickListener() {
            @Override
            public void onSettingItemClick() {
                Intent intent = new Intent(NavigationSettingActivity.this, MoneyDefaultValueActivity.class);
                startActivityForResult(intent, 1);
            }
        };
        mAdaptor.setOnSettingItemClick(listener);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    String defaultValue = data.getStringExtra("select_value");
                    SharedPreferenceUtil.setString(this, "default_money_number", defaultValue);
                    mAdaptor.notifyDataSetChanged();
                }
                break;
        }
    }

    private void printList(List<NaviSettingItem> list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "printList: " + list.get(i));
        }
    }
}
