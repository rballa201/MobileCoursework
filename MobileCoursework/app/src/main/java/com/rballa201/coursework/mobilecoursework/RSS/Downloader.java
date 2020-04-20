///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.RSS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Downloader extends AsyncTask<Void, Void, Object> {

    Context c;
    String urlAddress;
    ListView lv;
    String item;
    EditText editText;
    ArrayList<LatLng> latlngs;



    ProgressDialog pd;
    private Button allmap;
    private EditText datetxt;
    private EditText date1txt;


    public Downloader(Context c, String urlAddress, ListView lv, String item, EditText editText,Button allmap,EditText date1txt,EditText date2txt) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.lv = lv;
        this.item = item;
        this.editText = editText;
        this.allmap = allmap;
        this.datetxt = date1txt;
        this.date1txt = date2txt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd = new ProgressDialog(c);
//        pd.setTitle("Fetch Headlines");
//        pd.setMessage("Fetching....Please wait");
//        pd.show();
    }

    @Override
    protected Object doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        //pd.dismiss();

        if (data.toString().startsWith("Error")) {
            Toast.makeText(c, data.toString(), Toast.LENGTH_SHORT).show();
        } else {
            //PARSE
            new RSSParser(c, (InputStream) data, lv, item,editText,allmap,datetxt,date1txt).execute();
        }
    }

    private Object downloadData() {
        Object connection = Connector.connect(urlAddress);
        if (connection.toString().startsWith("Error")) {
            return connection.toString();
        }

        try {
//            if (item.equals("incidents")) {
//                AssetManager am = c.getAssets();
//                InputStream is = new BufferedInputStream(am.open("currentIncidents.xml"));
//                return is;
//            }
           //else {
                HttpURLConnection con = (HttpURLConnection) connection;
                InputStream is = new BufferedInputStream(con.getInputStream());
                return is;
            //}

        } catch (IOException e) {
            e.printStackTrace();
            return ErrorStrings.IO_EROR;
        }
    }
}
