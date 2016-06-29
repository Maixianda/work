package com.gzliangce.ui.fragment.calulator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentLoanProtfolioBinding;
import com.gzliangce.entity.CombinationLoanInfo;
import com.gzliangce.entity.InterestrateInfo;
import com.gzliangce.entity.LoanInfo;
import com.gzliangce.ui.activity.calculator.CalculationResultsActivity;
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
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/26.
 * 组合贷款
 */
public class LoanPortfolioFragment extends BaseFragment implements OptionsPickerView.OnOptionsSelectListener {
    private FragmentLoanProtfolioBinding binding;
    private OptionsPickerView onPickerView;
    private boolean isAccumulationInterestrate = true;//是否是选择公积金比例
    private int month = 240;//贷款期数
    private double aInterestrate = 0.0475f;//公积金贷款利率
    private double bInterestrate = 0.0475f;//商业贷款利率
    private InterestrateInfo aIinterestrateInfo;//公积金贷款利率数据
    private InterestrateInfo bIinterestrateInfo;//商业贷款利率数据
    private double aLoanSum = 70;//公积金贷款总额
    private double bLoanSum = 70;//商业贷款总额
    private int monthOptions;//记录按揭年数选中位置
    private static Logger logger = LoggerFactory.getLogger(LoanPortfolioFragment.class);

    public static LoanPortfolioFragment getInstance(String title) {
        LoanPortfolioFragment fragment = new LoanPortfolioFragment();
        fragment.setTitle(title);
        return fragment;
    }

    private void setTitle(String title) {
        setFragmentTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoanProtfolioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initView() {
        binding.tvCalculate.setSelected(true);
    }

    @Override
    public void initListener() {
        binding.tvCalculate.setOnClickListener(onSingleClickListener);
        binding.flyAInsterestrate.setOnClickListener(onSingleClickListener);
        binding.flyBInsterestrate.setOnClickListener(onSingleClickListener);
        binding.flyMortgageYear.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        initRateData();
        LiangCeUtil.setEditSelection(binding.etALoansum);
        LiangCeUtil.setEditSelection(binding.etBLoansum);
    }

    private void initRateData() {
        List<InterestrateInfo> aInfo = MetadataUtil.getRate(getActivity(), true);
        List<InterestrateInfo> bInfo = MetadataUtil.getRate(getActivity(), false);
        if (aInfo.size() > 0) {
            isAccumulationInterestrate = true;
            setInsterestrateData(aInfo.get(0));
        }
        if (bInfo.size() > 0) {
            isAccumulationInterestrate = false;
            setInsterestrateData(bInfo.get(0));
        }
    }

    /**
     * Onclick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fly_a_insterestrate:
                    isAccumulationInterestrate = true;
                    actionInsterestActivity();
                    break;
                case R.id.fly_b_insterestrate:
                    isAccumulationInterestrate = false;
                    actionInsterestActivity();
                    break;
                case R.id.fly_mortgage_year:
                    onPickerView = DialogUtil.showPickerView(getActivity(), DialogUtil.getMortgageNmuData(), monthOptions, LoanPortfolioFragment.this);
                    break;
                case R.id.tv_calculate:
                    actionCalculationResultsActivity();
                    break;
            }
        }
    };


    /**
     * 跳转到利率比例界面
     */
    private void actionInsterestActivity() {
        Intent intent = new Intent(getActivity(), InterestRateActivity.class);
        if (isAccumulationInterestrate) {
            intent.putExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO, aIinterestrateInfo);
        } else {
            intent.putExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO, bIinterestrateInfo);
        }
        intent.putExtra(Constants.IS_FUND_RATE, isAccumulationInterestrate);
        startActivityForResult(intent, Constants.INTERESTRATE_REQUST_CODE);
    }

    /**
     * 跳转到计算结果界面
     */
    private void actionCalculationResultsActivity() {
        if (!getLoanPortData()) {
            return;
        }
        ArrayList<LoanInfo> loanInfoArrayList = new ArrayList<>();
        loanInfoArrayList.add(getCombinationLoanInfo(aLoanSum, aInterestrate, month));
        loanInfoArrayList.add(getCombinationLoanInfo(bLoanSum, bInterestrate, month));
        Intent intent = new Intent(getActivity(), CalculationResultsActivity.class);
        intent.putExtra(Constants.LOAN_INFO, loanInfoArrayList);
        intent.putExtra(Constants.REQUEST_CALCULATION_TYPE, true);
        startActivity(intent);
    }


    /**
     * 获取贷款数据对象
     */
    private LoanInfo getCombinationLoanInfo(double loanSum, double interestrate, int month) {
        LoanInfo info = new LoanInfo();
        info.setLoanSum(MathUtils.mul(loanSum, 10000));
        info.setMortgageMonth(month);
        info.setInterestrate(interestrate);
        return info;
    }


    /**
     * 按揭期数选择回调
     */
    @Override
    public void onOptionsSelect(int options1, int option2, int options3) {
        monthOptions = options1;
        month = (options1 + 1) * 12;
        binding.tvMortgageYear.setText(DialogUtil.getMortgageNmuData().get(options1));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        InterestrateInfo info = (InterestrateInfo) data.getSerializableExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO);
        setInsterestrateData(info);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 设置利率数据
     */
    private void setInsterestrateData(InterestrateInfo info) {
        if (info == null) {
            return;
        }
        if (isAccumulationInterestrate) {
            aIinterestrateInfo = info;
            aInterestrate = info.getInterestrateDiscount();
            binding.tvAInsterestrate.setText(aIinterestrateInfo.getInterestrateDate() + "(" + MathUtils.mul(aInterestrate, 100) + "%)");
        } else {
            bIinterestrateInfo = info;
            bInterestrate = info.getInterestrateDiscount();
            binding.tvBInsterestrate.setText(bIinterestrateInfo.getInterestrateDate() + "(" + MathUtils.mul(bInterestrate, 100) + "%)");
        }
        logger.i("InterestrateDiscount:a:" + aInterestrate);
        logger.i("InterestrateDiscount:b:" + bInterestrate);
        logger.i("InterestrateDiscount:c:" + MathUtils.mul(bInterestrate, 100));
    }


    /**
     * 获取贷款数据
     */
    private boolean getLoanPortData() {
        String aLoan = binding.etALoansum.getText().toString().trim();
        String bLoan = binding.etBLoansum.getText().toString().trim();
        if (StringUtils.isEmpty(aLoan)) {
            ToastHelper.showMessage(getActivity(), "公积金贷款额不能为空");
            return false;
        }
        if (StringUtils.isEmpty(bLoan)) {
            ToastHelper.showMessage(getActivity(), "商业贷款额不能为空");
            return false;
        }
        aLoanSum = Integer.parseInt(aLoan);
        bLoanSum = Integer.parseInt(bLoan);
        if (aLoanSum <= 0) {
            ToastHelper.showMessage(getActivity(), "公积金贷款额不能为0");
            return false;
        }
        if (bLoanSum <= 0) {
            ToastHelper.showMessage(getActivity(), "商业贷款额不能为0");
            return false;
        }
        return true;
    }


    @Override
    public boolean onBackPressed() {
        if (onPickerView != null && onPickerView.isShowing()) {
            onPickerView.dismiss();
            return true;
        }
        return super.onBackPressed();
    }
}
