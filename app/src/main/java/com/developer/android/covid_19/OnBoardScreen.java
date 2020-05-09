package com.developer.android.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OnBoardScreen extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotlayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private TextView next;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_screen);

        slideViewPager=(ViewPager) findViewById(R.id.viewpager);
        dotlayout=findViewById(R.id.dotlayout);
        sliderAdapter = new SliderAdapter(this);
        next=findViewById(R.id.next);

        slideViewPager.setAdapter(sliderAdapter);


        slideViewPager.addOnPageChangeListener(viewListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(mCurrentPage+1);
                if (mCurrentPage==mDots.length-1){
                    startActivity(new Intent(OnBoardScreen.this,LoginActivity.class));
                }
            }
        });
    }

    public void addDots(int position){
        mDots=new TextView[3];
        dotlayout.removeAllViews();


        for(int i=0;i<mDots.length;i++){
            mDots[i]= new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getColor(R.color.black));
            dotlayout.addView(mDots[i]);




        }
        if (mDots.length>0){
            mDots[position].setTextColor(getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            mCurrentPage=position;
            if (position==0){
                next.setEnabled(true);
                next.setText("Next");
            }else if(position==mDots.length-1){
                next.setEnabled(true);
                next.setText("Finish");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
