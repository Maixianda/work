package com.gzliangce.ui.fragment.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.R;
import com.gzliangce.databinding.FragmentCumulativeDataBinding;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.PieChartUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/1/19.
 * 累计数据
 */
public class CumulativeDataFragment extends BaseFragment implements OnChartValueSelectedListener {
    private FragmentCumulativeDataBinding binding;
    private List<Float> data = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCumulativeDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_cumulative_data;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        data.add(90f);
        data.add(120f);
        data.add(150f);
        PieChartUtil.initPieChart(binding.chartLayout.piPiechare, data, PieChartUtil.getColor(), PieChartUtil.getCenterSpannableText("总金额", "159.65"), this);
    }

    /**
     * 饼图点击监听
     */
    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        ToastHelper.showMessage(getActivity(), LiangCeUtil.getPrecisionData(entry.getVal() / 360) + "%");
    }

    @Override
    public void onNothingSelected() {

    }
}
