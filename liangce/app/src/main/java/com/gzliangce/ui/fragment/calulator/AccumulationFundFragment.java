package com.gzliangce.ui.fragment.calulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentAccumulationFundBinding;
import com.gzliangce.entity.InterestrateInfo;
import com.gzliangce.entity.LoanInfo;
import com.gzliangce.ui.activity.calculator.CalculationResultsActivity;
import com.gzliangce.ui.activity.calculator.DownPaymentActivity;
import com.gzliangce.ui.activity.calculator.InterestRateActivity;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MathUtils;
import com.gzliangce.util.MetadataUtil;
import com.leo.gesturelibray.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/26.
 * 公积金贷款
 */
public class AccumulationFundFragment extends BaseFragment implements OptionsPickerView.OnOptionsSelectListener {
    private Logger logger = LoggerFactory.getLogger(AccumulationFundFragment.class);
    private FragmentAccumulationFundBinding binding;
    private OptionsPickerView optionsPickerView;
    public boolean isFundRate;//是否是公积金贷款
    private double mortgageRate = 0.3;//房款比例
    private int mortgageMonth = 240;//贷款期数
    private double interestrate = 0.0475;//利率
    private double loanSum = 70;//贷款总额
    private double houseSum = 100;//房款总额
    private double initialPayment = 30;//首付
    private int monthPtions;//按揭选中年数
    private InterestrateInfo interestrateInfo;//利率相关数据

    public void setFundRate(boolean fundRate) {
        isFundRate = fundRate;
    }

    public static AccumulationFundFragment getInstance(String title, boolean fundRate) {
        AccumulationFundFragment fundFragment = new AccumulationFundFragment();
        fundFragment.setTitle(title);
        fundFragment.setFundRate(fundRate);
        return fundFragment;
    }

