package com.gzliangce.ui.activity;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityDataCenterBinding;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.fragment.data.CumulativeDataFragment;
import com.gzliangce.ui.fragment.data.MonthDataFragment;
import com.gzliangce.ui.fragment.data.ToDayDataFragment;
import com.gzliangce.ui.model.HeaderModel;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/1/19.
 * 数据中心
 */
public class DataCenterActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private ActivityDataCenterBinding binding;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private int from = 0;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_center);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.flCumulativeData.setOnClickListener(this);
        binding.flDayData.setOnClickListener(this);
        binding.flMonthData.setOnClickListener(this);
    }

    @Override
    public void initData() {
        header.setMidTitle("数据中心");
        initTvState(binding.tvDayData);
        initFragment();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.add(new ToDayDataFragment());
        fragments.add(new MonthDataFragment());
        fragments.add(new CumulativeDataFragment());
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < 3; i++) {
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


    /**
     * 初始化textview按钮状态
     */
    private void initTvState(TextView textView) {
        binding.tvDayData.setSelected(false);
        binding.tvMonthData.setSelected(false);
        binding.tvCumulativeData.setSelected(false);
        textView.setSelected(true);
    }


    /**
     * onClick事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_day_data:
                initTvState(binding.tvDayData);
                switchFragment(0);
                break;
            case R.id.fl_month_data:
                initTvState(binding.tvMonthData);
                switchFragment(1);
                break;
            case R.id.fl_cumulative_data:
                initTvState(binding.tvCumulativeData);
                switchFragment(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!fragments.get(from).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
