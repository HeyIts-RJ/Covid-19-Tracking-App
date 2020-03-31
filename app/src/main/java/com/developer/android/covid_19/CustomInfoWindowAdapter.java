package com.developer.android.covid_19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    final private View mWindow;
    private Context mContext;
    final Activity mactivity;


    public CustomInfoWindowAdapter(Context context, Activity activity){
        mContext=context;
        mactivity=activity;
        mWindow= LayoutInflater.from(context).inflate(R.layout.map_custom_info_window,null);

    }


    private void renderWindowText(Marker marker, View view){
        String title=marker.getTitle();
        TextView markerCounrty=(TextView)view.findViewById(R.id.map_marker_country_name);


        if (!title.equals("")){
            markerCounrty.setText(title);
        }


        String snippet=marker.getSnippet();
        TextView totalCaseDisplay=view.findViewById(R.id.reported_case_display_marker);
        TextView totalDeathDisplay=view.findViewById(R.id.death_case_display_marker);
//        ImageView countryFlagImage=view.findViewById(R.id.country_flag_image_marker_display);

        if (!snippet.equals("")){
            String[] newSnippet=snippet.split("@",-2);
            totalCaseDisplay.setText(newSnippet[0]);
            totalDeathDisplay.setText(newSnippet[1]);
//            Glide.with(view).load(newSnippet[2]).into(countryFlagImage);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mWindow);
        return mWindow;
    }

}
