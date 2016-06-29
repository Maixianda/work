package com.gzliangce.ui.fragment.calulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.repacked.antlr.v4.runtime.tree.Tree;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentOldHouseBinding;
import com.gzliangce.entity.HouseInfo;
import com.gzliangce.ui.activity.calculator.CalcuationResultsOldHouseActivity;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.RateUtil;
import com.leo.gesturelibray.util.StringUtils;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/1/26.
 * 二手房界面
 */
public class OldHouseFragment extends BaseFragment implements OptionsPickerView.OnOptionsSelectListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private FragmentOldHouseBinding binding;
    private OptionsPickerView optionsPickerView;
    private boolean isHouseType;
    private int houseType = 0;//房屋类型
    private int houseYear = 0;//房屋年限
    private int houseAera = 100;//房屋面积
    private int houseFee = 10000;//房屋单价
    private boolean isFristBuy = true;//是否是第一次购买
    private boolean isOnlyHouse = true;//是否是唯一住房
    private int originalPrice;//房屋原价
    private boolean valuationType = true;//计价方式,true总价，false差价

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOldHouseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initView() {
        binding.tvCalulator.setEnabled(true);
        binding.tvCalulator.setSelected(true);
    }

    @Override
    public void initListener() {
        binding.flyHouseType.setOnClickListener(onSingleClickListener);
        binding.rbDifference.setOnClickListener(onSingleClickListener);
        binding.flyHouseYear.setOnClickListener(onSingleClickListener);
        binding.rbTotalPrice.setOnClickListener(onSingleClickListener);
        binding.tvCalulator.setOnClickListener(onSingleClickListener);
        binding.ckIsFrist.setOnCheckedChangeListener(this);
        binding.ckIsOnly.setOnCheckedChangeListener(this);
        binding.rgPrice.setOnCheckedChangeListener(this);

    }

    @Override
    public void initData() {
        LiangCeUtil.setEditSelection(binding.etHouseArea);
        LiangCeUtil.setEditSelection(binding.etHouseFee);
        LiangCeUtil.setEditSelection(binding.etOriginalPrice);
    }

    /**
     * onClick
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fly_house_type:
                    isHouseType = true;
                    optionsPickerView = DialogUtil.showPickerView(getActivity(), DialogUtil.getHouseTypeData(), houseType, OldHouseFragment.this);
                    break;
                case R.id.tv_calulator:
                    actionResultsOldHouseActivity();
                    break;
                case R.id.fly_house_year:
                    isHouseType = false;
                    optionsPickerView = DialogUtil.showPickerView(getActivity(), DialogUtil.getHouseYearData(), houseYear, OldHouseFragment.this);
                    break;
            }
        }
    };

    /**
     * 跳转到计算结果界面
     */
    private void actionResultsOldHouseActivity() {
        if (!isActionActivity()) {
            return;
        }
        Intent intent = new Intent(getActivity(), CalcuationResultsOldHouseActivity.class);
        HouseInfo info = new HouseInfo();
        info.setFristBuy(isFristBuy);
        info.setOnlyHouse(isOnlyHouse);
        info.setHouseAera(houseAera);
        info.setHouseFee(houseFee);
        info.setOriginalPrice(originalPrice);
        info.setHouseType(houseType);
        info.setHouseYear(houseYear);
        info.setValuationType(valuationType);

        info.setQiShuiRate(RateUtil.getOldHouseQiShuiRate(info));
        info.setOldHouseYingYeShuiRate(RateUtil.getSellOldYinYeShuiRate(info));
        info.setOldHouseFangWuMaiMaiDengJiFeiRate(RateUtil.getOldHouseFangWuMaiMaiDengJiFeiRate(info));
        info.setOldHouseFangWuMaiMaiShouXuFeiRate_buy(RateUtil.getOldHouseFangWuMaiMaiShouXuFeiRate(info, false));
        info.setOldHouseFangWuMaiMaiShouXuFeiRate_sell(RateUtil.getOldHouseFangWuMaiMaiShouXuFeiRate(info, true));
        info.setOldHouseGeRenShuoDeShuiRate(RateUtil.getOldHouseGeRenShuoDeShuiRate(houseType));
        info.setOldHouseGongBenYinHuaShuiRate(RateUtil.getOldHouseGongBengYinHuaShuiRate());
        info.setOldHouseZongHeDiJiaRate(RateUtil.getOldHouseZongHeDiJiaRate(info));
        info.setOldHouseYinHuaShuiRate_buy(RateUtil.getOldHouseYinHuaShuiRate(houseType,true));
        info.setOldHouseYinHuaShuiRate_sell(RateUtil.getOldHouseYinHuaShuiRate(houseType,false));
        intent.putExtra(Constants.CALCUATION_RESULT_OLD_HOUSE, info);
        startActivity(intent);
    }

    /**
     * 是否满足计算房税条件
     */
    private boolean isActionActivity() {
        String area = binding.etHouseArea.getText().toString().trim();
        String fee = binding.etHouseFee.getText().toString().trim();
        if (StringUtils.isEmpty(area)) {
            ToastHelper.showMessage(getActivity(), "房屋面积不能为空");
            return false;
        }
        if (StringUtils.isEmpty(fee)) {
            ToastHelper.showMessage(getActivity(), "房屋单价不能为空");
            return false;
        }
        houseAera = Integer.parseInt(area);
        houseFee = Integer.parseInt(fee);
        if (houseAera <= 0) {
            ToastHelper.showMessage(getActivity(), "房屋单价不能为0");
            return false;
        }
        if (houseFee <= 0) {
            ToastHelper.showMessage(getActivity(), "房屋单价不能为0");
            return false;
        }
        if (houseType == 2 && houseYear != 2) {
            DialogUtil.showHintDialog(getActivity(), "经济适用房未满5年不得上市交易");
            return false;
        }
        if (!valuationType) {//选择了差价计算方式
            String price = binding.etOriginalPrice.getText().toString().trim();
            if (StringUtils.isEmpty(price)) {
                ToastHelper.showMessage(getActivity(), "房屋原价不能为空");
                return false;
            }
            originalPrice = Integer.parseInt(price);
            if (originalPrice <= 0) {
                ToastHelper.showMessage(getActivity(), "房屋原价不能为0");
                return false;
            }
            if (houseAera * houseFee < originalPrice * 10000) {
                ToastHelper.showMessage(getActivity(), "房屋原价不能大于房屋总价");
                return false;
            }
        }
        return true;
    }


    /**
     * 房产类型选择回调
     */
    @Override
    public void onOptionsSelect(final int options1, final int option2, int options3) {
        if (isHouseType) {
            houseType = options1;
            binding.tvHouseType.setText(DialogUtil.getHouseTypeData().get(options1));
        } else {
            houseYear = options1;
            binding.tvHouseYear.setText(DialogUtil.getHouseYearData().get(options1));
        }
    }


    /**
     * RadioGroup事件
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_difference) {
            valuationType = false;
            binding.llyHousingPrice.setVisibility(View.VISIBLE);
        } else {
            valuationType = true;
            binding.llyHousingPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (optionsPickerView != null && optionsPickerView.isShowing()) {
            optionsPickerView.dismiss();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.ck_is_frist) {
            isFristBuy = isChecked;
        } else {
            isOnlyHouse = isChecked;
        }
    }
}
