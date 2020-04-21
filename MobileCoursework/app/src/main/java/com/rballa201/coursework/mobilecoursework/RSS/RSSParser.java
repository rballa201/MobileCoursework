///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.RSS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.rballa201.coursework.mobilecoursework.Map.MapsActivity;
import com.rballa201.coursework.mobilecoursework.fragments.incidents.IncidentsAdapter;
import com.rballa201.coursework.mobilecoursework.fragments.incidents.IncidentsItem;
import com.rballa201.coursework.mobilecoursework.fragments.plannedroadworks.PlannedRoadworksAdapter;
import com.rballa201.coursework.mobilecoursework.fragments.plannedroadworks.PlannedRoadworksItem;
import com.rballa201.coursework.mobilecoursework.fragments.roadworks.RoadworksItem;
import com.rballa201.coursework.mobilecoursework.fragments.roadworks.RoadworksAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RSSParser extends AsyncTask<Void, Void, Boolean> {

    Context c;
    private static IncidentsAdapter incidentsAdapter;
    private static PlannedRoadworksAdapter plannedRoadworksAdapter;
    private static RoadworksAdapter roadworksAdapter;
    private InputStream is;
    private ListView lv;
    private RoadworksItem Ritem;
    private PlannedRoadworksItem PRitem;
    private IncidentsItem Initem;
    private Button allmap;
    private Date Start;
    private Date End;
    private String page;
    private EditText editText;
    private EditText datetxt;
    private EditText date1txt;

    private ProgressDialog pd;
    private ArrayList<RoadworksItem> roadworks = new ArrayList<>();
    private ArrayList<PlannedRoadworksItem> plannedroadworks = new ArrayList<>();
    private ArrayList<IncidentsItem> incidents = new ArrayList<>();
    //ArrayList<LatLng> latlngs = new ArrayList<>();
    //ArrayList<Roadworks> roadworks = new ArrayList<>();

    public RSSParser(Context c, InputStream is, ListView lv, String page,EditText editText,Button allmap,EditText datetxt,EditText date1txt) {
        this.c = c;
        this.is = is;
        this.lv = lv;
        this.page = page;
        this.editText = editText;
        this.allmap = allmap;
        this.datetxt=datetxt;
        this.date1txt=date1txt;
    }

    public static Date DateConversion(String s) {
        Date returnDate = null;
        String[] date = s.split(": ");
        String[] actualDate = date[1].split("-");
        String[] dDate = actualDate[0].split(",");
        String[] split = dDate[1].split(" ");
        if (split[2].equals("January")) {
            split[2] = "1";
        } else if (split[2].equals("February")) {
            split[2] = "2";
        } else if (split[2].equals("March")) {
            split[2] = "3";
        } else if (split[2].equals("April")) {
            split[2] = "4";
        } else if (split[2].equals("May")) {
            split[2] = "5";
        } else if (split[2].equals("June")) {
            split[2] = "6";
        } else if (split[2].equals("July")) {
            split[2] = "7";
        } else if (split[2].equals("August")) {
            split[2] = "8";
        } else if (split[2].equals("September")) {
            split[2] = "9";
        } else if (split[2].equals("October")) {
            split[2] = "10";
        } else if (split[2].equals("November")) {
            split[2] = "11";
        } else if (split[2].equals("December")) {
            split[2] = "12";
        }

        String d = split[1] + "/" + split[2] + "/" + split[3];
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            returnDate = format.parse(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pd = new ProgressDialog(c);
//        pd.setTitle("Parse Data");
//        pd.setMessage("Parsing...Please wait");
//        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseRSS();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {

        super.onPostExecute(isParsed);
        //pd.dismiss();
        if (isParsed) {
            //BIND
            if (page.equals("plannedroadworks")) {
                plannedRoadworksAdapter = new PlannedRoadworksAdapter(c, plannedroadworks);
                lv.setAdapter(plannedRoadworksAdapter);
                allmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        Intent showMap = new Intent(c, MapsActivity.class);
                        b.putParcelableArrayList("Markers",plannedroadworks);
                        b.putString("Marker Amount","Many");
                        b.putString("Type","PlannedRoadworks");

//                        showMap.putExtra("Marker Amount","Single");
//                        showMap.
                        showMap.putExtras(b);
                        c.startActivity(showMap);                    }
                });
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        PlannedRoadworksItem data = (PlannedRoadworksItem) parent.getItemAtPosition(position);
                        String description = "Start Date: " + data.getStartDate() + "\nFinish Date: " + data.getEndDate()+ "\n" + data.getDelayInfo();

                        Intent showMap = new Intent(c, MapsActivity.class);
                        showMap.putExtra("longitude", data.getCoordinates().longitude);
                        showMap.putExtra("latitude", data.getCoordinates().latitude);
                        showMap.putExtra("Marker Title", data.getTitle());
                        showMap.putExtra("Marker Snippet", description);
                        showMap.putExtra("Link", data.getLink());
                        showMap.putExtra("PubDate", data.getPubdate());
                        showMap.putExtra("Marker Amount","Single");
                        c.startActivity(showMap);
//                        Snackbar.make(view,
//                                "GeoRSS: " + data.getGeorss()
//                                        //+ "\nLink: " + dataModel.getLink()
//                                        //+
//                                        + "\nPubDate: " + data.getPubdate(),
//                                Snackbar.LENGTH_LONG).setAction("No action", null).show();
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            plannedRoadworksAdapter.resetData();
                        }

                        plannedRoadworksAdapter.getTitleFilter().filter(s.toString());

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                datetxt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            plannedRoadworksAdapter.resetData();
                        }

                        plannedRoadworksAdapter.getStartDateFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                date1txt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            plannedRoadworksAdapter.resetData();
                        }

                        plannedRoadworksAdapter.getEndDateFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
            else if (page.equals("roadworks")) {
                roadworksAdapter = new RoadworksAdapter(c, roadworks);
                lv.setAdapter(roadworksAdapter);
                allmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        Intent showMap = new Intent(c, MapsActivity.class);
                        b.putParcelableArrayList("Markers",roadworks);
                        b.putString("Marker Amount","Many");
                        b.putString("Type","Roadworks");
                        showMap.putExtras(b);
                        c.startActivity(showMap);                    }
                });
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        RoadworksItem Rdata = (RoadworksItem) parent.getItemAtPosition(position);
                        String description = "Start Date: " + Rdata.getStartDate() + "\nFinish Date: " + Rdata.getEndDate()+ "\n" + Rdata.getDelayInfo();

                        Intent showMap = new Intent(c, MapsActivity.class);

                        showMap.putExtra("longitude", Rdata.getCoordinates().longitude);
                        showMap.putExtra("latitude", Rdata.getCoordinates().latitude);
                        showMap.putExtra("Marker Title", Rdata.getTitle());
                        showMap.putExtra("Marker Snippet", description);
                        showMap.putExtra("Link", Rdata.getLink());
                        showMap.putExtra("PubDate", Rdata.getPubdate());
                        showMap.putExtra("Marker Amount","Single");
                        c.startActivity(showMap);

