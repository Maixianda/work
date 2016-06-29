package com.gzliangce.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogErrorPasswordBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * Created by leo on 16/1/25.
 * 输错密码dialog
 */
public class ErrorPasswordDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private DialogErrorPasswordBinding binding;
    private String hintText;
    private IRemindDialogCallback iRemindDialogCallback;
    private LayoutInflater layoutInflater;

    public ErrorPasswordDialog(Context context, String message, IRemindDialogCallback callback) {
        super(context);
        this.context = context;
        hintText = message;
        this.iRemindDialogCallback = callback;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void beforeInitView() {
        binding = DialogErrorPasswordBinding.inflate(layoutInflater, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        setAnim(R.style.slideToUp);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.tvComfirm.setOnClickListener(this);
    }

    @Override
    public void initData() {
        binding.tvHint.setText(hintText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comfirm:
                iRemindDialogCallback.actionConfirm();
                dismiss();
                break;
        }
    }
}
