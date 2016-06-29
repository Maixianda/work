package com.gzliangce.util;

import android.content.Context;

import com.gzliangce.bean.Constants;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.dto.MetadataDTO;
import com.gzliangce.dto.RateListDTO;
import com.gzliangce.entity.AreaList;
import com.gzliangce.entity.InterestrateInfo;
import com.gzliangce.entity.RateListInfo;
import com.gzliangce.http.API;
import com.gzliangce.http.APICallback;
import com.gzliangce.service.AttestationService;
import com.gzliangce.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/1/15.
 * 获取固定数据相关工具类，例如一些说明文档链接
 */
public class MetadataUtil {
    private Logger logger = LoggerFactory.getLogger(MetadataUtil.class);
    private static MetadataUtil metadataUtil;
    private AttestationService attestationService;
    private OrderService orderService;
    private static RateListInfo rateListInfo;

    /**
     * 单例模式
     */
    public static MetadataUtil getInstance() {
        if (metadataUtil == null) {
            metadataUtil = new MetadataUtil();
        }
        return metadataUtil;
    }

    /**
     * 获取一个网络请求对象
     */
    private AttestationService getService() {
        if (attestationService == null) {
            attestationService = API.of(AttestationService.class);
        }
        return attestationService;
    }


    /**
     * 获取一个网络请求对象
     */
    private OrderService getOrderService() {
        if (orderService == null) {
            orderService = API.of(OrderService.class);
        }
        return orderService;
    }


    /**
     * 获取Metadata数据
     */
    public void getMetadata(final Context context) {
        Call<MetadataDTO> call = getService().getMtadata("");
        call.enqueue(new APICallback<MetadataDTO>() {
            @Override
            public void onSuccess(MetadataDTO metadataDTO) {
                logger.i("metadataDTO----获取数据成功" + metadataDTO.toString());
                putGCache(context, Constants.METADATA_DATA_KEY, metadataDTO);
            }

            @Override
            public void onFailed(String message) {
                logger.i("metadataDTO获取数据失败:" + message);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 获取物业区域数据
     */
    public void getAreaData(final Context context, final APICallback<ListDTO<AreaList>> apiCallback) {
        Call<ListDTO<AreaList>> call = getOrderService().getPropertyArea();
        call.enqueue(new APICallback<ListDTO<AreaList>>() {
            @Override
            public void onSuccess(ListDTO<AreaList> areaDTO) {
                putAreaGCache(context, areaDTO);
                if (apiCallback != null) {
                    apiCallback.onSuccess(areaDTO);
                }
            }

            @Override
            public void onFailed(String message) {
                if (apiCallback != null) {
                    apiCallback.onFailed(message);
                }
            }

            @Override
            public void onFinish() {
                if (apiCallback != null) {
                    apiCallback.onFinish();
                }
            }
        });
    }

    /**
     * 获取利率相关数据
     */
    public void getRateData(final Context context) {
        Call<RateListDTO> call = ApiUtil.getOtherDataService().getRateData();
        call.enqueue(new APICallback<RateListDTO>() {
            @Override
            public void onSuccess(RateListDTO rateListDTO) {
                logger.e("rateListDTO-----" + rateListDTO.toString());
                putGCache(context, Constants.RATE_LIST_DATA, rateListDTO);
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * @return
     */
    public static Object getGCache(Context context, String key) {
        Object dto = CacheManager.disk(context).get(key);
        return dto;
    }

    /**
     * @param value
     */
    public static void putGCache(Context context, String key, Object value) {
        CacheManager.disk(context).put(key, value);
    }


    /**
     * @param value
     */
    public static void putAreaGCache(Context context, ListDTO<AreaList> value) {
        CacheManager.disk(context).put(Constants.PROPERTY_AREA_KEY, value);
    }

    /**
     * @return
     */
    public static ListDTO<AreaList> getAreaCache(Context context) {
        ListDTO<AreaList> dto = (ListDTO<AreaList>) CacheManager.disk(context).get(Constants.PROPERTY_AREA_KEY);
        return dto;
    }

    /**
     * 获取公积金利率
     *
     * @param isFundRate 是否是公积金利率
     */
    public static List<InterestrateInfo> getRate(Context context, boolean isFundRate) {
        List<InterestrateInfo> list = new ArrayList<>();
        RateListDTO dto = (RateListDTO) getGCache(context, Constants.RATE_LIST_DATA);
        if (dto != null) {
            for (RateListInfo info : dto.getRateList()) {
                InterestrateInfo interestrateInfo = new InterestrateInfo();
                interestrateInfo.setInterestrateDate(info.getDescription());
                interestrateInfo.setRateDate(info.getRateDate());
                if (isFundRate) {
                    double fundRate = MathUtils.div(info.getFundRate(), 100, 4);
                    interestrateInfo.setInterestrateDiscount(fundRate);
                    interestrateInfo.setInterestrate(fundRate);
                } else {
                    double buiness = MathUtils.div(info.getBusinessRate(), 100, 4);
                    interestrateInfo.setInterestrateDiscount(buiness);
                    interestrateInfo.setInterestrate(buiness);
                }
                list.add(interestrateInfo);
            }
        }
        return list;
    }


    /**
     * 获取利率列表
     */
    public static List<RateListInfo> getHouseRate(Context context) {
        List<RateListInfo> list = new ArrayList<>();
        RateListDTO dto = (RateListDTO) getGCache(context, Constants.RATE_LIST_DATA);
        list.addAll(dto.getRateList());
        return list;
    }

    /**
     * 获取房税利率
     */
    public static RateListInfo getReteListInfo(Context context) {
        if (rateListInfo == null) {
            List<RateListInfo> list = getHouseRate(context);
            if (list.size() > 0) {
                rateListInfo = list.get(0);
            }
        }
        return rateListInfo;
    }

    /**
     * 在线客服id写入cache
     *
     * @param value
     */
    public static void putStaff(Context context, String value) {
        CacheManager.disk(context).put(Constants.STAFF_ID, value);
    }

    /**
     * 从cache中获取在线客服Id
     *
     * @return
     */
    public static String getStaffCache(Context context) {
        String staffId = (String) CacheManager.disk(context).get(Constants.STAFF_ID);
        return staffId;
    }
}