//                        Snackbar.make(view,
//                                "GeoRSS: " + Rdata.getGeorss()
//                                        //+ "\nLink: " + dataModel.getLink()
//                                        //+
//                                        + "\nPubDate: " + Rdata.getPubdate(),
//                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            roadworksAdapter.resetData();
                        }

                        roadworksAdapter.getTitleFilter().filter(s.toString());

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                datetxt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            roadworksAdapter.resetData();
                        }

                        roadworksAdapter.getStartDateFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                date1txt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            roadworksAdapter.resetData();
                        }

                        roadworksAdapter.getEndDateFilter().filter(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

            } else {
                //lv.setAdapter(new ArrayAdapter<IncidentsItem>(c, android.R.layout.simple_list_item_1, incidents));
                incidentsAdapter = new IncidentsAdapter(c, incidents);
                lv.setAdapter(incidentsAdapter);
                allmap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        Intent showMap = new Intent(c, MapsActivity.class);
                        b.putParcelableArrayList("Markers",incidents);
                        b.putString("Marker Amount","Many");
                        b.putString("Type","Incidents");

//                        showMap.putExtra("Marker Amount","Single");
//                        showMap.
                        showMap.putExtras(b);
                        c.startActivity(showMap);                    }
                });
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        IncidentsItem dataModel = (IncidentsItem) parent.getItemAtPosition(position);
                        Intent showMap = new Intent(c, MapsActivity.class);
                        showMap.putExtra("longitude", dataModel.getCoordinates().longitude);
                        showMap.putExtra("latitude", dataModel.getCoordinates().latitude);
                        showMap.putExtra("Marker Title", dataModel.getTitle());
                        showMap.putExtra("Marker Snippet", dataModel.getDescription());
                        showMap.putExtra("Link", dataModel.getLink());
                        showMap.putExtra("PubDate", dataModel.getPubdate());
                        showMap.putExtra("Marker Amount","Single");
                        c.startActivity(showMap);

