package net.sunniwell.georgeconversion;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sunniwell.georgeconversion.db.ColorBean;
import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.interfaces.ItemSwipeListener;
import net.sunniwell.georgeconversion.recyclerview.CustomAdapter;
import net.sunniwell.georgeconversion.recyclerview.DragItemHelperCallback;
import net.sunniwell.georgeconversion.util.ColorDBUtil;
import net.sunniwell.georgeconversion.util.HttpUtil;
import net.sunniwell.georgeconversion.util.MoneyDBUtil;
import net.sunniwell.georgeconversion.util.SharedPreferenceUtil;
import net.sunniwell.georgeconversion.util.SortFieldComparator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
    , NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = "jpd-MainActivity";
    private NavigationView mNavigationView;
    private Button mNavBtn;
    private DrawerLayout mDrawerLayout;
    private View mHeaderView;
    private Button mRefreshBtn;
    private RecyclerView mRecyclerLayout;
    private List<Money> mMoneyList = new ArrayList<>();
    private CustomAdapter mAdapter;
    private ItemSwipeListener mListener;
    private Button mDeleteBtn;
    private static final int requestDefaultMoney = 1;
    private static final String DEFAULT_MONEY_NUMBER = "default_money_number";
    /**
     * Money sortField字段排序的Comparator
     */
    private SortFieldComparator mComparator;
    /**
     * 主程序退出标记
     */
    private boolean isExit = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                Intent intent = new Intent(MainActivity.this, NavigationSettingActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Log.d(TAG, "onActivityResult: ...");
            mToolBar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
            mHeaderView.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        }
    }

    /**
     * item右滑状态
     */
    private boolean isSwiped = false;

    private static final String JUHE_APP_KEY = "225642569f50a0dbceacd72a94ef3519";
    private static final String JUHE_REAL_MONEY_RAT_URL = "http://op.juhe.cn/onebox/exchange/currency?";
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        initData();
        initView();
        registerListener();
        init();

        showMainpageData();
        refreshMoneyRate();

    }
    private void initData() {
        // 初始化Litepal数据库
        SQLiteDatabase db = Connector.getDatabase();
        if ("".equals(SharedPreferenceUtil.getString(this, DEFAULT_MONEY_NUMBER, ""))) {
            SharedPreferenceUtil.setString(this, DEFAULT_MONEY_NUMBER, "100");
        }
        if ("".equals(SharedPreferenceUtil.getString(this, "default_color", ""))) {
            SharedPreferenceUtil.setString(this, "default_color", "#3F51B5");
            SharedPreferenceUtil.setInt(this, "default_color_position", 6);
        }
        if (!SharedPreferenceUtil.getBoolean(this, "isConfigured", false)) {
            MoneyDBUtil.setDefaultMoneyList(this);
            SharedPreferenceUtil.setBoolean(this, "isConfigured", true);
        }
        // 判断初始主界面4个位置的pref
        if ("".equals(SharedPreferenceUtil.getString(this, "firstPos", ""))) {
            // 设置初始主界面3个位置的pref和对应的moneyCode
            setPositionRecordPrefs();
        }
        if (!SharedPreferenceUtil.getBoolean(this, "isColorConfigured", false)) {
            ColorDBUtil.setDefaultColorList(this);
            SharedPreferenceUtil.setBoolean(this, "isColorConfigured", true);
        }
    }
    private void initView() {

    }
    private void registerListener() {

    }

    /**
     * 设置初始主界面3个位置的pref和对应的moneyCode
     * "0"-"CNY"
     * "1"-"USD"
     * "2"-"EUR"
     * "3"-"HKD"
     */
    private void setPositionRecordPrefs() {
        SharedPreferenceUtil.setString(this, "0", "CNY");
        SharedPreferenceUtil.setString(this, "1", "USD");
        SharedPreferenceUtil.setString(this, "2", "EUR");
        SharedPreferenceUtil.setString(this, "3", "HKD");
    }

    /**
     * 获取制定moneyCode在Prefs中对应的位置
     * @param moneyCode 要获取的moneyCode
     * @return moneyCode在prefs中对应的位置
     */
