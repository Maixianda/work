package com.gzliangce.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMainBinding;
import com.gzliangce.dto.StaffDTO;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.ImRedPointEvent;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.lclibrary.base.BaseWebActivity;
import com.gzliangce.lclibrary.ui.MenuBtnWebActivity;
import com.gzliangce.seccond_ver.checkHourse.JSUtils;
import com.gzliangce.seccond_ver.checkHourse.webView_CheckHouse;
import com.gzliangce.seccond_ver.util.SaveParameter;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.calculator.MortgageCalculatorActivity;
import com.gzliangce.ui.activity.chat.ConversationActivity;
import com.gzliangce.ui.activity.college.MyCourseActivity;
import com.gzliangce.ui.activity.usercenter.MessageCenterActivity;
import com.gzliangce.ui.activity.usercenter.SearchActivity;
import com.gzliangce.ui.base.BaseFragmentActivtyBinding;
import com.gzliangce.ui.fragment.CollegeFragment;
import com.gzliangce.ui.fragment.HomeFragment;
import com.gzliangce.ui.fragment.MessageFragment;
import com.gzliangce.ui.fragment.MineFragment;
import com.gzliangce.ui.fragment.NewsAnticipateFragment;
import com.gzliangce.ui.fragment.usercenter.AllOrderFragment;
import com.gzliangce.ui.model.FooterModel;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.receiver.InnerRecevier;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MetadataUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.crypto.Rsas;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

import static android.support.v4.app.FragmentTransaction.*;

/**
 * 主界面
 */
public class MainActivity extends BaseFragmentActivtyBinding implements FooterModel.FootView, View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private ActivityMainBinding binding;
    private FooterModel footer;
    private MessageFragment messageFragment;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private int from = 0;
    private String staffId;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setHeader();
        setFooter();
    }

    @Override
    public void initView() {
        String token = "";
        String signature = "";
        String timestamp = System.currentTimeMillis() + "";
        String deviceId = Systems.getDeviceId(AppContext.me());
        if (AppContext.me().isLogined()) {
            logger.e("MainActivity:user:" + AppContext.me().getUser());
            token = AppContext.me().getUser().getToken();
            signature = Rsas.getMD5(token + timestamp + deviceId);
        }
        InnerRecevier.getInstance().startWatch(this);
        logger.e("main_timestamp----" + timestamp);
        logger.e("main_timestamp----" + timestamp);
        logger.e("main_deviceId----" + deviceId);
        logger.e("main_signature----" + signature);
    }

    @Override
    public void initListener() {
        binding.getHeader().onMenuClickListener();
    }

    @Override
    public void initData() {
        MetadataUtil.getInstance().getMetadata(this);
        MetadataUtil.getInstance().getAreaData(this, null);
        MetadataUtil.getInstance().getRateData(this);
        getOnlineStaff();
        initFragment();
        isShowPoint(Constants.SHOW_RED_POINT, binding.icdFooter.ivRedPoint);
        AppContext.me().registerUpDateApp();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        DialogUtil.exitByDialog(this);
    }


    @Override
    public void onBackClicked() {
        if (from == 2 || from == 0) {
            startActivity(new Intent(this, MortgageCalculatorActivity.class));
        }
    }

    @Override
    public void onMenuClicked() {
        if (from == 0) {
            actionSeachActivity();
        } else if (from == 1) {
            actionMyCourseActivity();
        } else if (from == 2) {
            actionConversationActivity();
        }else if (from == 3)
        {
            if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
                if (AppContext.me().isLogined()) {
                    Intent intent2 = new Intent(this, webView_CheckHouse.class);
                    intent2.putExtra(BaseWebActivity.WEB_URL, "http://test.lcedai.com/app_public/v1/search/search.html");

                    HashMap<String, String> map = new HashMap<>();
                    SaveParameter.Tocken_signature_deviceId para = SaveParameter.getInstance().getTocken_signature_deviceId();

                    map.put("token", para.getmTocken());
                    map.put("deviceId", para.getmDeviceId());
                    String js = JSUtils.getChangeElementJs(map);
                    intent2.putExtra(MenuBtnWebActivity.JAVASCRIPT_KEY, js);
                    startActivity(intent2);
                } else {
                    IntentUtil.actionActivity(this, LoginActivity.class);
                }
            }
        }else if (from == 4) {
            setMessageTitle();
        } else if (from == 5) {
            startActivity(new Intent(this, MessageCenterActivity.class));
        }
    }


    /**
     * 跳转到我的课程界面
     */
    private void actionMyCourseActivity() {
        if (!AppContext.me().isLogined()) {//判断是否已经登录
            IntentUtil.actionActivity(this, LoginActivity.class);
            return;
        }
        Intent intent = new Intent(this, MyCourseActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到搜索资讯页面
     */
    private void actionSeachActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_RESULT_TYPE, Constants.SEARCH_TYPE[3]);
        startActivity(intent);
    }

    /**
     * 界面切换到消息界面时，设置right menu 菜单名称
     */
    private void setMessageTitle() {
        if (messageFragment.getStatus()) {
            if (Strings.isEquals(header.getRightTitle(), "删除")) {
                messageFragment.deleteConversation();
            }
            header.setRightTitle("编辑");
        } else {
            header.setRightTitle("完成");
        }
        messageFragment.doEditModle();
        binding.setHeader(header);
    }


    /**
     * 跳转到在线客服界面
     */
    private void actionConversationActivity() {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(this, LoginActivity.class);
            return;
        }
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            return;
        }
        //测试
