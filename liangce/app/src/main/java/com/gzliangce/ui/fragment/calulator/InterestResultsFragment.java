package com.gzliangce.ui.fragment.calulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentCalculationResultsBinding;
import com.gzliangce.entity.DetailedMonthActivityInfo;
import com.gzliangce.entity.LoanInfo;
import com.gzliangce.entity.MonthPaymentslyInfo;
import com.gzliangce.ui.activity.calculator.CalculationResultsActivity;
import com.gzliangce.ui.activity.calculator.DetailMonthActivity;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MathUtils;
import com.gzliangce.util.PieChartUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/2/14.
 * 等额本息界面
 */
public class InterestResultsFragment extends BaseFragment implements OnChartValueSelectedListener {
    private Logger logger = LoggerFactory.getLogger(InterestResultsFragment.class);
    private FragmentCalculationResultsBinding binding;
    private int index;
    private boolean isCombination;
    private List<LoanInfo> loanInfoList = new ArrayList<>();
    private DetailedMonthActivityInfo otherDetailedInfo;
    private ArrayList<MonthPaymentslyInfo> month = new ArrayList<>();

    public List<LoanInfo> getLoanInfoList() {
        return loanInfoList;
    }

    public void setLoanInfoList(List<LoanInfo> loanInfoList) {
        this.loanInfoList = loanInfoList;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCombination(boolean combination) {
        isCombination = combination;
    }

    public static InterestResultsFragment getInstance(int index, boolean isCombination, List<LoanInfo> list) {
        InterestResultsFragment fragment = new InterestResultsFragment();
        fragment.setIndex(index);
        fragment.setLoanInfoList(list);
        fragment.setCombination(isCombination);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalculationResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_calculation_results;
    }


    @Override
    public void initView() {
        if (isCombination) {
            otherDetailedInfo = getCombinationDetailedInfo(loanInfoList);
        } else {
            otherDetailedInfo = getLoanPerMonth(loanInfoList.get(0));
        }
        setData();
        otherDetailedInfo.setLoanInfoList(loanInfoList);
    }

    @Override
    public void initListener() {
        binding.tvDetailedMonthly.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                if (isCombination) {
                    month.addAll(DetailMonthActivity.setCombinationMonthData(otherDetailedInfo, isCombination, index));
                } else {
                    month.addAll(DetailMonthActivity.getAllDetailData(otherDetailedInfo, otherDetailedInfo.getLoanInfoList().get(0), isCombination, 0, index));
                }
                setPieChartData();
            }
        });
    }


    /**
     * 设置饼图数据
     */
    private void setPieChartData() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPieChartData(otherDetailedInfo);
            }
        });
    }


    /**
     * 设置一些基本的提示数据及状态
     */
    private void setData() {
        otherDetailedInfo.setIndex(index);
        if (index == 0) {
            binding.tvTips.setText("Tips：每月还款金额固定，所还总利息较多");
        } else {
            binding.tvTips.setText("Tips：每月还款本金固定，所还总利息较少");
        }
        if (isCombination) {
            binding.llyInterest.setVisibility(View.GONE);
        }
    }


    /**
     * 获取等额本息饼图
     */
    private DetailedMonthActivityInfo getLoanPerMonth(LoanInfo loanInfo) {
        DetailedMonthActivityInfo detailedInfo = new DetailedMonthActivityInfo();
        double loanPerMonth;//月均还款
        double repaymentSum;//还款总额，单位：万元
        double interestPayment;//支付利息款,单位：元
        if (index == 0) {
            loanPerMonth = PieChartUtil.getLoanPerMonth(loanInfo);//月均还款
            logger.e("等额本息：月均还款：" + loanPerMonth);
            repaymentSum = loanPerMonth * loanInfo.getMortgageMonth() / 10000;//还款总额，单位：万元
            interestPayment = repaymentSum * 10000 - loanInfo.getLoanSum();//支付利息款,单位：元
            logger.e("loanPerMonth0----" + loanPerMonth);
        } else {
            interestPayment = PieChartUtil.getPrincipalInterestPayment(loanInfo);
            loanPerMonth = PieChartUtil.getPrincipalMonth(loanInfo);
            repaymentSum = (interestPayment + loanInfo.getLoanSum()) / 10000;//还款总额，单位：万元
            logger.e("loanPerMonth1----" + loanPerMonth);
        }
        detailedInfo.setLoanPerMonth(loanPerMonth);
        detailedInfo.setLoanSum(loanInfo.getLoanSum());
        detailedInfo.setMortgageMonth(loanInfo.getMortgageMonth());
        detailedInfo.setInterestPayment(interestPayment);
        detailedInfo.setRepaymentSum(repaymentSum);
        return detailedInfo;
    }

    /**
     * 获取组合贷款数据对象
     */
    private DetailedMonthActivityInfo getCombinationDetailedInfo(List<LoanInfo> loanInfoList) {
        DetailedMonthActivityInfo aInfo = getLoanPerMonth(loanInfoList.get(0));
        DetailedMonthActivityInfo bInfo = getLoanPerMonth(loanInfoList.get(1));
        DetailedMonthActivityInfo resultInfo = new DetailedMonthActivityInfo();
        resultInfo.setLoanSum(aInfo.getLoanSum() + bInfo.getLoanSum());
        resultInfo.setOneLoanPerMonth(aInfo.getLoanPerMonth());
        resultInfo.setTowLoanPerMonth(bInfo.getLoanPerMonth());
        resultInfo.setRepaymentSum(aInfo.getRepaymentSum() + bInfo.getRepaymentSum());
        resultInfo.setLoanPerMonth(aInfo.getLoanPerMonth() + bInfo.getLoanPerMonth());
        resultInfo.setInterestPayment(aInfo.getInterestPayment() + bInfo.getInterestPayment());
        resultInfo.setMortgageMonth(aInfo.getMortgageMonth());
        resultInfo.setIndex(index);
        resultInfo.setLoanInfoList(loanInfoList);
        return resultInfo;
    }

    /**
     * 设置饼图数据
     */
    private void setPieChartData(DetailedMonthActivityInfo info) {
        if (info == null) {
            return;
        }
        if (month.size() > 0) {
            binding.tvInitialPayment.setText(Math.round(month.get(0).getPayments()) + "");
        }
        binding.tvTotalLoan.setText(MathUtils.div(info.getLoanSum(), 10000, 2) + "");
        binding.tvInterest.setText(MathUtils.mul(loanInfoList.get(0).getInterestrate(), 100) + "");
        binding.tvInterestPayment.setText(Math.round(info.getInterestPayment()) + "");
        binding.tvReimbursement.setText(MathUtils.getBigTwoDouble(info.getRepaymentSum()) + "");
        List<Float> list = getDataList(info.getLoanSum(), info.getInterestPayment());
        List<Integer> color = getColors();
        binding.tvMoney.setText(Math.round(info.getLoanPerMonth()) + "元");
        PieChartUtil.initPieChart(binding.piPiechare, list, color, PieChartUtil.getCenterSpannableText("平均月供", Math.round(info.getLoanPerMonth()) + "元"), this);
    }


    private List<Float> getDataList(double interestPayment, double repaymentSum) {
        List<Float> data = new ArrayList<>();
        float sum = (float) (interestPayment + repaymentSum);
        data.add((float) (interestPayment / sum * 360));
        data.add((float) (repaymentSum / sum * 360));
        return data;
    }

    private List<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.blue_00a));
        colors.add(getResources().getColor(R.color.orange_f8));
        return colors;
    }


    /**
     * 饼图块点击接口回调
     */
    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        ToastHelper.showMessage(getActivity(), LiangCeUtil.getPrecisionData(entry.getVal() / 360) + "%");
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
            Intent intent = new Intent(getActivity(), DetailMonthActivity.class);
            intent.putExtra(Constants.INTENT_DETAIL_MONTH_ACTIVITY_INFO, otherDetailedInfo);
            intent.putExtra(Constants.FRAGMENT_INDEX, index);
            intent.putExtra(Constants.PAYMENT_MONTHLY, month);
            intent.putExtra(Constants.REQUEST_CALCULATION_TYPE, isCombination);
            startActivity(intent);
        }
    };
}
