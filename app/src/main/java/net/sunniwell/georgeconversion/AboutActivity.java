package net.sunniwell.georgeconversion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.sunniwell.georgeconversion.db.UpgradeBean;
import net.sunniwell.georgeconversion.util.HttpUtil;
import net.sunniwell.georgeconversion.util.JSONParserUtil;
import net.sunniwell.georgeconversion.util.UpgradeUtil;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "jpd-AboutAct";
    private static final String UPDATE_URL = "http://maven.sunniwell.net:8082/doc/test/upgrade.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button button = (Button)findViewById(R.id.upgrade_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = HttpUtil.getByURLConnection(UPDATE_URL);
                        if (response != null) {
                            UpgradeBean bean = JSONParserUtil.parseUpgradeJSON(response);
                            Log.d(TAG, "checkUpdate run: bean:" + bean);
                            UpgradeUtil.from(AboutActivity.this).serverVersionName(bean.getVersionName())
                                    .serverVersionCode(bean.getVersionCode()).serverIsForce(bean.getIsForce())
                                    .description(bean.getDescription()).apkUrl(bean.getApkUrl()).update();
                        }
                    }
                }).start();
            }
        });
        Button statusBtn = (Button)findViewById(R.id.status_button);
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeUtil.from(AboutActivity.this).checkStatus();
            }
        });
    }
}
