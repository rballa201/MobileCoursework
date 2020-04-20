///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.plannedroadworks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rballa201.coursework.mobilecoursework.RSS.Downloader;
import com.rballa201.coursework.mobilecoursework.R;

public class PlannedRoadworksFragment extends Fragment {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ListView rawDataDisplay;
    private String result;
    private Button allmap;
    private String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_plannedroadworks, container, false);
        rawDataDisplay = root.findViewById(R.id.lv);
        rawDataDisplay.setTextFilterEnabled(true);
        allmap = root.findViewById(R.id.AllMap);
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
        new Downloader(PlannedRoadworksFragment.this.getActivity(),urlSource,rawDataDisplay,"plannedroadworks",editText,allmap,datetxt,date1text).execute();
        return root;
    }
}