//    private int getPositionInPrefs(String moneyCode) {
//        String position = null;
//        Map map = SharedPreferenceUtil.getInstance(this).getAll();
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            if (map.get(key).equals(moneyCode)) {
//                position = key;
//            }
//        }
//        return Integer.parseInt(position);
//    }

    /**
     * 设置Adapter数据，刷新RecyclerView
     */
    private void showMainpageData() {
        // 获取默认顺序的四种主要货币
        List<Money> list = MoneyDBUtil.getMain4Money();
        // 按照sortField字段对四种默认货币排序
        Collections.sort(list, mComparator);
        // 清空Adaptor数据
        mMoneyList.clear();
        // 为Adaptor添加数据
        mMoneyList.addAll(list);
        // 刷新RecyclerView的数据
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMoneyRate();
        Log.d(TAG, "onResume: ");
        showMainpageData();
//        if (isSwiped) {
//            Log.d(TAG, "onResume: isSwiped.");
//            mAdapter.notifyDataSetChanged();
//            isSwiped = false;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:... ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_nav:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.toolbar_refresh:
                refreshMoneyRate();
                refreshAnim();
                break;
            default:
                break;
        }
    }

    /**
     * 打印list数据，仅用于测试
     * @param list 要打印的list
     */
    private void printList(List<Money> list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "printList: " + list.get(i));
        }
    }

    /**
     * 刷新主界面货币数据并保存数据库
     */
    private void refreshMoneyRate() {
        Log.d(TAG, "refreshMoneyRate: ");
       new Thread(new Runnable() {
           @Override
           public void run() {
               List<Money> list = MoneyDBUtil.getMain4Money();
               try {
                   for (int i = 0; i < list.size(); i++) {
                       Money money = list.get(i);
                       if (!"CNY".equals(money.getCode())) {
                           Response response = HttpUtil.sendPostByOkHttp(JUHE_REAL_MONEY_RAT_URL, JUHE_APP_KEY,
                                   "CNY", money.getCode());
//                           Log.d(TAG, "refreshMoneyRate: response:" + response.body().string());
                           if (response.isSuccessful()) {
                               Log.d(TAG, "run: response:" + response.body().string());
                               Double[] data = parseRealRateJSON(response.body().string());
                               Log.d(TAG, "refreshMoneyRate: d1:" + data[0] + ",d2:" + data[1]);
                               money.setBase1CNYToCurrent(data[0]);
                               money.setBase1CurrentToCNY(data[1]);
                               money.save();
                               Log.d(TAG, "run: " + money);
                           }
                       }
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }

               mMoneyList.clear();
               Collections.sort(list, mComparator);
               mMoneyList.addAll(list);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mAdapter.notifyDataSetChanged();
                       mAdapter.needToRefresh = true;
                   }
               });
           }
       }).start();
    }

    /**
     * 刷新旋转动画-1秒钟旋转360度
     */
    private void refreshAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRefreshBtn, "rotation", 0f, 360f);
        animator.setDuration(1000);
        animator.start();
    }

    private Double[] parseRealRateJSON(String jsonData) {
        Double[] resultStr = new Double[2];
        try {
            JSONObject rateObj = new JSONObject(jsonData);
            int errCode = rateObj.getInt("error_code");
            Log.d(TAG, "parseRealRateJSON: errCode:" + errCode);
            if (errCode == 0) {
                JSONArray resultArr = rateObj.getJSONArray("result");
                JSONObject fromObj = resultArr.getJSONObject(0);
                JSONObject toObj = resultArr.getJSONObject(1);
                resultStr[0] = Double.parseDouble(fromObj.getString("exchange"));
                resultStr[1] = Double.parseDouble(toObj.getString("exchange"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    /**
     * 初始化数据库和各种控件
     */
    private void init() {
        Log.d(TAG, "init: ");
        mToolBar = (Toolbar)findViewById(R.id.tool_bar);
        mToolBar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        mComparator = new SortFieldComparator();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_layout);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
        mHeaderView = mNavigationView.getHeaderView(0);
        mHeaderView.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        mNavBtn = (Button)findViewById(R.id.toolbar_nav);
        mRefreshBtn = (Button)findViewById(R.id.toolbar_refresh);
        mNavBtn.setOnClickListener(this);
        mRefreshBtn.setOnClickListener(this);
        mDeleteBtn = (Button)findViewById(R.id.button_delete);
        mDeleteBtn.setOnLongClickListener(this);
        mRecyclerLayout = (RecyclerView)findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerLayout.setLayoutManager(manager);
        mAdapter = new CustomAdapter(this, mMoneyList);
        mRecyclerLayout.setAdapter(mAdapter);
//        recyclerLayout.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mListener = new ItemSwipeListener() {
            @Override
            public void onItemSwipe(int position) {
                isSwiped = true;
                Intent intent = new Intent(MainActivity.this, SelectMoneyActivity.class);
                intent.putExtra("money_name", mMoneyList.get(position).getName());
                intent.putExtra("position", position);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        };
        ItemTouchHelper.Callback callback = new DragItemHelperCallback(mListener);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                Log.d(TAG, "onBackPressed: open.");
                mDrawerLayout.closeDrawers();
                return false;
            } else {
                exit();
                return false;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if (!isExit) {
            Toast.makeText(this, "再按一次退出George汇率", Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 数字键盘的点击事件
     * @param view 点击的键盘按键
     */
    public void onButtonClicked(View view) {
        Button btn = (Button)view;
        Log.d(TAG, "onButtonClicked: text:" + btn.getText() + ",tag:" + btn.getTag());
        mAdapter.numberChanged(Integer.parseInt((String)btn.getTag()));
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "onLongClick: ");
        mAdapter.longPressDeleteButton();
        return true;
    }
}
