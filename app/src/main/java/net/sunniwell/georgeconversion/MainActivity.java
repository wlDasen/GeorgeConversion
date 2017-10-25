package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import net.sunniwell.georgeconversion.db.DefaultMoney;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jpd-MainActivity";
    private NavigationView navigationView;
    private Button navBtn;
    private DrawerLayout drawerLayout;
    private Button refreshBtn;
    private RecyclerView recyclerLayout;
    private List<DefaultMoney> mDefMoneyList = new ArrayList<>();
    private CustomAdapter adapter;
    private ItemSwipeListener listener;
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

    /**
     * okhttp请求的返回callback
     */
    private Callback callback;
    private static final String juheAPPKey = "225642569f50a0dbceacd72a94ef3519";
    private static final String juheMoneyListUrl = "http://op.juhe.cn/onebox/exchange/list?key=" + juheAPPKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = Connector.getDatabase();
        requestDataFromJuHe();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isConfigured = prefs.getBoolean("isConfigured", false);
        Log.d(TAG, "onCreate: isConfigured:" + isConfigured);
        if (!isConfigured) {
            setDefalutMoneyList();
        }
        mDefMoneyList = DataSupport.findAll(DefaultMoney.class);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_layout);
        navigationView.setItemIconTintList(null);
        navBtn = (Button)findViewById(R.id.toolbar_nav);
        refreshBtn = (Button)findViewById(R.id.toolbar_refresh);
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            }
        });
        listener = new ItemSwipeListener() {
            @Override
            public void onItemSwipe(int position) {
                Log.d(TAG, "onItemSwipe: ");
                isSwiped = true;
                swipePostion = position;
                Intent intent = new Intent(MainActivity.this, SelectMoneyActivity.class);
                intent.putExtra("countryName", mDefMoneyList.get(position).getName());
                Log.d(TAG, "onItemSwipe: pos:" + position + "cn:" + mDefMoneyList.get(position).getName());
                startActivity(intent);
            }
        };
        recyclerLayout = (RecyclerView)findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerLayout.setLayoutManager(manager);
        adapter = new CustomAdapter(mDefMoneyList);
        recyclerLayout.setAdapter(adapter);
//        recyclerLayout.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ItemTouchHelper.Callback callback = new DragItemHelperCallback(listener);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerLayout);
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

    private void setDefalutMoneyList() {
        Log.d(TAG, "setDefalutMoneyList: ");
        DefaultMoney china = new DefaultMoney("人民币", "CNY", false, 100);
        DefaultMoney hongkong = new DefaultMoney("港币", "HKD", false, 85.13);
        DefaultMoney america = new DefaultMoney("美元", "USD", false, 15.09);
        DefaultMoney europe = new DefaultMoney("欧元", "EUR", false, 12.82);
        Log.d(TAG, "setDefalutMoneyList: 1");
        china.save();
        hongkong.save();
        america.save();
        europe.save();
        Log.d(TAG, "setDefalutMoneyList: 2");
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
