package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by leo on 16/1/15.
 * 设置信息接口
 */
public class MetadataDTO extends BaseDTO implements Serializable {
    /**
     * 一期相关链接字段
     */
    @SerializedName("updateExplain")
    private String updateExplain;//版本更新说明URL
    @SerializedName("languages")
    private String languages;//
    @SerializedName("protocol")
    private String protocol;//隐私使用协议
    @SerializedName("disclaimer")
    private String disclaimer;//免责声明
    @SerializedName("liangce")
    private String liangce;//良策金融
    @SerializedName("partner")
    private String partner;//战略合作金融机构
    @SerializedName("serviceTel")
    private String serviceTel;//客服电话
    @SerializedName("business")
    private String business;//

    /**
     * 二期相关链接字段
     */
    @SerializedName("mediatorGuide")
    private String mediatorGuide;//中介操作指引
    @SerializedName("mortgageGuide")
    private String mortgageGuide;//按揭操作指引
    @SerializedName("simpleUserGuide")
    private String simpleUserGuide;//普通用户操作指引

    public String getRegisterGuide() {
        return registerGuide;
    }

    public void setRegisterGuide(String registerGuide) {
        this.registerGuide = registerGuide;
    }

    public String getMortgageGuide() {
        return mortgageGuide;
    }

    public void setMortgageGuide(String mortgageGuide) {
        this.mortgageGuide = mortgageGuide;
    }

    @Override
    public String toString() {
        return "MetadataDTO{" +
                "updateExplain='" + updateExplain + '\'' +
                ", languages='" + languages + '\'' +
                ", protocol='" + protocol + '\'' +
                ", disclaimer='" + disclaimer + '\'' +
                ", liangce='" + liangce + '\'' +
                ", partner='" + partner + '\'' +
                ", serviceTel='" + serviceTel + '\'' +
                ", business='" + business + '\'' +
                ", mediatorGuide='" + mediatorGuide + '\'' +
                ", mortgageGuide='" + mortgageGuide + '\'' +
                ", simpleUserGuide='" + simpleUserGuide + '\'' +
                ", registerGuide='" + registerGuide + '\'' +
                '}';
    }

    public String getSimpleUserGuide() {
        return simpleUserGuide;
    }

    public void setSimpleUserGuide(String simpleUserGuide) {
        this.simpleUserGuide = simpleUserGuide;
    }

    public String getMediatorGuide() {
        return mediatorGuide;
    }

    public void setMediatorGuide(String mediatorGuide) {
        this.mediatorGuide = mediatorGuide;
    }

    @SerializedName("registerGuide")

    private String registerGuide;//注册指引

    public void setUpdateExplain(String updateExplain) {
        this.updateExplain = updateExplain;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public void setLiangce(String liangce) {
        this.liangce = liangce;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getUpdateExplain() {
        return updateExplain;
    }

    public String getLanguages() {
        return languages;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getLiangce() {
        return liangce;
    }

    public String getPartner() {
        return partner;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public String getBusiness() {
        return business;
    }

}
