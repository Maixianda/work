package com.gzliangce.util;

import com.gzliangce.dto.BaseDTO;
import com.gzliangce.http.API;
import com.gzliangce.http.APICallback;
import com.gzliangce.service.AttestationService;
import com.gzliangce.service.OrderService;
import com.gzliangce.service.OtherDataService;
import com.gzliangce.service.UserCenterService;

import retrofit.Call;

/**
 * Created by leo on 16/1/16.
 * Api请求工具类
 */
public class ApiUtil {
    private static AttestationService attestationService;
    private static OrderService orderService;
    private static UserCenterService userCenterService;
    private static OtherDataService otherDataService;

    /**
     * 获取一个AttestationService对象
     */
    public static AttestationService getAttestation() {
        if (attestationService == null) {
            attestationService = API.of(AttestationService.class);
        }
        return attestationService;
    }


    /**
     * 获取一个OrderService对象
     */
    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = API.of(OrderService.class);
        }
        return orderService;
    }


    /**
     * 验证验证码是否正确
     */
    public static void postValidateSms(String phone, String code, APICallback callback) {
        Call<BaseDTO> call = ApiUtil.getAttestation().postValidateSms(phone, code);
        call.enqueue(callback);
    }

    /**
     * 获取一个UserCenterService对象
     */
    public static UserCenterService getUserCenterService() {
        if (userCenterService == null) {
            userCenterService = API.of(UserCenterService.class);
        }
        return userCenterService;
    }


    /**
     * 获取一个OtherDataService对象
     */
    public static OtherDataService getOtherDataService() {
        if (otherDataService == null) {
            otherDataService = API.of(OtherDataService.class);
        }
        return otherDataService;
    }
}
