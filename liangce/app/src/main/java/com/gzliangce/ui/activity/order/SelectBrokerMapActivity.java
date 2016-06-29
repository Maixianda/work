package com.gzliangce.ui.activity.order;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySelectBrokerBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.SelectBroberAdapter;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.MapUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.ToastHelper;
import retrofit.Call;

/**
 * Created by zzwdream on 1/22/16.
 * 选择金融经纪人 -  地图界面
 */
public class SelectBrokerMapActivity extends BaseActivityBinding implements LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapLoadedListener, AMap.OnMyLocationChangeListener {
    public ActivitySelectBrokerBinding binding;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMap mAMap;
    private Bundle savedInstanceState;
    private LatLng mCurrLatLng;
    private List<BrokeInfo> brokeInfoList = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private boolean isLocationPermissionGranted;
    private PlaceAnOrder placeAnOrder;
    private SelectBroberAdapter adapter;
    private int index;
    private String mClassName;
    private AMapLocation amapLocation;
    private long dia;
    private float zoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isLocationPermissionGranted = requestPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, Constants.REQUEST_ACCESS_COARSE_LOCATION);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_broker);
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        setHeader();
        placeAnOrder = (PlaceAnOrder) getIntent().getSerializableExtra(Constants.PLACE_ON_ORDER_PARM);
        mClassName = SelectBrokerMapActivity.class.getName();
        binding.map.onCreate(savedInstanceState);
        mAMap = binding.map.getMap();
        setupMap();
        adapter = new SelectBroberAdapter(this, brokeInfoList, placeAnOrder);
        binding.vpBroker.setAdapter(adapter);
        binding.vpBroker.setPageMargin(getResources().getDimensionPixelSize(R.dimen.dp_10));
        getMortgageMapList();

    }


    @Override
    public void initListener() {
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnMyLocationChangeListener(this);
        header.onMenuClickListener();
        binding.vpBroker.addOnPageChangeListener(pagerChangeListener);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onMenuClicked() {
        Intent intent = new Intent(this, SelectBrokerListActivity.class);
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
        intent.putExtra(Constants.PLACE_ON_ORDER_PARM_LIST, (Serializable) brokeInfoList);
        intent.putExtra(Constants.REQUEST_BUTTON_STATUS, ButtonStatus.OrderDoBtn);
        startActivity(intent);
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("选择金融经纪");
        header.setRightIcon(R.drawable.ic_list);
        header.setRightBackground(R.drawable.ripple_default);
        binding.setHeader(header);
    }

    private void setupMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.ic_location_marker));
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.transparent));
        myLocationStyle.strokeWidth(0f);
        myLocationStyle.strokeColor(getResources().getColor(R.color.transparent));
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 显示定位按钮
        mAMap.getUiSettings().setScaleControlsEnabled(false);
        mAMap.getUiSettings().setZoomControlsEnabled(false); // 隐藏缩放按钮
        mAMap.setMyLocationEnabled(true);
        mAMap.moveCamera(new CameraUpdateFactory().zoomTo(18));
        zoom = mAMap.getCameraPosition().zoom;
