package com.gzliangce.ui.activity.calculator;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCalcuationOldHouseBinding;
import com.gzliangce.dto.RateListDTO;
import com.gzliangce.entity.HouseInfo;
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
 * 税费计算器 - 二手房计算结果
 */
public class CalcuationResultsOldHouseActivity extends BaseSwipeBackActivityBinding implements OnChartValueSelectedListener {
    private ActivityCalcuationOldHouseBinding binding;
    private HouseInfo houseInfo;
    private double taxSum;
    private List<Integer> sellColor = new ArrayList<>();
    private List<Integer> buyColor = new ArrayList<>();
    private List<Float> sellData = new ArrayList<>();
    private List<Float> buyData = new ArrayList<>();

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calcuation_old_house);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.tvHousingTax.setOnClickListener(onSingleClickListener);
        binding.tvVat.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        houseInfo = (HouseInfo) getIntent().getSerializableExtra(Constants.CALCUATION_RESULT_OLD_HOUSE);
        initSellData();
        initBuyData();
    }

    /**
     * 点击事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_vat:
                    actionHintActivity(false);
                    break;
                case R.id.tv_housing_tax:
                    actionHintActivity(true);
                    break;
            }
        }
    };


    /**
     * 初始化卖家税收
     */
    private void initSellData() {
        double stampTax = getYinHuaShui(true);//印花税
        double individualIncomeTax = getGeRenShuiDeShui();//个人所得税
        double businessTax = getYinYeShuiSum();//营业税
        double poundageFee = getFangWuMaiMaiShouXuFei(2);//手续费
        double zongHeDiJiaShui = getZongHeDiJiaShui();//综合地价税
        taxSum = zongHeDiJiaShui + individualIncomeTax + stampTax + businessTax + poundageFee;
        if (stampTax > 0) {
            sellColor.add(AppContext.me().getResources().getColor(R.color.green_7e));
            sellData.add((float) (stampTax / taxSum * 360));
            binding.tvSellStampTax.setText(Math.round(stampTax / 100) + "");
        } else {
            binding.tvSellStampTax.setText("目前免征");
            binding.tvSellStamp.setVisibility(View.GONE);
        }
        addDataPieChat(sellData, sellColor, R.color.blue_00a, taxSum, businessTax);
        addDataPieChat(sellData, sellColor, R.color.orange_f8, taxSum, individualIncomeTax);
        addDataPieChat(sellData, sellColor, R.color.purple_dc, taxSum, zongHeDiJiaShui);
        addDataPieChat(sellData, sellColor, R.color.red_fa4, taxSum, poundageFee);
        binding.tvSellBusinessTax.setText(Math.round(businessTax) + "");
        binding.tvSellIndividualIncomeTax.setText(Math.round(individualIncomeTax) + "");
        binding.tvSellLandtax.setText(Math.round(zongHeDiJiaShui) + "");
        binding.tvSellPoundageFee.setText(Math.round(poundageFee) + "");
        PieChartUtil.initPieChart(binding.piSellPiechare, sellData, sellColor, getCenterSpannableText("卖方缴纳"), this);
        logger.d("totalFee:" + taxSum + "--individualIncomeTax:" + individualIncomeTax + "--stampTax:" + stampTax + "--businessTax" + businessTax + "--poundageFee" + poundageFee);
    }


    /**
     * 初始化买家税收
     */
    private void initBuyData() {
        if (houseInfo == null) {
            return;
        }
        double totalFee;//总费用
        double deedTax = getHouseSum() * houseInfo.getQiShuiRate() / 100;//契税
        double poundageFee = getFangWuMaiMaiShouXuFei(2);//手续费
        double registrationFee = getRegistrationFee();//登记费
        double stampTax = getYinHuaShui(false);//印花税
        double trendStampTax = houseInfo.getOldHouseGongBenYinHuaShuiRate();//工本印花税
        totalFee = deedTax + poundageFee + registrationFee + stampTax + trendStampTax;
        taxSum += totalFee;
        addDataPieChat(buyData, buyColor, R.color.green_7e, totalFee, deedTax);
        if (stampTax > 0) {
            buyColor.add(AppContext.me().getResources().getColor(R.color.blue_00a));
            buyData.add((float) (stampTax / totalFee * 360));
            binding.tvBuyStampTax.setText(Math.round(stampTax / 100) + "");
        } else {
            binding.tvBuyStampTax.setText("目前免征");
            binding.tvBuyStamp.setVisibility(View.GONE);
        }
        addDataPieChat(buyData, buyColor, R.color.orange_f8, totalFee, trendStampTax);
        addDataPieChat(buyData, buyColor, R.color.purple_dc, totalFee, registrationFee);
        addDataPieChat(buyData, buyColor, R.color.red_fa4, totalFee, poundageFee);
        binding.tvBuyRegistrationFee.setText(Math.round(registrationFee) + "");
        binding.tvTrendStampTax.setText(Math.round(trendStampTax) + "");
        binding.tvBuyPoundageFee.setText(Math.round(poundageFee) + "");
        binding.tvBuyDeedTax.setText(Math.round(deedTax) + "");
        binding.tvTotalSum.setText(Math.round(taxSum) + "");
        PieChartUtil.initPieChart(binding.piBuyPiechare, buyData, buyColor, getCenterSpannableText("买方缴纳"), this);
    }


    /**
     * 获取登记费
     */
    private double getRegistrationFee() {
        if (houseInfo.getHouseType() == 1) {
            return 550;
        }
        return 50;
    }

    /**
     * 添加饼图数据
     *
     * @param color   颜色数据集合
     * @param data    数据集合
     * @param colorId 颜色值Id
     * @param shuiFee 单种税费
     * @param sum     税费总和
     */
    private void addDataPieChat(List<Float> data, List<Integer> color, int colorId, double sum, double shuiFee) {
        if (shuiFee > 1) {
            color.add(AppContext.me().getResources().getColor(colorId));
            data.add((float) (shuiFee / sum * 360));
        }
    }

    /**
     * 房屋买卖印花税
     */
    private double getYinHuaShui(boolean isSell) {
        double sum = 0;
        if (houseInfo.getHouseType() != 1) {
            return sum;
        }
        if (isSell) {
            sum = getHouseSum() * houseInfo.getOldHouseYinHuaShuiRate_sell();
        } else {
            sum = getHouseSum() * houseInfo.getOldHouseYinHuaShuiRate_buy();
        }
        logger.e("getYinHuaShui:" + sum);
        return sum;
    }


    /**
     * 房屋买卖手续费
     */
    private double getFangWuMaiMaiShouXuFei(double rate) {
        return houseInfo.getHouseAera() * rate;
    }


    /**
     * 获取个人所得税
     */
    private double getGeRenShuiDeShui() {
        if (houseInfo.getHouseType() == 1 || houseInfo.getHouseType() != 1 && !houseInfo.isOnlyHouse() || houseInfo.getHouseType() != 1 && houseInfo.getHouseYear() != 2) {
            return getHouseSum() * houseInfo.getOldHouseGeRenShuoDeShuiRate() / 100;
        }
        return 0;
    }


    /**
     * 根据房屋计价方式处理房屋价格
     */
    private double getHouseSum() {
        double sum = houseInfo.getHouseAera() * houseInfo.getHouseFee();//总价
        if (!houseInfo.isValuationType()) {//差价
            sum = houseInfo.getHouseAera() * houseInfo.getHouseFee() - houseInfo.getOriginalPrice();
        }
        return sum;
    }


    /**
     * 获取房屋综合地价税
     */
    private double getZongHeDiJiaShui() {
        double sum = 0;
        if (houseInfo.getHouseYear() == 2 && houseInfo.getHouseType() == 2) {
            sum = getHouseSum() * houseInfo.getOldHouseZongHeDiJiaRate() / 100;
        }
        return sum;
    }


    /**
     * 获取房屋营业税
     */
    private double getYinYeShuiSum() {
        double sum = 0;
        if (houseInfo.getHouseType() == 1 || houseInfo.getHouseYear() == 0) {
            sum = getHouseSum() * houseInfo.getOldHouseYingYeShuiRate() / 100;
            return sum;
        }
        return sum;
    }

    /**
     * 跳转到税费说明界面
     */
    private void actionHintActivity(boolean isHouseTips) {
        String url;
        String title = "税费说明";
        RateListDTO rateListDTO = (RateListDTO) MetadataUtil.getGCache(this, Constants.RATE_LIST_DATA);
        if (rateListDTO == null) {
            return;
        }
        url = rateListDTO.getLandValueIncrementTax();
        if (isHouseTips) {
            url = rateListDTO.getSecondHandHouseTax();
        }
        Intent intent = new Intent(CalcuationResultsOldHouseActivity.this, WebActivity.class);
        intent.putExtra(Constants.ABOUT_TEXT_URL, url);
        intent.putExtra(Constants.ABOUT_TEXT_TITLE, title);
        startActivity(intent);
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
     * 饼图中间所显示的文字
     */
    public SpannableString getCenterSpannableText(String str1) {
        SpannableString s = new SpannableString(str1);
        s.setSpan(new AbsoluteSizeSpan(AppContext.me().getResources().getDimensionPixelSize(R.dimen.font_14), true), 0, str1.length(), 0);
        s.setSpan(new ForegroundColorSpan(AppContext.me().getResources().getColor(R.color.gray_95)), 0, str1.length(), 0);
        return s;
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

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
