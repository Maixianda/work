package com.gzliangce.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.entity.HouseInfo;
import com.gzliangce.entity.LoanInfo;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/2/14.
 */
public class PieChartUtil {
    private static Logger logger = LoggerFactory.getLogger(PieChartUtil.class);

    /**
     * 初始化饼图配置
     */
    public static void initPieChart(PieChart pieChart, List<Float> dataList, List<Integer> colors, SpannableString spannableString, OnChartValueSelectedListener onChartValueSelectedListener) {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
//        pieChart.setExtraOffsets(10, 10, 10, 10);设置边距
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(61f);
        if (spannableString == null) {
            pieChart.setDrawCenterText(false);
        } else {
            pieChart.setDrawCenterText(false);
            pieChart.setCenterText(spannableString);
        }
        // enable rotation of the chart by touch
        //是否可以触摸转动饼图
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setDrawSliceText(false);
        // add a selection listener
        pieChart.setOnChartValueSelectedListener(onChartValueSelectedListener);
        //设置是否显示文字提示
        pieChart.getLegend().setEnabled(false);
        setPieChartData(dataList, colors, pieChart);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    /**
     * 设置饼图数据
     */
    public static void setPieChartData(List<Float> dataList, List<Integer> colors, PieChart pieChart) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            yVals1.add(new Entry(dataList.get(i), i));
            xVals.add("");
        }
        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        //设置是否显示百分比
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(2f);
        dataSet.setColors(colors);
        //设置饼图块文字显示
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }


    /**
     * 饼图中间所显示的文字
     */
    public static SpannableString getCenterSpannableText(String str1, String str2) {
        SpannableString s = new SpannableString(str1 + "\n" + str2);
        s.setSpan(new AbsoluteSizeSpan(AppContext.me().getResources().getDimensionPixelSize(R.dimen.font_14), true), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(AppContext.me().getResources().getColor(R.color.gray_95)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(AppContext.me().getResources().getDimensionPixelSize(R.dimen.font_16), true), str1.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(AppContext.me().getResources().getColor(R.color.black_0a)), str1.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }


    /**
     * 获取饼图颜色数据
     */
    public static List<Integer> getColor() {
        List<Integer> color = new ArrayList<>();
        color.add(AppContext.me().getResources().getColor(R.color.blue_00a));
        color.add(AppContext.me().getResources().getColor(R.color.orange_f8));
        color.add(AppContext.me().getResources().getColor(R.color.green_7e));
        return color;
    }


    /**
     * 获取等额本息月均还款数
     */
    public static double getLoanPerMonth(LoanInfo loanInfo) {
        double monthInterest = (float) (loanInfo.getInterestrate() / 12);
        //月均还款
        double loanPerMonth = loanInfo.getLoanSum() / (Math.pow((1 + monthInterest), loanInfo.getMortgageMonth()) - 1) * (monthInterest * Math.pow((1 + monthInterest), loanInfo.getMortgageMonth()));
        return loanPerMonth;
    }


//    /**
//     * 获取等额本息月均还款数
//     */
//    public static double getLoanPerMonths(LoanInfo loanInfo) {
//        float monthInterest = (float) (loanInfo.getInterestrate() / 12);
//        //月均还款
//        double loanPerMonth = loanInfo.getLoanSum() * monthInterest * Math.pow(1+monthInterest,loanInfo.getMortgageMonth()) / (Math.pow(1 + monthInterest, loanInfo.getMortgageMonth()) - 1);
//        return loanPerMonth;
//    }




    /**
     * 获取等额本金月均还款数
     */
    public static double getPrincipalMonth(LoanInfo loanInfo) {
        //还款月数+1）*贷款额*月利率/2
        double interestPayment = getPrincipalInterestPayment(loanInfo);
        return (loanInfo.getLoanSum() + interestPayment) / loanInfo.getMortgageMonth();
    }


    /**
     * 获取等额本金总利息
     */
    public static double getPrincipalInterestPayment(LoanInfo loanInfo) {
        double loanSum = loanInfo.getLoanSum();
        double interest = loanInfo.getInterestrate();
        double month = loanInfo.getMortgageMonth();
        double monthInterest = interest / 12;
        //总利息：还款月数+1）*贷款额*月利率/2
        return (month + 1) * loanSum * monthInterest / 2;
    }


    /**
     * 获取房屋公证费（新房）
     *
     * @param houseInfo 房屋数据对象
     */
    public static double getNewHouseFairFee(HouseInfo houseInfo) {
        int houseArea = houseInfo.getHouseAera();//房屋面积
        int houseFee = houseInfo.getHouseFee();//房屋单价
        double fairFee = houseArea * houseFee * (houseInfo.getGongZhengFeiRate() / 100);
        if (fairFee < 200) {
            fairFee = 200;
        }
        return fairFee;
    }


}
