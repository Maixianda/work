package com.gzliangce.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gzliangce.AppContext;
import com.gzliangce.bean.Constants;
import com.gzliangce.dto.AllProductDTO;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.ChangeOrderEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.order.ChangeOrderActivity;
import com.gzliangce.ui.activity.order.OrderDetailsActivity;
import com.gzliangce.ui.activity.order.PlaceAnOrderActivity;
import com.gzliangce.ui.activity.order.SelectBrokerListActivity;
import com.gzliangce.ui.activity.order.SelectBrokerMapActivity;
import com.gzliangce.ui.activity.product.AllProductActivity;
import com.gzliangce.ui.activity.product.ProductsDetailsActivity;
import com.gzliangce.ui.activity.usercenter.PersonalDetailsActivity;
import com.gzliangce.ui.activity.usercenter.SearchActivity;
import com.gzliangce.ui.callback.onDeleteCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/4/1.
 * 订单相关工具类
 */
public class ApiDataUtil {
    public static io.ganguo.library.util.log.Logger logger = LoggerFactory.getLogger(ApiDataUtil.class);

    /**
     * 下单复用方法
     */
    public static void doPlaceAnOrder(final PlaceAnOrder placeAnOrder, final Activity activity) {
        if (!AppContext.me().isLogined()) {
            MetadataUtil.putGCache(activity, Constants.SAVE_OREDER_INFO, placeAnOrder);
            IntentUtil.actionActivity(activity, LoginActivity.class);
            return;
        }
        LoadingHelper.showMaterLoading(activity, "正在下单");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postPlaceAnOrder(placeAnOrder, activity);
            }
        }, 200);
    }

    /**
     * 发起下单请求
     */
    public static void postPlaceAnOrder(PlaceAnOrder placeAnOrder, final Activity activity) {
        Map<String, String> map = new HashMap<>();
        map.put("productId", placeAnOrder.getProductId());
        map.put("areaId", placeAnOrder.getAreaId());
        map.put("mortgageId", placeAnOrder.getMortgageId());
        map.put("address", placeAnOrder.getAddress());
        map.put("linkman", placeAnOrder.getLinkman());
        map.put("phone", placeAnOrder.getPhone());
        Call<BaseDTO> call = ApiUtil.getOrderService().postPlaceAnOrder(map);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                CacheManager.disk(activity).remove(Constants.SAVE_OREDER_INFO);
                ToastHelper.showMessage(activity, "下单成功");
                finishActivitys(activity, SelectBrokerListActivity.class);
                finishActivitys(activity, SelectBrokerMapActivity.class);
                finishActivitys(activity, PlaceAnOrderActivity.class);
                finishActivitys(activity, SearchActivity.class);
                finishActivitys(activity, ProductsDetailsActivity.class);
                finishActivitys(activity, AllProductActivity.class);
                finishActivitys(activity, PersonalDetailsActivity.class);
                if (activity.getClass() != MainActivity.class) {
                    activity.finish();
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(activity, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 关闭下单相关页面
     */
    public static void finishActivitys(Activity activity, Class classes) {
        if (!activity.getClass().equals(classes)) {
            AppManager.finishActivity(classes);
        }
    }


    /**
     * 转单复用方法
     */
    public static void changeOrder(final PlaceAnOrder placeAnOrder, final Activity activity) {
        LoadingHelper.showMaterLoading(activity, "转单中...");
        Call<BaseDTO> call = ApiUtil.getOrderService().postTransferOrder(placeAnOrder.getNumber(), placeAnOrder.getMortgageId(), placeAnOrder.getReason());
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                ToastHelper.showMessage(activity, "转单成功");
                EventHub.post(new ChangeOrderEvent(placeAnOrder, Activity.class));
                AppManager.finishActivity(ChangeOrderActivity.class);
                AppManager.finishActivity(OrderDetailsActivity.class);
                AppManager.finishActivity(SelectBrokerListActivity.class);
                AppManager.finishActivity(SelectBrokerMapActivity.class);
                if (activity != null) {
                    activity.finish();
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(activity, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 删除订单数据(按揭用户用)
     */
    public static void removeOrderData(Object obj, boolean isCancel, LoadMoreAdapter adapter, onDeleteCallBack callBack) {
        if (LiangCeUtil.getUserType() != UserType.mortgage) {
            return;
        }
        if (obj == null) {
            return;
        }
        String number;
        if (isCancel) {
            OrderInfo orderInfo = (OrderInfo) obj;
            number = orderInfo.getNumber();
        } else {
            PlaceAnOrder placeAnOrder = (PlaceAnOrder) obj;
            number = placeAnOrder.getNumber();
        }
        ApiDataUtil.taskRemoveData(number, adapter.getData(), adapter, callBack);
    }

    /**
     * 异步删除订单数据
     */
    public static void taskRemoveData(final String number, final List<OrderInfo> list, final LoadMoreAdapter adapter, final onDeleteCallBack callback) {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    OrderInfo orderInfo = list.get(i);
                    if (Strings.isEquals(number, orderInfo.getNumber())) {
                        runOnNotifyData(list, adapter, i, callback);
                        break;
                    }
                }

            }
        });
    }

    /**
     * UI线程中刷新订单列表
     */
    public static void runOnNotifyData(final List<OrderInfo> list, final LoadMoreAdapter adapter, final int index, final onDeleteCallBack callback) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.remove(index);
                adapter.notifyItemRemoved(index);
                callback.onFinish();
            }
        });
    }


    /**
     * 跳转到直接下单界面
     */
    public static void actionDirectOrder(Activity activity, String memberId) {
        if (AppContext.me().isLogined() && !AppContext.me().isAuthorization()) {
            DialogUtil.AtterstationDialog(activity, "未通过资格认证前无法下单");
            return;
        }
        Intent intent = new Intent(activity, PlaceAnOrderActivity.class);
        intent.putExtra(Constants.DIRECT_ORDER_MEMBERID, memberId);
        activity.startActivity(intent);
    }

    /**
     * 获取全部产品数据
     */
    public static void getAllProducts(APICallback callback) {
        Call<AllProductDTO> call = ApiUtil.getOrderService().getAllProducts();
        call.enqueue(callback);
    }
}
