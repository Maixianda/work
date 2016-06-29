package com.gzliangce.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.gzliangce.entity.ProductsInfo;

import java.util.List;

/**
 * Created by leo on 16/1/20.
 * 全部参评
 */
public class AllProductDTO extends BaseDTO implements Parcelable {
    /**
     * id : 1
     * productName : 按揭贷款
     * icon : 1
     * sort : 1
     * url : 1
     * status : 0
     * children : [{"id":3,"productName":"公积金贷款","icon":"3","url":"1"}]
     */

    private List<ProductsInfo> list;

    protected AllProductDTO(Parcel in) {
        list = in.createTypedArrayList(ProductsInfo.CREATOR);
    }

    public static final Creator<AllProductDTO> CREATOR = new Creator<AllProductDTO>() {
        @Override
        public AllProductDTO createFromParcel(Parcel in) {
            return new AllProductDTO(in);
        }

        @Override
        public AllProductDTO[] newArray(int size) {
            return new AllProductDTO[size];
        }
    };

    public void setList(List<ProductsInfo> list) {
        this.list = list;
    }

    public List<ProductsInfo> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "AllProductDTO{" +
                "list=" + list +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }
}
