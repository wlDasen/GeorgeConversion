package net.sunniwell.georgeconversion.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import net.sunniwell.georgeconversion.R;

/**
 * Created by admin on 2017/10/24.
 */

public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = "jpd-ClearEditText";
    private Drawable mDrawable;

    public ClearEditText(Context context) {
        super(context);
//        Log.d(TAG, "ClearEditText: 1");
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
//        Log.d(TAG, "ClearEditText: 2");
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Log.d(TAG, "ClearEditText: 3");
        init();
    }

    private void init() {
//        Log.d(TAG, "init: ");
        mDrawable = getCompoundDrawables()[2];
        if (mDrawable == null) {
            mDrawable = getResources().getDrawable(R.drawable.emotionstore_progresscancelbtn);
        }
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
//        Log.d(TAG, "init: width:" + mDrawable.getIntrinsicWidth()
//                + ",height:" + mDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchClear = (event.getX() > getWidth() - getPaddingRight() - mDrawable.getIntrinsicWidth())
                    && (event.getX() < getWidth() - getPaddingRight());
            if (touchClear) {
                Log.d(TAG, "onTouchEvent: clear text.");
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
//        Log.d(TAG, "setClearIconVisible: visible:" + visible);
        Drawable right = visible ? mDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right
                , getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        Log.d(TAG, "onTextChanged: s:" + charSequence.toString());
        setClearIconVisible(getText().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {
//        Log.d(TAG, "afterTextChanged: ");
    }

    @Override
    public void onFocusChange(View view, boolean b) {
//        Log.d(TAG, "onFocusChange: ");
    }
}