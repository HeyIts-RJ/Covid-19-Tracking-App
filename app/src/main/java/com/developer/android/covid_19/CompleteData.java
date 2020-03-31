package com.developer.android.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class CompleteData extends AppCompatActivity {

    ImageView countryFlag;
    TextView countryName;
    TextView totalCases, totalDeaths, totalRecover, todayCases, todayDeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(200,25,19));
        }

        setContentView(R.layout.activity_complete_data);
        Intent i = getIntent();
        countryFlag = (ImageView) findViewById(R.id.country_flag);
        countryName = (TextView) findViewById(R.id.country_name);
        totalCases = (TextView) findViewById(R.id.reported_case_display_country);
        totalDeaths = (TextView) findViewById(R.id.reported_deaths_display_country);
        totalRecover = (TextView) findViewById(R.id.reported_recovery_display_country);
        todayCases = (TextView) findViewById(R.id.today_reported_case);
        todayDeath = (TextView) findViewById(R.id.today_death_case);

        Glide.with(this)
                .load(i.getStringExtra("country_flag"))
                .centerCrop()
                .placeholder(R.drawable.round_flag_icon)
                .apply(RequestOptions.circleCropTransform())
                .into(countryFlag);


        countryName.setText(i.getStringExtra("country_name"));
        totalCases.setText(String.valueOf(i.getIntExtra("total_cases",0)).toString());

        totalDeaths.setText(String.valueOf(i.getIntExtra("total_deaths",0)).toString());
        totalRecover.setText(String.valueOf(i.getIntExtra("total_recover",0)).toString());
        todayCases.setText(String.valueOf(i.getIntExtra("today_cases",0)).toString());
        todayDeath.setText(String.valueOf(i.getIntExtra("today_deaths",0)).toString());

    }
}
