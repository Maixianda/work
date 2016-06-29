package com.gzliangce.service;

import com.gzliangce.dto.BannerDTO;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.IsEnrollDTO;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.dto.MessageCenterDTO;
import com.gzliangce.dto.NewsDetailDTO;
import com.gzliangce.dto.RateListDTO;
import com.gzliangce.dto.SupplementDTO;
import com.gzliangce.dto.TutorDTO;
import com.gzliangce.dto.TypeListDTO;
import com.gzliangce.dto.VersionDTO;
import com.gzliangce.entity.CompanyInfo;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsInfo;
import com.gzliangce.entity.NewsTypeInfo;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by leo on 16/2/23.
 */
public interface OtherDataService {
    /**
     * 获取产品额外数据
     */
    @GET("app/v1/product/detail/supplement.json")
    Call<SupplementDTO> getSupplementData(@Query("productId") long productId);


    /**
     * 获取广告数据
     *
     * @param location
     * @param port
     */
    @GET("app_public/v1/banner/pull.json")
    Call<BannerDTO> getBannerData(@Query("port") String port, @Query("location") String location);


    /**
     * 获取消息中心数据
     *
     * @param page
     * @param size
     */
    @GET("app/v1/message/list.json")
    Call<MessageCenterDTO> getMessageCenterData(@Query("page") int page, @Query("size") int size);

    /**
     * 利率数据
     */
    @GET("app_public/v1/calc/rate/list.json")
    Call<RateListDTO> getRateData();


    /**
     * 获取未登录资讯类型
     */
    @GET("app_public/v1/news/type/get.json")
    Call<ListDTO<NewsTypeInfo>> getNoLoginNewsType();


    /**
     * 获取登录用户资讯类型
     */
    @GET("app/v1/news/type/get.json")
    Call<ListDTO<NewsTypeInfo>> getLoginNewsType();

    /**
     * 获取资讯列表
     */
    @GET("app_public/v1/news/list.json ")
    Call<ListDTO<NewsInfo>> getNewsListData(@Query("type") int type, @Query("page") int page, @Query("size") int size);

    /**
     * 获取资讯列表
     */
    @GET("app_public/v1/news/list.json ")
    Call<ListDTO<NewsInfo>> getNewsListData(@QueryMap Map<String, String> parameter);

    /**
     * 获取资讯详情
     */
    @GET("app_public/v1/news/detail.json")
    Call<NewsDetailDTO> getNewsDetail(@Query("id") long id);

    /**
     * 获取登录用户学院首页
     */
    @GET("app/v1/college/index.json")
    Call<TypeListDTO> getLoginCollegeHome();

    /**
     * 获取未登录学院首页
     */
    @GET("app_public/v1/college/index.json")
    Call<TypeListDTO> getUnLoginCollegeHome();

    /**
     * 登录 - 学院根据typeid获取课程列表
     */
    @GET("app/v1/college/course/list.json")
    Call<ListDTO<CourseInfo>> getMyCourseListData(@QueryMap Map<String, String> parameter);


    /**
     * 未登录 - 学院根据typeid获取课程列表
     */
    @GET("app_public/v1/college/course/list.json ")
    Call<ListDTO<CourseInfo>> getMyCourseListNoLoginData(@QueryMap Map<String, String> parameter);

    /**
     * 获取我的课程列表
     */
    @GET("/app/v1/college/course/myEnroll.json")
    Call<ListDTO<CourseInfo>> getMyCourseListData(@Query("page") int page, @Query("size") int size);


    /**
     * 检查是否已经报名
     */
    @FormUrlEncoded
    @POST("app/v1/college/course/checkEnroll.json")
    Call<IsEnrollDTO> getCourseCheckEnroll(@Field("id") int id);

    /**
     * 报名
     */
    @FormUrlEncoded
    @POST("app/v1/college/course/enroll.json")
    Call<BaseDTO> postCourseEnroll(@Field("id") int id);

    /**
     * 取消报名
     */
    @FormUrlEncoded
    @POST("app/v1/college/course/unEnroll.json")
    Call<BaseDTO> postCourseUnEnroll(@Field("id") int id);


    /**
     * 获取导师详情
     */
    @GET("app_public/v1/college/course/tutor.json")
    Call<TutorDTO> getTutorData(@Query("id") String id);

    /**
     * 获取机构和分行列表
     */
    @GET("app/v2/mediator/auth/organization/list.json")
    Call<ListDTO<CompanyInfo>> getOrganizationData();

    /**
     * 获取版本信息
     */
    @GET("app_public/v1/setting/version.json?os=android")
    Call<VersionDTO> getVersion();
}
