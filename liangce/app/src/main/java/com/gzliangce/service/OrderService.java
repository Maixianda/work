package com.gzliangce.service;

import com.gzliangce.dto.AddOrderDTO;
import com.gzliangce.dto.AllProductDTO;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.HeaderDTO;
import com.gzliangce.dto.ImagesDTO;
import com.gzliangce.dto.ProductsDetailsDTO;
import com.gzliangce.dto.ProgressDTO;
import com.gzliangce.dto.SupplementDTO;
import com.gzliangce.dto.HomeProDuctDTO;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.dto.OrderDetailDTO;
import com.gzliangce.dto.UploadImageDTO;
import com.gzliangce.entity.AreaList;
import com.gzliangce.entity.Images;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.Supplement;
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
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by leo on 16/1/20.
 */
public interface OrderService {

    /**
     * 获取主页产品
     */
    @GET("app_public/v1/product/index.json")
    Call<HomeProDuctDTO> getHomeProducts();


    /**
     * 获取全部产品
     */
    @GET("app_public/v1/product/list.json")
    Call<AllProductDTO> getAllProducts();

    /**
     * 获取物业区域
     */
    @GET("app_public/v1/setting/area.json")
    Call<ListDTO<AreaList>> getPropertyArea();

    /**
     * 中介下单
     */
    @FormUrlEncoded
    @POST("app/v1/order/push.json")
    Call<BaseDTO> postPlaceAnOrder(@FieldMap Map<String, String> parameter);

    /**
     * 获取中介我的订单列表
     */
    @GET("app/v1/order/mediator/list.json")
    Call<ListDTO<OrderInfo>> getMediatorMyOrderList(@QueryMap Map<String, String> parameter);

    /**
     * 获取按揭我的订单列表
     */
    @GET("app/v1/order/mortgage/list.json")
    Call<ListDTO<OrderInfo>> getMortgageMyOrderList(@QueryMap Map<String, String> parameter);

    /**
     * 获取普通用户我的订单列表
     */
    @GET("app/v1/order/simpleUser/list.json")
    Call<ListDTO<OrderInfo>> getSimpleUserMyOrderList(@QueryMap Map<String, String> parameter);

    /**
     * 中介取消订单
     */
    @FormUrlEncoded
    @POST("app/v1/order/cancel.json")
    Call<BaseDTO> postCancelOrder(@Field("number") String orderNumber);

    /**
     * 按揭接单
     */
    @FormUrlEncoded
    @POST("app/v1/order/receive.json")
    Call<BaseDTO> postReceiveOrder(@Field("number") String orderNumber);

    /**
     * 按揭转单
     */
    @FormUrlEncoded
    @POST("app/v1/order/transfer.json")
    Call<BaseDTO> postTransferOrder(@Field("number") String orderNumber, @Field("targetId") String targetId, @Field("reason") String reason);

    /**
     * 订单详情
     */
    @GET("app/v1/order/detail.json")
    Call<OrderDetailDTO> getOrderDetail(@Query("number") String orderNumber);

    /**
     * 获取补充资料
     */
    @GET("app/v1/order/supplement.json")
    Call<SupplementDTO> getSupplement(@Query("number") String orderNumber);

    /**
     * 保存补充资料
     */
    @FormUrlEncoded
    @POST("app/v1/order/supplement/push.json")
    Call<BaseDTO> postSupplementData(@FieldMap Map<String, String> parameter);

    /**
     * 补充资料图片上传接口
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("app/v1/order/supplement/upload.json")
    Call<UploadImageDTO> uploadSupplementPic(@PartMap Map<String, RequestBody> params);

    /**
     * 补充资料图片删除
     */
    @FormUrlEncoded
    @POST("app/v1/order/supplement/image/delete.json")
    Call<BaseDTO> postDeletePic(@Field("number") String orderNumber, @Field("imageId") String imageId);

    /**
     * 获取进度查询
     */
    @GET("app/v1/order/progress.json")
    Call<ProgressDTO> getProgress(@Query("number") String orderNumber);

    /**
     * 中介评价订单
     */
    @FormUrlEncoded
    @POST("app/v1/order/grade/push.json")
    Call<BaseDTO> postEvaluationOrder(@FieldMap Map<String, String> parameter);

    /**
     * 获取签约拍照照片
     */
    @GET("app/v1/order/signed/upload.json")
    Call<ImagesDTO> getSignedPhoto(@Query("number") String orderNumber);

    /**
     * 获取签约拍照照片(上传照片界面)
     */
    @GET("app/v1/order/signed/image.json")
    Call<ImagesDTO> getSignedPhoto(@Query("number") String orderNumber, @Query("label") String label);

    /**
     * 签约拍照图片上传接口
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("app/v1/order/signed/upload.json")
    Call<UploadImageDTO> uploadSignPic(@PartMap Map<String, RequestBody> params);

    /**
     * 签约拍照图片删除
     */
    @FormUrlEncoded
    @POST("app/v1/order/signed/image/delete.json")
    Call<BaseDTO> postDeleteSignPic(@Field("number") String orderNumber, @Field("imageId") String imageId);

    /**
     * 获取文档资料的订单列表
     */
    @GET("app/v1/document/order/list.json")
    Call<ListDTO<OrderInfo>> getDocumentList(@QueryMap Map<String, String> parameter);

    /**
     * 获取文档资料图片列表
     */
    @GET("app/v1/order/document/list.json")
    Call<ListDTO<Images>> getDocumentPhotoList(@QueryMap Map<String, String> parameter);

    /**
     * 文档资料图片上传接口
     *
     * @param params
     * @return
     */
    @Multipart
    @POST("app/v1/order/document/upload.json")
    Call<UploadImageDTO> uploadDocumentPic(@PartMap Map<String, RequestBody> params);

    /**
     * 客户端添加订单
     *
     * @param orderNumber
     * @return
     */
    @FormUrlEncoded
    @POST("app/v1/order/relate.json")
    Call<AddOrderDTO> simpleUserAddOrder(@Field("number") String orderNumber, @Field("code") String code);

    /**
     * 获取签约拍照列表界面缩略图
     */
    @GET("app/v1/order/signed/label.json")
    Call<ImagesDTO> getSignedLabePhoto(@Query("number") String orderNumber);


    /**
     * 获取产品详情
     */
    @GET("app_public/v1/product/detail.json")
    Call<ProductsDetailsDTO> getProductdetail(@Query("productId") long productId);

}
