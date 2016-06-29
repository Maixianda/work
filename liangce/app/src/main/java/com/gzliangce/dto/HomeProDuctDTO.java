package com.gzliangce.dto;

import com.gzliangce.entity.ProductsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/1/20.
 * 主页产品详情
 */
public class HomeProDuctDTO extends BaseDTO implements Serializable {

    /**
     * id : 3
     * icon : http://www.yooyoo360.com/photo/2009-1-1/20090112111116425.jpg
     * productName : 公积金贷款
     * url : http://www.gzliangce.com
     */
    private List<ProductsInfo> list;


    public void setList(List<ProductsInfo> list) {
        this.list = list;
    }


    public List<ProductsInfo> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "HomeProDuctDTO{" +
                "list=" + list +
                '}';
    }
}