//                        Snackbar.make(view,
//                                "GeoRSS: " + dataModel.getGeorss()
//                                //+ "\nLink: " + dataModel.getLink()
//                                //+
//                                + "\nPubDate: " + dataModel.getPubdate(),
//                                Snackbar.LENGTH_LONG).setAction("No action", null).show();
                    }
                });
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            incidentsAdapter.resetData();
                        }

                        incidentsAdapter.getFilter().filter(s.toString());

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        } else {
            Toast.makeText(c, "Unable To Parse", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseRSS() {
        if (page.equals("roadworks")) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                //SET STREAM
                parser.setInput(is, null);
                roadworks.clear();
                String text = "";

                Boolean isWebsiteTitle = true;

                int event = parser.getEventType();

                do {
                    String name = parser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                Ritem = new RoadworksItem();
                            } else if (Ritem != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    Ritem.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    Ritem.setDescription(parser.nextText());
                                    String description = Ritem.getDescription();
                                    try {
                                        String[] seperate = description.split("<br />");
                                        //StringTokenizer tokens = new StringTokenizer(description, "<br />");
                                        Date startDate = DateConversion(seperate[0]);
                                        Date endDate = DateConversion(seperate[1]);
                                        String DelayInfo = seperate[2];
                                        DelayInfo = DelayInfo.trim();
                                        //System.out.println(DelayInfo);
                                        Ritem.setDelayInfo(DelayInfo);
                                        //if (!startDate.equals(null))
                                        //{
                                        Ritem.setStartDate(startDate);
                                        Start = startDate;
                                        //}
                                        //if (!endDate.equals(null))
                                        //{
                                        Ritem.setEndDate(endDate);
                                        End = endDate;
                                        //}
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //data.add(DelayInfo.toLowerCase());

                                } else if (name.equals("georss:point")) {
                                    Ritem.setGeorss(parser.nextText());
                                    String georss = Ritem.getGeorss();

                                    String [] latlng = georss.split(" ");
                                    double latitude = Double.parseDouble(latlng[0]);
                                    double longitude = Double.parseDouble(latlng[1]);
                                    LatLng coords = new LatLng(latitude,longitude);
                                    Ritem.setCoordinates(coords);
                                } else if (name.equals("link")) {
                                    Ritem.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase("pubdate")) {
                                    Ritem.setPubdate(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("author")) {
                                    Ritem.setAuthor(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("comments")) {
                                    Ritem.setComments(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                }
                            }
                            break;


//                    case XmlPullParser.TEXT:
//                        text=parser.getText();
//                        break;

                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                roadworks.add(Ritem);
                            }
                            break;

                    }
                    event = parser.next();

                } while (event != XmlPullParser.END_DOCUMENT);

                return true;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (page.equals("plannedroadworks")) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                //SET STREAM
                parser.setInput(is, null);
                plannedroadworks.clear();
                String text = "";

                Boolean isWebsiteTitle = true;

                int event = parser.getEventType();

                do {
                    String name = parser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                PRitem = new PlannedRoadworksItem();
                            } else if (PRitem != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    PRitem.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    PRitem.setDescription(parser.nextText());
                                    String description = PRitem.getDescription();
                                    try {
                                        String[] seperate = description.split("<br />");
                                        //StringTokenizer tokens = new StringTokenizer(description, "<br />");
                                        Date startDate = DateConversion(seperate[0]);
                                        Date endDate = DateConversion(seperate[1]);

                                        String DelayInfo = seperate[2];
                                        //DelayInfo = DelayInfo.trim();
                                        //System.out.println(DelayInfo);
                                        PRitem.setDelayInfo(DelayInfo);
                                        PRitem.setStartDate(startDate);
                                        PRitem.setEndDate(endDate);
                                        Start = startDate;
                                        End = endDate;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //data.add(DelayInfo.toLowerCase());

                                } else if (name.equals("georss:point")) {
                                    PRitem.setGeorss(parser.nextText());
                                    String georss = PRitem.getGeorss();
                                    String [] latlng = georss.split(" ");
                                    double latitude = Double.parseDouble(latlng[0]);
                                    double longitude = Double.parseDouble(latlng[1]);
                                    LatLng coords = new LatLng(latitude,longitude);
                                    PRitem.setCoordinates(coords);
                                } else if (name.equals("link")) {
                                    PRitem.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase("pubdate")) {
                                    PRitem.setPubdate(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("author")) {
                                    PRitem.setAuthor(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("comments")) {
                                    PRitem.setComments(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                }
                            }
                            break;


//                    case XmlPullParser.TEXT:
//                        text=parser.getText();
//                        break;

                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                plannedroadworks.add(PRitem);
                            }
                            break;

                    }
                    event = parser.next();

                } while (event != XmlPullParser.END_DOCUMENT);

                return true;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (page.equals("incidents")) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                //SET STREAM
                parser.setInput(is, null);
                incidents.clear();
                String text = "";

                Boolean isWebsiteTitle = true;

                int event = parser.getEventType();

                do {
                    String name = parser.getName();
                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                Initem = new IncidentsItem();
                            } else if (Initem != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    Initem.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    Initem.setDescription(parser.nextText());
                                } else if (name.equals("georss:point")) {
                                    Initem.setGeorss(parser.nextText());
                                    String georss = Initem.getGeorss();

                                    String [] latlng = georss.split(" ");
                                    double latitude = Double.parseDouble(latlng[0]);
                                    double longitude = Double.parseDouble(latlng[1]);
                                    LatLng coords = new LatLng(latitude,longitude);
                                    Initem.setCoordinates(coords);

                                    //String georss = Initem.getGeorss();

                                    //String [] latlng = georss.split(" ");
                                    //double latitude = Double.parseDouble(latlng[0]);
                                    // double longitude = Double.parseDouble(latlng[1]);
                                    //latlngs.add(new LatLng(latitude, longitude)); //some latitude and logitude value
                                    //LatLng marker = new LatLng(latitude,longitude);
                                    //googleMap.addMarker(new MarkerOptions().position(marker).title(Initem.getTitle()));
//                                    Fragment fragment = new IncidentsFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putParcelableArrayList("LatLng",latlngs);
////                                    fragment.setArguments(bundle);
//                                    assert fragment.getArguments() != null;
//                                    fragment.getArguments().putBundle("LatLong",bundle);
                                } else if (name.equals("link")) {
                                    Initem.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase("pubdate")) {
                                    Initem.setPubdate(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("author")) {
                                    Initem.setAuthor(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                } else if (name.equalsIgnoreCase("comments")) {
                                    Initem.setComments(parser.nextText());
                                    //headlines.add(roadwork.toString());
                                }
                            }
                            break;


//                    case XmlPullParser.TEXT:
//                        text=parser.getText();
//                        break;

                        case XmlPullParser.END_TAG:
                            if (name.equalsIgnoreCase("item")) {
                                incidents.add(Initem);
                            }
                            break;

                    }
                    event = parser.next();

                } while (event != XmlPullParser.END_DOCUMENT);

                return true;

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
