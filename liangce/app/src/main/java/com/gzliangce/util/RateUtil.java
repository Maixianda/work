package com.gzliangce.util;

import com.gzliangce.AppContext;
import com.gzliangce.entity.HouseInfo;
import com.gzliangce.entity.NewSimpleEntity;
import com.gzliangce.entity.NewUnSimpleEntity;
import com.gzliangce.entity.OldSimpleEntity;
import com.gzliangce.entity.OldUnSimpleEntity;
import com.gzliangce.entity.RateListInfo;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/3/2.
 * 税率相关工具类
 */
public class RateUtil {
    private static Logger logger = LoggerFactory.getLogger(RateUtil.class);

    /**
     * 获取房屋公证费税率
     */
    public static double getGongzhengFeiRate(HouseInfo houseInfo) {
        int houseArea = houseInfo.getHouseAera();//房屋面积
        int houseFee = houseInfo.getHouseFee();//房屋单价
        double houseFeeSum = houseArea * houseFee / 10000;//房屋总价
        double rate = 0.003;
        NewSimpleEntity newSimpleEntity = getNewSimple();
        if (newSimpleEntity == null) {
            return rate;
        }
        if (houseFeeSum < 50) {
            rate = newSimpleEntity.getGongZhengFeiRate_a();
        } else if (houseFeeSum >= 50 && houseArea < 500) {
            rate = newSimpleEntity.getGongZhengFeiRate_b();
        } else if (houseFeeSum >= 500 && houseArea < 1000) {
            rate = newSimpleEntity.getGongZhengFeiRate_c();
        } else if (houseFeeSum >= 1000 && houseArea < 2000) {
            rate = newSimpleEntity.getGongZhengFeiRate_d();
        } else if (houseFeeSum >= 2000 && houseArea < 5000) {
            rate = newSimpleEntity.getGongZhengFeiRate_e();
        } else if (houseFeeSum >= 5000 && houseArea < 10000) {
            rate = newSimpleEntity.getGongZhengFeiRate_f();
        } else {
            rate = newSimpleEntity.getGongZhengFeiRate_g();
        }
        return rate;
    }

    /**
     * 获取新房契税税率
     */
    public static double getNewHouseQiShuiRate(HouseInfo houseInfo) {
        int houseArea = houseInfo.getHouseAera();
        boolean isOnlyHouse = houseInfo.isOnlyHouse();
        double rate;
        NewSimpleEntity newSimpleEntity;
        if (houseInfo.getHouseType() == 1) {//非普通房
            newSimpleEntity = getNewUnSimple();
        } else {
            newSimpleEntity = getNewSimple();
        }
        if (newSimpleEntity == null) {//普通和经济适用房
            rate = 0.03f;
            return rate;
        }
        if (houseInfo.getHouseType() == 1) {
            rate = newSimpleEntity.getQiShuiRate();
            return rate;
        }
        if (!isOnlyHouse) {
            rate = newSimpleEntity.getQiShuiRate_c();
        } else if (houseArea <= 90 && isOnlyHouse) {
            rate = newSimpleEntity.getQiShuiRate_a();
        } else {
            rate = newSimpleEntity.getQiShuiRate_b();
        }
        return rate;
    }


    /**
     * 获取印花税税率
     */
    public static double getNewStampRate(HouseInfo info) {
        NewSimpleEntity newSimpleEntity;
        if (info.getHouseType() == 1) {//非普通房
            newSimpleEntity = getNewUnSimple();
        } else {
            newSimpleEntity = getNewSimple();
        }
        if (newSimpleEntity == null) {
            return 0.5;
        }
        return newSimpleEntity.getYinHuaShuiRate();
    }


    /**
     * 获取委托办理利率
     */
    public static double getweiTuoBanLiRate(HouseInfo info) {
        NewSimpleEntity newSimpleEntity;
        if (info.getHouseType() == 1) {//非普通房
            newSimpleEntity = getNewUnSimple();
        } else {
            newSimpleEntity = getNewSimple();
        }
        if (newSimpleEntity == null) {
            return 0.5;
        }
        logger.e("getweiTuoBanLiRate----" + newSimpleEntity.getWeiTuoBanLiRate());
        return newSimpleEntity.getWeiTuoBanLiRate();
    }


    /**
     * 获取买卖手续费利率利率
     */
    public static double getNewMaiMaiRate(HouseInfo info) {
        NewSimpleEntity newSimpleEntity;
        if (info.getHouseType() == 1) {//非普通房
            newSimpleEntity = getNewUnSimple();
        } else {
            newSimpleEntity = getNewSimple();
        }
        if (newSimpleEntity == null) {
            return 0.05;
        }
        return newSimpleEntity.getMaiMaiRate();
    }


    /**
     * 获取二手房-买方契税税率
     */
    public static double getOldHouseQiShuiRate(HouseInfo info) {
        int houseArea = info.getHouseAera();
        boolean isFrist = info.isFristBuy();
        double rate;
        OldSimpleEntity oldSimpleEntity = getOldSimple(info);
        if (oldSimpleEntity == null) {//普通和经济适用房
            rate = 0.03f;
            return rate;
        }
        if (info.getHouseType() == 1) {
            rate = oldSimpleEntity.getQiShuiRate();
            return rate;
        }
        if (!isFrist) {
            rate = oldSimpleEntity.getQiShuiRate_c();
        } else if (houseArea <= 90 && isFrist) {
            rate = oldSimpleEntity.getQiShuiRate_a();
        } else {
            rate = oldSimpleEntity.getQiShuiRate_b();
        }
        return rate;
    }


