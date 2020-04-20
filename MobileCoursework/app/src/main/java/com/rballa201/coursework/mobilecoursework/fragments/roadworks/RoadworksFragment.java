///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.roadworks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rballa201.coursework.mobilecoursework.Map.MapsActivity;
import com.rballa201.coursework.mobilecoursework.RSS.Downloader;
import com.rballa201.coursework.mobilecoursework.R;
import com.rballa201.coursework.mobilecoursework.fragments.incidents.IncidentsItem;

public class RoadworksFragment extends Fragment {

    //private GalleryViewModel galleryViewModel;
    private ListView rawDataDisplay;
    private Button allmap;
    private Button date;
    private String urlSource = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    ListView lv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_roadworks, container, false);
        rawDataDisplay = root.findViewById(R.id.lv);
        allmap = root.findViewById(R.id.AllMap);
        rawDataDisplay.setTextFilterEnabled(true);
        EditText editText = (EditText) root.findViewById(R.id.editTxt);
        EditText datetxt = root.findViewById(R.id.Date_PickerTxt);
        EditText date1text =root.findViewById(R.id.Date_Picker2Txt);
        FloatingActionButton fab =root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rawDataDisplay.setSelectionAfterHeaderView();

            }
        });
        new Downloader(RoadworksFragment.this.getActivity(),urlSource,rawDataDisplay,"roadworks",editText,allmap,datetxt,date1text).execute();
        return root;
    }
}
