package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/2/29.
 * 二手房-普通&经济
 */
public class OldSimpleEntity implements Serializable {
    //卖家
    //"yinHuaShuiRate_sell": 0.1,//印花税利率-卖家
    //"yingYeShuiRate": 0.2,//营业税利率
    //"geRenSuoDeShuiRate":0.3,//个人所得税利率
    //"fangWuMaiMaiShouXuFeiRate_sell":0.4,//房屋买卖手续费-卖家

    //买家
    //"qiShuiRate":0.5,//契税利率
    //"yinHuaShuiRate_buy":0.6,//印花税利率
    //"gongBenYinHuaShuiRate":0.7,//工本印花税利率
    //"fangWuMaiMaiDengJiFeiRate":0.8,//房屋买卖登记费利率
    //"fangWuMaiMaiShouXuFeiRate_buy":0.9//房屋买卖手续费利率
    @SerializedName("yinHuaShuiRate_sell")
    private double yinHuaShuiRate_sell;
    @SerializedName("yingYeShuiRate")
    private double yingYeShuiRate;
    @SerializedName("geRenSuoDeShuiRate")
    private double geRenSuoDeShuiRate;
    @SerializedName("fangWuMaiMaiShouXuFeiRate_sell")
    private double fangWuMaiMaiShouXuFeiRate_sell;
    @SerializedName("qiShuiRate")
    private double qiShuiRate;
    @SerializedName("yinHuaShuiRate_buy")
    private double yinHuaShuiRate_buy;
    @SerializedName("gongBenYinHuaShuiRate")
    private double gongBenYinHuaShuiRate;
    @SerializedName("fangWuMaiMaiDengJiFeiRate")
    private double fangWuMaiMaiDengJiFeiRate;
    @SerializedName("zongHeDiJiaRate")
    private double zongHeDiJiaRate;
    @SerializedName("qiShuiRate_a")
    private double qiShuiRate_a;
    @SerializedName("qiShuiRate_b")
    private double qiShuiRate_b;
    @SerializedName("qiShuiRate_c")
    private double qiShuiRate_c;
    @SerializedName("fangWuMaiMaiShouXuFeiRate_buy")
    private double fangWuMaiMaiShouXuFeiRate_buy;


    public double getYinHuaShuiRate_sell() {
        return yinHuaShuiRate_sell;
    }

    public void setYinHuaShuiRate_sell(double yinHuaShuiRate_sell) {
        this.yinHuaShuiRate_sell = yinHuaShuiRate_sell;
    }

    public double getYingYeShuiRate() {
        return yingYeShuiRate;
    }

    public void setYingYeShuiRate(double yingYeShuiRate) {
        this.yingYeShuiRate = yingYeShuiRate;
    }

    public double getGeRenSuoDeShuiRate() {
        return geRenSuoDeShuiRate;
    }

    public void setGeRenSuoDeShuiRate(double geRenSuoDeShuiRate) {
        this.geRenSuoDeShuiRate = geRenSuoDeShuiRate;
    }

    public double getFangWuMaiMaiShouXuFeiRate_sell() {
        return fangWuMaiMaiShouXuFeiRate_sell;
    }

    public void setFangWuMaiMaiShouXuFeiRate_sell(double fangWuMaiMaiShouXuFeiRate_sell) {
        this.fangWuMaiMaiShouXuFeiRate_sell = fangWuMaiMaiShouXuFeiRate_sell;
    }

    public double getQiShuiRate() {
        return qiShuiRate;
    }

    public void setQiShuiRate(double qiShuiRate) {
        this.qiShuiRate = qiShuiRate;
    }

    public double getYinHuaShuiRate_buy() {
        return yinHuaShuiRate_buy;
    }

    public void setYinHuaShuiRate_buy(double yinHuaShuiRate_buy) {
        this.yinHuaShuiRate_buy = yinHuaShuiRate_buy;
    }

    public double getGongBenYinHuaShuiRate() {
        return gongBenYinHuaShuiRate;
    }

    public void setGongBenYinHuaShuiRate(double gongBenYinHuaShuiRate) {
        this.gongBenYinHuaShuiRate = gongBenYinHuaShuiRate;
    }

    public double getFangWuMaiMaiDengJiFeiRate() {
        return fangWuMaiMaiDengJiFeiRate;
    }

    public void setFangWuMaiMaiDengJiFeiRate(double fangWuMaiMaiDengJiFeiRate) {
        this.fangWuMaiMaiDengJiFeiRate = fangWuMaiMaiDengJiFeiRate;
    }

    public double getZongHeDiJiaRate() {
        return zongHeDiJiaRate;
    }

    public void setZongHeDiJiaRate(double zongHeDiJiaRate) {
        this.zongHeDiJiaRate = zongHeDiJiaRate;
    }

    public double getQiShuiRate_a() {
        return qiShuiRate_a;
    }

    public void setQiShuiRate_a(double qiShuiRate_a) {
        this.qiShuiRate_a = qiShuiRate_a;
    }

    public double getQiShuiRate_b() {
        return qiShuiRate_b;
    }

    public void setQiShuiRate_b(double qiShuiRate_b) {
        this.qiShuiRate_b = qiShuiRate_b;
    }

    public double getQiShuiRate_c() {
        return qiShuiRate_c;
    }

    public void setQiShuiRate_c(double qiShuiRate_c) {
        this.qiShuiRate_c = qiShuiRate_c;
    }

    public double getFangWuMaiMaiShouXuFeiRate_buy() {
        return fangWuMaiMaiShouXuFeiRate_buy;
    }

    public void setFangWuMaiMaiShouXuFeiRate_buy(double fangWuMaiMaiShouXuFeiRate_buy) {
        this.fangWuMaiMaiShouXuFeiRate_buy = fangWuMaiMaiShouXuFeiRate_buy;
    }

    @Override
    public String toString() {
        return "OldSimpleEntity{" +
                "yinHuaShuiRate_sell=" + yinHuaShuiRate_sell +
                ", yingYeShuiRate=" + yingYeShuiRate +
                ", geRenSuoDeShuiRate=" + geRenSuoDeShuiRate +
                ", fangWuMaiMaiShouXuFeiRate_sell=" + fangWuMaiMaiShouXuFeiRate_sell +
                ", qiShuiRate=" + qiShuiRate +
                ", yinHuaShuiRate_buy=" + yinHuaShuiRate_buy +
                ", gongBenYinHuaShuiRate=" + gongBenYinHuaShuiRate +
                ", fangWuMaiMaiDengJiFeiRate=" + fangWuMaiMaiDengJiFeiRate +
                ", zongHeDiJiaRate=" + zongHeDiJiaRate +
                ", qiShuiRate_a=" + qiShuiRate_a +
                ", qiShuiRate_b=" + qiShuiRate_b +
                ", qiShuiRate_c=" + qiShuiRate_c +
                ", fangWuMaiMaiShouXuFeiRate_buy=" + fangWuMaiMaiShouXuFeiRate_buy +
                '}';
    }
}
