package com.gzliangce.ui.fragment.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gzliangce.R;
import com.gzliangce.databinding.FragmentMonthDataBinding;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.PieChartUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Animations;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/20.
 * 月度数据
 */
public class MonthDataFragment extends BaseFragment implements OnChartValueSelectedListener {
    private Logger logger = LoggerFactory.getLogger(MonthDataFragment.class);
    private FragmentMonthDataBinding binding;
    private List<Float> data = new ArrayList();
    private boolean monthIsUp = true, allProductIsUp = true;
    private OptionsPickerView productOptions;
    private OptionsPickerView yearOptions;
    private ArrayList<String> productDataList = new ArrayList<String>();
    private ArrayList<String> yearDataList = new ArrayList<String>();
    private ArrayList<ArrayList<String>> monthDataList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMonthDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_month_data;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.tvMonth.setOnClickListener(onSingleClickListener);
        binding.tvAllProduct.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        data.add(20f);
        data.add(180f);
        data.add(160f);
        PieChartUtil.initPieChart(binding.chartLayout.piPiechare, data, PieChartUtil.getColor(), PieChartUtil.getCenterSpannableText("总金额", "159.65"), this);
        initProductData();
        initMonthData();
        initYearData();
        initProductPickerView();
        initYearPickerView();
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_month:
                    setMonthAnimation();
                    yearOptions.show();
                    break;
                case R.id.tv_all_product:
                    setProductAnimation();
                    productOptions.show();
                    break;
            }
        }
    };


    /**
     * 月份点击动画
     */
    private void setProductAnimation() {
        binding.ibtnProductArrow.clearAnimation();
        if (allProductIsUp) {
            binding.ibtnProductArrow.setAnimation(Animations.getRotateUpAnimation());
            allProductIsUp = false;
        } else {
            binding.ibtnProductArrow.setAnimation(Animations.getRotateDownAnimation());
            allProductIsUp = true;
        }
    }

    /**
     * 全部产品
     */
    private void setMonthAnimation() {
        binding.ibtnMonthArrow.clearAnimation();
        if (monthIsUp) {
            binding.ibtnMonthArrow.setAnimation(Animations.getRotateUpAnimation());
            monthIsUp = false;
        } else {
            binding.ibtnMonthArrow.setAnimation(Animations.getRotateDownAnimation());
            monthIsUp = true;
        }
    }


    /**
     * 全部产品选择器
     */
    private void initProductPickerView() {
        //选项选择器
        productOptions = new OptionsPickerView(getActivity());
        //三级联动效果
        productOptions.setPicker(productDataList, null, null, true);
        productOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        productOptions.setSelectOptions(0);
        productOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                ToastHelper.showMessage(getActivity(), productDataList.get(options1));
            }
        });
        productOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                setProductAnimation();
            }
        });
    }

    /**
     * 数据
     */
    private void initProductData() {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                productDataList.add("点按揭产品");
            } else {
                productDataList.add("公积金贷款");
            }
        }
    }


    /**
     * 年份选择器
     */
    private void initYearPickerView() {
        //选项选择器
        yearOptions = new OptionsPickerView(getActivity());
        //三级联动效果
        yearOptions.setPicker(yearDataList, monthDataList, null, true);
        yearOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        yearOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        yearOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                ToastHelper.showMessage(getActivity(), yearDataList.get(options1) + "年" + monthDataList.get(options1).get(option2) + "月");
            }
        });
        yearOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                setMonthAnimation();
            }
        });
    }

    /**
     * 年份数据
     */
    private void initYearData() {
        for (int i = 0; i < 10; i++) {
            yearDataList.add((2014 + i) + "");
            if (i % 2 == 0) {
                monthDataList.add(initMonthData());
            } else {
                monthDataList.add(initTestMonthData());
            }
        }
    }

    /**
     * 月份数据
     */
    private ArrayList<String> initMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add((i + 1) + "");
        }
        return list;
    }

    /**
     * 月份测试数据
     */
    private ArrayList<String> initTestMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 12; i < 24; i++) {
            list.add((i + 1) + "");
        }
        return list;
    }

    @Override
    public boolean onBackPressed() {
        if (!monthIsUp) {
            yearOptions.dismiss();
            return true;
        }
        if (!allProductIsUp) {
            productOptions.dismiss();
            return true;
        }
        return super.onBackPressed();
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
