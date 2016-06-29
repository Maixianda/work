package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 2/3/16.
 */
public class Supplement implements Serializable {

    /**
     * address : null
     * loanPrice : null
     * images : []
     * acreage : null
     */

    private String address;
    @SerializedName("loanPrice")
    private long loanPrice;
    private String acreage;
    private List<Images> images;
    @SerializedName("isFavorite")
    private int isFavorite;

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLoanPrice() {
        return loanPrice;
    }

    public void setLoanPrice(long loanPrice) {
        this.loanPrice = loanPrice;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Supplement{" +
                "address='" + address + '\'' +
                ", loanPrice=" + loanPrice +
                ", acreage='" + acreage + '\'' +
                ", images=" + images +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
