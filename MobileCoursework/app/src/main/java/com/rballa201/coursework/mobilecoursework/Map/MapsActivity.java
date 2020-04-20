///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.rballa201.coursework.mobilecoursework.MainActivity;
import com.rballa201.coursework.mobilecoursework.R;
import com.rballa201.coursework.mobilecoursework.fragments.incidents.IncidentsItem;
import com.rballa201.coursework.mobilecoursework.fragments.plannedroadworks.PlannedRoadworksItem;
import com.rballa201.coursework.mobilecoursework.fragments.roadworks.RoadworksItem;

import java.util.ArrayList;
import java.util.Date;

import static com.rballa201.coursework.mobilecoursework.RSS.RSSParser.DateConversion;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_incidents, R.id.nav_roadworks, R.id.nav_plannedroadworks)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home){
            Intent i= new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getIntent().getExtras();
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if (bundle.get("Marker Amount").equals("Single")) {
            LatLng coords = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(coords).title((bundle.getString("Marker Title"))).snippet(bundle.getString("Marker Snippet"));

            final InfoWindowData info = new InfoWindowData();
            info.setLink(bundle.getString("Link"));
            info.setPubDate(bundle.getString("PubDate"));


            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(info.getLink()));
                    startActivity(intent);

                }
            });

            Marker m = mMap.addMarker(markerOptions);
            m.setTag(info);
            m.showInfoWindow();


            //mMap.addMarker(

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 12.0f));
        }else if (bundle.get("Marker Amount").equals("Many")){
            Bundle b = getIntent().getExtras();
            if (b != null) {
                if (b.get("Type").equals("PlannedRoadworks")) {
                    ArrayList<PlannedRoadworksItem> q = null;
                    q = b.getParcelableArrayList("Markers");
                    if (q != null) {
                        for (PlannedRoadworksItem p : q) {
                            String description = p.getDescription();
                            Date startDate = null;
                            Date endDate = null;
                            String DelayInfo = null;
                            try {


                                String[] seperate = description.split("<br />");
                                //StringTokenizer tokens = new StringTokenizer(description, "<br />");
                                startDate = DateConversion(seperate[0]);
                                endDate = DateConversion(seperate[1]);
                                DelayInfo = seperate[2];
                                DelayInfo = DelayInfo.trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String Description = "Start Date: " + startDate + "\nFinish Date: " + endDate + "\n" + DelayInfo;
                            LatLng coords = new LatLng(p.getCoordinates().latitude, p.getCoordinates().longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(coords).title(p.getTitle()).snippet(Description);

                            final InfoWindowData info = new InfoWindowData();
                            info.setLink(p.getLink());
                            info.setPubDate(p.getPubdate());


                            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                            mMap.setInfoWindowAdapter(customInfoWindow);

                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                                @Override
                                public void onInfoWindowClick(Marker arg0) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse(info.getLink()));
                                    startActivity(intent);

                                }
                            });

                            Marker m = mMap.addMarker(markerOptions);
                            m.setTag(info);
                            //m.showInfoWindow();
                        }
                    }
                }
                if (b.get("Type").equals("Roadworks")) {
                    ArrayList<RoadworksItem> q = null;
                    q = b.getParcelableArrayList("Markers");
                    if (q != null) {
                        for (RoadworksItem p : q) {
                            String description = p.getDescription();
                            Date startDate = null;
                            Date endDate = null;
                            String DelayInfo = null;
                            try {


                                String[] seperate = description.split("<br />");
                                //StringTokenizer tokens = new StringTokenizer(description, "<br />");
                                startDate = DateConversion(seperate[0]);
                                endDate = DateConversion(seperate[1]);
                                DelayInfo = seperate[2];
                                DelayInfo = DelayInfo.trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String Description = "Start Date: " + startDate + "\nFinish Date: " + endDate + "\n" + DelayInfo;
                            LatLng coords = new LatLng(p.getCoordinates().latitude, p.getCoordinates().longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(coords).title(p.getTitle()).snippet(Description);

                            final InfoWindowData info = new InfoWindowData();
                            info.setLink(p.getLink());
                            info.setPubDate(p.getPubdate());


                            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                            mMap.setInfoWindowAdapter(customInfoWindow);

                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                                @Override
                                public void onInfoWindowClick(Marker arg0) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse(info.getLink()));
                                    startActivity(intent);

                                }
                            });

                            Marker m = mMap.addMarker(markerOptions);
                            m.setTag(info);
                            //m.showInfoWindow();
                        }
                }
                }
                if (b.get("Type").equals("Incidents")) {
                    ArrayList<IncidentsItem> q = null;
                    q = b.getParcelableArrayList("Markers");
                    for (IncidentsItem p : q) {

                        LatLng coords = new LatLng(p.getCoordinates().latitude,p.getCoordinates().longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(coords).title(p.getTitle()).snippet(p.getDescription());

                        final InfoWindowData info = new InfoWindowData();
                        info.setLink(p.getLink());
                        info.setPubDate(p.getPubdate());


                        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                        mMap.setInfoWindowAdapter(customInfoWindow);

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick(Marker arg0) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse(info.getLink()));
                                startActivity(intent);

                            }
                        });

                        Marker m = mMap.addMarker(markerOptions);
                        m.setTag(info);
                        //m.showInfoWindow();
                    }
                }
            }

        }
    }
}
