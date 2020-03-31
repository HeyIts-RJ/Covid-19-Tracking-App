package com.developer.android.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    final private View mWindow;
    private Context mContext;


    public CustomInfoWindowAdapter(Context context){
        mContext=context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.map_custom_info_window,null);

    }


    private void renderWindowText(Marker marker, View view){
        String title=marker.getTitle();
        TextView markerCounrty=(TextView)view.findViewById(R.id.map_marker_country_name);


        if (!title.equals("")){
            markerCounrty.setText(title);
        }


        String snippet=marker.getSnippet();
        TextView mapSnippet=view.findViewById(R.id.reported_case_display_marker);

        if (!snippet.equals("")){
            mapSnippet.setText(snippet);
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
