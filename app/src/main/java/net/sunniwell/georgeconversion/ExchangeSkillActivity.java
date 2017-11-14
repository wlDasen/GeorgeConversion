package net.sunniwell.georgeconversion;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import net.sunniwell.georgeconversion.util.ColorDBUtil;

public class ExchangeSkillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_skill);

        Toolbar toolbar = (Toolbar)findViewById(R.id.exchange_skill_toolbar);
        toolbar.setBackgroundColor(Color.parseColor(ColorDBUtil.getDefaultColor().getColorStr()));
        Button backBtn = (Button)findViewById(R.id.exchange_back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
