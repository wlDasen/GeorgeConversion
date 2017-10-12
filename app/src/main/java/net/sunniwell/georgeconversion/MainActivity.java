package net.sunniwell.georgeconversion;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        recyclerLayout = (RecyclerView)findViewById(R.id.recycler_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerLayout.setLayoutManager(manager);
        adapter = new CustomAdapter(countryList);
        recyclerLayout.setAdapter(adapter);
        recyclerLayout.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ItemTouchHelper.Callback callback = new DragItemHelperCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerLayout);
    }

    private void initData() {
        for(int i = 0; i < 4; i++) {
            Country country = new Country("中国", "CNY", "100", "人民币 ¥");
            countryList.add(country);
        }
    }

    public void onButtonClicked(View view) {
        Button btn = (Button)view;
        Log.d(TAG, "onButtonClicked: text:" + btn.getText());
    }

    /**
     * 返回按键处理接口
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            Log.d(TAG, "onBackPressed: open.");
            drawerLayout.closeDrawers();
        } else {
            Log.d(TAG, "onBackPressed: not open.");
            super.onBackPressed();
        }
    }
}
