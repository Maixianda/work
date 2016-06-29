package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogHeadPortraitBinding;
import com.gzliangce.util.PhotoUtil;

import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Systems;

/**
 * Created by aaron on 11/24/15.
 * 选择照片
 */
public class PictureChoseDialog extends BaseDialog implements View.OnClickListener {
    private Activity activity;
    private DialogHeadPortraitBinding binding;

    public PictureChoseDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void beforeInitView() {
        binding = DialogHeadPortraitBinding.inflate(activity.getLayoutInflater(), null, false);
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
        binding.tvTakePicture.setOnClickListener(this);
        binding.tvChoseGallery.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_picture:
                PhotoUtil.takePhoto(activity);
                break;
            case R.id.tv_chose_gallery:
                PhotoUtil.choseGalleryPhoto(activity);
                break;
        }
        dismiss();
    }

}
