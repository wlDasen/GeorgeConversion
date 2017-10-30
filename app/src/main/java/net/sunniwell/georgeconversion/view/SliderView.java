package net.sunniwell.georgeconversion.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import net.sunniwell.georgeconversion.R;

/**
 * Created by admin on 2017/10/24.
 */

public class SliderView extends View {
    private static final String TAG = "jpd-slider";
    String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"
            , "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mLetterPaint;
    private Rect mLetterBound;
    private int mLetterSize;
    private int mLetterColor;
    private int mLetterPaddingTop;
    private int mLetterPaddingBottom;
    private int choose = -1;
    private OnTouchingLetterChangedListener listener;

    public SliderView(Context context) {
        this(context, null);
//        Log.d(TAG, "SliderView: ");
    }

    public SliderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        Log.d(TAG, "SliderView: ");
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "SliderView: ");
        mLetterPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.letter_top_padding);
        mLetterPaddingBottom = context.getResources().getDimensionPixelSize(R.dimen.letter_bottom_padding);
//        Log.d(TAG, "SliderView: TP:" + mLetterPaddingTop + ",BP:" + mLetterPaddingBottom);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SliderView);
        mLetterSize = ta.getDimensionPixelSize(R.styleable.SliderView_letterSize, 25);
        mLetterColor = ta.getColor(R.styleable.SliderView_letterColor, Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d(TAG, "onDraw: w:" + canvas.getWidth() + ",h:" + canvas.getHeight());
        int itemHeight = (getHeight() - mLetterPaddingBottom - mLetterPaddingTop) / letters.length;
        for (int i = 0; i < letters.length; i++) {
            mLetterPaint = new Paint();
            mLetterPaint.setTextSize(mLetterSize);
            mLetterPaint.setColor(mLetterColor);
            if (choose == i) {
                mLetterPaint.setColor(Color.parseColor("#ff0000"));
            }
            mLetterPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mLetterPaint.setAntiAlias(true);
            mLetterBound = new Rect();
            mLetterPaint.getTextBounds(letters[i], 0, letters[i].length(), mLetterBound);
            int startX = getWidth() / 2 - mLetterBound.width() / 2;
            int startY = mLetterPaddingTop + itemHeight * i + itemHeight / 2 + mLetterBound.height() / 2;
            canvas.drawText(letters[i], startX, startY, mLetterPaint);
            mLetterPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Down:0 UP:1 MOVE:2 CANCEL:3
        int action = event.getAction();
        float positionY = event.getY();
//        Log.d(TAG, "dispatchTouchEvent: action:" + action + ",y:" + positionY);
        int c = (int)((positionY - mLetterPaddingTop) / (getHeight() - mLetterPaddingTop - mLetterPaddingBottom) * letters.length);
//        Log.d(TAG, "dispatchTouchEvent: c:" + c);
        int oldChoose = choose;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (oldChoose != c && c >= 0 && c < letters.length && listener != null) {
                    choose = c;
                    invalidate();
                    listener.onTouchingLetterChanged(letters[c]);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && c >= 0 && c < letters.length && listener != null) {
                    choose = c;
                    invalidate();
                    listener.onTouchingLetterChanged(letters[c]);
                }
                break;
            case MotionEvent.ACTION_UP:
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 定义一个接口，点击字母后回调
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
}
