package net.sunniwell.georgeconversion.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;

import net.sunniwell.georgeconversion.R;

/**
 * Created by admin on 2017/11/22.
 */

public class CustomDialog extends AppCompatDialog {
    private static final String TAG = "jpd-CustomDialog";

    public CustomDialog(Context context) {
        super(context);
        Log.d(TAG, "CustomDialog: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_layout);
        Log.d(TAG, "onCreate: ");
    }
}
