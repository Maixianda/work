package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemInterestCustomBinding;
import com.gzliangce.databinding.ItemInterestTextBinding;
import com.gzliangce.entity.DownPaymentInfo;
import com.gzliangce.ui.activity.calculator.MortgageCalculatorActivity;
import com.gzliangce.util.MathUtils;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/2/2.
 * 首付比例adapter
 */
public class DownPaymentAdapter extends ListAdapter<DownPaymentInfo, ItemInterestTextBinding> {
    private Activity activity;
    private ItemInterestCustomBinding binding;

    public DownPaymentAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
        binding = ItemInterestCustomBinding.inflate(activity.getLayoutInflater(), null);
    }

    public ItemInterestCustomBinding getBinding() {
        return binding;
    }

    public void setBinding(ItemInterestCustomBinding binding) {
        this.binding = binding;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_interest_custom) {
            return new BaseViewHolder<>(binding);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemInterestTextBinding> vh, final int position) {
        if (getItemViewType(position) == R.layout.item_interest_custom) {
            binding.tvHint.setText("万元");
            return;
        }
        DownPaymentInfo info = get(position);
        vh.getBinding().tvInterest.setText(info.getProportion());
        if (info.isChecked()) {
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
        for (DownPaymentInfo info : this.getData()) {
            info.setChecked(false);
        }
        getData().get(position).setChecked(true);
        notifyDataSetChanged();
        Intent intent = new Intent(activity, MortgageCalculatorActivity.class);
        intent.putExtra(Constants.REQUEST_ACTIVITY_HOUSER_SCALE, MathUtils.div(get(position).getScale(), 10, 2));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    protected int getItemLayoutId(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.item_interest_custom;
        }
        return R.layout.item_interest_text;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.item_interest_custom;
        }
        return super.getItemViewType(position);
    }
}
