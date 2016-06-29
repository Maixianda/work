package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leo on 16/2/2.
 * 首付比例
 */
public class DownPaymentInfo implements Parcelable {
    private String proportion;
    private boolean isChecked;
    private double scale;

    public DownPaymentInfo(String proportion, boolean isChecked, double scale) {
        this.proportion = proportion;
        this.isChecked = isChecked;
        this.scale = scale;
    }

    protected DownPaymentInfo(Parcel in) {
        proportion = in.readString();
        isChecked = in.readByte() != 0;
        scale = in.readInt();
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public static Creator<DownPaymentInfo> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<DownPaymentInfo> CREATOR = new Creator<DownPaymentInfo>() {
        @Override
        public DownPaymentInfo createFromParcel(Parcel in) {
            return new DownPaymentInfo(in);
        }

        @Override
        public DownPaymentInfo[] newArray(int size) {
            return new DownPaymentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proportion);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeDouble(scale);
    }
}
