///////////////////////////////////////////////////////////////////////////////
// Title:       Mobile Coursework
// Student:     Ryan Ballantine
// Student ID:  S1829049
///////////////////////////////////////////////////////////////////////////////
package com.rballa201.coursework.mobilecoursework.fragments.incidents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class IncidentsItem implements Parcelable {
    private String title;
    //private int id;
    private String description;
    private String georss;
    private String link;
    private String pubdate;
    private String author;
    private String comments;
    private LatLng coordinates;

    public IncidentsItem(){
        this.author = getAuthor();
        this.comments= getComments();
        this.coordinates = getCoordinates();
        this.pubdate = getPubdate();
        this.link = getLink();
        this.georss = getGeorss();
        this.description=getDescription();
        this.title=getTitle();

    }

    protected IncidentsItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        georss = in.readString();
        link = in.readString();
        pubdate = in.readString();
        author = in.readString();
        comments = in.readString();
        coordinates = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(georss);
        dest.writeString(link);
        dest.writeString(pubdate);
        dest.writeString(author);
        dest.writeString(comments);
        dest.writeParcelable(coordinates, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IncidentsItem> CREATOR = new Creator<IncidentsItem>() {
        @Override
        public IncidentsItem createFromParcel(Parcel in) {
            return new IncidentsItem(in);
        }

        @Override
        public IncidentsItem[] newArray(int size) {
            return new IncidentsItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Title: " + getTitle()  + "\nDescription: " + getDescription() + "\nLink: " + getLink()
                + "\nGeoRSS Points: " + getGeorss()  + "\nPublished Date: " + getPubdate();
    }
}

