package com.beautyteam.everpay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 09.03.2015.
 */
public class User implements Parcelable {
    private int id;
    private String name;
    private String last_name;
    private String photo;

    public User(int id, String name, String last_name, String photo) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getPhoto() {return photo;}

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(last_name);
        parcel.writeString(photo);

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source.readInt(), source.readString(), source.readString(), source.readString());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
