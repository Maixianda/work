package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leo on 16/2/1.
 * 利率对象
 */
public class InsterestInfo implements Parcelable {
    private String insterestName;
    private boolean isChecked;
    private double interestrateDiscount;//利率折扣

    public InsterestInfo(String insterestName, double interestrateDiscount, boolean isChecked) {
        this.insterestName = insterestName;
        this.isChecked = isChecked;
        this.interestrateDiscount = interestrateDiscount;
    }

    protected InsterestInfo(Parcel in) {
        insterestName = in.readString();
        isChecked = in.readByte() != 0;
        interestrateDiscount = in.readFloat();
    }

    public static final Creator<InsterestInfo> CREATOR = new Creator<InsterestInfo>() {
        @Override
        public InsterestInfo createFromParcel(Parcel in) {
            return new InsterestInfo(in);
        }

        @Override
        public InsterestInfo[] newArray(int size) {
            return new InsterestInfo[size];
        }
    };

    public String getInsterestName() {
        return insterestName;
    }

    public void setInsterestName(String insterestName) {
        this.insterestName = insterestName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getInterestrateDiscount() {
        return interestrateDiscount;
    }

    public void setInterestrateDiscount(float interestrateDiscount) {
        this.interestrateDiscount = interestrateDiscount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(insterestName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeDouble(interestrateDiscount);
    }
}
