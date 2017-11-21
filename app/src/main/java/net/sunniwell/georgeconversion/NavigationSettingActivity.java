package net.sunniwell.georgeconversion;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.sunniwell.georgeconversion.db.ColorBean;
import net.sunniwell.georgeconversion.db.NaviSettingItem;
import net.sunniwell.georgeconversion.interfaces.OnSettingItemClickListener;
import net.sunniwell.georgeconversion.recyclerview.NavigationItemDecoration;
import net.sunniwell.georgeconversion.recyclerview.NavigationItemHeader;
import net.sunniwell.georgeconversion.recyclerview.NavigationSettingAdaptor;
import net.sunniwell.georgeconversion.util.ColorDBUtil;
import net.sunniwell.georgeconversion.util.MoneyDBUtil;
import net.sunniwell.georgeconversion.util.SharedPreferenceUtil;

import java.lang.reflect.Field;
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
    private PopupWindow mPopWindow;
    private int currentColor;
    private Button currentButton;
    private List<Button> colorButtonList;
    private Handler mHandler;
    private Toolbar mToolBar;
    private int windowWidth;
    private int windowHeight;
    private int popwindowHorizontalMargin = 50;
    private int popwindowVerticalMargin = 400;
    private static final String APP_KEY = "wx751e35f50733882f";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_setting);

        regToWx();
        initData();
        initView();
        registerListener();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_KEY, true);
        api.registerApp(APP_KEY);
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth = dm.widthPixels;
        windowHeight = dm.heightPixels;
        Log.d(TAG, "initData: width:" + dm.widthPixels + "h:" + dm.heightPixels);
        Log.d(TAG, "initData: ");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mPopWindow.dismiss();
            }
        };
        mItemList = new ArrayList<>();
        colorButtonList = new ArrayList<>();
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
        mToolBar = (Toolbar)findViewById(R.id.navi_tool_bar);
        mToolBar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        mBackButton = (Button)findViewById(R.id.back_button);
        mRecyclerView = (RecyclerView)findViewById(R.id.setting_recycler_layout);
        mAdaptor = new NavigationSettingAdaptor(this, mItemList);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdaptor);
        mRecyclerView.addItemDecoration(new NavigationItemHeader(this, mItemList));
        mRecyclerView.addItemDecoration(new NavigationItemDecoration(this));
