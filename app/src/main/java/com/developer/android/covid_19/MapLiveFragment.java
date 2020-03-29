package com.developer.android.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.android.covid_19.country.CovidCountry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapLiveFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    ArrayList<CovidCountry> covidCountry;

    JSONArray jsonArray;

    SearchView searchView;

    MarkerOptions markerOptions;

    private FloatingActionButton gpsLocationFAB;
    private FloatingActionButton realtimeCovidFAB;
    private FloatingActionButton realtimeHospitalFAB;
    private FloatingActionButton realtimeQuarantineFAB;

    private LinearLayout rightSideWipeIcons;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String TAG = "MapLiveFragment";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARS_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final float DEFAULT_ZOOM = 15f;

    private boolean mLocationPermissionGranted = false;

    public MapLiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        ((AppCompatActivity) getActivity()).getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        searchView = (SearchView) mView.findViewById(R.id.search_for_location_map);

        markerOptions = new MarkerOptions();

        rightSideWipeIcons = (LinearLayout) mView.findViewById(R.id.map_right_side_swipe);

        realtimeCovidFAB = (FloatingActionButton) mView.findViewById(R.id.realtime_covid_on_map);
        realtimeHospitalFAB = (FloatingActionButton) mView.findViewById(R.id.realtime_hospital_on_map);
        realtimeQuarantineFAB = (FloatingActionButton) mView.findViewById(R.id.realtime_quarantine_on_map);

        gpsLocationFAB = (FloatingActionButton) mView.findViewById(R.id.gps_locaton_fab);

        gpsLocationFAB.setColorFilter(Color.rgb(0, 128, 255));

        realtimeCovidFAB.setColorFilter(Color.rgb(235, 85, 105));
        realtimeHospitalFAB.setColorFilter(Color.rgb(12, 121, 255));
        realtimeQuarantineFAB.setColorFilter(Color.rgb(255, 222, 51));
        gpsLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });

        realtimeCovidFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(2f));
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermission();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        boolean success = mGoogleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
        }
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == 1) {
                    gpsLocationFAB.setColorFilter(Color.BLACK);

                    View view = ((AppCompatActivity) getActivity()).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) ((AppCompatActivity) getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapPlotCovidMarker();
                Toast.makeText(getContext(), "Map is updated", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initMap() {
        mMapView = (MapView) mView.findViewById(R.id.maps_live_feed);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    COARS_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
                getLocationPermission();
            }
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
            getLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }


    private void getDeviceLocation() {
        Log.d(TAG, "Getting Device Location...");
        gpsLocationFAB.setColorFilter(Color.rgb(0, 128, 255));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Found Current Location");
                            Location currentLcoation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLcoation.getLatitude(), currentLcoation.getLongitude()),
                                    DEFAULT_ZOOM);
                        } else {
                            Log.d(TAG, "Current Location is null");
                            Toast.makeText(getContext(), "Unable to Get Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Securite Exception " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG, "Moving the Camera... LAT:" + latlng.latitude + " LONG:" + latlng.longitude);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void mapPlotCovidMarker() {

//        Log.d(TAG, "Map Wala:" + covidCountry.get(0).getmCountryName());
        for (int i = 0; i < covidCountry.size(); i++) {
            mGoogleMap.addMarker(markerOptions.
                    title(covidCountry.get(i).getmCountryName())
                    .position(new LatLng(covidCountry.get(i).getmLat(), covidCountry.get(i).getmLan()))
                    .snippet("Total Cases:"+covidCountry.get(i).getmTotalCases()+
                            "\nTotal Recovered:"+covidCountry.get(i).getmTotalRecovered()+
                            "\nTotal Deaths:"+covidCountry.get(i).getmTotalDeaths()));

            if (covidCountry.get(i).getmTotalCases() < 10000) {

                mGoogleMap.addCircle(new CircleOptions()
                        .center(new LatLng(covidCountry.get(i).getmLat(), covidCountry.get(i).getmLan()))
                        .radius(covidCountry.get(i).getmTotalCases() * 100)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(70, 255, 102, 102)));

            } else {
                mGoogleMap.addCircle(new CircleOptions()
                        .center(new LatLng(covidCountry.get(i).getmLat(), covidCountry.get(i).getmLan()))
                        .radius(covidCountry.get(i).getmTotalCases() * 10)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(70, 255, 102, 102)));
            }
        }
    }


    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        covidCountry = new ArrayList<>();

        String url = "https://corona.lmao.ninja/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    Log.e(TAG, "Response:" + response);

                    try {
                        jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            Log.d(TAG, "onResponse:" + data.getString("country") + data.getJSONObject("countryInfo").getInt("lat"));
                            covidCountry.add(new CovidCountry(data.getString("country"),
                                    data.getJSONObject("countryInfo").getString("flag"),
                                    data.getJSONObject("countryInfo").getInt("lat"),
                                    data.getJSONObject("countryInfo").getInt("long"),
                                    data.getInt("cases"),
                                    data.getInt("deaths"),
                                    data.getInt("recovered")
                            ));
//                            Log.d(TAG,"Map Wala:"+covidCountry.get(i).getmLan());
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
            }
        });
        queue.add(stringRequest);


    }


    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getData();
            return null;
        }
    }

}
