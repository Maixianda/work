package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemInterestCustomBinding;
import com.gzliangce.databinding.ItemInterestRateCustomBinding;
import com.gzliangce.databinding.ItemInterestTextBinding;
import com.gzliangce.entity.InsterestInfo;
import com.gzliangce.entity.InterestrateInfo;
import com.gzliangce.ui.activity.calculator.MortgageCalculatorActivity;
import com.gzliangce.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/2/1.
 * 利率adapter
 */
public class InsterestAdapter extends ListAdapter<InsterestInfo, ItemInterestTextBinding> {
    private Logger logger = LoggerFactory.getLogger(InsterestAdapter.class);
    private ItemInterestRateCustomBinding binding;
    private Activity activity;
    private InterestrateInfo interestrateInfo;

    public InsterestAdapter(Activity context, List<InterestrateInfo> interestrateInfos) {
        super(context);
        this.activity = context;
        binding = ItemInterestRateCustomBinding.inflate(context.getLayoutInflater(), null);
    }

    public ItemInterestRateCustomBinding getBinding() {
        return binding;
    }

    public void setBinding(ItemInterestRateCustomBinding binding) {
        this.binding = binding;
    }


    public InterestrateInfo getInterestrateInfo() {
        return interestrateInfo;
    }

    public void setInterestrateInfo(InterestrateInfo interestrateInfo) {
        this.interestrateInfo = interestrateInfo;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_interest_rate_custom) {
            return new BaseViewHolder<>(binding);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemInterestTextBinding> vh, final int position) {
        if (getItemViewType(position) == R.layout.item_interest_rate_custom) {
            return;
        }
        InsterestInfo insterestInfo = get(position);
        vh.getBinding().tvInterest.setText(insterestInfo.getInsterestName());
        if (insterestInfo.isChecked()) {
            vh.getBinding().ibtnCheck.setVisibility(View.VISIBLE);
        } else {
            vh.getBinding().ibtnCheck.setVisibility(View.INVISIBLE);
        }
        vh.getBinding().flyAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                actionSelect(position);
            }
        });
    }

    /**
     * 选择
     */
    private void actionSelect(int position) {
        for (InsterestInfo info : this.getData()) {
            info.setChecked(false);
        }
        InsterestInfo info = getData().get(position);
        info.setChecked(true);
        double discount = MathUtils.mul(info.getInterestrateDiscount(), interestrateInfo.getInterestrate());
        logger.e("InterestrateDiscount----" + info.getInterestrateDiscount());
        logger.e("Interestrate----" + interestrateInfo.getInterestrate());
        logger.e("discount--" + discount);
        interestrateInfo.setInterestrateDiscount(discount);
        interestrateInfo.setInsterestName(info.getInsterestName());
        interestrateInfo.setInterestrateIndex(position);
        Intent intent = new Intent(activity, MortgageCalculatorActivity.class);
        intent.putExtra(Constants.INTENT_DETAIL_INERESTEATE_INFO, interestrateInfo);
        activity.setResult(Activity.RESULT_OK, intent);
        notifyDataSetChanged();
        activity.finish();
    }

    @Override
    protected int getItemLayoutId(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.item_interest_rate_custom;
        }
        return R.layout.item_interest_text;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.item_interest_rate_custom;
        }
        return super.getItemViewType(position);
    }


}
