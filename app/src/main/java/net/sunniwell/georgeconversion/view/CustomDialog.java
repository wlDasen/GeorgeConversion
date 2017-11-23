package net.sunniwell.georgeconversion.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.sunniwell.georgeconversion.R;
import net.sunniwell.georgeconversion.interfaces.DialogClickCallback;

/**
 * Created by admin on 2017/11/22.
 */

public class CustomDialog extends AppCompatDialog implements View.OnClickListener {
    private static final String TAG = "jpd-CustomDialog";
    private TextView text;
    private Button confirm;
    private Button cancle;
    private DialogClickCallback callback;
    public static final int CUSTOM_DIALOG_TYPE_WITH_TWOBUTTON = 0;
    public static final int CUSTOM_DIALOG_TYPE_WITH_CONFIRMBUTTON = 1;
    private int dialogType = CUSTOM_DIALOG_TYPE_WITH_TWOBUTTON;

    public CustomDialog(Context context) {
        this(context, null);
    }

    public CustomDialog(Context context, DialogClickCallback callback) {
        super(context);
        this.callback = callback;
        Log.d(TAG, "CustomDialog: ");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_layout, null);
        text = (TextView)view.findViewById(R.id.content);
        confirm = (Button)view.findViewById(R.id.button_confirm);
        cancle = (Button)view.findViewById(R.id.button_cancle);
        confirm.setOnClickListener(this);
        cancle.setOnClickListener(this);
        super.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_confirm) {
            Log.d(TAG, "onClick: confirm...");
            cancel();
            if (callback != null) {
                callback.onConfirmButtonClicked();
            }
        } else {
            Log.d(TAG, "onClick: cancle...");
            cancel();
        }
    }

    public CustomDialog setContent(String content) {
        Log.d(TAG, "setContent: ");
        text.setText(content);
        return this;
    }

    public CustomDialog setDialogType(int type) {
        if (type == CUSTOM_DIALOG_TYPE_WITH_CONFIRMBUTTON) {
            cancle.setVisibility(View.GONE);
        }
        return this;
    }
    public CustomDialog setCancel(boolean flag) {
        super.setCancelable(flag);
        return this;
    }
}
