package com.developer.android.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.textview.MaterialTextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImages={
            R.drawable.map_vector,
            R.drawable.family_vector,
            R.drawable.app_vector
    };
    public String[] slideHeadings={
            "Share Your Location",
            "Add Your Family",
            "Keep App Updated"
    };
    public String[] slideDesc={
            "We need your location to monitor status and well being of your family and communnity around",
            "Add your family and other members living with you at your place and update status of everyone",
            "Keep app updated always as we add new features to better take care of your family and community"
    };

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView=view.findViewById(R.id.imageView);
        MaterialTextView slideHeading= (MaterialTextView) view.findViewById(R.id.headings);
        MaterialTextView slideDescp= view.findViewById(R.id.desc);

        slideImageView.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescp.setText(slideDesc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);
    }
}
