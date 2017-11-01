package com.trackin.iodroid.trackin;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class cordmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView mTapTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public String getAddress(LatLng point)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            return address;
        }
        catch(Exception e)
        {
            Toast.makeText(cordmap.this,"GPS not enabled!",Toast.LENGTH_LONG);
        }
        return "Unknown Location";
    }
    public int getZoomLevel(Circle circle)
    {
        int zoomlevel=11;
        if(circle!=null)
        {
            double radius=circle.getRadius() + circle.getRadius()/2;
            double scale=radius/500;
            zoomlevel=(int) (16-Math.log(scale)/Math.log(2));
        }
        return zoomlevel;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnMapClickListener(this);
        Bundle bundle = getIntent().getExtras();

        double r_long=bundle.getDouble("lattitude");
        double r_lat=bundle.getDouble("longitude");
        // Add a marker in Sydney and move the camera
        LatLng mark = new LatLng(r_lat, r_long);
        CircleOptions circleoptions=new CircleOptions().strokeWidth(2).strokeColor(Color.BLUE).fillColor(Color.parseColor("#500084d3"));
        mMap.addMarker(new MarkerOptions().position(mark).title(getAddress(mark)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        Circle circle=mMap.addCircle(circleoptions.center(mark).radius(5000.0));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(circleoptions.getCenter(),getZoomLevel(circle)));
        final TextView textViewToChange = (TextView) findViewById(R.id.cord);
        textViewToChange.setText("Lattitude:"+r_lat+"\nLongitude"+r_long);
    }
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
}