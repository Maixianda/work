package com.gzliangce.ui.activity.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySettingBinding;
import com.gzliangce.db.DBManager;
import com.gzliangce.dto.MetadataDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.ui.activity.qualification.QualificationActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.dialog.ShareAppDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AVImClientManagerUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.MetadataUtil;
import com.gzliangce.util.ShareUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/1/12.
 * 设置界面
 */
public class SettingActivity extends BaseSwipeBackActivityBinding implements ShareAppDialog.OnShareAppListener, MaterialDialog.SingleButtonCallback, PlatformActionListener {
    private ActivitySettingBinding binding;
    private ShareAppDialog shareAppDialog;
    private String phone;
    private String title = "住房消费金融O2O平台";
    private String content = "一键完成房产，金融政策咨询查询，住房按揭，装修分期，投资理财等产品及人员对接。";
    private String url = "http://sj.qq.com/myapp/detail.htm?apkName=com.gzliangce";

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.llyQualification.setOnClickListener(onSingleClickListener);
        binding.tvAboutMe.setOnClickListener(onSingleClickListener);
        binding.tvShareApp.setOnClickListener(onSingleClickListener);
        binding.tvEditPassword.setOnClickListener(onSingleClickListener);
        binding.llyLockSetting.setOnClickListener(onSingleClickListener);
        binding.tvLogout.setOnClickListener(onSingleClickListener);
        binding.llyVersionUpdate.setOnClickListener(onSingleClickListener);
        binding.llyPhoneNumber.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        header.setMidTitle("设置");
        binding.setUser(AppContext.me().getUser());
        binding.tvVersion.setText("V" + Systems.getVersionName(this));
        initLockState();
        getAppPhone();
        setLayoutState();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }


    /**
     * onClick
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_share_app:
                    shaowShareDialog();
                    break;
                case R.id.tv_about_me:
                    IntentUtil.actionActivity(v.getContext(), AboutAppActivity.class);
                    break;
                case R.id.lly_phone_number:
                    DialogUtil.showCallDialog(SettingActivity.this, phone);
                    break;
                case R.id.lly_lock_setting:
                    IntentUtil.actionActivity(v.getContext(), LockSettingActivity.class);
                    break;
                case R.id.tv_edit_password:
                    IntentUtil.actionActivity(v.getContext(), EditPasswordActivity.class);
                    break;
                case R.id.tv_logout:
                    DialogUtil.getMaterialDialog(SettingActivity.this, getResources().getString(R.string.m_action_logout), SettingActivity.this).show();
                    break;
                case R.id.lly_qualification:
                    IntentUtil.actionActivity(v.getContext(), QualificationActivity.class);
                    break;
                case R.id.lly_version_update:
                    IntentUtil.actionWebActivity(SettingActivity.this, "版本更新说明", 1);
                    break;
            }
        }
    };


    /**
     * 获取客服电话
     */
    private void getAppPhone() {
        MetadataDTO dto = (MetadataDTO) MetadataUtil.getGCache(this, Constants.METADATA_DATA_KEY);
        if (dto != null) {
            phone = dto.getServiceTel();
            binding.tvPhone.setText(phone);
        }
    }

    /**
     * 拨打客服电话
     */
    private void actionCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * 打开分享Dialog
     */
    private void shaowShareDialog() {
        if (shareAppDialog == null) {
            shareAppDialog = new ShareAppDialog(this, this);
        }
        shareAppDialog.show();
    }

    /**
     * 初始化锁屏状态(对应的key,后期要加上user_id)
     */
    private void initLockState() {
        String pinLock = Config.getString(AppContext.me().getPinKey());
        String gestureLock = Config.getString(AppContext.me().getGestureKey());
        if (Strings.isNotEmpty(pinLock)) {
            binding.tvLockState.setText("PIN解锁");
        } else if (Strings.isNotEmpty(gestureLock)) {
            binding.tvLockState.setText("手势解锁");
        } else {
            binding.tvLockState.setText("未设置");
        }
    }


    /**
     * 分享到新浪微博
     */
    @Override
    public void onShareSina() {
        ShareUtil.shareApp(this, SinaWeibo.NAME, title, content, url, this);
    }

    /**
     * 分享到微信
     */
    @Override
    public void onShareWechat() {
        ShareUtil.shareApp(this, Wechat.NAME, title, content, url, this);
    }

    /**
     * 分享到微信朋友圈
     */
    @Override
    public void onShareWechatMoments() {
        ShareUtil.momentShare(this, content, url);
    }

    /**
     * 分享给QQ好友
     */
    @Override
    public void onShareQQfriend() {
        ShareUtil.shareApp(this, QQ.NAME, title, content, url, this);
    }


    /**
     * 分享到QQ空间
     */
    @Override
    public void onShareQQZone() {
        ShareUtil.shareApp(this, QZone.NAME, title, content, url, this);
    }


    /**
     * 设置资格认证通过状态
     */
    private void setLayoutState() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        AccountInfo info = AppContext.me().getUser();
        if (!Strings.isEquals(info.getType(), UserType.mediator.toString()) && binding.llyQualification.getVisibility() == View.VISIBLE) {
            binding.llyQualification.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        initLockState();
        binding.setUser(AppContext.me().getUser());
        super.onResume();
    }

    private void cleanData() {
        Config.remove(AppContext.me().getGestureKey());
        Config.remove(AppContext.me().getPinKey());
        AppContext.me().setUser(null);
        AppContext.me().removeAvInstallation();
        EventHub.post(new LogoutEvent());
        onBackPressed();
    }


    /**
     * 分享回调
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        logger.e("分享成功-------");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        logger.e("分享失败-------" + throwable.toString());
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    /**
     * Dialog确定
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        LoadingHelper.showMaterLoading(this, "退出登录中...");
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DBManager.getInstance().dropTable();
                if (AVImClientManagerUtil.getInstance().getClient() == null) {
                    cleanData();
                    LoadingHelper.hideMaterLoading();
                    return;
                }
                AVImClientManagerUtil.getInstance().close(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        AVImClientManagerUtil.getInstance().doColseClient();
                        cleanData();
                        LoadingHelper.hideMaterLoading();
                    }
                });
            }
        });
    }
}
