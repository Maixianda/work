package com.gzliangce.dto;

import com.gzliangce.entity.ProductsInfo;

import java.util.List;

/**
 * Created by leo on 16/1/29.
 * 收藏的产品
 */
public class CollectionProductDTO extends BaseDTO {

    /**
     * id : 4
     * productName : 装修贷款
     * icon : http://jinrongqiao.oss-cn-shenzhen.aliyuncs.comhttp://www.yooyoo360.com/photo/2009-1-1/20090112111116425.jpg
     * url : http://www.gzliangce.com
     * children : []
     */

    private List<ProductsInfo> list;

    public void setList(List<ProductsInfo> list) {
        this.list = list;
    }

    public List<ProductsInfo> getList() {
        return list;
    }

}
