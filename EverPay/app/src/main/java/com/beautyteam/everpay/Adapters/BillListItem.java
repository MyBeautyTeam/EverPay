package com.beautyteam.everpay.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 06.04.2015.
 */
public class BillListItem implements Parcelable {
    public String id;
    public String vkid;
    public String name;
    public String img;
    public int need;
    public int invest;
    public boolean isRemoved;

    public BillListItem(String id, String vkId, String name, String img,int need, int invest, boolean isRemoved) {
        this.id = id;
        this.vkid = vkId;
        this.name = name;
        this.img = img;
        this.need = need;
        this.invest = invest;
        this.isRemoved = isRemoved;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(vkid);
        parcel.writeString(name);
        parcel.writeString(img);
        parcel.writeInt(need);
        parcel.writeInt(invest);
        parcel.writeByte((byte) (isRemoved ? 1 : 0));
    }

    public static final Parcelable.Creator<BillListItem> CREATOR = new Parcelable.Creator<BillListItem>() {

        @Override
        public BillListItem createFromParcel(Parcel source) {
            BillListItem billListItem = new BillListItem(source.readString(), source.readString(),source.readString(), source.readString(), source.readInt(), source.readInt(), source.readByte() == 1);
            return billListItem;
        }

        @Override
        public BillListItem[] newArray(int size) {
            return new BillListItem[size];
        }
    };

}
