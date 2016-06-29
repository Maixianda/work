package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemDetailMonthlyBinding;
import com.gzliangce.databinding.ItemDetailMonthlyHeaderBinding;
import com.gzliangce.entity.DetailedMonthActivityInfo;
import com.gzliangce.entity.DetailedMonthyInfo;
import com.gzliangce.entity.MonthPaymentslyInfo;
import com.gzliangce.ui.widget.AutoHeightLinearLayoutManager;
import com.gzliangce.ui.widget.FullyLinearLayoutManager;
import com.gzliangce.util.MathUtils;
import com.gzliangce.util.PieChartUtil;

import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/2/17.
 * 月供详情
 */
public class DetailedMonthlyAdapter extends ListAdapter<DetailedMonthyInfo, ItemDetailMonthlyBinding> {
    private Logger logger = LoggerFactory.getLogger(DetailedMonthlyAdapter.class);
    private ItemDetailMonthlyHeaderBinding binding;
    private Activity activity;
    private DetailedMonthActivityInfo info;

    public DetailedMonthActivityInfo getInfo() {
        return info;
    }

    public void setInfo(DetailedMonthActivityInfo info) {
        this.info = info;
    }

    public DetailedMonthlyAdapter(Activity context) {
        super(context);
        View view = context.getLayoutInflater().inflate(R.layout.item_detail_monthly_header, null);
        binding = ItemDetailMonthlyHeaderBinding.bind(view);
        this.activity = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_detail_monthly_header) {
            return new BaseViewHolder<>(binding);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemDetailMonthlyBinding> vh, int position) {
        if (vh.getItemViewType() == R.layout.item_detail_monthly_header) {
            if (info != null) {
                binding.tvReimbursementNum.setText(String.valueOf(MathUtils.getBigTwoDouble(info.getRepaymentSum())));
                binding.tvLoanNum.setText(String.valueOf(MathUtils.div(info.getLoanSum(), 10000, 2)));
                binding.tvLoanMonth.setText(String.valueOf(info.getMortgageMonth()));
                binding.tvInterest.setText(Math.round(info.getInterestPayment()) + "");
                binding.tvMonthReimbursementNum.setText(Math.round(info.getLoanPerMonth()) + "");
            }
        } else {
            vh.getBinding().tvYear.setText("第" + position + "年");
            setMonthData(vh.getBinding(), get(position).getData());
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        if (position == 0 && binding != null) {
            return R.layout.item_detail_monthly_header;
        }
        return R.layout.item_detail_monthly;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && binding != null) {
            return R.layout.item_detail_monthly_header;
        }
        return super.getItemViewType(position);
    }

