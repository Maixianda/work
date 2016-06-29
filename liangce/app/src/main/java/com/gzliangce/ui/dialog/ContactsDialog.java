package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.DialogContactsBinding;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.enums.UserType;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.chat.ConversationActivity;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;

/**
 * Created by aaron on 11/24/15.
 * 订单详情联系经纪人Dialog
 */
public class ContactsDialog extends BaseDialog implements View.OnClickListener {
    private Activity activity;
    private DialogContactsBinding binding;
    private String memberId;
    private String phone;

    public ContactsDialog(Activity activity, String phone, String memberId) {
        super(activity);
        this.activity = activity;
        this.phone = phone;
        this.memberId = memberId;
    }

    @Override
    public void beforeInitView() {
        binding = DialogContactsBinding.inflate(activity.getLayoutInflater(), null, false);
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
        binding.tvDial.setOnClickListener(this);
        binding.tvSendMessage.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        binding.tvDial.setText(phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dial:
                CallDialog callDialog = new CallDialog(activity, phone);
                callDialog.show();
                break;
            case R.id.tv_send_message:
                doSendMessage();
                break;
        }
        dismiss();
    }

    private void doSendMessage() {
        Intent intent = new Intent(activity, ConversationActivity.class);
        if (!AppContext.me().isLogined()) {
            ToastHelper.showMessage(activity, "登录后方可联系金融经纪");
            IntentUtil.actionActivity(activity, LoginActivity.class);
            return;
        }
        if (Strings.isEmpty(memberId)) {
            ToastHelper.showMessage(activity, "该用户ID为空");
            return;
        }
        if (LiangCeUtil.judgeUserId(memberId)) {
            ToastHelper.showMessage(activity, "不能跟自己聊天");
            return;
        }
        intent.putExtra(Constants.CHAT_MEMBER_ID, memberId);
        intent.putExtra(Constants.CHAT_CONVERSATION_IS_STAFF, false);
        activity.startActivity(intent);
    }

}
