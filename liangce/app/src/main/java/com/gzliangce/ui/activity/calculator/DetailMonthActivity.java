package com.gzliangce.ui.activity.calculator;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityDetailMonthBinding;
import com.gzliangce.entity.DetailedMonthActivityInfo;
import com.gzliangce.entity.DetailedMonthyInfo;
import com.gzliangce.entity.LoanInfo;
import com.gzliangce.entity.MonthPaymentslyInfo;
import com.gzliangce.ui.adapter.DetailedMonthlyAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/2/17.
 * 详细月供
 */
public class DetailMonthActivity extends BaseSwipeBackActivityBinding {
    private ActivityDetailMonthBinding binding;
    private DetailedMonthlyAdapter adapter;
    private DetailedMonthActivityInfo detailedMonthActivityInfo;
    private ArrayList<MonthPaymentslyInfo> month;
    private int index = 0;
    private boolean isCombination;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_month);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new DetailedMonthlyAdapter(this);
        binding.rvDetailedMonthly.setLayoutManager(new LinearLayoutManager(this));
        month = (ArrayList<MonthPaymentslyInfo>) getIntent().getSerializableExtra(Constants.PAYMENT_MONTHLY);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        isCombination = getIntent().getBooleanExtra(Constants.REQUEST_CALCULATION_TYPE, false);
        detailedMonthActivityInfo = (DetailedMonthActivityInfo) getIntent().getSerializableExtra(Constants.INTENT_DETAIL_MONTH_ACTIVITY_INFO);
        index = getIntent().getIntExtra(Constants.FRAGMENT_INDEX, 0);
        if (detailedMonthActivityInfo != null) {
            adapter.setInfo(detailedMonthActivityInfo);
        }
        adapter.add(new DetailedMonthyInfo());
        if (month != null) {
            getMonthDetailData(month);
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("详细月供");
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    /**
     * 获取每一期的还款数据
     * http://www.zzgjj.gov.cn/jsq/return1.asp 等额本息计算器（结果只作为参考）
     * http://www.zzgjj.gov.cn/jsq/return.asp  等额本金计算器（结果只作为参考）
     * http://www.360doc.com/content/12/0113/17/297861_179199283.shtml 计算公式
     */
    public static ArrayList<MonthPaymentslyInfo> getAllDetailData(DetailedMonthActivityInfo info, LoanInfo loanInfo, boolean isCombination, int mIndex, int fragmentIndex) {
        int month = loanInfo.getMortgageMonth();//按揭期数
        double loanSum = loanInfo.getLoanSum();//贷款总额
        double monthInsterest = loanInfo.getInterestrate() / 12;//月利率
        double payments = info.getLoanPerMonth();//月还款
        if (isCombination) {
            if (mIndex == 0) {
                payments = info.getOneLoanPerMonth();
            } else {
                payments = info.getTowLoanPerMonth();
            }
        }
        ArrayList<MonthPaymentslyInfo> list = new ArrayList<>();
        for (int i = 0; i < month; i++) {
            double principal;//本金
            double interest;//利息
            double remainingLian;//剩余贷款
            //剩余贷款
            if (i == 0) {
                remainingLian = loanSum;
            } else {
                remainingLian = list.get(i - 1).getRemainingLoan();
            }
            if (fragmentIndex == 0) {
                //等额本息
                //每月利息 = 本月余额×月利率
                interest = remainingLian * monthInsterest;
                //每月本金 = 每月还款额-本月还息
                principal = payments - interest;
                //剩余贷款 = 总剩余还款额 - 每月还款额
                remainingLian = remainingLian - principal;
            } else {
                //等额本金
                //每月本金 = 总贷款额 / 分期月数
                principal = loanSum / month;
                //每月利息 = 剩余贷款 * 月利率
                interest = remainingLian * monthInsterest;
                remainingLian = remainingLian - principal;
                //月还款 = 每月本金 + 月利率
                payments = principal + interest;
            }
            list.add(getMnthPaymentsly(payments, interest, remainingLian, principal));
        }
        return list;
    }

    /**
     * 处理组合贷款类型的月供详情
     */
    public static ArrayList<MonthPaymentslyInfo> setCombinationMonthData(DetailedMonthActivityInfo info, boolean isCombination, int fragmentIndex) {
        ArrayList<MonthPaymentslyInfo> dataList = new ArrayList<>();
        //取出公积金和商业贷款单独计算的结果进行累加
        ArrayList<MonthPaymentslyInfo> aMothList = getAllDetailData(info, info.getLoanInfoList().get(0), isCombination, 0, fragmentIndex);
        ArrayList<MonthPaymentslyInfo> bMothList = getAllDetailData(info, info.getLoanInfoList().get(1), isCombination, 1, fragmentIndex);
        for (int i = 0; i < aMothList.size(); i++) {
            MonthPaymentslyInfo aInfo = aMothList.get(i);
            MonthPaymentslyInfo bInfo = bMothList.get(i);
            MonthPaymentslyInfo dataInfo = new MonthPaymentslyInfo();
            dataInfo.setMonth(aInfo.getMonth());
            dataInfo.setPayments(aInfo.getPayments() + bInfo.getPayments());
            dataInfo.setPrincipal(aInfo.getPrincipal() + bInfo.getPrincipal());
            dataInfo.setInterest(aInfo.getInterest() + bInfo.getInterest());
            dataInfo.setRemainingLoan(aInfo.getRemainingLoan() + bInfo.getRemainingLoan());
            dataList.add(dataInfo);
        }
        return dataList;
    }


    /**
     * 获取MonthPaymentslyInfo 数据对象
     */
    public static MonthPaymentslyInfo getMnthPaymentsly(double payments, double interest, double remainingLian, double principal) {
        MonthPaymentslyInfo paymentslyInfo = new MonthPaymentslyInfo();
        paymentslyInfo.setPayments(payments);
        paymentslyInfo.setInterest(interest);
        paymentslyInfo.setRemainingLoan(remainingLian);
        paymentslyInfo.setPrincipal(principal);
        return paymentslyInfo;
    }

    /**
     * 将所有分期数据，分割成年份
     */
    private void getMonthDetailData(ArrayList<MonthPaymentslyInfo> list) {
        int size = list.size() / 12;
        for (int i = 0; i < size; i++) {
            DetailedMonthyInfo info = new DetailedMonthyInfo();
            info.setData(list.subList(i * 12, (i + 1) * 12));
            adapter.add(info);
        }
        binding.rvDetailedMonthly.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
