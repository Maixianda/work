package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by aaron on 2/23/16.
 */
public class StaffDTO extends BaseDTO implements Serializable {
    @SerializedName("staff")
    private String staff;

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
                "staff='" + staff + '\'' +
                '}';
    }
}