    private void setTitle(String title) {
        setFragmentTitle(title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccumulationFundBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_accumulation_fund;
    }

    @Override
    public void initView() {
        binding.tvCalculate.setEnabled(true);
        binding.tvCalculate.setSelected(true);
    }

    @Override
    public void initListener() {
        binding.flyMortgageYear.setOnClickListener(onSingleClickListener);
        binding.flyDownPayment.setOnClickListener(onSingleClickListener);
        binding.tvCalculate.setOnClickListener(onSingleClickListener);
        binding.flyInterestRate.setOnClickListener(onSingleClickListener);
        binding.etHouseTotalPrice.addTextChangedListener(houseTwatcher);
        binding.etTotalLoan.addTextChangedListener(loanWatcher);
    }

    @Override
    public void initData() {
        initRate();
        LiangCeUtil.setEditSelection(binding.etHouseTotalPrice);
        LiangCeUtil.setEditSelection(binding.etTotalLoan);
    }

    /**
     * 初始化利率相关数据
     */
    private void initRate() {
        List<InterestrateInfo> rateData;
        rateData = MetadataUtil.getRate(getActivity(), isFundRate);
        if (rateData.size() > 0) {
            setInsterestrateData(rateData.get(0));
        }
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fly_down_payment:
                    actionDownPayment();
                    break;
                case R.id.fly_mortgage_year:
                    optionsPickerView = DialogUtil.showPickerView(getActivity(), DialogUtil.getMortgageNmuData(), monthPtions, AccumulationFundFragment.this);
                    break;
                case R.id.tv_calculate:
                    actionCalculationResultsActivity();
                    break;
                case R.id.fly_interest_rate:
                    actionInsterestActivity();
                    break;
            }
        }
    };


    /**
     * 跳转到计算结果界面
     */
    private void actionCalculationResultsActivity() {
        if (!getLoanPortData()) {
            return;
        }
        ArrayList<LoanInfo> loanInfoList = new ArrayList<>();
        loanInfoList.add(getLoanInfo());
        Intent intent = new Intent(getActivity(), CalculationResultsActivity.class);
        intent.putExtra(Constants.LOAN_INFO, loanInfoList);
        startActivity(intent);
    }


    /**
     * 获取贷款数据
     */
    private boolean getLoanPortData() {
        String house = binding.etHouseTotalPrice.getText().toString().trim();
        String loan = binding.etTotalLoan.getText().toString().trim();
        if (StringUtils.isEmpty(house)) {
            ToastHelper.showMessage(getActivity(), "房款总价不能为空");
            return false;
        }
        if (StringUtils.isEmpty(loan)) {
            ToastHelper.showMessage(getActivity(), "贷款总额不能为空");
            return false;
        }
        houseSum = Double.parseDouble(house);
        loanSum = Double.parseDouble(loan);
        if (houseSum <= 0) {
            ToastHelper.showMessage(getActivity(), "房款总价不能为0");
            return false;
        }
        if (loanSum <= 0) {
            ToastHelper.showMessage(getActivity(), "贷款总额不能为0");
            return false;
        }
        return true;
    }

    /**
     * 获取贷款数据
     */
    private LoanInfo getLoanInfo() {
        LoanInfo info = new LoanInfo();
        info.setHouseSum((int) houseSum);
        info.setInterestrate(interestrate);
        info.setLoanSum(MathUtils.mul(loanSum, 10000));
        info.setMortgageMonth(mortgageMonth);
        info.setMortgageRate(mortgageRate);
        return info;
    }


    /**
     * 跳转到利率比例界面
     */
    private void actionInsterestActivity() {
        Intent intent = new Intent(getActivity(), InterestRateActivity.class);
        intent.putExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO, interestrateInfo);
        intent.putExtra(Constants.IS_FUND_RATE, isFundRate);
        startActivityForResult(intent, Constants.INTERESTRATE_REQUST_CODE);
    }


    /**
     * 跳转到首付比例计算界面
     */
    private void actionDownPayment() {
        String housePrice = binding.etHouseTotalPrice.getText().toString().trim();
        if (Strings.isEmpty(housePrice)) {
            ToastHelper.showMessage(getActivity(), "请先输入房款总价");
            return;
        }
        Intent intent = new Intent(getActivity(), DownPaymentActivity.class);
        intent.putExtra(Constants.REQUEST_ACTIVITY_HOUSER_PRICE, Integer.parseInt(housePrice));
        startActivityForResult(intent, Constants.MORTGAGE_RATE_REQUST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        if (requestCode == Constants.INTERESTRATE_REQUST_CODE) {
            interestrateInfo = (InterestrateInfo) data.getSerializableExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO);
            setInsterestrateData(interestrateInfo);
        } else {
            mortgageRate = data.getDoubleExtra(Constants.REQUEST_ACTIVITY_HOUSER_SCALE, 3);
            calculateHousePayment();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置利率数据
     */
    private void setInsterestrateData(InterestrateInfo info) {
        if (info == null) {
            return;
        }
        interestrate = info.getInterestrateDiscount();
        binding.tvInterestRate.setText(info.getInterestrateDate() + "(" + MathUtils.mul(info.getInterestrateDiscount(), 100) + "%)");
    }

    /**
     * 计算房款比例
     */
    private void calculateHousePayment() {
        String etPrices = binding.etHouseTotalPrice.getText().toString().trim();
        if (Strings.isEmpty(etPrices)) {
            return;
        }
        houseSum = Integer.parseInt(etPrices);
        if (houseSum <= 0) {
            return;
        }
        initialPayment = MathUtils.mul(houseSum, mortgageRate);
        loanSum = MathUtils.sub(houseSum, initialPayment);
        logger.e("houseSum---1" + houseSum);
        logger.e("initialPayment---1" + initialPayment);
        logger.e("loanSum---1" + loanSum);
        logger.e("mortgageRate---1" + mortgageRate);
        binding.etTotalLoan.setText(loanSum + "");
        binding.tvDownPayment.setText(MathUtils.mul(mortgageRate, 10) + "成（" + initialPayment + "万元）");
    }


    /**
     * 计算房款比例
     */
    private void calculateLoanPayment() {
        String loan = binding.etTotalLoan.getText().toString().trim();
        if (Strings.isEmpty(loan)) {
            return;
        }
        loanSum = Double.parseDouble(loan);
        if (loanSum <= 0) {
            return;
        }
        if (loanSum > houseSum) {
            return;
        }
        initialPayment = MathUtils.sub(houseSum, loanSum);
        mortgageRate = MathUtils.div(initialPayment, houseSum, 2);
        logger.e("houseSum---2" + houseSum);
        logger.e("initialPayment---2" + initialPayment);
        logger.e("loanSum---2" + loanSum);
        logger.e("mortgageRate---2" + mortgageRate);
        binding.tvDownPayment.setText(MathUtils.mul(mortgageRate, 10) + "成（" + initialPayment + "万元）");
    }

    /**
     * 房屋总价输入监听
     */
    private TextWatcher houseTwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculateHousePayment();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * 贷款总额输入监听
     */
    private TextWatcher loanWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculateLoanPayment();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    /**
     * 按揭期数选择回调
     */
    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        int year = options1 + 1;
        mortgageMonth = year * 12;
        monthPtions = options1;
        binding.tvMortgageYear.setText(DialogUtil.getMortgageNmuData().get(options1));
    }

    @Override
    public boolean onBackPressed() {
        if (optionsPickerView != null && optionsPickerView.isShowing()) {
            optionsPickerView.dismiss();
            return true;
        }
        return super.onBackPressed();
    }
}
