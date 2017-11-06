package net.sunniwell.georgeconversion;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sunniwell.georgeconversion.db.Money;
import net.sunniwell.georgeconversion.recyclerview.CustomItemDecoration;
import net.sunniwell.georgeconversion.recyclerview.SectorItemDecoration;
import net.sunniwell.georgeconversion.recyclerview.SortAdapter;
import net.sunniwell.georgeconversion.util.MoneyDBUtil;
import net.sunniwell.georgeconversion.util.PinyinComparator;
import net.sunniwell.georgeconversion.util.PinyinUtils;
import net.sunniwell.georgeconversion.view.ClearEditText;
import net.sunniwell.georgeconversion.view.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectMoneyActivity extends AppCompatActivity {
    private static final String TAG = "jpd-SMActivity";
    private ClearEditText mEditText;
    private List<Money> mDataList;
    private List<Money> originalList;
    /**
     * RecyclerView实例
     */
    private RecyclerView mRecyclerView;
    /**
     * RecyclerView的布局管理器
     */
    private LinearLayoutManager mManager;
    private SortAdapter mAdapter;
    private SliderView.OnTouchingLetterChangedListener listener;
    private SliderView mSliderView;
    private TextView overlay;
    private Map<String, Integer> letterPMap = new HashMap<>();
    private PinyinComparator mComparator;
    private Button backBtn;
    /**
     * 点击数字索引时，RecyclerView滚动屏幕后面不可见的item到顶端时的标志位，供onScrolled监听接口使用
     * true－有屏幕后面的item需要滚动到屏幕最顶端
     */
    private boolean moveFlag;
    /**
     * 点击数字索引时，记录要滚动的位置，供onScroll监听接口使用
     */
    private int movePosition;
    /**
     * 分组标题栏的高度
     */
    private int headerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_money);

        //        pinyin4jTest();
        initData();
        initView();
        registerListener();
        initOverlay();
    }
    /**
     * 搜索数据
     * @param str 要搜索的数据
     */
    private void searchData(String str) {
        List<Money> filterData = new ArrayList<>();

        if (!TextUtils.isEmpty(str)) {
            Log.d(TAG, "searchData: not empty..");
            filterData.clear();
            Log.d(TAG, "searchData: oringin:" + originalList.size());
            for (Money data : originalList) {
                String pinyin = PinyinUtils.getPinyin(str);
                Log.d(TAG, "searchData: pinyin:" + pinyin + ",letters:" + data.getLetters()
                        + ",name:" + data.getName());
                if (data.getName().contains(str) || data.getLetters().contains(pinyin)) {
                    filterData.add(data);
                }
            }

            Collections.sort(filterData, mComparator);
            Log.d(TAG, "searchData: fLength:" + filterData.size());
//            printDataList(filterData);
            mDataList.clear();
            mDataList.addAll(filterData);
            Log.d(TAG, "searchData: mLength:" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 给数据中的字母对应到position
     */
    private void setLetterPosition() {
        for (int i = 0; i < mDataList.size(); i++) {
            if (i == 0) {
                String letter = mDataList.get(i).getFirstLetter();
                letterPMap.put(letter, i);
            } else {
                if (!mDataList.get(i).getFirstLetter().equals(mDataList.get(i - 1).getFirstLetter())) {
                    letterPMap.put(mDataList.get(i).getFirstLetter(), i);
                }
            }
        }
    }

    /**
     * 打印RecyclerView数据
     * @param dt 要打印的数据
     */
    private void printDataList(List<Money> dt) {
        for (int i = 0; i < dt.size(); i++) {
            Log.d(TAG, "printDataList: name:" + dt.get(i).getName() + ",letters:" + dt.get(i).getLetters()
                    + "firstLetter:" + dt.get(i).getFirstLetter());
        }
    }

    /**
     * RecyclerView的填充数据
     */
    private void initData() {
        initAdapterData();
        headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mComparator = new PinyinComparator();
        Collections.sort(mDataList, mComparator);
        printDataList(mDataList);
        originalList = new ArrayList<>();
        originalList.addAll(mDataList);
        setLetterPosition();
    }

    /**
     * 初始化RecyclerView的适配器数据
     */
    private void initAdapterData() {
        mDataList = MoneyDBUtil.getAllMoney();
        Log.d(TAG, "initAdapterData: ");
        for (int i = 0; i < mDataList.size(); i++) {
            Log.d(TAG, "initAdapterData: " + mDataList.get(i));
        }
    }

    private void initView() {
        Intent intent = getIntent();
        String selectMoney = intent.getStringExtra("money_name");
        mSliderView = (SliderView)findViewById(R.id.slider_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.rclv);
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new SortAdapter(this, mDataList, selectMoney);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SectorItemDecoration(this, mDataList));
        mRecyclerView.addItemDecoration(new CustomItemDecoration(this));
        mEditText = (ClearEditText)findViewById(R.id.cet);
        backBtn = (Button)findViewById(R.id.back);
    }

    /**
     * 初始化中央显示字母列表点击后的View
     */
    private void initOverlay() {
        Log.d(TAG, "initOverlay: ");
        overlay = (TextView) LayoutInflater.from(this).inflate(R.layout.overlay, null, false);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        wm.addView(overlay, lp);
    }

    /**
     * 注册字母索引列表点击的回调listener
     */
    private void registerListener() {
        SortAdapter.OnItemClickedListener itemListener = new SortAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick() {
                Log.d(TAG, "onItemClick: ");
                mAdapter.notifyChangeDataList();
                finish();
            }
        };
        mAdapter.setOnItemClickListener(itemListener);
        listener = new SliderView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.d(TAG, "onTouchingLetterChanged: letter:" + s);
                overlay.setText(s);
                Integer position = letterPMap.get(s);
                if (position != null) {
                    Log.d(TAG, "onTouchingLetterChanged: position:" + position);
                } else {
                    position = getClosestPosition(s);
                    Log.d(TAG, "onTouchingLetterChanged: closest po:" + position);
                }
                moveToPosition(position.intValue());
                overlay.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        overlay.setVisibility(View.INVISIBLE);
                    }
                }, 1000);
            }
        };
        mSliderView.setOnTouchingLetterChangedListener(listener);
        // 设置RecyclerView的滚动监听接口
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (moveFlag) {
                    moveFlag = false;
                    int n = movePosition - mManager.findFirstVisibleItemPosition();
                    if (n >= 0 && n < mRecyclerView.getChildCount()) {
                        int top = mRecyclerView.getChildAt(n).getTop();
                        mRecyclerView.scrollBy(0, top - headerHeight);
                    }
                }

            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: s:" + charSequence.toString());
                searchData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: ");
                if (TextUtils.isEmpty(editable.toString())) {
                    mDataList.clear();
                    mDataList.addAll(originalList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click back button.");
                finish();
            }
        });
    }

    /**
     * 字母索引点击后的滚动位置处理接口
     * @param position item在RecyclerView中的位置
     */
    private void moveToPosition(int position) {
        // 获取屏幕第一个可见Item的位置
        int firstVisibleItem = mManager.findFirstVisibleItemPosition();
        // 获取屏幕最后一个可见Item的位置
        int lastVisibleItem = mManager.findLastVisibleItemPosition();
        if (position <= firstVisibleItem) { // 需要滚动位置在第一个可见位置之前
            mRecyclerView.scrollToPosition(position);
        } else if (position <= lastVisibleItem) { // 需要滚动位置在第一个和最后一个可见位置之间
            int top = mRecyclerView.getChildAt(position - firstVisibleItem).getTop();
            mRecyclerView.scrollBy(0, top - headerHeight);
        } else { // 需要滚动位置在最后一个可见位置之后
            moveFlag = true;
            movePosition = position;
            mRecyclerView.scrollToPosition(position);
        }
    }

    /**
     * 字母索引找不到对应条目时算法设计接口
     * @param letter 要查找的字母索引
     * @return 根据算法返回距离字母索引最近的字母
     */
    private Integer getClosestPosition(String letter) {
        String[] letters = {"#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"
                , "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        int position = -1;
        int returnPo = -1;
        int leftPosition = -1;
        int rightPosition = -1;

        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == letter) {
                position = i;
            }
        }

        for (int i = position - 1; i >= 0; i--) {
            if (letterPMap.containsKey(letters[i])) {
                Log.d(TAG, "getClosestPosition: left:" + letters[i]);
                leftPosition = i;
                break;
            }
        }
        for (int i = position + 1; i < letters.length; i++) {
            if (letterPMap.containsKey(letters[i])) {
                Log.d(TAG, "getClosestPosition: right:" + letters[i]);
                rightPosition = i;
                break;
            }
        }

        returnPo = (leftPosition == -1 ? rightPosition : (rightPosition == -1 ? leftPosition :
                (Math.abs(leftPosition - position) > Math.abs(rightPosition - position) ? rightPosition : leftPosition)));

        return letterPMap.get(letters[returnPo]);
    }

    /**
     * 测试pinyin4j接口
     */
    private void pinyin4jTest() {
        String[] pyStr = PinyinHelper.toHanyuPinyinStringArray('重');
        for (String s : pyStr) {
            Log.d(TAG, "pinyin4jTest: s:" + s);
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            String[] hao = PinyinHelper.toHanyuPinyinStringArray('女', format);
            for (String str : hao) {
                Log.d(TAG, "pinyin4jTest: str:" + str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
