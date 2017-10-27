package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.interfaces.ItemSwipeListener;
import net.sunniwell.georgeconversion.recyclerview.CustomAdapter;
import net.sunniwell.georgeconversion.recyclerview.DragItemHelperCallback;
import net.sunniwell.georgeconversion.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "jpd-MainActivity";
    private NavigationView mNavigationView;
    private Button mNavBtn;
    private DrawerLayout mDrawerLayout;
    private Button mRefreshBtn;
    private RecyclerView mRecyclerLayout;
    private List<Money> mMoneyList = new ArrayList<>();
    private CustomAdapter mAdapter;
    private ItemSwipeListener mListener;
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
    /**
     * item右滑状态
     */
    private boolean isSwiped = false;
    /**
     * 右滑item位置
     */
    private int swipePostion = -1;

    private static final String JUHE_APP_KEY = "225642569f50a0dbceacd72a94ef3519";
    private static final String JUHE_REAL_MONEY_RAT_URL = "http://op.juhe.cn/onebox/exchange/currency?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if (!getConfigureFlag()) {
            setMoneyList();
        }

        mMoneyList = getMain4Money();
        mAdapter.notifyDataSetChanged();

        refreshMoneyRate();

        mListener = new ItemSwipeListener() {
            @Override
            public void onItemSwipe(int position) {
                Log.d(TAG, "onItemSwipe: ");
                isSwiped = true;
                swipePostion = position;
                Intent intent = new Intent(MainActivity.this, SelectMoneyActivity.class);
                intent.putExtra("countryName", mMoneyList.get(position).getName());
                Log.d(TAG, "onItemSwipe: pos:" + position + "cn:" + mMoneyList.get(position).getName());
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (isSwiped) {
            Log.d(TAG, "onResume: isSwiped.");
            adapter.notifyItemChanged(swipePostion);
            isSwiped = false;
        }
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
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.toolbar_refresh:
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取主界面4种用户选择货币的list信息
     */
    private List<Money> getMain4Money() {
        List<Money> list = DataSupport.where("isMain4Money == ?", "1").find(Money.class);
        Log.d(TAG, "getMain4Money: query:" + mMoneyList.size());
        for (int i = 0; i < mMoneyList.size(); i++) {
            Money money = mMoneyList.get(i);
            Log.d(TAG, "getMain4Money: " + money);
        }
        return list;
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
        List<Money> list = getMain4Money();
        try {
            for (int i = 0; i < list.size(); i++) {
                Money money = list.get(i);
                if (!"CNY".equals(money.getCode())) {
                    Response response = HttpUtil.sendPostByOkHttp(JUHE_REAL_MONEY_RAT_URL, JUHE_APP_KEY,
                            "CNY", money.getCode());
                    if (response.isSuccessful()) {
                        Double[] data = parseRealRateJSON(response.body().string());
                        Log.d(TAG, "refreshMoneyRate: d1:" + data[0] + ",d2:" + data[1]);
                        money.setBase1CNYToCurrent(data[0]);
                        money.setBase1CurrentToCNY(data[1]);
                        money.save();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        printList(list);
        mMoneyList.clear();
        mMoneyList.addAll(list);
        mAdapter.notifyDataSetChanged();
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
        // 初始化Litepal数据库
        SQLiteDatabase db = Connector.getDatabase();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_layout);
        mNavigationView.setItemIconTintList(null);
        mNavBtn = (Button)findViewById(R.id.toolbar_nav);
        mRefreshBtn = (Button)findViewById(R.id.toolbar_refresh);
        mNavBtn.setOnClickListener(this);
        mRefreshBtn.setOnClickListener(this);
        mRecyclerLayout = (RecyclerView)findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerLayout.setLayoutManager(manager);
        mAdapter = new CustomAdapter(mMoneyList);
        mRecyclerLayout.setAdapter(mAdapter);
//        recyclerLayout.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ItemTouchHelper.Callback callback = new DragItemHelperCallback(mListener);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerLayout);
    }

    /**
     * 获取默认数据配置标志位
     * @return true-已经配置过默认数据库 false-没有配置过默认数据库
     */
    private boolean getConfigureFlag() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConfigured = prefs.getBoolean("isConfigured", false);
        Log.d(TAG, "onCreate: isConfigured:" + isConfigured);
        return  isConfigured;
    }

    /**
     * 设置默认货币的各种信息
     */
    private void setMoneyList() {
        Log.d(TAG, "setMoneyList: ");

        String[] nameArrays = getResources().getStringArray(R.array.name);
        String[] codeArrays = getResources().getStringArray(R.array.code);
        int[] isMain4MoneyArrays = getResources().getIntArray(R.array.isMain4Money);
        String[] base1CNYToCurrentArrays = getResources().getStringArray(R.array.base1CNYToCurrent);
        String[] base1CurrentToCNYArrays = getResources().getStringArray(R.array.base1CurrentToCNY);

        for (int i = 0; i < nameArrays.length; i++) {
            boolean isMain4Money = isMain4MoneyArrays[i] == 1 ? true : false;
            double base1CNYToCurrent = Double.parseDouble(base1CNYToCurrentArrays[i]);
            double base1CurrentToCNY = Double.parseDouble(base1CurrentToCNYArrays[i]);
            Money money = new Money(nameArrays[i], codeArrays[i], isMain4Money, base1CNYToCurrent, base1CurrentToCNY);
            money.save();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isConfigured", true);
        editor.apply();
    }

    private void requestDataFromJuHe() {
        callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "网络异常，获取最新汇率失败，建议调整网络重新点击右上角的刷新按钮重新获获取。",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                parseDataFromJuhe(response.body().string());
            }
        };
        HttpUtil.sendRequest(juheMoneyListUrl, callback);
    }
    
    private void parseDataFromJuhe(String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            for (int i = 0; i < jsonObj.length(); i++) {
                String reason = jsonObj.getString("reason");
                int errorcode = jsonObj.getInt("error_code");
                JSONObject result = jsonObj.getJSONObject("result");
//                Log.d(TAG, "requestDataFromJuHe: reason:" + reason + ",errorcode:" + errorcode
//                        + ",len:" + result.length());
                JSONArray list = result.getJSONArray("list");
//                Log.d(TAG, "requestDataFromJuHe: len:" + list.length());
                for (int j = 0; j <list.length(); j++) {
                    JSONObject listObj = (JSONObject)list.get(j);
                    String name = listObj.getString("name");
                    String code = listObj.getString("code");
//                    Log.d(TAG, "requestDataFromJuHe: name:" + name + ",code:" + code);
                    Money money = new Money();
                    money.setName(listObj.getString("name"));
                    money.setCode(listObj.getString("code"));
                    money.save();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                Log.d(TAG, "onBackPressed: open.");
                drawerLayout.closeDrawers();
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
        Log.d(TAG, "onButtonClicked: text:" + btn.getText() + ",tag:" + view.getTag());
    }
}
