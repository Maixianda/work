package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/1/11.
 */
public class OrderInfo implements Serializable {

    /**
     * productName : 公积金贷款
     * number : 20160129594390208
     * realName : mortgage
     * status : wait
     * createTime : 1454056239000
     * icon : http://www.yooyoo360.com/photo/2009-1-1/20090112111116425.jpg
     */
    @SerializedName("productName")
    private String productName;

    @SerializedName("number")
    private String number;

    @SerializedName("realName")
    private String realName;

    @SerializedName("status")
    private String status;

    @SerializedName("createTime")
    private long createTime;

    @SerializedName("icon")
    private String icon;

    @SerializedName("areaId")
    private long areaId;

    @SerializedName("canTransfer")
    private boolean canTransfer;

    @SerializedName("buyer")
    private String buyer;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "productName='" + productName + '\'' +
                ", number='" + number + '\'' +
                ", realName='" + realName + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", icon='" + icon + '\'' +
                ", areaId=" + areaId +
                ", canTransfer=" + canTransfer +
                ", buyer='" + buyer + '\'' +
                ", index=" + index +
                ", areaName='" + areaName + '\'' +
                ", address='" + address + '\'' +
                ", linkman='" + linkman + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", accountPhone='" + accountPhone + '\'' +
                ", productId=" + productId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", evaluate=" + evaluate +
                ", serviceAttitude=" + serviceAttitude +
                ", professionalAbility=" + professionalAbility +
                ", speed=" + speed +
                ", gradeTime='" + gradeTime + '\'' +
                ", comment='" + comment + '\'' +
                ", actualPrice=" + actualPrice +
                ", netPrice=" + netPrice +
                ", loanPrice=" + loanPrice +
                ", chargePrice=" + chargePrice +
                ", loanBank='" + loanBank + '\'' +
                ", loanType='" + loanType + '\'' +
                '}';
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    /**
     * areaName : 白云区
     * address : 测试
     * linkman : 测试人员
     * linkPhone : 13800138000
     * accountPhone : 13888888888
     * productId : 2
     * sender : 171
     * receiver : 4
     * evaluate : false
     */
    @SerializedName("areaName")
    private String areaName;
    @SerializedName("address")
    private String address;
    @SerializedName("linkman")
    private String linkman;
    @SerializedName("linkPhone")
    private String linkPhone;
    @SerializedName("accountPhone")
    private String accountPhone;
    @SerializedName("productId")
    private int productId;
    @SerializedName("sender")
    private int sender;
    @SerializedName("receiver")
    private int receiver;
    @SerializedName("evaluate")
    private boolean evaluate;
    @SerializedName("serviceAttitude")
    private float serviceAttitude;
    @SerializedName("professionalAbility")
    private float professionalAbility;
    @SerializedName("speed")
    private float speed;
    @SerializedName("gradeTime")
    private String gradeTime;
    @SerializedName("comment")
    private String comment;
    @SerializedName("actualPrice")//实际成交价
    private double actualPrice;
    @SerializedName("netPrice")//网签价
    private double netPrice;
    @SerializedName("loanPrice")//贷款金额
    private double loanPrice;
    @SerializedName("chargePrice")//收费金额
    private double chargePrice;
    @SerializedName("loanBank")//贷款银行
    private String loanBank;
    @SerializedName("loanType")//贷款类型
    private String loanType;


    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public double getLoanPrice() {
        return loanPrice;
    }

    public void setLoanPrice(double loanPrice) {
        this.loanPrice = loanPrice;
    }

    public double getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(double chargePrice) {
        this.chargePrice = chargePrice;
    }

    public String getLoanBank() {
        return loanBank;
    }

    public void setLoanBank(String loanBank) {
        this.loanBank = loanBank;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProductName() {
        return productName;
    }

    public String getNumber() {
        return number;
    }

    public String getRealName() {
        return realName;
    }

    public String getStatus() {
        return status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getIcon() {
        return icon;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public void setEvaluate(boolean evaluate) {
        this.evaluate = evaluate;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getAddress() {
        return address;
    }

    public String getLinkman() {
        return linkman;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public int getProductId() {
        return productId;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public boolean isEvaluate() {
        return evaluate;
    }


    public float getServiceAttitude() {
        return serviceAttitude;
    }

    public void setServiceAttitude(float serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
    }

    public float getProfessionalAbility() {
        return professionalAbility;
    }

    public void setProfessionalAbility(float professionalAbility) {
        this.professionalAbility = professionalAbility;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getGradeTime() {
        return gradeTime;
    }

    public void setGradeTime(String gradeTime) {
        this.gradeTime = gradeTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(boolean canTransfer) {
        this.canTransfer = canTransfer;
    }

}
