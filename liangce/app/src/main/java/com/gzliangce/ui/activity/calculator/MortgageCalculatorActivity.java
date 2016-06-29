package com.gzliangce.ui.activity.calculator;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMortgageCalculatorBinding;
import com.gzliangce.ui.activity.usercenter.SearchActivity;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.fragment.calulator.AccumulationFundFragment;
import com.gzliangce.ui.fragment.calulator.LoanPortfolioFragment;
import com.gzliangce.ui.fragment.calulator.TaxCalculationFragment;
import com.gzliangce.ui.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.adapter.CommonFPagerAdapter;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/26.
 * 按揭计算器界面
 */
public class MortgageCalculatorActivity extends BaseSwipeBackFragmentBinding {
    private Logger logger = LoggerFactory.getLogger(MortgageCalculatorActivity.class);
    private ActivityMortgageCalculatorBinding binding;
    private CommonFPagerAdapter commonFPagerAdapter;
    private int page = 0;

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mortgage_calculator);
        setHeader();
    }

    @Override
    public void initView() {
        getSwipeBackLayout().setEdgeSize(50);
        commonFPagerAdapter = new CommonFPagerAdapter(getSupportFragmentManager(), fragments);
        binding.vpCalculator.setOffscreenPageLimit(4);
        binding.vpCalculator.setAdapter(commonFPagerAdapter);
        binding.pagerTabs.setCustomTabView(R.layout.item_calculator_tab, android.R.id.text1);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.red_fa));
    }

    @Override
    public void initListener() {
        binding.pagerTabs.setOnPageChangeListener(onPageChangeListener);
    }


    @Override
    public void initData() {
        addFragment();
        binding.pagerTabs.setViewPager(binding.vpCalculator);
    }

    /**
     * 滑动监听
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            page = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        startActivity(new Intent(this, SearchActivity.class).putExtra(Constants.SEARCH_RESULT_TYPE, Constants.SEARCH_TYPE[0]));
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("按揭计算");
        binding.setHeader(header);
    }

    private void addFragment() {
        fragments.add(AccumulationFundFragment.getInstance("公积金贷款", true));
        fragments.add(AccumulationFundFragment.getInstance("商业贷款", false));
        fragments.add(LoanPortfolioFragment.getInstance("组合贷款"));
        fragments.add(TaxCalculationFragment.getInstance("税费计算"));
        commonFPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (!fragments.get(page).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
