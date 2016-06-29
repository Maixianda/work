package com.gzliangce.service;

import com.gzliangce.dto.AuthUploadDTO;
import com.gzliangce.dto.BannerDTO;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.HeaderDTO;
import com.gzliangce.dto.HomeProDuctDTO;
import com.gzliangce.dto.MetadataDTO;
import com.gzliangce.dto.UserDTO;
import com.gzliangce.dto.VerifyCodeDTO;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;

/**
 * Created by leo on 16/1/11.
 */
public interface AttestationService {


    /**
     * 获取注册验证码
     *
     * @param deviceId
     */
    @FormUrlEncoded
    @POST("app_public/v1/reg/verifyCode/get.json")
    Call<VerifyCodeDTO> getVerifyCode(@Field("deviceId") String deviceId);


    /**
     * 获取设置相关数据
     *
     * @param deviceId
     */
    @FormUrlEncoded
    @POST("app_public/v1/setting/metadata.json")
    Call<MetadataDTO> getMtadata(@Field("deviceId") String deviceId);


    /**
     * 获取注册验证码
     *
     * @param deviceId
     * @param phone
     * @param verifyCode
     */
    @FormUrlEncoded
    @POST("app_public/v1/reg/sms/send.json")
    Call<BaseDTO> getSmsCode(@Field("deviceId") String deviceId, @Field("phone") String phone, @Field("verifyCode") String verifyCode);

    /**
     * 登录
     *
     * @param os
     * @param phone
     * @param password
     * @param deviceId
     */
    @FormUrlEncoded
    @POST("app_public/v1/login.json")
    Call<UserDTO> postLogin(@Field("os") String os, @Field("deviceId") String deviceId, @Field("phone") String phone, @Field("password") String password);


    /**
     * 验证手机号是否已经注册
     *
     * @param phone
     */
    @FormUrlEncoded
    @POST("app_public/v1/reg/sms/send.json")
    Call<BaseDTO> postValidatePhone(@Field("phone") String phone);//没用到，因为获取手机验证码时就会判断手机是否已经注册


    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     */
    @FormUrlEncoded
    @POST("app_public/v1/validateSms.json")
    Call<BaseDTO> postValidateSms(@Field("phone") String phone, @Field("code") String code);


    /**
     * 注册接口
     *
     * @param parameter
     */
    @FormUrlEncoded
    @POST("app_public/v1/reg.json")
    Call<BaseDTO> postRegister(@FieldMap Map<String, String> parameter);


    /**
     * 获取重置密码验证码
     *
     * @param phone
     */
    @FormUrlEncoded
    @POST("app_public/v1/forget/sms/send.json")
    Call<BaseDTO> getForgotSmsCode(@Field("phone") String phone);


    /**
     * 重置密码
     *
     * @param phone
     * @param password
     * @param code
     */
    @FormUrlEncoded
    @POST("app_public/v1/forget/change.json")
    Call<BaseDTO> postResetPassword(@Field("phone") String phone, @Field("password") String password, @Field("code") String code);


    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     */
    @FormUrlEncoded
    @POST("app/v1/setting/password/change.json")
    Call<BaseDTO> postEditPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);


    /**
     * 资格认证上传图片接口
     *
     * @param params
     */
    @Multipart
    @POST("app/v1/mediator/auth/upload.json")
    Call<AuthUploadDTO> posAuthtUpLoadImage(@PartMap Map<String, RequestBody> params);


    /**
     * 资格认证接口
     *
     * @param parameter
     */
    @FormUrlEncoded
    @POST("app/v1/mediator/auth.json")
    Call<BaseDTO> postMediatorAuthData(@FieldMap Map<String, String> parameter);


    /**
     * 获取主页产品
     */
    @POST("app/v1/setting/personal.json")
    Call<UserDTO> getUserInfo();
}