//        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                if (cameraPosition.zoom < 12) {
//                    mAMap.animateCamera(new CameraUpdateFactory().zoomTo(12));
//                }
//            }
//
//            @Override
//            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                if (zoom != cameraPosition.zoom) {
//                    zoom = cameraPosition.zoom;
//                    getMortgageMapList();
//                }
//            }
//        });
    }

    /**
     * 请求权限
     */
    public boolean requestPermission(final Activity activity, final String permission, final int requestCode) {
        final int version = Build.VERSION.SDK_INT;
        boolean isGranted = false;
        int checkStoragePermission = ContextCompat.checkSelfPermission(AppContext.me(), permission);

        if (checkStoragePermission != PackageManager.PERMISSION_GRANTED && activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        } else {
            isGranted = true;
            logger.d("permission " + permission + " granted");
        }
        return isGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logger.d("granted!");
                isLocationPermissionGranted = true;
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                mCurrLatLng = MapUtils.getLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                logger.e("currentLocation: " + mCurrLatLng);
                onLocationChange(aMapLocation);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                logger.e("AMap Error" + errText);
            }
        }
    }

    public void onLocationChange(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                logger.d(amapLocation.toString());
                this.amapLocation = amapLocation;
//                getMortgageMapList();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    public void addGreenMarker(int id, double lat, double lng) {
        MarkerOptions options = new MarkerOptions();
        boolean isCurrent = false;
//        if (id - 1 == index) {
//            isCurrent = true;
//        }
        if (lat == 0.0 && lng == 0.0) {
            options.icon(BitmapDescriptorFactory.fromBitmap(initMarkerTranslateView()));
        } else {
            options.icon(BitmapDescriptorFactory.fromBitmap(initMarkerView(id, isCurrent)));
        }
        options.position(MapUtils.getLatLng(lat, lng));
        Marker marker = binding.map.getMap().addMarker(options);
        marker.setObject(id);
        markers.add(marker);
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int index = (int) marker.getObject();
        binding.vpBroker.setCurrentItem(index - 1);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    //初始化marker图标
    private Bitmap initMarkerView(int id, boolean isCurrent) {
        View view = View.inflate(this, R.layout.item_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_num);
        textView.setText(id + "");
        if (isCurrent) {
            textView.setSelected(true);
        }
        return convertViewToBitmap(view);
    }

    //初始化透明图标
    private Bitmap initMarkerTranslateView() {
        View view = View.inflate(this, R.layout.item_marker_transparent, null);
        return convertViewToBitmap(view);
    }

    //view 转bitmap
    private Bitmap convertViewToBitmap(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    //ViewPager滑动监听事件
    private ViewPager.OnPageChangeListener pagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            logger.e("map position: " + position);
//            if (brokeInfoList.get(position).getLat() == 0.0 && brokeInfoList.get(position).getLon() == 0.0) {
//                if (position > 0) {
//                    if (brokeInfoList.get(position - 1).getLat() != 0.0 && brokeInfoList.get(position - 1).getLon() != 0.0) {
//                        markers.get(position - 1).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(index, false)));
//                    }
//                }
//                index = position + 1;
//                return;
//            }
//            if (index != position + 1) {
//                markers.get(index - 1).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(index, false)));
//                index = position + 1;
//                markers.get(position).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(index, true)));
//                mAMap.animateCamera(new CameraUpdateFactory().changeLatLng(markers.get(position).getPosition()));
//            }
            markers.get(index).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(index + 1, false)));
            if (brokeInfoList.get(position).getLat() == 0.0 && brokeInfoList.get(position).getLon() == 0.0) {
                index = position;
                return;
            }
            markers.get(position).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(position + 1, true)));
            mAMap.animateCamera(new CameraUpdateFactory().changeLatLng(markers.get(position).getPosition()));
            index = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onMyLocationChange(Location location) {
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MapUtils.getLatLng(location), 16));
    }

    private void getMortgageMapList() {
//        if (amapLocation == null) {
//            return;
//        }
//        index = 1;
//        VisibleRegion visibleRegion = mAMap.getProjection().getVisibleRegion();
//        dia = (long) AMapUtils.calculateLineDistance(visibleRegion.nearLeft, visibleRegion.farRight);
//        getMortgageMapList(amapLocation.getLatitude(), amapLocation.getLongitude(), dia);
        getMortgageUserList();
//        logger.e("left" + visibleRegion.nearLeft.toString() + "  right" + visibleRegion.farRight + " " + AMapUtils.calculateLineDistance(visibleRegion.nearLeft, visibleRegion.farRight));
    }

    /**
     * 获取文档资料列表
     */
    public void getMortgageMapList(Double lat, Double lon, final long dia) {
        Map<String, String> map = new HashMap<>();
        map.put("lat", String.valueOf(lat));
        map.put("lon", String.valueOf(lon));
        map.put("dia", String.valueOf(dia));
        Call<ListDTO<BrokeInfo>> call = ApiUtil.getUserCenterService().getMortgageMap(map);
        call.enqueue(new APICallback<ListDTO<BrokeInfo>>() {
            @Override
            public void onSuccess(ListDTO<BrokeInfo> brokeInfoListDTO) {
//                handlerMortgageData(brokeInfoListDTO.getList(), dia);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SelectBrokerMapActivity.this, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    /**
     * 处理金融经纪数据
     */
    private void handlerMortgageData(List<BrokeInfo> brokeInfos, long dia) {
        if (dia != this.dia) {
            return;
        }
        brokeInfoList.clear();
        markers.clear();
        logger.e(brokeInfos.size());
        for (int i = 0; i < brokeInfos.size(); i++) {
            addGreenMarker(i + 1, brokeInfos.get(i).getLat(), brokeInfos.get(i).getLon());
            brokeInfoList.add(brokeInfos.get(i));
        }
        adapter.notifyDataSetChanged();
        if (markers.size() > 0) {
            binding.vpBroker.setCurrentItem(0);
            markers.get(0).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(1, true)));
            mAMap.animateCamera(new CameraUpdateFactory().changeLatLng(markers.get(0).getPosition()));
        }
    }


    /**
     * 获取金融经纪列表
     */
    private void getMortgageUserList() {
        Map<String, String> map = new HashMap<>();
        map.put("areaId", placeAnOrder.getAreaId());
        logger.e("获取金融经纪列表-----areaId" + placeAnOrder.getAreaId());
        Call<ListDTO<BrokeInfo>> call = ApiUtil.getUserCenterService().getMortgageUserList(map);
        call.enqueue(new APICallback<ListDTO<BrokeInfo>>() {
            @Override
            public void onSuccess(ListDTO<BrokeInfo> brokeInfoListDTO) {
                handlerMortgageData(brokeInfoListDTO.getList());
                logger.e("获取金融经纪列表-----onSuccess" + brokeInfoListDTO.toString());
            }

            @Override
            public void onFailed(String message) {
                logger.e("获取数据失败----onFailed" + message);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    /**
     * 处理金融经纪数据
     */
    private void handlerMortgageData(List<BrokeInfo> brokeInfos) {
        if (brokeInfos == null) {
            return;
        }
        brokeInfoList.clear();
        markers.clear();
        logger.e(brokeInfos.size());
        for (int i = 0; i < brokeInfos.size(); i++) {
            addGreenMarker(i + 1, brokeInfos.get(i).getLat(), brokeInfos.get(i).getLon());
            brokeInfoList.add(brokeInfos.get(i));
        }
        adapter.notifyDataSetChanged();
        if (markers.size() > 0) {
            if (brokeInfoList.get(0).getLat() != 0.0 && brokeInfoList.get(0).getLon() != 0.0) {
                index = 0;
                markers.get(0).setIcon(BitmapDescriptorFactory.fromBitmap(initMarkerView(1, true)));
            }
            binding.vpBroker.setCurrentItem(0);
            mAMap.animateCamera(new CameraUpdateFactory().changeLatLng(markers.get(0).getPosition()));
        }
    }

}