//        mRecyclerView.setOnTouchListener(this);
    }
    private void registerListener() {
        mBackButton.setOnClickListener(this);
        listener = new OnSettingItemClickListener() {
            @Override
            public void onSettingItemClick(int position) {
                if (position == 0) { // 货币默认值选择界面
                    Intent intent = new Intent(NavigationSettingActivity.this, MoneyDefaultValueActivity.class);
                    startActivityForResult(intent, 1);
                } else if (position == 1) { // 颜色默认值选择界面
                    mAdaptor.disableItemClick = true;
                    colorButtonList.clear();
                    final WindowManager.LayoutParams dp = getWindow().getAttributes();
                    dp.alpha = 0.3f;
                    getWindow().setAttributes(dp);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    View contentView = LayoutInflater.from(NavigationSettingActivity.this).inflate(R.layout.popup_window_layout, null);
                    Log.d(TAG, "onSettingItemClick: w:" + contentView.getWidth() + ",h:" + contentView.getHeight());
                    mPopWindow = new PopupWindow(contentView, windowWidth - popwindowHorizontalMargin * 2,
                            windowHeight - popwindowVerticalMargin * 2);
                    Button button1 = (Button)contentView.findViewById(R.id.button1);
                    Button button2 = (Button) contentView.findViewById(R.id.button2);
                    Button button3 = (Button) contentView.findViewById(R.id.button3);
                    Button button4 = (Button) contentView.findViewById(R.id.button4);
                    Button button5 = (Button) contentView.findViewById(R.id.button5);
                    Button button6 = (Button) contentView.findViewById(R.id.button6);
                    button1.setOnClickListener(NavigationSettingActivity.this);
                    button2.setOnClickListener(NavigationSettingActivity.this);
                    button3.setOnClickListener(NavigationSettingActivity.this);
                    button4.setOnClickListener(NavigationSettingActivity.this);
                    button5.setOnClickListener(NavigationSettingActivity.this);
                    button6.setOnClickListener(NavigationSettingActivity.this);
                    colorButtonList.add(button1);
                    colorButtonList.add(button2);
                    colorButtonList.add(button3);
                    colorButtonList.add(button4);
                    colorButtonList.add(button5);
                    colorButtonList.add(button6);
                    Log.d(TAG, "onSettingItemClick: size:" + colorButtonList.size());
                    mPopWindow.setBackgroundDrawable(new PaintDrawable());
                    mPopWindow.setOutsideTouchable(true);
                    mPopWindow.setTouchable(true);
                    View rootView = LayoutInflater.from(NavigationSettingActivity.this).inflate(R.layout.activity_navigation_setting, null);
                    mPopWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
                    ColorBean colorBean = ColorDBUtil.getDefaultColor();
                    colorButtonList.get(colorBean.getPosition()).setBackgroundResource(colorBean.getSelectResourceId());
                    mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.d(TAG, "onDismiss: ");
                            dp.alpha = 1.0f;
                            getWindow().setAttributes(dp);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(200);
                                        mAdaptor.disableItemClick = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                } else if (position == 2) { // 换算小技巧界面
                    Log.d(TAG, "onSettingItemClick: enter exchange skill activity..");
                    Intent intent = new Intent(NavigationSettingActivity.this, ExchangeSkillActivity.class);
                    startActivity(intent);
                } else if (position == 3) { // 恢复默认币种列表
                    Log.d(TAG, "onSettingItemClick: enter restore money list...");
                    AlertDialog.Builder builder = new AlertDialog.Builder(NavigationSettingActivity.this);
                    builder.setTitle("恢复默认币种列表");
                    builder.setMessage("将恢复至首次启动时首页显示的默认货币列表");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("恢复默认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: .....");
                            MoneyDBUtil.setDefaultMoneyList(NavigationSettingActivity.this);
                            Toast.makeText(NavigationSettingActivity.this, "数据恢复成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // 通过反射设置内容文字颜色为蓝色，必须在show之后设置，否则会报错
                    Class<?> clazz = AlertDialog.class;
                    try {
                        Field mAlert = clazz.getDeclaredField("mAlert");
                        mAlert.setAccessible(true);
                        Object mAlertController = mAlert.get(dialog);
                        Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                        mMessage.setAccessible(true);
                        TextView mMessageView = (TextView)mMessage.get(mAlertController);
                        mMessageView.setTextColor(Color.BLUE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 设置取消按钮文字颜色为蓝色，必须在show之后设置，否则会报错
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                } else if (position == 4) { // 分享给朋友界面
                    Log.d(TAG, "onSettingItemClick: click share item...");
                    mAdaptor.disableItemClick = true;
                    final WindowManager.LayoutParams dp = getWindow().getAttributes();
                    dp.alpha = 0.3f;
                    getWindow().setAttributes(dp);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    View contentView = LayoutInflater.from(NavigationSettingActivity.this).inflate(R.layout.share_to_friends_pop_layout, null);
                    LinearLayout friendsLayout = (LinearLayout)contentView.findViewById(R.id.friends_layout);
                    LinearLayout shareLayout = (LinearLayout)contentView.findViewById(R.id.timeline_layout);
                    friendsLayout.setOnClickListener(NavigationSettingActivity.this);
                    shareLayout.setOnClickListener(NavigationSettingActivity.this);
                    Log.d(TAG, "onSettingItemClick: w:" + contentView.getWidth() + ",h:" + contentView.getHeight());
                    mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 300);
                    mPopWindow.setBackgroundDrawable(new PaintDrawable());
                    mPopWindow.setOutsideTouchable(true);
                    mPopWindow.setTouchable(true);
                    View rootView = LayoutInflater.from(NavigationSettingActivity.this).inflate(R.layout.activity_navigation_setting, null);
                    mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                    mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            Log.d(TAG, "onDismiss: ");
                            dp.alpha = 1.0f;
                            getWindow().setAttributes(dp);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(200);
                                        mAdaptor.disableItemClick = false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                }
            }
        };
        mAdaptor.setOnSettingItemClick(listener);
    }

    // TODO: 2017/11/21 待GeorgeConversion发布后需要替换webpgeUrl
    private void shareWebSession(int type) {
        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = "http://maven.sunniwell.net:8082/doc/test/1.html";
        webpage.webpageUrl = "http://zhushou.360.cn/detail/index/soft_id/5427?recrefer=SE_D_%E5%BE%AE%E4%BF%A1";

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "George汇率";
        msg.description = "跳转到APK下载网页";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: .....");
        if (v.getId() == R.id.back_button) { // 点击了ToolBar返回按钮
            finish();
        } else if (v.getId() == R.id.friends_layout ) {
            mPopWindow.dismiss();
            shareWebSession(SendMessageToWX.Req.WXSceneSession);
        } else if (v.getId() == R.id.timeline_layout) {
            mPopWindow.dismiss();
            shareWebSession(SendMessageToWX.Req.WXSceneTimeline);
        } else { // 点击了颜色选择界面的某种颜色
            ColorBean oldColorBean = ColorDBUtil.getDefaultColor();
            int oldPos = oldColorBean.getPosition();
            String tag = (String)v.getTag();
            int newPos = Integer.parseInt(tag);
            ColorBean newColorBean = ColorDBUtil.getColorBeanAtPosition(tag);
            Log.d(TAG, "onClick: tag:" + tag);
            if (oldPos != newPos) {
                colorButtonList.get(oldPos).setBackgroundResource(oldColorBean.getNormalResourceId());
                v.setBackgroundResource(newColorBean.getSelectResourceId());
                oldColorBean.setDefault(false);
                newColorBean.setDefault(true);
                oldColorBean.save();
                newColorBean.save();
                mToolBar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
            }
            mHandler.sendEmptyMessageDelayed(0, 300);
            ColorDBUtil.printColorBean();
        }
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

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ....");
        if (mPopWindow.isShowing()) {
            Log.d(TAG, "onBackPressed: close popwindow");
            mPopWindow.dismiss();
        } else {
            Log.d(TAG, "onBackPressed: finish...");
            finish();
        }
    }
}