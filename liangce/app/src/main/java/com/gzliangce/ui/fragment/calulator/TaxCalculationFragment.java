package com.gzliangce.ui.fragment.calulator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.databinding.FragmentTaxCalculationBinding;
import com.gzliangce.ui.fragment.MineFragment;
import com.gzliangce.util.LiangCeUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/1/26.
 * 税费计算器
 */
public class TaxCalculationFragment extends BaseFragment implements View.OnClickListener {
    private FragmentTaxCalculationBinding binding;
    private List<BaseFragment> fragments = new ArrayList<>();
    private int from = -1;

    public static TaxCalculationFragment getInstance(String title) {
        TaxCalculationFragment fragment = new TaxCalculationFragment();
        fragment.setTitle(title);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTaxCalculationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setTitle(String title) {
        setFragmentTitle(title);
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initView() {
        initFragment();
    }

    @Override
    public void initListener() {
        binding.flNewHouse.setOnClickListener(this);
        binding.flOldHouse.setOnClickListener(this);
    }

    @Override
    public void initData() {
        binding.flNewHouse.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_new_house:
                switchFragment(0);
                binding.flNewHouse.setSelected(true);
                binding.flOldHouse.setSelected(false);
                break;
            case R.id.fl_old_house:
                switchFragment(1);
                binding.flOldHouse.setSelected(true);
                binding.flNewHouse.setSelected(false);
                break;
        }
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();
        fragments.add(new newHouseFrgment());
        fragments.add(new OldHouseFragment());
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < 2; i++) {
            fragmentTransaction.add(R.id.fly_main, fragments.get(i));
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commit();
        switchFragment(0);
    }


    /**
     * 切换Fragment
     */
    private void switchFragment(int to) {
        if (from == to) {
            return;
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (from != -1) {
            fragmentTransaction.hide(fragments.get(from));
        }
        fragmentTransaction.show(fragments.get(to));
        fragmentTransaction.commit();
        from = to;
    }

    @Override
    public boolean onBackPressed() {
        if (!fragments.get(from).onBackPressed()) {
            return super.onBackPressed();
        }
        return true;
    }
}
