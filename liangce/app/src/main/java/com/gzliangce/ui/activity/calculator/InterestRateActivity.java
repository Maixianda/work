package com.gzliangce.ui.activity.calculator;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityInterestRateBinding;
import com.gzliangce.entity.InsterestInfo;
import com.gzliangce.entity.InterestrateInfo;
import com.gzliangce.ui.adapter.InsterestAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.MathUtils;
import com.gzliangce.util.MetadataUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Animations;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.date.Date;
import io.ganguo.library.util.date.DateTime;

/**
 * Created by leo on 16/2/1.
 * 利率界面
 */
public class InterestRateActivity extends BaseSwipeBackActivityBinding {
    private ActivityInterestRateBinding binding;
    private InsterestAdapter insterestAdapter;
    private boolean allProductIsUp = false;
    private OptionsPickerView yearOptions;
    private ArrayList<String> yearDataList = new ArrayList<String>();
    private List<InterestrateInfo> interestrateInfos = new ArrayList<>();
    private boolean isFundRate;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_interest_rate);
        setHeader();
    }

    @Override
    public void initView() {
        insterestAdapter = new InsterestAdapter(this, interestrateInfos);
        binding.rvInterest.setLayoutManager(new LinearLayoutManager(this));
        binding.rvInterest.setAdapter(insterestAdapter);
    }

    @Override
    public void initListener() {
        binding.tvDate.setOnClickListener(onSingleClickListener);
        insterestAdapter.getBinding().etCustomInterest.setOnEditorActionListener(editorActionListener);
    }

    @Override
    public void initData() {
        isFundRate = getIntent().getBooleanExtra(Constants.IS_FUND_RATE, false);
        insterestAdapter.add(new InsterestInfo("基准利率", 1, false));
        insterestAdapter.add(new InsterestInfo("7折基准利率", 0.7, false));
        insterestAdapter.add(new InsterestInfo("85折基准利率", 0.85, false));
        insterestAdapter.add(new InsterestInfo("88折基准利率", 0.88, false));
        insterestAdapter.add(new InsterestInfo("1.1倍基准利率", 1.1, false));
        insterestAdapter.add(new InsterestInfo("测试", 0, false));
        insterestAdapter.notifyDataSetChanged();
        interestrateInfos.addAll(MetadataUtil.getRate(this, isFundRate));
        addDateData();
        getIntentData();
        initYearPickerView();
    }

    /**
     * 获取传递过来的数据
     */
    private void getIntentData() {
        InterestrateInfo interestrateInfo = (InterestrateInfo) getIntent().getSerializableExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO);
        if (interestrateInfo == null) {
            interestrateInfo = interestrateInfos.get(0);
        } else {
            insterestAdapter.get(interestrateInfo.getInterestrateIndex()).setChecked(true);
            insterestAdapter.notifyDataSetChanged();
        }
        insterestAdapter.setInterestrateInfo(interestrateInfo);
        binding.tvDate.setText(DateTime.formatForI(interestrateInfo.getRateDate()));
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
        header.setMidTitle("利率");
        header.setLeftIcon(R.drawable.ic_back);
        binding.setHeader(header);
    }


    /**
     * 年份选择器
     */
    private void initYearPickerView() {
        //选项选择器
        yearOptions = new OptionsPickerView(this);
        //三级联动效果
        yearOptions.setPicker(yearDataList, null, null, true);
        yearOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        yearOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        yearOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                insterestAdapter.setInterestrateInfo(interestrateInfos.get(options1));
                binding.tvDate.setText(yearDataList.get(options1));
            }
        });
        yearOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                setDateAnimation();
            }
        });
    }

    /**
     * 添加日期
     */
    private void addDateData() {
        for (int i = 0; i < interestrateInfos.size(); i++) {
            yearDataList.add(DateTime.formatForI(interestrateInfos.get(i).getRateDate()));
        }
        if (interestrateInfos.size() > 0) {
            binding.tvDate.setText(yearDataList.get(0));
        }
    }

    /**
     * 日期点击动画
     */
    private void setDateAnimation() {
        binding.ibtnDateArrow.clearAnimation();
        if (allProductIsUp) {
            binding.ibtnDateArrow.setAnimation(Animations.getRotateUpAnimation());
            allProductIsUp = false;
        } else {
            binding.ibtnDateArrow.setAnimation(Animations.getRotateDownAnimation());
            allProductIsUp = true;
        }
    }


    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            setDateAnimation();
            yearOptions.show();
        }
    };


    /**
     * 键盘完成按钮监听
     */
    private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionInterestRate();
                return true;
            }
            return false;
        }
    };


    /**
     * 计算比例
     */
    private void actionInterestRate() {
        Systems.hideKeyboard(this);
        String etPrices = insterestAdapter.getBinding().etCustomInterest.getText().toString().trim();
        if (Strings.isEmpty(etPrices)) {
            return;
        }
        double interest = Double.parseDouble(etPrices);
        if (interest <= 0) {
            ToastHelper.showMessage(this,"利率不能为0");
            return;
        }
        InterestrateInfo interestrateInfo = insterestAdapter.getInterestrateInfo();
        double discount = MathUtils.mul(MathUtils.div(interest, 100, 2), interestrateInfo.getInterestrate());
        logger.e("interest:" + discount);
        interestrateInfo.setInterestrateDiscount(discount);
        interestrateInfo.setInsterestName("自定义基准利率");
        interestrateInfo.setInterestrateIndex(5);
        Intent intent = new Intent(this, MortgageCalculatorActivity.class);
        intent.putExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO, interestrateInfo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (yearOptions.isShowing()) {
            yearOptions.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
