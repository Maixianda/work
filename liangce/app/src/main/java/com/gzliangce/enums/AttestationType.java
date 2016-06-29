package com.gzliangce.enums;

/**
 * Created by leo on 16/1/22.
 * 认证类型
 */
public enum AttestationType {
    noauth,//未认证
    check,//等待审核
    pass,//认证通过
    nopass//认证不通过
}
