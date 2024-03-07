package com.example.project48;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Toilet implements Parcelable {
    private int rating;
    private String name;
    private double distance;
    private String id;

    private String open_hours;
    private String address;


    // Constructor
    public Toilet(int rating, String name, double distance, String id, String open_hours, String address) {
        this.rating = rating;
        this.name = name;
        this.distance = distance;
        this.id = id;
        this.open_hours = open_hours;
        this.address = address;
    }

    protected Toilet(Parcel in) {
        rating = in.readInt();
        name = in.readString();
        distance = in.readDouble();
        id = in.readString();
        open_hours = in.readString();
        address = in.readString();
    }

    public static final Creator<Toilet> CREATOR = new Creator<Toilet>() {
        @Override
        public Toilet createFromParcel(Parcel in) {
            return new Toilet(in);
        }

        @Override
        public Toilet[] newArray(int size) {
            return new Toilet[size];
        }
    };

    // Getters
    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }
    public String getId(){return id;}

    public String getOpen_hours() {
        return open_hours;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(rating);
        parcel.writeString(name);
        parcel.writeDouble(distance);
        parcel.writeString(id);
        parcel.writeString(open_hours);
        parcel.writeString(address);


    }
}
