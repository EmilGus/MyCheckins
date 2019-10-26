package android.bignerdranch.mycheckin;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.UUID;

public class Checkin {
    private UUID mID;
    private String mTitle;
    private String mPlace;
    private String mDetails;
    private Date mDate;
    private String mPhoto;
    private Double mLng;
    private Double mLat;


    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Checkin(){
        this(UUID.randomUUID());
    }

    public Checkin(UUID id){
        mID = id;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getPhotoFilename(){
        return "IMG_" + getID().toString() + ".jpg";
    }
}
