package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.Supplement;

import java.io.Serializable;

/**
 * Created by aaron on 2/3/16.
 */
public class SupplementDTO extends BaseDTO implements Serializable {
    @SerializedName("supplement")
    private Supplement supplement;

    public Supplement getSupplement() {
        return supplement;
    }

    public void setSupplement(Supplement supplement) {
        this.supplement = supplement;
    }

    @Override
    public String toString() {
        return "SupplementDTO{" +
                "supplement=" + supplement +
                '}';
    }
}
