package com.gzliangce.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogNoPassHintBinding;

import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Strings;

/**
 * Created by leo on 16/5/27.
 * 认证失败，提示Dialog
 */
public class NoPassHintDialog extends BaseDialog implements View.OnClickListener {
    private DialogNoPassHintBinding binding;
    private String hint;
    private LayoutInflater layoutInflater;

    public NoPassHintDialog(Context context, String hint) {
        super(context);
        this.hint = hint;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void beforeInitView() {
        binding = DialogNoPassHintBinding.inflate(layoutInflater, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        setAnim(R.style.slideToUp);
    }

    @Override
    public void initView() {
        if (Strings.isEmpty(hint)) {
            return;
        }
        binding.tvHint.setText(hint);
    }

    @Override
    public void initListener() {
        binding.flSure.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
