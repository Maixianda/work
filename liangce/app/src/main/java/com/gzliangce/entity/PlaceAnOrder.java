package com.gzliangce.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 1/26/16.
 */
public class PlaceAnOrder implements Serializable {
    private String productId;
    private String areaId;
    private String address;
    private String linkman;
    private String phone;
    private String mortgageId;
    private String number;
    private String reason;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMortgageId() {
        return mortgageId;
    }

    public void setMortgageId(String mortgageId) {
        this.mortgageId = mortgageId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "PlaceAnOrder{" +
                "productId='" + productId + '\'' +
                ", areaId='" + areaId + '\'' +
                ", address='" + address + '\'' +
                ", linkman='" + linkman + '\'' +
                ", phone='" + phone + '\'' +
                ", mortgageId='" + mortgageId + '\'' +
                ", number='" + number + '\'' +
                ", reason='" + reason + '\'' +
                ", index=" + index +
                '}';
    }
}
