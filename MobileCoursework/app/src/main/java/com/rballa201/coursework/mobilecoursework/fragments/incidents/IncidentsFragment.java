///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.incidents;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rballa201.coursework.mobilecoursework.R;
import com.rballa201.coursework.mobilecoursework.RSS.Downloader;

import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class IncidentsFragment extends Fragment{

    //private SupportMapFragment mapFragment;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ListView rawDataDisplay;
    private String result;
    private Button allmap;
    private EditText dummy;
    private EditText dummy1;
    private String urlSource = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    //ArrayList<LatLng> latlngs = new ArrayList<LatLng>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_incidents, container, false);
        rawDataDisplay = root.findViewById(R.id.lv);
        rawDataDisplay.setTextFilterEnabled(true);
        allmap = root.findViewById(R.id.AllMap);
        EditText editText = (EditText) root.findViewById(R.id.editTxt);
        FloatingActionButton fab =root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rawDataDisplay.setSelectionAfterHeaderView();
            }
        });
        new Downloader(IncidentsFragment.this.getActivity(),urlSource,rawDataDisplay,"incidents",editText,allmap,dummy,dummy1).execute();
        return root;
    }
}
