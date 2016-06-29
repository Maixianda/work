package com.gzliangce.entity;

import java.io.Serializable;

/**
 * Created by leo on 16/2/26.
 * 房产税费计算 - 数据对象
 */
public class HouseInfo implements Serializable {
    private int houseType;//房屋类型
    private int houseYear;//房屋年限
    private int houseAera;//房屋面积
    private int houseFee;//房屋单价
    private boolean isFristBuy;//是否是第一次购买
    private boolean isOnlyHouse;//是否是唯一住房
    private int originalPrice;//房屋原价
    private boolean valuationType;//计价方式

    private double qiShuiRate;//契税税率
    private double gongZhengFeiRate;//公证费税率
    private double yinHuaShuiRate;//印花税税率
    private double weiTuoBanLiRate;//委托办理产权费利率
    private double maiMaiRata;//房屋买卖手续费利率

    private double oldHouseYinHuaShuiRate_sell;//卖家印花税税率
    private double oldHouseYinHuaShuiRate_buy;//买家印花税税率
    private double oldHouseYingYeShuiRate;//卖家营业税
    private double oldHouseZongHeDiJiaRate;//综合地价税
    private double oldHouseFangWuMaiMaiShouXuFeiRate_buy;//买家房屋买卖手续费
    private double oldHouseFangWuMaiMaiShouXuFeiRate_sell;//卖家房屋买卖手续费
    private double oldHouseGongBenYinHuaShuiRate;//工本印花税
    private double oldHouseFangWuMaiMaiDengJiFeiRate;//卖家房屋买卖登记费
    private double oldHouseGeRenShuoDeShuiRate;//个人所得税

    public double getOldHouseYinHuaShuiRate_sell() {
        return oldHouseYinHuaShuiRate_sell;
    }

    public void setOldHouseYinHuaShuiRate_sell(double oldHouseYinHuaShuiRate_sell) {
        this.oldHouseYinHuaShuiRate_sell = oldHouseYinHuaShuiRate_sell;
    }

    public double getOldHouseYinHuaShuiRate_buy() {
        return oldHouseYinHuaShuiRate_buy;
    }

    public void setOldHouseYinHuaShuiRate_buy(double oldHouseYinHuaShuiRate_buy) {
        this.oldHouseYinHuaShuiRate_buy = oldHouseYinHuaShuiRate_buy;
    }

    public double getOldHouseYingYeShuiRate() {
        return oldHouseYingYeShuiRate;
    }

    public void setOldHouseYingYeShuiRate(double oldHouseYingYeShuiRate) {
        this.oldHouseYingYeShuiRate = oldHouseYingYeShuiRate;
    }

    public double getOldHouseZongHeDiJiaRate() {
        return oldHouseZongHeDiJiaRate;
    }

    public void setOldHouseZongHeDiJiaRate(double oldHouseZongHeDiJiaRate) {
        this.oldHouseZongHeDiJiaRate = oldHouseZongHeDiJiaRate;
    }

    public double getOldHouseFangWuMaiMaiShouXuFeiRate_buy() {
        return oldHouseFangWuMaiMaiShouXuFeiRate_buy;
    }

    public void setOldHouseFangWuMaiMaiShouXuFeiRate_buy(double oldHouseFangWuMaiMaiShouXuFeiRate_buy) {
        this.oldHouseFangWuMaiMaiShouXuFeiRate_buy = oldHouseFangWuMaiMaiShouXuFeiRate_buy;
    }

    public double getOldHouseFangWuMaiMaiShouXuFeiRate_sell() {
        return oldHouseFangWuMaiMaiShouXuFeiRate_sell;
    }

    public void setOldHouseFangWuMaiMaiShouXuFeiRate_sell(double oldHouseFangWuMaiMaiShouXuFeiRate_sell) {
        this.oldHouseFangWuMaiMaiShouXuFeiRate_sell = oldHouseFangWuMaiMaiShouXuFeiRate_sell;
    }

    public double getOldHouseGongBenYinHuaShuiRate() {
        return oldHouseGongBenYinHuaShuiRate;
    }

    public void setOldHouseGongBenYinHuaShuiRate(double oldHouseGongBenYinHuaShuiRate) {
        this.oldHouseGongBenYinHuaShuiRate = oldHouseGongBenYinHuaShuiRate;
    }

    public double getOldHouseFangWuMaiMaiDengJiFeiRate() {
        return oldHouseFangWuMaiMaiDengJiFeiRate;
    }

    public void setOldHouseFangWuMaiMaiDengJiFeiRate(double oldHouseFangWuMaiMaiDengJiFeiRate) {
        this.oldHouseFangWuMaiMaiDengJiFeiRate = oldHouseFangWuMaiMaiDengJiFeiRate;
    }

    public double getOldHouseGeRenShuoDeShuiRate() {
        return oldHouseGeRenShuoDeShuiRate;
    }

    public void setOldHouseGeRenShuoDeShuiRate(double oldHouseGeRenShuoDeShuiRate) {
        this.oldHouseGeRenShuoDeShuiRate = oldHouseGeRenShuoDeShuiRate;
    }

    public double getQiShuiRate() {
        return qiShuiRate;
    }

    public void setQiShuiRate(double qiShuiRate) {
        this.qiShuiRate = qiShuiRate;
    }

    public double getGongZhengFeiRate() {
        return gongZhengFeiRate;
    }

    public void setGongZhengFeiRate(double gongZhengFeiRate) {
        this.gongZhengFeiRate = gongZhengFeiRate;
    }

    public double getYinHuaShuiRate() {
        return yinHuaShuiRate;
    }

    public void setYinHuaShuiRate(double yinHuaShuiRate) {
        this.yinHuaShuiRate = yinHuaShuiRate;
    }

    public double getWeiTuoBanLiRate() {
        return weiTuoBanLiRate;
    }

    public void setWeiTuoBanLiRate(double weiTuoBanLiRate) {
        this.weiTuoBanLiRate = weiTuoBanLiRate;
    }

    public double getMaiMaiRata() {
        return maiMaiRata;
    }

    public void setMaiMaiRata(double maiMaiRata) {
        this.maiMaiRata = maiMaiRata;
    }

    public int getHouseType() {
        return houseType;
    }

    public void setHouseType(int houseType) {
        this.houseType = houseType;
    }

    public int getHouseYear() {
        return houseYear;
    }

    public void setHouseYear(int houseYear) {
        this.houseYear = houseYear;
    }

    public int getHouseAera() {
        return houseAera;
    }

    public void setHouseAera(int houseAera) {
        this.houseAera = houseAera;
    }

    public int getHouseFee() {
        return houseFee;
    }

    public void setHouseFee(int houseFee) {
        this.houseFee = houseFee;
    }

    public boolean isFristBuy() {
        return isFristBuy;
    }

    public void setFristBuy(boolean fristBuy) {
        isFristBuy = fristBuy;
    }

    public boolean isOnlyHouse() {
        return isOnlyHouse;
    }

    public void setOnlyHouse(boolean onlyHouse) {
        isOnlyHouse = onlyHouse;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public boolean isValuationType() {
        return valuationType;
    }

    public void setValuationType(boolean valuationType) {
        this.valuationType = valuationType;
    }

}
