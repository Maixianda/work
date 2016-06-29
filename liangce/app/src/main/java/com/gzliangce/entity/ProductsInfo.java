package com.gzliangce.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.enums.MenuType;
import com.gzliangce.enums.ProductType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/1/20.
 * 产品类型详情
 */
public class ProductsInfo implements Parcelable {

    /**
     * id : 2
     * icon : xxxx
     * productName : 商业贷款
     * url : xxxx
     * children:
     */

    private int id;
    private String icon;
    @SerializedName("productName")
    private String productName;
    private String url;
    private String picUrl = "http://img4.duitang.com/uploads/item/201206/30/20120630131928_UQJjX.thumb.600_0.png";
    private List<ProductsInfo> children;
    //自定义字段
    private int iconRes;
    private String productType;
    private MenuType type;
    private boolean isBigType;

    public ProductsInfo() {
    }

    public ProductsInfo(String productName, int iconRes, MenuType type) {
        this.productName = productName;
        this.iconRes = iconRes;
        this.type = type;
    }

    public boolean isBigType() {
        return isBigType;
    }

    public void setBigType(boolean bigType) {
        isBigType = bigType;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public static Creator<ProductsInfo> getCREATOR() {
        return CREATOR;
    }

    public List<ProductsInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ProductsInfo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ProductsInfo{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", productName='" + productName + '\'' +
                ", url='" + url + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", iconRes=" + iconRes +
                ", productType='" + productType + '\'' +
                ", type=" + type +
                ", children=" + children +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.icon);
        dest.writeString(this.productName);
        dest.writeString(this.url);
        dest.writeString(this.picUrl);
        dest.writeInt(this.iconRes);
        dest.writeString(this.productType);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeTypedList(this.children);
    }

    protected ProductsInfo(Parcel in) {
        this.id = in.readInt();
        this.icon = in.readString();
        this.productName = in.readString();
        this.url = in.readString();
        this.picUrl = in.readString();
        this.iconRes = in.readInt();
        this.productType = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MenuType.values()[tmpType];
        this.children = in.createTypedArrayList(ProductsInfo.CREATOR);
    }

    public static final Creator<ProductsInfo> CREATOR = new Creator<ProductsInfo>() {
        @Override
        public ProductsInfo createFromParcel(Parcel source) {
            return new ProductsInfo(source);
        }

        @Override
        public ProductsInfo[] newArray(int size) {
            return new ProductsInfo[size];
        }
    };
}