    /**
     * 设置月份数据
     */
    private void setMonthData(ItemDetailMonthlyBinding monthlyBinding, List<MonthPaymentslyInfo> infoList) {
        for (int i = 0; i < 12; i++) {
            switch (i) {
                case 0:
                    setTextData(monthlyBinding.layoutMonth1.tvInterest, monthlyBinding.layoutMonth1.tvPayments, monthlyBinding.layoutMonth1.tvPrincipal,
                            monthlyBinding.layoutMonth1.tvRemainingLoan, monthlyBinding.layoutMonth1.tvMonth, infoList.get(i), i);
                    break;
                case 1:
                    setTextData(monthlyBinding.layoutMonth2.tvInterest, monthlyBinding.layoutMonth2.tvPayments, monthlyBinding.layoutMonth2.tvPrincipal,
                            monthlyBinding.layoutMonth2.tvRemainingLoan, monthlyBinding.layoutMonth2.tvMonth, infoList.get(i), i);
                    break;
                case 2:
                    setTextData(monthlyBinding.layoutMonth3.tvInterest, monthlyBinding.layoutMonth3.tvPayments, monthlyBinding.layoutMonth3.tvPrincipal,
                            monthlyBinding.layoutMonth3.tvRemainingLoan, monthlyBinding.layoutMonth3.tvMonth, infoList.get(i), i);
                    break;
                case 3:
                    setTextData(monthlyBinding.layoutMonth4.tvInterest, monthlyBinding.layoutMonth4.tvPayments, monthlyBinding.layoutMonth4.tvPrincipal,
                            monthlyBinding.layoutMonth4.tvRemainingLoan, monthlyBinding.layoutMonth4.tvMonth, infoList.get(i), i);
                    break;
                case 4:
                    setTextData(monthlyBinding.layoutMonth5.tvInterest, monthlyBinding.layoutMonth5.tvPayments, monthlyBinding.layoutMonth5.tvPrincipal,
                            monthlyBinding.layoutMonth5.tvRemainingLoan, monthlyBinding.layoutMonth5.tvMonth, infoList.get(i), i);
                    break;
                case 5:
                    setTextData(monthlyBinding.layoutMonth6.tvInterest, monthlyBinding.layoutMonth6.tvPayments, monthlyBinding.layoutMonth6.tvPrincipal,
                            monthlyBinding.layoutMonth6.tvRemainingLoan, monthlyBinding.layoutMonth6.tvMonth, infoList.get(i), i);
                    break;
                case 6:
                    setTextData(monthlyBinding.layoutMonth7.tvInterest, monthlyBinding.layoutMonth7.tvPayments, monthlyBinding.layoutMonth7.tvPrincipal,
                            monthlyBinding.layoutMonth7.tvRemainingLoan, monthlyBinding.layoutMonth7.tvMonth, infoList.get(i), i);
                    break;
                case 7:
                    setTextData(monthlyBinding.layoutMonth8.tvInterest, monthlyBinding.layoutMonth8.tvPayments, monthlyBinding.layoutMonth8.tvPrincipal,
                            monthlyBinding.layoutMonth8.tvRemainingLoan, monthlyBinding.layoutMonth8.tvMonth, infoList.get(i), i);
                    break;
                case 8:
                    setTextData(monthlyBinding.layoutMonth9.tvInterest, monthlyBinding.layoutMonth9.tvPayments, monthlyBinding.layoutMonth9.tvPrincipal,
                            monthlyBinding.layoutMonth9.tvRemainingLoan, monthlyBinding.layoutMonth9.tvMonth, infoList.get(i), i);
                    break;
                case 9:
                    setTextData(monthlyBinding.layoutMonth10.tvInterest, monthlyBinding.layoutMonth10.tvPayments, monthlyBinding.layoutMonth10.tvPrincipal,
                            monthlyBinding.layoutMonth10.tvRemainingLoan, monthlyBinding.layoutMonth10.tvMonth, infoList.get(i), i);
                    break;
                case 10:
                    setTextData(monthlyBinding.layoutMonth11.tvInterest, monthlyBinding.layoutMonth11.tvPayments, monthlyBinding.layoutMonth11.tvPrincipal,
                            monthlyBinding.layoutMonth11.tvRemainingLoan, monthlyBinding.layoutMonth11.tvMonth, infoList.get(i), i);
                    break;
                case 11:
                    setTextData(monthlyBinding.layoutMonth12.tvInterest, monthlyBinding.layoutMonth12.tvPayments, monthlyBinding.layoutMonth12.tvPrincipal,
                            monthlyBinding.layoutMonth12.tvRemainingLoan, monthlyBinding.layoutMonth12.tvMonth, infoList.get(i), i);
                    break;
            }
        }

    }

    /**
     * 设置文本数据
     */
    private void setTextData(TextView tvInterest, TextView tvPayments, TextView tvPrincipal, TextView tvRemainingLoan, TextView tvMonth, MonthPaymentslyInfo info, int index) {
        tvInterest.setText(Math.round(info.getInterest()) + "元");
        tvPayments.setText(Math.round(info.getPayments()) + "元");
        tvPrincipal.setText(Math.round(info.getPrincipal()) + "元");
        tvRemainingLoan.setText(Math.round(info.getRemainingLoan()) + "元");
        tvMonth.setText((index + 1) + "月");

    }
}
