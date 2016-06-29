package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.ProductsInfo;

/**
 * Created by leo on 16/3/30.
 * 产品详情 - 实例
 */
public class ProductsDetailsDTO extends BaseDTO {

    /**
     * icon : 1.jpg
     * url : http://1.html
     * productName : name
     */

    @SerializedName("product")
    private ProductsInfo product;

    public ProductsInfo getProduct() {
        return product;
    }

    public void setProduct(ProductsInfo product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductsDetailsDTO{" +
                "product=" + product +
                '}';
    }
}
