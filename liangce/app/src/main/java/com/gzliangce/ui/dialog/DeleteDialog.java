package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogDeleteReminderBinding;
import com.gzliangce.databinding.DialogHeadPortraitBinding;
import com.gzliangce.databinding.ItemSwipeBinding;
import com.gzliangce.ui.activity.usercenter.UserSwipeActivity;
import com.gzliangce.ui.adapter.UserSwipedAdapter;
import com.gzliangce.util.PhotoUtil;

import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Systems;

/**
 * Created by aaron on 11/24/15.
 * 删除语言或者职能范围提示
 */
public class DeleteDialog extends BaseDialog implements View.OnClickListener {
    private Activity activity;
    private DialogDeleteReminderBinding binding;
    private BaseViewHolder<ItemSwipeBinding> vh;
    private UserSwipedAdapter adapter;
    private int fromType;

    public DeleteDialog(Activity activity, UserSwipedAdapter adapter, BaseViewHolder<ItemSwipeBinding> vh, int fromType) {
        super(activity);
        this.activity = activity;
        this.vh = vh;
        this.adapter = adapter;
        this.fromType = fromType;
    }

    @Override
    public void beforeInitView() {
        binding = DialogDeleteReminderBinding.inflate(activity.getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        if (fromType == 1) {
            binding.setData(true);
        } else {
            binding.setData(false);

        }
        setCanceledOnTouchOutside(false);
//        setWidthSize(Systems.getScreenWidth(activity));
//        setLocation(Gravity.BOTTOM);
        setAnim(R.style.slideToUp);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.tvComfirm.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comfirm:
                String postionData = adapter.get(vh.getAdapterPosition());
                adapter.remove(vh.getAdapterPosition());
                ((UserSwipeActivity) activity).updateDatum(false, vh.getAdapterPosition(), postionData);
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }

}
