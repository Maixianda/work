package com.gzliangce.ui.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.gzliangce.R;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.enums.UserType;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.activity.BaseFragmentActivity;
import io.ganguo.library.util.Systems;
import retrofit.Call;

/**
 * Created by Aaron on 11/10/15.
 * FragmentActivity - 基类 - 无右滑返回上一页效果
 */
public abstract class BaseFragmentActivtyBinding extends BaseFragmentActivity implements HeaderModel.HeaderView {
    public HeaderModel header;
    private double latitude, longitude;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Systems.setBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        UiUtil.bindSwipeRefreshView(this, (SwipeRefreshLayout) findViewById(R.id.srv_refresh));
    }

    @Override
    public void onBackClicked() {
    }

    @Override
    public void onTitleClicked() {

    }

    @Override
    public void onMenuClicked() {
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                latitude = aMapLocation.getLatitude();//获取纬度
                longitude = aMapLocation.getLongitude();//获取经度
                logger.e("latitude: " + latitude + " " + longitude);
                if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
                    updateLocation(latitude, longitude);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 更新按揭员位置信息
     */
    public void updateLocation(Double lat, Double lon) {
        Call<BaseDTO> call = ApiUtil.getUserCenterService().updateBrokerLocation(lat, lon);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                logger.e("baseDTO: " + "success");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getApplicationContext(), message);
            }

            @Override
            public void onFinish() {
            }
        });
    }
}
