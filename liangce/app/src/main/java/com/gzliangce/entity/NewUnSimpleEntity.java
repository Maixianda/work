package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/2/29.
 * 新房-非普通
 */
public class NewUnSimpleEntity implements Serializable {
    //qiShuiRate": 0.1,//契税利率
    //"yinHuaShuiRate":0.2,//印花税利率
    //"gongZhengFeiRate_a":0.3,//公证费利率<50w
    //"gongZhengFeiRate_b":0.4,//<=50公证费利率<500w
    //"gongZhengFeiRate_c":0.5,//<=500公证费利率<1000w
    //"gongZhengFeiRate_d":0.6,//<=1000公证费利率<2000w
    //"gongZhengFeiRate_e":0.7,//<=2000公证费利率<5000w
    //"gongZhengFeiRate_f":0.8,//<=5000公证费利率<1y
    //"gongZhengFeiRate_g":0.9,//公证费利率>1y
    //"weiTuoBanLiRate":1,//委托办理利率
    //"maiMaiRate":1.1//房屋买卖手续费利率
    @SerializedName("qiShuiRate")
    private double qiShuiRate;
    @SerializedName("yinHuaShuiRate")
    private double yinHuaShuiRate;
    @SerializedName("gongZhengFeiRate_a")
    private double gongZhengFeiRate_a;
    @SerializedName("gongZhengFeiRate_b")
    private double gongZhengFeiRate_b;
    @SerializedName("gongZhengFeiRate_c")
    private double gongZhengFeiRate_c;
    @SerializedName("gongZhengFeiRate_d")
    private double gongZhengFeiRate_d;
    @SerializedName("gongZhengFeiRate_e")
    private double gongZhengFeiRate_e;
    @SerializedName("gongZhengFeiRate_f")
    private double gongZhengFeiRate_f;
    @SerializedName("gongZhengFeiRate_g")
    private double gongZhengFeiRate_g;
    @SerializedName("weiTuoBanLiRate")
    private int weiTuoBanLiRate;
    @SerializedName("maiMaiRate")
    private double maiMaiRate;

    public double getQiShuiRate() {
        return qiShuiRate;
    }

    public void setQiShuiRate(double qiShuiRate) {
        this.qiShuiRate = qiShuiRate;
    }

    public double getYinHuaShuiRate() {
        return yinHuaShuiRate;
    }

    public void setYinHuaShuiRate(double yinHuaShuiRate) {
        this.yinHuaShuiRate = yinHuaShuiRate;
    }

    public double getGongZhengFeiRate_a() {
        return gongZhengFeiRate_a;
    }

    public void setGongZhengFeiRate_a(double gongZhengFeiRate_a) {
        this.gongZhengFeiRate_a = gongZhengFeiRate_a;
    }

    public double getGongZhengFeiRate_b() {
        return gongZhengFeiRate_b;
    }

    public void setGongZhengFeiRate_b(double gongZhengFeiRate_b) {
        this.gongZhengFeiRate_b = gongZhengFeiRate_b;
    }

    public double getGongZhengFeiRate_c() {
        return gongZhengFeiRate_c;
    }

    public void setGongZhengFeiRate_c(double gongZhengFeiRate_c) {
        this.gongZhengFeiRate_c = gongZhengFeiRate_c;
    }

    public double getGongZhengFeiRate_d() {
        return gongZhengFeiRate_d;
    }

    public void setGongZhengFeiRate_d(double gongZhengFeiRate_d) {
        this.gongZhengFeiRate_d = gongZhengFeiRate_d;
    }

    public double getGongZhengFeiRate_e() {
        return gongZhengFeiRate_e;
    }

    public void setGongZhengFeiRate_e(double gongZhengFeiRate_e) {
        this.gongZhengFeiRate_e = gongZhengFeiRate_e;
    }

    public double getGongZhengFeiRate_f() {
        return gongZhengFeiRate_f;
    }

    public void setGongZhengFeiRate_f(double gongZhengFeiRate_f) {
        this.gongZhengFeiRate_f = gongZhengFeiRate_f;
    }

    public double getGongZhengFeiRate_g() {
        return gongZhengFeiRate_g;
    }

    public void setGongZhengFeiRate_g(double gongZhengFeiRate_g) {
        this.gongZhengFeiRate_g = gongZhengFeiRate_g;
    }

    public int getWeiTuoBanLiRate() {
        return weiTuoBanLiRate;
    }

    public void setWeiTuoBanLiRate(int weiTuoBanLiRate) {
        this.weiTuoBanLiRate = weiTuoBanLiRate;
    }

    public double getMaiMaiRate() {
        return maiMaiRate;
    }

    public void setMaiMaiRate(double maiMaiRate) {
        this.maiMaiRate = maiMaiRate;
    }

    @Override
    public String toString() {
        return "NewUnSimpleEntity{" +
                "qiShuiRate=" + qiShuiRate +
                ", yinHuaShuiRate=" + yinHuaShuiRate +
                ", gongZhengFeiRate_a=" + gongZhengFeiRate_a +
                ", gongZhengFeiRate_b=" + gongZhengFeiRate_b +
                ", gongZhengFeiRate_c=" + gongZhengFeiRate_c +
                ", gongZhengFeiRate_d=" + gongZhengFeiRate_d +
                ", gongZhengFeiRate_e=" + gongZhengFeiRate_e +
                ", gongZhengFeiRate_f=" + gongZhengFeiRate_f +
                ", gongZhengFeiRate_g=" + gongZhengFeiRate_g +
                ", weiTuoBanLiRate=" + weiTuoBanLiRate +
                ", maiMaiRate=" + maiMaiRate +
                '}';
    }
}
