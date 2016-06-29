package com.gzliangce.ui.fragment.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.databinding.FragmentTodayDataBinding;

import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/1/19.
 * 今日数据
 */
public class ToDayDataFragment extends BaseFragment {
    private FragmentTodayDataBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodayDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_today_data;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