//        staffId = "214";
        if (!Strings.isEmpty(staffId)) {
            if (LiangCeUtil.judgeUserId(staffId)) {
                ToastHelper.showMessage(this, "不能跟自己聊天");
                return;
            }
            Intent intent = new Intent(this, ConversationActivity.class);
            intent.putExtra(Constants.CHAT_MEMBER_ID, staffId);
            intent.putExtra(Constants.CHAT_CONVERSATION_IS_STAFF, true);
            startActivity(intent);
        } else {
            ToastHelper.showMessage(this, "在线客服还没有准备好....");
        }
    }

    @Override
    public void onOrderClicked() {
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            header.setRightIcon(0);
            header.setRightBackground(0);
        } else {
            header.setRightIcon(R.drawable.ic_contact_staff);
            header.setRightBackground(R.drawable.ripple_default);
        }
        if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
            header.setLeftIcon(R.drawable.ic_left_caculator);
            header.setLeftBackground(R.drawable.ripple_default);
        } else {
            header.setLeftIcon(0);
            header.setLeftBackground(0);
        }
        header.setRightTitle("");
        header.setMidTitle("良策金服");
        switchFragment(2);
    }

    @Override
    public void onMessageClicked() {
        header.setLeftIcon(0);
        header.setLeftBackground(0);
        header.setRightIcon(0);
        header.setRightBackground(R.drawable.ripple_default);
        header.setRightTitle("编辑");
        header.setMidTitle("消息");
        if (AppContext.me().isLogined()) {
            header.setMidTitle("消息");
            switchFragment(4);
        } else {
            IntentUtil.actionActivity(this, LoginActivity.class);
        }
    }

    @Override
    public void onCollegeClicked() {
        header.setLeftIcon(0);
        header.setRightIcon(0);
        header.setLeftBackground(0);
        header.setMidTitle("良策学院");
        header.setRightIcon(R.drawable.ic_my_course);
        header.setRightBackground(R.drawable.ripple_default);
        header.setRightTitle("");
        switchFragment(1);

    }

    @Override
    public void onNewsClicked() {
        header.setLeftIcon(R.drawable.ic_left_caculator);
        header.setLeftBackground(R.drawable.ripple_default);
        header.setRightIcon(R.drawable.ic_search);
        header.setRightBackground(R.drawable.ripple_default);
        header.setRightTitle("");
        header.setMidTitle("资讯中心");
        switchFragment(0);

    }

    @Override
    public void onMineClicked() {
        header.setLeftIcon(0);
        header.setLeftBackground(0);
        header.setRightBackground(R.drawable.ripple_default);
        header.setRightIcon(R.drawable.ic_mine_message_center);
        header.setRightTitle("");
        if (AppContext.me().isLogined()) {
            header.setMidTitle("我的");
            switchFragment(5);
        } else {
            IntentUtil.actionActivity(this, LoginActivity.class);
        }
    }


    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    /**
     * 设置footer
     */
    private void setFooter() {
        footer = new FooterModel(this);
        binding.setFooter(footer);
    }


    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.add(new NewsAnticipateFragment());
        fragments.add(new CollegeFragment());
        fragments.add(new HomeFragment());
        fragments.add(AllOrderFragment.getInstance(UserType.mortgage, MainActivity.class));
        messageFragment = new MessageFragment();
        fragments.add(messageFragment);
        fragments.add(new MineFragment());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < 6; i++) {
            fragmentTransaction.add(R.id.fly_main, fragments.get(i));
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commit();
        onOrderClicked();
    }


    /**
     * 根据用户类型设置菜单
     */
    private int switchOrderFragmentMenu() {
        if (!AppContext.me().isLogined()) {
            binding.icdFooter.tvOrder.setText("首页");
            return 2;
        }
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            initMortgage();
            return 3;
        } else if ((LiangCeUtil.judgeUserType(UserType.simpleUser))) {
            binding.icdFooter.tvOrder.setText("首页");
            return 2;
        } else {
            binding.icdFooter.tvOrder.setText("首页");
            return 2;
        }
    }


    /**
     * 当用户类型为按揭时，设置菜单以及右上角的我的查册按钮
     * 创建者：maixianda
     * 创建日期： 16-6-29
     */
    private void initMortgage() {
        binding.icdFooter.tvOrder.setText("我的订单");
        header.setRightIcon(R.drawable.ic_datum);
        header.setRightClickable(true);
    }

    /**
     * 切换Fragment
     */
    private void switchFragment(int to) {
        if (to == from) {
            return;
        }
        if (messageFragment != null) {
            messageFragment.setCompleteModle();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.hide(fragments.get(from));
        if (to == 2) {
            from = switchOrderFragmentMenu();
        } else {
            from = to;
        }
        if (to != 5) {
            goneWhitePoint();
        }
        if (to > 2) {
            footer.setPosition(to);
        } else {
            footer.setPosition(to + 1);
        }
        fragmentTransaction.show(fragments.get(from));
        fragmentTransaction.commit();
        binding.setFooter(footer);
        binding.setHeader(header);
    }


    /**
     * 接收退出账号Event,切换Fragment
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        if (event != null) {
            staffId = "";
            getOnlineStaff();
            onOrderClicked();
        }
    }


    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        doStartLocation();
        if (event == null) {
            return;
        }
        staffId = "";
        getOnlineStaff();
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            switchOrderFragmentMenu();
            onNewsClicked();
        } else {
            onOrderClicked();
        }

    }

    /**
     * 设置右上角button的text
     */
    public void setHeaderRightText(String text) {
        header.setRightTitle(text);
        binding.setHeader(header);
    }

    /**
     * 获取在线客服id
     */
    private void getOnlineStaff() {
        Call<StaffDTO> call = ApiUtil.getUserCenterService().getOnlineStaff();
        call.enqueue(new APICallback<StaffDTO>() {
            @Override
            public void onSuccess(StaffDTO staffDTO) {
                staffId = staffDTO.getStaff();
                if (Strings.isNotEmpty(staffId) && staffId.startsWith("system-")) {
                    staffId = staffId.replace("system-", "");
                }
                MetadataUtil.putStaff(MainActivity.this, staffId);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(MainActivity.this, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        InnerRecevier.getInstance().stopWatch(this);
        super.onDestroy();
    }


    /**
     * 接收IM消息推送,显示小红点
     */
    @Subscribe
    public void showRedPoint(ImRedPointEvent event) {
        binding.icdFooter.ivRedPoint.setVisibility(View.VISIBLE);
    }


    /**
     * 显示点 IM消息推送红点/消息中心白点 复用
     */
    public void isShowPoint(String key, ImageView imageView) {
        if (Strings.isEmpty(key)) {
            return;
        }
        if (Strings.isEquals(Config.getString(key), "enable")) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏IM消息红点
     */
    public void showRedPoint() {
        isShowPoint(Constants.SHOW_RED_POINT, binding.icdFooter.ivRedPoint);
    }

    /**
     * 显示消息中心白点
     */
    public void showWhitePoint() {
        isShowPoint(Constants.SHOW_WHITE_POINT, binding.ivWhitePoint);
    }

    /**
     * 隐藏消息中心的白点
     */
    public void goneWhitePoint() {
        binding.ivWhitePoint.setVisibility(View.GONE);
    }


}
