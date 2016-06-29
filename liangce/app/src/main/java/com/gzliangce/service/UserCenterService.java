package com.gzliangce.service;

import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.ChatInfoDTO;
import com.gzliangce.dto.CollectionBrokeDTO;
import com.gzliangce.dto.CollectionProductDTO;
import com.gzliangce.dto.HeaderDTO;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.dto.MortgageUserDTO;
import com.gzliangce.dto.MortgageUserListDTO;
import com.gzliangce.dto.ProgressDTO;
import com.gzliangce.dto.StaffDTO;
import com.gzliangce.dto.UserDTO;
import com.gzliangce.dto.VerifyCodeDTO;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.OrderInfo;
import com.squareup.okhttp.RequestBody;

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
import retrofit.http.QueryMap;

/**
 * Created by leo on 16/1/11.
 */
public interface UserCenterService {

    /**
     * 获取用户资料接口
     *
     * @return
     */
    @POST("app/v1/setting/personal.json")
    Call<UserDTO> getUserInfo();

    /**
     * 头像上传接口
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("app/v1/setting/upload/icon.json")
    Call<HeaderDTO> uploadPic(@PartMap Map<String, RequestBody> params);

    /**
     * 修改用户名接口
     *
     * @param realName
     * @return
     */
    @FormUrlEncoded
    @POST("app/v1/setting/realName/modify.json")
    Call<BaseDTO> postUserName(@Field("realName") String realName);

    /**
     * 修改用户名接口
     *
     * @param parameter
     * @return
     */
    @FormUrlEncoded
    @POST("app/v1/setting/mortgage/modify.json")
    Call<BaseDTO> postUserDatum(@FieldMap() Map<String, String> parameter);

    /**
     * 获取按揭员列表
     */
    @GET("app_public/v1/mortgage/list.json")
    Call<ListDTO<BrokeInfo>> getMortgageUserList(@QueryMap Map<String, String> parameter);

    /**
     * 获取按揭员详情
     *
     * @param mortgage
     */
    @GET("app_public/v1/mortgage/detail.json")
    Call<MortgageUserDTO> getMortgageUserData(@Query("mortgage") long mortgage);


    /**
     * 获取经纪人收藏列表
     *
     * @param type
     */
    @GET("app/v1/favorite/list.json")
    Call<CollectionBrokeDTO> getCollectionBrokerList(@Query("type") String type);


    /**
     * 获取收藏列表
     *
     * @param type
     */
    @GET("app/v1/favorite/list.json")
    Call<CollectionProductDTO> getCollectionProductList(@Query("type") String type);

    /**
     * 收藏金融经纪
     *
     * @param type
     */
    @FormUrlEncoded
    @POST("app/v1/favorite/push.json")
    Call<BaseDTO> postCollectionMortgage(@Field("type") String type, @Field("targetId") String targetId);

    /**
     * 收藏产品
     *
     * @param type
     */
    @FormUrlEncoded
    @POST("app/v1/favorite/push.json")
    Call<BaseDTO> postProduct(@Field("type") String type, @Field("productId") String productId);

    /**
     * 取消收藏产品
     *
     * @param productId
     */
    @FormUrlEncoded
    @POST("app/v1/favorite/cancel.json")
    Call<BaseDTO> postDeleteProduct(@Field("productId") String productId);

    /**
     * 取消金融经纪收藏
     *
     * @param targetId
     */
    @FormUrlEncoded
    @POST("app/v1/favorite/cancel.json")
    Call<BaseDTO> postDeleteMortgage(@Field("targetId") String targetId);

    /**
     * 获取按揭地图列表
     */
    @GET("app_public/v1/mortgage/map/list.json")
    Call<ListDTO<BrokeInfo>> getMortgageMap(@QueryMap Map<String, String> parameter);

    /**
     * 获取在线客服Client_Id
     */
    @GET("app_public/v1/mortgage/staff/get.json")
    Call<StaffDTO> getOnlineStaff();

    /**
     * 更新按揭员位置信息
     */
    @FormUrlEncoded
    @POST("app/v1/mortgage/location/push.json")
    Call<BaseDTO> updateBrokerLocation(@Field("lat") double lat, @Field("lon") double lon);

    /**
     * 获取对话窗基本信息
     */
    @GET("app_public/v1/chat/data.json")
    Call<ChatInfoDTO> getChatInfo(@Query("accountId") String accountId);


}
