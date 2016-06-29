package com.gzliangce.ui.activity.usercenter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityPersonalDetailsBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.MortgageUserDTO;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.OrderStatusType;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.RefreshMyCollecttionBrokerEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.activity.chat.ConversationActivity;
import com.gzliangce.ui.base.BaseSwipeBackFragmentBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.ApiDataUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by Aaron on 16/1/14.
 * 个人详情界面（只用于金融经纪列表跳转、包括收藏部分，订单相关的个人详情，另外新建）
 */
public class PersonalDetailsActivity extends BaseSwipeBackFragmentBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(PersonalDetailsActivity.class);
    private ActivityPersonalDetailsBinding binding;
    private ButtonStatus buttonStatus;
    private int brokeInfoId;
    private AnimationDrawable animationDrawable;
    private boolean collection = false;
    private ObjectAnimator amplificationAnim;
    private PlaceAnOrder placeAnOrder;
    private String memberId;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_details);
        setHeader();
    }

    @Override
    public void initView() {
        amplificationAnim = AnimUtil.getAmplificationAnim(binding.icdHeader.rightHeader);
        binding.ivLoading.setImageResource(R.drawable.frame_loading);
        animationDrawable = (AnimationDrawable) binding.ivLoading.getDrawable();
        initMenuVisibility();
        if (LiangCeUtil.getUserType() != UserType.mortgage) {
            animationDrawable.start();
        } else {
            binding.ivLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {
        binding.tvJoinOrder.setOnClickListener(this);
        binding.tvChangeOrder.setOnClickListener(this);
        binding.tvSendMessage.setOnClickListener(this);
        binding.tvDirectOrder.setOnClickListener(this);
    }

    @Override
    public void initData() {
        buttonStatus = (ButtonStatus) getIntent().getSerializableExtra(Constants.REQUEST_BUTTON_STATUS);
        brokeInfoId = getIntent().getIntExtra(Constants.REQUEST_BROKE_INFO_ID, -1);
        placeAnOrder = (PlaceAnOrder) getIntent().getSerializableExtra(Constants.PLACE_ON_ORDER_PARM);
        getBrokerData();
    }


    /**
     * 获取传递过来的经纪人数据
     */
    private void getBrokerData() {
        if (brokeInfoId != -1) {
            Tasks.handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMortgageUserData(brokeInfoId);
                }
            }, 100);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_direct_order:
                ApiDataUtil.actionDirectOrder(this, memberId);
                break;
            case R.id.tv_change_order:
                showChangeOrderDialog(false,placeAnOrder);
                break;
            case R.id.tv_join_order:
                showChangeOrderDialog(true,placeAnOrder);
                break;
            case R.id.tv_send_message:
                actionConversationActivity();
                break;
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(this, LoginActivity.class);
            return;
        }
        toggleCollection();
    }


    /**
     * 跳转到聊天界面
     */
    private void actionConversationActivity() {
        if (!AppContext.me().isLogined()) {
            IntentUtil.actionActivity(this, LoginActivity.class);
            return;
        }
        if (LiangCeUtil.judgeUserId(memberId)) {
            ToastHelper.showMessage(this, "不能跟自己聊天");
            return;
        }
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(Constants.CHAT_MEMBER_ID, memberId);
        intent.putExtra(Constants.CHAT_CONVERSATION_IS_STAFF, false);
        startActivity(intent);
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("个人详情");
        header.setRightIcon(0);
        binding.setHeader(header);
    }

    private void toggleCollection() {
        isCollection();
        if (collection) {
            collection = false;
        } else {
            collection = true;
            amplificationAnim.start();
        }
    }


    /**
     * 设置收藏icon
     */
    private void setCollectionIcon() {
        if (collection) {
            header.setRightIcon(R.drawable.ic_collectied);
            binding.setHeader(header);
        } else {
            header.setRightIcon(R.drawable.ic_uncollection);
            binding.setHeader(header);
        }
    }


    /**
     * 收藏
     */
    private void isCollection() {
        if (!collection) {
            postCollection();
        } else {
            postDeleteCollection();
        }
    }


    /**
     * 收藏
     */
    private void postCollection() {
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postCollectionMortgage("mortgage", brokeInfoId + "");
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(PersonalDetailsActivity.this, "已收藏");
                setCollectionIcon();
                postEvent();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PersonalDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 取消收藏
     */
    private void postDeleteCollection() {
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postDeleteMortgage(brokeInfoId + "");
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                ToastHelper.showMessage(PersonalDetailsActivity.this, "已取消收藏");
                setCollectionIcon();
                postEvent();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PersonalDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 发送刷新收藏列表event
     */
    private void postEvent() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventHub.post(new RefreshMyCollecttionBrokerEvent());
            }
        });
    }


    /**
     * 获取按揭员详情
     */
    private void getMortgageUserData(long mortgageId) {
        LoadingHelper.showMaterLoading(this, "正在加载中...");
        Call<MortgageUserDTO> call = ApiUtil.getUserCenterService().getMortgageUserData(mortgageId);
        call.enqueue(new APICallback<MortgageUserDTO>() {
            @Override
            public void onSuccess(MortgageUserDTO mortgageUserDTO) {
                handlerData(mortgageUserDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(PersonalDetailsActivity.this, message);
            }

            @Override
            public void onFinish() {
                animationDrawable.stop();
                binding.ivLoading.setVisibility(View.GONE);
                DialogUtil.hideLoading();
            }
        });
    }


    /**
     * 获取按揭员详情
     */
    private void handlerData(MortgageUserDTO mortgageUserDTO) {
        if (mortgageUserDTO == null) {
            return;
        }
        memberId = mortgageUserDTO.getInfo().getAccountId() + "";
        binding.setData(mortgageUserDTO.getInfo());
        if (Strings.isEmpty(mortgageUserDTO.getInfo().getIsFavorite()) ||
                Strings.isEquals(mortgageUserDTO.getInfo().getIsFavorite(), "0")) {
            collection = false;
        } else {
            collection = true;
        }
        setCollectionIcon();
        setBtnVisibility();
    }


    /**
     * 设置按钮状态显示
     */
    private void setBtnVisibility() {
        binding.tvSendMessage.setVisibility(View.VISIBLE);
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            setMortgageBtnVisibility();
        } else if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            setMediatorBtnVisibility();
        } else if (LiangCeUtil.judgeUserType(UserType.simpleUser)) {
            binding.tvSendMessage.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 登录用户类型为按揭员的按钮显示状态
     */
    private void setMortgageBtnVisibility() {
        binding.icdHeader.rightHeader.setVisibility(View.GONE);
        binding.icdHeader.rightHeader.setEnabled(false);
        if (!Strings.isEquals(memberId, AppContext.me().getUserId())) {
            binding.tvSendMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始按钮状态
     */
    private void initMenuVisibility() {
        binding.tvJoinOrder.setVisibility(View.GONE);
        binding.tvSendMessage.setVisibility(View.GONE);
        binding.tvChangeOrder.setVisibility(View.GONE);
        binding.tvDirectOrder.setVisibility(View.GONE);
    }

    /**
     * 登录用户类型为中介的按钮显示状态
     */
    private void setMediatorBtnVisibility() {
        if (buttonStatus == null) {
            return;
        }
        if (buttonStatus == ButtonStatus.OrderDirectBtn) {
            binding.tvDirectOrder.setVisibility(View.VISIBLE);
            return;
        }
        if (buttonStatus == ButtonStatus.OrderDoBtn) {
            binding.tvJoinOrder.setVisibility(View.VISIBLE);
            return;
        }
        //buttonStatus == ButtonStatus.OrderStatusBtn 时，根据订单状态显示按钮
//        if (Strings.isEquals(orderState, OrderStatusType.wait.toString())
//                || Strings.isEquals(orderState, OrderStatusType.cancel.toString())) {
//            binding.llyBtn.setVisibility(View.GONE);
//        } else {
//            binding.tvSendMessage.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * 转单提示弹窗
     */
    private void showChangeOrderDialog(final boolean isPlaceOrder, final PlaceAnOrder placeAnOrder) {
        String str = "确定下单吗？";
        if (!isPlaceOrder) {
            str = "确定转单吗？";
        }
        DialogUtil.getMaterialDialog(this, str, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                placeAnOrder.setMortgageId(brokeInfoId + "");
                if (isPlaceOrder) {
                    ApiDataUtil.doPlaceAnOrder(placeAnOrder, PersonalDetailsActivity.this);
                } else {
                    ApiDataUtil.changeOrder(placeAnOrder, PersonalDetailsActivity.this);
                }
            }
        }).show();
    }
}
