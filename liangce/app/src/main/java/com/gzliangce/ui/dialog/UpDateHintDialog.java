package com.gzliangce.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.DialogUpdateDialogBinding;

import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Systems;

/**
 * Created by leo on 16/5/20.
 * 升级提示Dilog
 */
public class UpDateHintDialog extends BaseDialog implements View.OnClickListener {
    private String title, content;
    private DialogUpdateDialogBinding binding;

    public UpDateHintDialog(Context context, String title, String content) {
        super(context);
        this.content = content;
        this.title = title;
    }

    @Override
    public void beforeInitView() {
        binding = DialogUpdateDialogBinding.inflate(getLayoutInflater(), null, false);
        setCanceledOnTouchOutside(false);
        setContentView(binding.getRoot());
        setWidthSize(Systems.getScreenWidth(getContext()));
        setLocation(Gravity.CENTER);
        setAnim(R.style.slideToUp);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.ibtnClose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        binding.tvTitle.setText("您已升级到" + title + "版本");
        binding.tvContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        CacheManager.disk(getContext()).remove(Constants.APP_UPDATE_NKOTE);
        dismiss();
    }
}
