package com.gzliangce.ui.fragment.calulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentNewHouseBinding;
import com.gzliangce.entity.HouseInfo;
import com.gzliangce.ui.activity.calculator.CalcuationResultsNewHouseActivity;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.RateUtil;
import com.leo.gesturelibray.util.StringUtils;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/29.
 * 新房
 */
public class newHouseFrgment extends BaseFragment implements OptionsPickerView.OnOptionsSelectListener, CompoundButton.OnCheckedChangeListener {
    private Logger logger = LoggerFactory.getLogger(newHouseFrgment.class);
    private FragmentNewHouseBinding binding;
    private OptionsPickerView optionsPickerView;
    private int houseArea = 100;
    private int housePrice = 10000;
    private int houseType = 0;
    private boolean isOnlyHouse = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewHouseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initView() {
        binding.tvCalulator.setSelected(true);
        binding.tvCalulator.setEnabled(true);
    }

    @Override
    public void initListener() {
        binding.ckIsOnly.setOnCheckedChangeListener(this);
        binding.tvCalulator.setOnClickListener(onSingleClickListener);
        binding.flyHouseType.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        LiangCeUtil.setEditSelection(binding.etArea);
        LiangCeUtil.setEditSelection(binding.etPirce);
    }

    /**
     * onClick
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fly_house_type:
                    optionsPickerView = DialogUtil.showPickerView(getActivity(), DialogUtil.getHouseTypeData(), houseType, newHouseFrgment.this);
                    break;
                case R.id.tv_calulator:
                    actionCalcuationNewHouseActivity();
                    break;
            }
        }
    };

    /**
     * 跳转到计算结果界面
     */
    private void actionCalcuationNewHouseActivity() {
        HouseInfo info = getCalcuationResult();
        if (!isActionCalcuationResult()) {
            return;
        }
        if (info != null) {
            Intent intent = new Intent(getActivity(), CalcuationResultsNewHouseActivity.class);
            intent.putExtra(Constants.CALCUATION_RESULT_NEW_HOUSE, info);
            startActivity(intent);
        }
    }

    /**
     * 获取税费计算结果
     */
    private HouseInfo getCalcuationResult() {
        HouseInfo info = new HouseInfo();
        info.setHouseType(houseType);
        info.setHouseFee(housePrice);
        info.setOnlyHouse(isOnlyHouse);
        info.setHouseAera(houseArea);
        info.setGongZhengFeiRate(RateUtil.getGongzhengFeiRate(info));
        info.setMaiMaiRata(RateUtil.getNewMaiMaiRate(info));
        info.setQiShuiRate(RateUtil.getNewHouseQiShuiRate(info));
        info.setYinHuaShuiRate(RateUtil.getNewStampRate(info));
        info.setWeiTuoBanLiRate(RateUtil.getweiTuoBanLiRate(info));
        return info;
    }


    /**
     * 判断是否满足跳转条件
     */
    private boolean isActionCalcuationResult() {
        String area = binding.etArea.getText().toString().trim();
        String pirce = binding.etPirce.getText().toString().trim();
        if (StringUtils.isEmpty(area)) {
            ToastHelper.showMessage(getActivity(), "房屋面积不能为空");
            return false;
        }
        if (StringUtils.isEmpty(pirce)) {
            ToastHelper.showMessage(getActivity(), "房屋单价不能为空");
            return false;
        }
        houseArea = Integer.parseInt(area);
        housePrice = Integer.parseInt(pirce);
        if (houseArea <= 0) {
            ToastHelper.showMessage(getActivity(), "房屋面积不能为0");
            return false;
        }
        if (housePrice <= 0) {
            ToastHelper.showMessage(getActivity(), "房屋单价不能为0");
            return false;
        }
        return true;
    }


    /**
     * 房产类型选择回调
     */
    @Override
    public void onOptionsSelect(int options1, final int option2, int options3) {
        houseType = options1;
        binding.tvHouseType.setText(DialogUtil.getHouseTypeData().get(options1));
    }


    @Override
    public boolean onBackPressed() {
        if (optionsPickerView != null && optionsPickerView.isShowing()) {
            optionsPickerView.dismiss();
            return true;
        }
        return super.onBackPressed();
    }

    /**
     * 是否是唯一住房
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isOnlyHouse = isChecked;
    }
}
