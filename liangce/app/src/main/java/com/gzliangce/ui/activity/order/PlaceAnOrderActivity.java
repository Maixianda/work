package com.gzliangce.ui.activity.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityPlaceAnOrderBinding;
import com.gzliangce.dto.AllProductDTO;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.AreaList;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.event.ProductInfoEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.product.ProductsDetailsActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.ItemCallBack;
import com.gzliangce.ui.callback.OnPopupWindowListener;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.MetadataUtil;
import com.gzliangce.util.PopupListMenuUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Animations;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;

/**
 * 下单界面
 * Created by aaron on 1/23/16.
 */
public class PlaceAnOrderActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener,
        ItemCallBack<ProductsInfo>, OnPopupWindowListener {
    private ActivityPlaceAnOrderBinding binding;
    private ProductsInfo productsInfo;
    private OptionsPickerView pvOptions;
    private ListDTO<AreaList> dto;
    private PopupListMenuUtil<ProductsInfo> popupListMenuUtil;

    private ArrayList<String> province = new ArrayList<String>();
    private ArrayList<ArrayList<String>> city = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> region = new ArrayList<ArrayList<ArrayList<String>>>();

    private ArrayList<String> provinceId = new ArrayList<String>();
    private ArrayList<ArrayList<String>> cityId = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> regionId = new ArrayList<ArrayList<ArrayList<String>>>();

    private ArrayList<String> secondStageCity = new ArrayList<String>();
    private ArrayList<ArrayList<String>> secondStageRegion = new ArrayList<ArrayList<String>>();
    private ArrayList<String> secondStageCityId = new ArrayList<String>();
    private ArrayList<ArrayList<String>> secondStageRegionId = new ArrayList<ArrayList<String>>();
    private ArrayList<String> thirdStageRegion = new ArrayList<String>();
    private ArrayList<String> thirdStageRegionId = new ArrayList<String>();

    private String areaId;
    private String memberId;//对收藏的按揭员直接下单时需要用到
    private boolean isUp = true;


    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_an_order);
        setHeader();
    }

    @Override
    public void initView() {
        popupListMenuUtil = new PopupListMenuUtil<ProductsInfo>(this, this, R.layout.item_product_list);
        popupListMenuUtil.setCallBack(this);
    }

    @Override
    public void initListener() {
        binding.rlyProductType.setOnClickListener(this);
        binding.llyPropertyArea.setOnClickListener(this);
        binding.rlyProductDetails.setOnClickListener(this);
        binding.tvChoseBroker.setOnClickListener(this);
    }

    @Override
    public void initData() {
        productsInfo = getIntent().getParcelableExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY);
        memberId = getIntent().getStringExtra(Constants.DIRECT_ORDER_MEMBERID);
        if (AppContext.me().isLogined()) {
            binding.setUser(AppContext.me().getUser());
        }
        if (!Strings.isEmpty(memberId)) {
            binding.tvChoseBroker.setText("直接下单");
        }
        binding.setData(productsInfo);
        getServiceData();
    }

    /**
     * 获取网络数据
     */
    private void getServiceData() {
        LoadingHelper.showMaterLoading(this, "加载中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPropertyArea();
                getAllProducts();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_product_type:
                popupListMenuUtil.toggleMenu(binding.viewMenu);
                break;
            case R.id.lly_property_area:
                if (pvOptions != null) {
                    Systems.hideKeyboard(this);
                    pvOptions.show();
                }
                break;
            case R.id.rly_product_details:
                actionProductDetails();
                break;
            case R.id.tv_chose_broker:
                setIntentData();
                break;
        }
    }


    /**
     * 查看产品详情
     */
    private void actionProductDetails() {
        if (productsInfo == null) {
            ToastHelper.showMessage(this, "请先选择产品类型");
            return;
        }
        Intent intent = new Intent(this, ProductsDetailsActivity.class);
        intent.putExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY, productsInfo);
        intent.putExtra(Constants.REQUEST_ACTIVITY_PLACE_ORDER, true);
        startActivity(intent);
    }


    @Override
    public void onBackClicked() {
        finish();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("下单");
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 109) {
            if (resultCode == RESULT_OK) {
                productsInfo = data.getParcelableExtra(Constants.ACTION_PRODUCT_DETAIL_ACTIVITY);
                binding.setData(productsInfo);
            }
        }
    }


    /**
     * 获取所在区域数据
     */
    private void getPropertyArea() {
        dto = MetadataUtil.getAreaCache(this);
        if (dto != null) {
            FilterData(dto.getList(), 1);
            initPickerView();
        } else {
            getAreaData();
        }
    }

    /**
     * 本地无数据时重新获取区域数据
     */
    private void getAreaData() {
        MetadataUtil.getInstance().getAreaData(this, new APICallback<ListDTO<AreaList>>() {
            @Override
            public void onSuccess(ListDTO<AreaList> areaListListDTO) {
                if (areaListListDTO != null) {
                    FilterData(areaListListDTO.getList(), 1);
                    initPickerView();
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PlaceAnOrderActivity.this, message);
                showAreaFiledDialog();
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 获取区域数据失败时提示弹窗
     */
    private void showAreaFiledDialog() {
        DialogUtil.showHintDialog(this, getResources().getText(R.string.area_onfiled), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                LoadingHelper.showMaterLoading(dialog.getContext(), "加载中");
                getAreaData();
            }
        }).show();
    }


    /**
     * 为pickerview筛选数据
     */
    private void FilterData(List<AreaList> areaLists, int postion) {
        if (areaLists != null && areaLists.size() > 0) {
            for (AreaList nextAreaList : areaLists) {
                handleData(nextAreaList, postion);
            }
        } else {
            handleEmptyData(postion);
        }
    }

    /**
     * 处理物业区域列表不为空的数据
     *
     * @param areaList
     * @param position
     */
    private void handleData(AreaList areaList, int position) {
        switch (position) {
            case 1:
                //初始化区域名称的list
                secondStageCity = new ArrayList<String>();
                secondStageRegion = new ArrayList<ArrayList<String>>();
                //初始化区域ID的list
                secondStageCityId = new ArrayList<String>();
                secondStageRegionId = new ArrayList<ArrayList<String>>();

                FilterData(areaList.getChildren(), position + 1);

                province.add(areaList.getName());
                city.add(secondStageCity);
                region.add(secondStageRegion);

                provinceId.add(areaList.getAreaId() + "");
                cityId.add(secondStageCityId);
                regionId.add(secondStageRegionId);
                break;
            case 2:
                thirdStageRegion = new ArrayList<String>();
                thirdStageRegionId = new ArrayList<String>();

                FilterData(areaList.getChildren(), position + 1);

                secondStageCity.add(areaList.getName());
                secondStageRegion.add(thirdStageRegion);

                secondStageCityId.add(areaList.getAreaId() + "");
                secondStageRegionId.add(thirdStageRegionId);
                break;
            case 3:
                thirdStageRegion.add(areaList.getName());
                thirdStageRegionId.add(areaList.getAreaId() + "");
                break;
        }
    }

    /**
     * 处理物业区域列表为空的数据
     *
     * @param position
     */
    private void handleEmptyData(int position) {
        switch (position) {
            case 1:
                break;
            case 2:
                secondStageCity.add("");
                ArrayList<String> emptyThirdStageRegion = new ArrayList<String>();
                secondStageRegion.add(emptyThirdStageRegion);
                secondStageCityId.add("");
                secondStageRegionId.add(emptyThirdStageRegion);
                break;
            case 3:
                thirdStageRegion.add("");
                thirdStageRegionId.add("");
                break;
        }
    }

    /**
     * 初始化选择器
     */
    private void initPickerView() {
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        //三级联动效果
        pvOptions.setPicker(province, city, region, true);
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        pvOptions.setSelectOptions(18, 0, 0);
        //监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                binding.tvPropertyArea.setText(province.get(options1) + " " +
                        city.get(options1).get(options2) + " " +
                        region.get(options1).get(options2).get(options3));
                if (Strings.isNotEmpty(region.get(options1).get(options2).get(options3))) {
                    areaId = regionId.get(options1).get(options2).get(options3);
                } else if (Strings.isNotEmpty(city.get(options1).get(options2))) {
                    areaId = cityId.get(options1).get(options2);
                } else {
                    areaId = provinceId.get(options1);
                }
            }
        });
    }


    /**
     * 跳转到下单界面
     */
    private void setIntentData() {
        if (!checkDataValid()) {
            return;
        }
        Systems.hideKeyboard(this);
        PlaceAnOrder placeAnOrder = new PlaceAnOrder();
        placeAnOrder.setAreaId(areaId);
        placeAnOrder.setProductId(productsInfo.getId() + "");
        placeAnOrder.setAddress(binding.etPropertyAddress.getText() + "");
        placeAnOrder.setLinkman(binding.etContactPerson.getText() + "");
        placeAnOrder.setPhone(binding.etContactPhone.getText() + "");
        if (!Strings.isEmpty(memberId)) {
            showPlaceAnOrderDialog(placeAnOrder);
        } else {
            Intent orderIntent = new Intent(this, SelectBrokerMapActivity.class);
            orderIntent.putExtra(Constants.PLACE_ON_ORDER_PARM, placeAnOrder);
            startActivity(orderIntent);
        }
    }

    /**
     * 确认下单弹窗
     */
    private void showPlaceAnOrderDialog(final PlaceAnOrder placeAnOrder) {
        DialogUtil.getMaterialDialog(this, getResources().getString(R.string.Order_place_text), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                placeAnOrder.setMortgageId(memberId);
                ApiDataUtil.doPlaceAnOrder(placeAnOrder, PlaceAnOrderActivity.this);
            }
        }).show();
    }


    /**
     * 判断是否满足下单条件
     */
    private boolean checkDataValid() {
        if (Strings.isEmpty(binding.tvProductTypeName.getText().toString())) {
            ToastHelper.showMessage(this, "产品类型不能为空");
            return false;
        }
        if (Strings.isEmpty(binding.tvPropertyArea.getText().toString())) {
            ToastHelper.showMessage(this, "物业区域不能为空");
            return false;
        }
        if (Strings.isEmpty(binding.etPropertyAddress.getText().toString())) {
            ToastHelper.showMessage(this, "物业地址不能为空");
            binding.etPropertyAddress.requestFocus();
            return false;
        }
        if (Strings.isEmpty(binding.etContactPerson.getText().toString())) {
            ToastHelper.showMessage(this, "联系人员不能为空");
            binding.etContactPerson.requestFocus();
            return false;
        }
        if (Strings.isEmpty(binding.etContactPhone.getText().toString())) {
            ToastHelper.showMessage(this, "联系电话不能为空");
            binding.etContactPhone.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (pvOptions != null && pvOptions.isShowing()) {
            pvOptions.dismiss();
            return;
        }
        super.onBackPressed();
    }


    /**
     * 选择产品
     */
    @Subscribe
    public void onProductInfoEvent(ProductInfoEvent event) {
        if (event.getProductsInfo() == null) {
            return;
        }
        productsInfo = event.getProductsInfo();
        binding.setData(event.getProductsInfo());
    }


    /**
     * 获取全部产品数据
     */
    private void getAllProducts() {
        ApiDataUtil.getAllProducts(new APICallback<AllProductDTO>() {
            @Override
            public void onSuccess(AllProductDTO allProductDTO) {
                handlerAllProduct(allProductDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PlaceAnOrderActivity.this, message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }


    /**
     * 选择产品类型 - 箭头动画
     */
    private void setArrowAnimation() {
        binding.ivArrow.clearAnimation();
        if (isUp) {
            binding.ivArrow.setAnimation(Animations.getRotateUpAnimation());
            isUp = false;
        } else {
            binding.ivArrow.setAnimation(Animations.getRotateDownAnimation());
            isUp = true;
        }
    }


    /**
     * 处理全部产品数据
     */
    private void handlerAllProduct(AllProductDTO allProductDTO) {
        if (allProductDTO == null) {
            return;
        }
        for (ProductsInfo info : allProductDTO.getList()) {
            info.setBigType(true);
            popupListMenuUtil.getAdapter().add(info);
            popupListMenuUtil.getAdapter().addAll(info.getChildren());
        }
        popupListMenuUtil.getAdapter().notifyDataSetChanged();
    }


    /**
     * 产品 onclick
     */
    @Override
    public void onItemCallBack(ProductsInfo info, int index) {
        this.productsInfo = info;
        binding.setData(productsInfo);
        popupListMenuUtil.toggleMenu(binding.viewMenu);
    }


    /**
     * 打开产品列表 回调
     */
    @Override
    public void onShow() {
        setArrowAnimation();
    }


    /**
     * 关闭产品列表 回调
     */
    @Override
    public void onDismiss() {
        setArrowAnimation();
    }
}
