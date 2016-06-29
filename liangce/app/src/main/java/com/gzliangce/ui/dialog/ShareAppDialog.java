package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogShareAppBinding;

import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Systems;

/**
 * Created by leo on 16/1/12.
 * 分享app
 */
public class ShareAppDialog extends BaseDialog implements View.OnClickListener {
    private DialogShareAppBinding binding;
    private Activity activity;
    private OnShareAppListener onShareAppListener;

    public ShareAppDialog(Activity activity, OnShareAppListener onShareAppListener) {
        super(activity);
        this.activity = activity;
        this.onShareAppListener = onShareAppListener;
    }

    @Override
    public void beforeInitView() {
        binding = DialogShareAppBinding.inflate(activity.getLayoutInflater(), null, false);
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
        binding.tvCancel.setOnClickListener(this);
        binding.tvQqFriend.setOnClickListener(this);
        binding.tvQqZone.setOnClickListener(this);
        binding.tvSina.setOnClickListener(this);
        binding.tvWechat.setOnClickListener(this);
        binding.tvWechatMoments.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sina:
                onShareAppListener.onShareSina();
                break;
            case R.id.tv_wechat:
                onShareAppListener.onShareWechat();
                break;
            case R.id.tv_wechat_moments:
                onShareAppListener.onShareWechatMoments();
                break;
            case R.id.tv_qq_friend:
                onShareAppListener.onShareQQfriend();
                break;
            case R.id.tv_qq_zone:
                onShareAppListener.onShareQQZone();
                break;
        }
       this.cancel();
    }


    /**
     * 分享监听接口
     */
    public interface OnShareAppListener {
        public void onShareSina();

        public void onShareWechat();

        public void onShareWechatMoments();

        public void onShareQQfriend();

        public void onShareQQZone();
    }
}
