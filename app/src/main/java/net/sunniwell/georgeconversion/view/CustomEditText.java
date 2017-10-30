package net.sunniwell.georgeconversion.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import net.sunniwell.georgeconversion.MainApplication;

/**
 * Created by admin on 2017/10/25.
 */

public class CustomEditText extends EditText implements View.OnFocusChangeListener, View.OnTouchListener{
    private static final String TAG = "jpd-CET";
    private OnEditTouchListener listener;

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Log.d(TAG, "CustomEditText: 3");
        setOnFocusChangeListener(this);
        setOnTouchListener(this);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        Log.d(TAG, "CustomEditText: 2");
    }

    public CustomEditText(Context context) {
        this(context, null);
//        Log.d(TAG, "CustomEditText: 1");
    }

    public void obtainFocus() {
//        Log.d(TAG, "obtainFocus: ");
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        moveCursorToEnd();
    }

    public void loseFocus() {
//        Log.d(TAG, "loseFocus: ");
        clearFocus();
    }

    public void moveCursorToEnd() {
//        Log.d(TAG, "moveCursorToEnd: ");
        setSelection(getText().length());
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        Log.d(TAG, "onFocusChange: text:" + hasFocus);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch: ");
        if (listener != null) {
            int tag = (int)getTag();
            listener.onEditTouch(this, tag);
        }
        return true;
    }

    public void setOnEditTouchListener(OnEditTouchListener listener) {
        this.listener = listener;
    }

    public interface OnEditTouchListener {
        void onEditTouch(CustomEditText cet, int position);
    }
}
