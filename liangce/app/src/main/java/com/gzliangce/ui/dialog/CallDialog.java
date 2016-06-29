package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogCallBinding;
import com.gzliangce.databinding.DialogContactsBinding;
import com.gzliangce.entity.OrderInfo;

import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Systems;

/**
 * Created by aaron on 11/24/15.
 * 订单详情拨打电话Dialog
 */
public class CallDialog extends BaseDialog implements View.OnClickListener {
    private Activity activity;
    private DialogCallBinding binding;
    private String phone;

    public CallDialog(Activity activity, String phone) {
        super(activity);
        this.activity = activity;
        this.phone = phone;
    }

    @Override
    public void beforeInitView() {
        binding = DialogCallBinding.inflate(activity.getLayoutInflater(), null, false);
        setCanceledOnTouchOutside(false);
        setContentView(binding.getRoot());
        setWidthSize(Systems.getScreenWidth(activity));
        setLocation(Gravity.BOTTOM);
        setAnim(R.style.slideToUp);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.tvMakeSure.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_make_sure:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;
            case R.id.tv_send_message:
                break;
        }
        dismiss();
    }

}
