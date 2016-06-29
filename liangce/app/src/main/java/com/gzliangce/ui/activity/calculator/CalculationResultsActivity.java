package com.gzliangce.ui.activity.calculator;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCalculationResultsBinding;
import com.gzliangce.entity.LoanInfo;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.fragment.calulator.InterestResultsFragment;
import com.gzliangce.ui.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/2/14.
 * 计算器 - 计算结果
 */
public class CalculationResultsActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private ActivityCalculationResultsBinding binding;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private int from = 0;
    private boolean isCombination;
    private List<LoanInfo> loanInfoList;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calculation_results);
        setHeader();
    }

    @Override
    public void initView() {
        isCombination = getIntent().getBooleanExtra(Constants.REQUEST_CALCULATION_TYPE, false);
        loanInfoList = (List<LoanInfo>) getIntent().getSerializableExtra(Constants.LOAN_INFO);
        initFragment();
        switchFragment(0);
        binding.flInterestResults.setSelected(true);
        binding.flPrincipalResults.setSelected(false);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.flInterestResults.setOnClickListener(this);
        binding.flPrincipalResults.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("计算结果");
        binding.setHeader(header);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_interest_results:
                switchFragment(0);
                binding.flInterestResults.setSelected(true);
                binding.flPrincipalResults.setSelected(false);
                break;
            case R.id.fl_principal_results:
                binding.flInterestResults.setSelected(false);
                binding.flPrincipalResults.setSelected(true);
                switchFragment(1);
                break;
        }
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();
        fragments.add(InterestResultsFragment.getInstance(0, isCombination, loanInfoList));
        fragments.add(InterestResultsFragment.getInstance(1, isCombination, loanInfoList));
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < 2; i++) {
            fragmentTransaction.add(R.id.fly_main, fragments.get(i));
            if (i != 0) {
                fragmentTransaction.hide(fragments.get(i));
            }
        }
        fragmentTransaction.commit();
    }


    /**
     * 切换Fragment
     */
    private void switchFragment(int to) {
        if (from == to) {
            return;
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments.get(from));
        fragmentTransaction.show(fragments.get(to));
        fragmentTransaction.commit();
        from = to;
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

}
