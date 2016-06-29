package com.gzliangce.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentMineBinding;
import com.gzliangce.dto.UserDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.UserState;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.MessageCenterRedPointEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.lclibrary.base.BaseWebActivity;
import com.gzliangce.lclibrary.ui.MenuBtnWebActivity;
import com.gzliangce.seccond_ver.SearchHourse.JSUtils;
import com.gzliangce.seccond_ver.SearchHourse.activity_header_test;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.qualification.QualificationActivity;
import com.gzliangce.ui.activity.setting.SettingActivity;
import com.gzliangce.ui.activity.usercenter.MyCollectionActivity;
import com.gzliangce.ui.activity.usercenter.MyDataActivity;
import com.gzliangce.ui.activity.usercenter.MyOrderActivity;
import com.gzliangce.ui.activity.usercenter.SimpleUserOderActivity;
import com.gzliangce.ui.activity.usercenter.UserInfoActivity;
import com.gzliangce.ui.dialog.NoPassHintDialog;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.XmlUtil;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Numbers;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.crypto.Rsas;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 我的界面
 * <p/>
 * Created by Aaron on 8/21/14.
 */
public class MineFragment extends BaseFragment {
    private static Logger logger = LoggerFactory.getLogger(MineFragment.class);
    private FragmentMineBinding binding;
    private boolean isHidden = true;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater, container, false);
        mainActivity = ((MainActivity) getActivity());
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        binding.llyAction.setOnClickListener(onClick);
        binding.rlyMyOrder.setOnClickListener(onClick);
        binding.rlyMyCollection.setOnClickListener(onClick);
        binding.rlySetting.setOnClickListener(onClick);
        binding.rlMyData.setOnClickListener(onClick);
        binding.rlyOperate.setOnClickListener(onClick);
        binding.tvTick.setOnClickListener(onClick);
        binding.tvNoPass.setOnClickListener(onClick);
        binding.tvRetry.setOnClickListener(onClick);
        binding.rlyMySearchHourse.setOnClickListener(onClick);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }


    /**
     * onclick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.lly_action:
                    IntentUtil.actionActivity(getContext(), UserInfoActivity.class);
                    break;
                case R.id.rly_my_order:
                    actionOrderActivity();
                    break;
                case R.id.rly_my_collection:
                    IntentUtil.actionActivity(getContext(), MyCollectionActivity.class);
                    break;
                case R.id.rly_setting:
                    IntentUtil.actionActivity(getContext(), SettingActivity.class);
                    break;
                case R.id.rl_my_data:
                    IntentUtil.actionActivity(getContext(), MyDataActivity.class);
                    break;
                case R.id.rly_operate:
                    actionOperateActivity();
                    break;
                case R.id.tv_retry:
                    IntentUtil.actionActivity(getContext(), QualificationActivity.class);
                    break;
                case R.id.tv_tick:
                    IntentUtil.actionActivity(getContext(), QualificationActivity.class);
                    break;
                case R.id.tv_no_pass:
                    showNoPassHint();
                    break;
                case R.id.rly_my_search_hourse:
                    searchHouse();
                    break;
            }
        }
    };

    /**
     * 显示我的查册列表
     */
    private void searchHouse() {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(getActivity(), LoginActivity.class);
        }
        Intent intent2 = new Intent(getActivity(), activity_header_test.class);
        String token = "";
        String signature = "";
        String timestamp = System.currentTimeMillis() + "";
        String deviceId = Systems.getDeviceId(AppContext.me());
        token = AppContext.me().getUser().getToken();
        signature = Rsas.getMD5(token + timestamp + deviceId);

        String url = "http://test.lcedai.com/app/v1/search/list.html?signature=" + signature + "&timestamp=" + timestamp + "&deviceId=" + deviceId;
        intent2.putExtra(BaseWebActivity.WEB_URL, url);
        String js1 = "javascript:(function(){\n" +
                "document.getElementById(\"tokenId\").value =' " + "我爱!!罗明通" + "'" +
                "})()";

        HashMap<String, String> map = new HashMap<>();

        map.put("token", token);
        map.put("deviceId", deviceId);
        String js = JSUtils.getChangeElementJs(map);

        intent2.putExtra(MenuBtnWebActivity.JAVASCRIPT_KEY, js);
        getActivity().startActivity(intent2);
    }

    /**
     * 显示未通过认证原因提示
     */
    private void showNoPassHint() {
        NoPassHintDialog dialog = new NoPassHintDialog(getContext(), AppContext.me().getUser().getInfo().getReason());
        dialog.show();
    }


    /**
     * 跳转到操作指引界面
     */
    private void actionOperateActivity() {
        int index = 0;
        switch (LiangCeUtil.getUserType()) {
            case mediator:
                index = 5;
                break;
            case mortgage:
                index = 6;
                break;
            case simpleUser:
                index = 7;
                break;
        }
        IntentUtil.actionWebActivity(getContext(), "操作指引", index);
    }

    /**
     * 跳转到我的订单界面
     */
    private void actionOrderActivity() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        //普通用户类型
        if (LiangCeUtil.getUserType() == UserType.simpleUser) {
            IntentUtil.actionActivity(getContext(), SimpleUserOderActivity.class);
        } else {
            IntentUtil.actionActivity(getContext(), MyOrderActivity.class);
        }
    }


    /**
     * 例子：获取用户信息
     */
    private void getUserInfo() {
        AccountInfo userInfo = AppContext.me().getUser();
        if (userInfo == null) {
            return;
        }
        String status = null;
        if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            status = userInfo.getInfo().getStatus();
        }
        if (LiangCeUtil.judgeUserType(UserType.simpleUser)) {
            binding.tvPhone.setVisibility(View.VISIBLE);
        } else {
            binding.tvPhone.setVisibility(View.INVISIBLE);
        }
        if (XmlUtil.isPassVisbile()) {
            binding.ivPassIcon.setVisibility(View.VISIBLE);
        } else {
            binding.ivPassIcon.setVisibility(View.GONE);
        }
        binding.setUserState(new UserState(status));
        binding.setUser(userInfo);
        setRatingBtnState(userInfo);
    }

    /**
     * 根据登录用户类型 刷新按钮状态
     */
    private void setRatingBtnState(AccountInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            binding.rbBrokeRating.setVisibility(View.VISIBLE);
            binding.rbBrokeRating.setRating(userInfo.getInfo().getGrade());
        } else {
            binding.rbBrokeRating.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        showWhitePoint();
        getServiceUserInfo();
        super.onResume();
    }

    /**
     * 获取用户资料
     */
    private void getServiceUserInfo() {
        if (!AppContext.me().isLogined()) {
            return;
        }

        ApiUtil.getAttestation().getUserInfo().enqueue(new APICallback<UserDTO>() {
            @Override
            public void onSuccess(UserDTO userDTO) {
                if (userDTO != null && userDTO.getAccount() != null) {
                    AccountInfo info = userDTO.getAccount();
                    info.setToken(AppContext.me().getUser().getToken());
                    AppContext.me().setUser(info);
                }
            }

            @Override
            public void onFailed(String message) {
                logger.d("MineFragment:onFailed:" + message);
            }

            @Override
            public void onFinish() {
                getUserInfo();
            }
        });
    }


    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        getUserInfo();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        showWhitePoint();
    }

    /**
     * 接收消息推送,显示白点
     */
    @Subscribe
    public void showMessageCenterPoint(MessageCenterRedPointEvent event) {
        showWhitePoint();
    }

    /**
     * 显示消息提醒
     */
    private void showWhitePoint() {
        if (!isHidden) {
            mainActivity.showWhitePoint();
        }
    }

}