    /**
     * 获取二手房-卖家营业税
     */
    public static double getSellOldYinYeShuiRate(HouseInfo info) {
        double rate;
        OldSimpleEntity oldSimpleEntity;
        if (info.getHouseType() == 1) {//非普通房
            oldSimpleEntity = getOldUnSimple();
        } else {
            oldSimpleEntity = getOldSimple();
        }
        if (oldSimpleEntity == null) {//普通和经济适用房
            rate = 0.3f;
            return rate;
        }
        rate = oldSimpleEntity.getYingYeShuiRate();
        return rate;
    }


    /**
     * 获取二手房-印花税
     */
    public static double getOldHouseYinHuaShuiRate(int houseType, boolean isBuy) {
        double rate;
        OldSimpleEntity oldSimpleEntity;
        if (houseType == 1) {
            oldSimpleEntity = getOldUnSimple();
        } else {
            oldSimpleEntity = getOldSimple();
        }
        rate = oldSimpleEntity.getYinHuaShuiRate_sell();
        if (isBuy) {
            rate = oldSimpleEntity.getYinHuaShuiRate_buy();
        }
        return rate;
    }


    /**
     * 获取二手房-卖家个人所得税
     */
    public static double getOldHouseGeRenShuoDeShuiRate(int houseType) {
        double rate;
        OldSimpleEntity oldSimpleEntity;
        if (houseType == 1) {
            oldSimpleEntity = getOldUnSimple();
        } else {
            oldSimpleEntity = getOldSimple();
        }
        rate = oldSimpleEntity.getGeRenSuoDeShuiRate();
        return rate;
    }


    /**
     * 获取二手房-工本印花税
     */
    public static double getOldHouseGongBengYinHuaShuiRate() {
        double rate;
        OldSimpleEntity oldSimpleEntity = getOldUnSimple();
        rate = oldSimpleEntity.getGongBenYinHuaShuiRate();
        return rate;
    }

    /**
     * 获取二手房-房屋买卖登记费税率
     */
    public static double getOldHouseFangWuMaiMaiDengJiFeiRate(HouseInfo info) {
        double rate;
        OldSimpleEntity oldSimpleEntity = getOldSimple(info);
        if (oldSimpleEntity == null) {
            rate = 0.3;
            return rate;
        }
        rate = oldSimpleEntity.getFangWuMaiMaiDengJiFeiRate();
        return rate;
    }


    /**
     * 获取二手房-房屋买卖登记费税率
     */
    public static double getOldHouseFangWuMaiMaiShouXuFeiRate(HouseInfo info, boolean isSell) {
        double rate;
        OldSimpleEntity oldSimpleEntity = getOldSimple(info);
        if (oldSimpleEntity == null) {
            rate = 0.3;
            return rate;
        }
        if (isSell) {
            rate = oldSimpleEntity.getFangWuMaiMaiShouXuFeiRate_sell();
        } else {
            rate = oldSimpleEntity.getFangWuMaiMaiShouXuFeiRate_buy();
        }
        return rate;
    }

    /**
     * 获取二手房-综合地价税
     */
    public static double getOldHouseZongHeDiJiaRate(HouseInfo info) {
        double rate;
        OldSimpleEntity oldSimpleEntity = getOldSimple(info);
        if (oldSimpleEntity == null) {
            rate = 0.3;
            return rate;
        }
        rate = oldSimpleEntity.getZongHeDiJiaRate();
        return rate;
    }

    /**
     * 获取二手房-数据对象(根据房产类型返回不同的数据对象)
     */
    private static OldSimpleEntity getOldSimple(HouseInfo info) {
        OldSimpleEntity oldSimpleEntity;
        if (info.getHouseType() == 1) {//非普通房
            oldSimpleEntity = getOldUnSimple();
        } else {//普通房和经济适用房
            oldSimpleEntity = getOldSimple();
        }
        return oldSimpleEntity;
    }

    /**
     * 获取二手房-非普通类型数据对象
     */
    private static OldSimpleEntity getOldUnSimple() {
        RateListInfo rateListInfo = MetadataUtil.getInstance().getReteListInfo(AppContext.me());
        if (rateListInfo != null) {
            return rateListInfo.getOldUnSimpleEntity();
        }
        return null;
    }


    /**
     * 获取二手房-普通&经济数据对象
     */
    private static OldSimpleEntity getOldSimple() {
        RateListInfo rateListInfo = MetadataUtil.getInstance().getReteListInfo(AppContext.me());
        if (rateListInfo != null) {
            return rateListInfo.getOldSimpleEntity();
        }
        return null;
    }

    /**
     * 获取新房-普通&经济数据对象
     */
    private static NewSimpleEntity getNewSimple() {
        RateListInfo rateListInfo = MetadataUtil.getInstance().getReteListInfo(AppContext.me());
        if (rateListInfo != null) {
            return rateListInfo.getNewSimpleEntity();
        }
        return null;
    }


    /**
     * 获取新房-非普通数据对象
     */
    private static NewSimpleEntity getNewUnSimple() {
        RateListInfo rateListInfo = MetadataUtil.getInstance().getReteListInfo(AppContext.me());
        if (rateListInfo != null) {
            return rateListInfo.getNewUnSimpleEntity();
        }
        return null;
    }
}
