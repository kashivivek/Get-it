package com.example.vivek.yes;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {
    static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Assume thisActivity is the current activity
       /* int permissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.LOCATION_HARDWARE);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+permissionCheck);*/
        // Here, thisActivity is the current activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .tilt(10)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.u);
        LatLng india_home = new LatLng(17.421347, 78.492047);
        map.addMarker(new MarkerOptions().position(india_home).title("india_home").icon(icon));
        map.moveCamera(CameraUpdateFactory.newLatLng(india_home));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.421347, 78.492047))      // Sets the center of the map to location user
                .zoom(16)// Sets the zoom
                .tilt(10)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                new BottomSheet.Builder(HomeActivity.this).title("title").sheet(R.menu.menu_home).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.book_service:
                                Intent intent1 = new Intent(HomeActivity.this, ProductActivity.class);
                                startActivity(intent1);
                                /*Toast.makeText(HomeActivity.this, "Sagar demo", Toast.LENGTH_SHORT).show();*/
                                break;

                            case R.id.Profile:
                                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                                startActivity(intent);
                                /*Toast.makeText(HomeActivity.this, "Sagar demo", Toast.LENGTH_SHORT).show();*/
                                break;
                        }
                    }
                }).show();

               /* if (arg0.getTitle().equals("india_home")) { //if marker source is clicked
                    *//*Toast.makeText(HomeActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast*//*
                    Intent intent = new Intent(HomeActivity.this, TimePass.class);
                    startActivity(intent);
                }*/
                return true;
            }

        });

    }

}


