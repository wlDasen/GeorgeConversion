package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Button;
import android.widget.Toast;

import net.sunniwell.georgeconversion.db.Country;
import net.sunniwell.georgeconversion.interfaces.ItemSwipeListener;
import net.sunniwell.georgeconversion.recyclerview.CustomAdapter;
import net.sunniwell.georgeconversion.recyclerview.DividerItemDecoration;
import net.sunniwell.georgeconversion.recyclerview.DragItemHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jpd-MainActivity";
    private NavigationView navigationView;
    private Button navBtn;
    private DrawerLayout drawerLayout;
    private Button refreshBtn;
    private RecyclerView recyclerLayout;
    private List<Country> countryList = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        initData();
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
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        };
        recyclerLayout = (RecyclerView)findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerLayout.setLayoutManager(manager);
        adapter = new CustomAdapter(countryList);
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

    private void initData() {
        Country country1 = new Country("中国1", "CNY1", "101", "人民币1 ¥", false);
        Country country2 = new Country("中国2", "CNY2", "102", "人民币2 ¥", false);
        Country country3 = new Country("中国3", "CNY3", "103", "人民币3 ¥", false);
        Country country4 = new Country("中国4", "CNY4", "104", "人民币4 ¥", false);
        countryList.add(country1);
        countryList.add(country2);
        countryList.add(country3);
        countryList.add(country4);

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
        Log.d(TAG, "onButtonClicked: text:" + btn.getText());
    }
}
