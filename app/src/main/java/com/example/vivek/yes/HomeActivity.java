package com.example.vivek.yes;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.vivek.pojo.LatLngBean;
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

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {
    static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    GoogleMap map ;
    private ArrayList<LatLng>listLatLng = null;
    ArrayList <LatLng>arrayList = null ;
    HashMap<Marker,LatLngBean> hashMapMarker = new HashMap<Marker,LatLngBean>();
    Context ctx ;
    TestAdapter t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       statusCheck();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    public void statusCheck()
    {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        try{
            map.setMyLocationEnabled(true);
        }
        catch (Exception e){
            statusCheck();
        }

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
        ctx=this.getApplicationContext();
        t = new TestAdapter(ctx);
        Bundle extras = getIntent().getExtras();
        String tag=null;
        System.out.println("hi this is tag extra");
        if (extras != null) {
             tag = extras.getString("tag2");
            //The key argument here must match that used in the other activity
            System.out.println(tag);
            try {

                LoadingGoogleMap(t.getCordinates(tag),map);
                System.out.println("cordinates are done");
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }


        // very important
        /*String jam=null;


        String[] parts = jam.split(":");//houshold:electrician
        double lon =Double.parseDouble(parts[0]);
        double lat =Double.parseDouble(parts[1]);
        String id=
        LatLng xyz = new LatLng(lat,lon);*/
       // System.out.print("@$#%^!*#^$%&@*!(#^$%@*");

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.u);
       /* LatLng india_home = new LatLng(17.421347, 78.492047);
        LatLng andra_bank = new LatLng(17.424506, 78.490533);
        LatLng school = new LatLng(17.423768, 78.492239);
        LatLng po = new LatLng(17.419704, 78.495178);
        LatLng temple = new LatLng(17.419787, 78.491166);



        map.addMarker(new MarkerOptions().position(india_home).title("india_home").icon(icon));
        map.addMarker(new MarkerOptions().position(school).title("school").icon(icon));
        map.addMarker(new MarkerOptions().position(andra_bank).title("andra_bank").icon(icon));
        map.addMarker(new MarkerOptions().position(po).title("po").icon(icon));
        map.addMarker(new MarkerOptions().position(temple).title("temple").icon(icon));

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

                new BottomSheet.Builder(HomeActivity.this).title("fdsn").sheet(R.menu.menu_home).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.book_service:
                                Intent intent1 = new Intent(HomeActivity.this, ProductActivity.class);
                                startActivity(intent1);
                                *//*Toast.makeText(HomeActivity.this, "Sagar demo", Toast.LENGTH_SHORT).show();*//*
                                break;

                            case R.id.Profile:
                                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                                startActivity(intent);
                                *//*Toast.makeText(HomeActivity.this, "Sagar demo", Toast.LENGTH_SHORT).show();*//*
                                break;
                        }
                    }
                }).show();

               *//* if (arg0.getTitle().equals("india_home")) { //if marker source is clicked
                    *//**//*Toast.makeText(HomeActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast*//**//*
                    Intent intent = new Intent(HomeActivity.this, TimePass.class);
                    startActivity(intent);
                }*//*
                return true;
            }

        });*/

    }

    void LoadingGoogleMap(ArrayList<LatLngBean> arrayList, GoogleMap googlemap)
    {
        System.out.println("LoadingGoogle map start");
        System.out.println("%%%arraylist size is "+arrayList.size());
        LatLngBean bean;
        map = googlemap;
        map.clear();
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

            if(arrayList.size()>0)
            {
                try
                {
                    listLatLng=new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++)
                    {
                        bean = arrayList.get(i);
                        if(bean.getLatitude().length()>0 && bean.getLongitude().length()>0)
                        {
                            double lat=Double.parseDouble(bean.getLatitude());
                            double lon=Double.parseDouble(bean.getLongitude());
                            System.out.println("%%%% "+lat+","+lon);
                            LatLng x= new LatLng(lat,lon);

                            Marker marker = map.addMarker(new MarkerOptions().title(bean.getName()).position(x).icon(BitmapDescriptorFactory.fromResource(R.drawable.u)));
                            System.out.println("%%%%%markers are done");
                            //Add Marker to Hashmap
                            hashMapMarker.put(marker,bean);

                            //Set Zoom Level of Map pin
                            LatLng object=new LatLng(lat, lon);
                            listLatLng.add(object);
                        }
                    }
                    LatLng zoom = new LatLng(17.3850, 78.4867);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoom,12));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker position)
                    {
                        LatLngBean bean=hashMapMarker.get(position);

                        new BottomSheet.Builder(HomeActivity.this).title(bean.getName()).sheet(R.menu.menu_home).listener(new DialogInterface.OnClickListener() {
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
                    }
                });
            }
    }
}