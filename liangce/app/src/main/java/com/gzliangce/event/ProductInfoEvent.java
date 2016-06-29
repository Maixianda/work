package com.gzliangce.event;

import com.gzliangce.entity.ProductsInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/4/1.
 * 产品event
 */
public class ProductInfoEvent implements Event {
    private ProductsInfo productsInfo;

    public ProductInfoEvent(ProductsInfo productsInfo) {
        this.productsInfo = productsInfo;
    }

    public ProductsInfo getProductsInfo() {
        return productsInfo;
    }

    public void setProductsInfo(ProductsInfo productsInfo) {
        this.productsInfo = productsInfo;
    }

    @Override
    public String toString() {
        return "ProductInfoEvent{" +
                "productsInfo=" + productsInfo +
                '}';
    }
}
