package com.gzliangce.ui.activity.calculator;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCalcuationNewHouseBinding;
import com.gzliangce.dto.RateListDTO;
import com.gzliangce.entity.HouseInfo;
import com.gzliangce.entity.NewHouseTaxFeeInfo;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MetadataUtil;
import com.gzliangce.util.PieChartUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;

/**
 * Created by leo on 16/2/15.
 * 税费计算器 - 新房计算结果
 */
public class CalcuationResultsNewHouseActivity extends BaseSwipeBackActivityBinding implements OnChartValueSelectedListener {
    private ActivityCalcuationNewHouseBinding binding;
    private HouseInfo houseInfo;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calcuation_new_house);
        setHeader();
    }

    @Override
    public void initView() {
        houseInfo = (HouseInfo) getIntent().getSerializableExtra(Constants.CALCUATION_RESULT_NEW_HOUSE);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.tvTips.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        NewHouseTaxFeeInfo newHouseTaxFeeInfo = getCalcuationResult(houseInfo);
        if (newHouseTaxFeeInfo != null) {
            PieChartUtil.initPieChart(binding.piPiechare, getData(newHouseTaxFeeInfo), getColor(), null, this);
            binding.tvDeedTax.setText(Math.round(newHouseTaxFeeInfo.getDeedTax()) + "");
            binding.tvPoundageFee.setText(Math.round(newHouseTaxFeeInfo.getPoundageFee()) + "");
            binding.tvStampTax.setText(Math.round(newHouseTaxFeeInfo.getStampTax()) + "");
            binding.tvPropertyFee.setText(Math.round(newHouseTaxFeeInfo.getPropertyFee()) + "");
            binding.tvFairFee.setText(Math.round(newHouseTaxFeeInfo.getFairFee()) + "");
            binding.tvTotal.setText(Math.round(newHouseTaxFeeInfo.getTotalFee()) + "");
        }

    }


    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("计算结果");
        header.setLeftIcon(R.drawable.ic_back);
        binding.setHeader(header);
    }

    /**
     * 获取数据比例
     */
    private List<Float> getData(NewHouseTaxFeeInfo info) {
        List<Float> data = new ArrayList<>();
        if (info == null) {
            data.add(360f);
        } else {
            data.add((float) (info.getDeedTax() / info.getTotalFee() * 360));
            data.add((float) (info.getStampTax() / info.getTotalFee() * 360));
            data.add((float) (info.getFairFee() / info.getTotalFee()) * 360);
            data.add((float) (info.getPropertyFee() / info.getTotalFee()) * 360);
            data.add((float) (info.getPoundageFee() / info.getTotalFee()) * 360);
        }
        return data;
    }


    /**
     * 获取饼图颜色数据
     */
    public List<Integer> getColor() {
        List<Integer> color = new ArrayList<>();
        color.add(AppContext.me().getResources().getColor(R.color.blue_00a));
        color.add(AppContext.me().getResources().getColor(R.color.orange_f8));
        color.add(AppContext.me().getResources().getColor(R.color.green_7e));
        color.add(AppContext.me().getResources().getColor(R.color.purple_dc));
        color.add(AppContext.me().getResources().getColor(R.color.red_fa4));
        return color;
    }

    /**
     * 饼图块点击接口回调
     */
    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        ToastHelper.showMessage(this, LiangCeUtil.getPrecisionData(entry.getVal() / 360) + "%");
    }

    @Override
    public void onNothingSelected() {

    }


    /**
     * 点击事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            RateListDTO rateListDTO = (RateListDTO) MetadataUtil.getGCache(CalcuationResultsNewHouseActivity.this, Constants.RATE_LIST_DATA);
            if (rateListDTO == null) {
                return;
            }
            Intent intent = new Intent(CalcuationResultsNewHouseActivity.this, WebActivity.class);
            intent.putExtra(Constants.ABOUT_TEXT_URL, rateListDTO.getNewHouseTax());
            intent.putExtra(Constants.ABOUT_TEXT_TITLE, "税费说明");
            startActivity(intent);
        }
    };

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    /**
     * 获取税费计算结果
     */
    private NewHouseTaxFeeInfo getCalcuationResult(HouseInfo houseInfo) {
        NewHouseTaxFeeInfo info = new NewHouseTaxFeeInfo();
        double deedTax;//契税
        double stampTax;//印花税
        double fairFee;//公正费
        double propertyFee;//委托办理产权费
        double poundageFee;//房屋买卖手续费
        double totalFee;//总费用
        if (houseInfo == null) {
            return null;
        }
        deedTax = getSum(houseInfo, houseInfo.getQiShuiRate());
        fairFee = PieChartUtil.getNewHouseFairFee(houseInfo);
        poundageFee = houseInfo.getHouseAera() * 3;
        propertyFee = getSum(houseInfo, houseInfo.getWeiTuoBanLiRate());
        stampTax = getSum(houseInfo, houseInfo.getYinHuaShuiRate());
        totalFee = deedTax + stampTax + fairFee + poundageFee + propertyFee;
        info.setDeedTax(deedTax);
        info.setFairFee(fairFee);
        info.setPoundageFee(poundageFee);
        info.setStampTax(stampTax);
        info.setPropertyFee(propertyFee);
        info.setTotalFee(totalFee);
        return info;
    }


    /**
     * 获取费用，适用于新房除房屋买卖手续以外的费用
     */
    public double getSum(HouseInfo info, double rate) {
        double sum = info.getHouseAera() * info.getHouseFee() * rate / 100;
        return sum;
    }
}
