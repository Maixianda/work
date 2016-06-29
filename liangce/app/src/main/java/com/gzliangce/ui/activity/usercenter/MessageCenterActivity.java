package com.gzliangce.ui.activity.usercenter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMessageCenterBinding;
import com.gzliangce.dto.MessageCenterDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.adapter.MessageCenterAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/2/24.
 * 消息中心页面
 */
public class MessageCenterActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private ActivityMessageCenterBinding binding;
    private MessageCenterAdapter adapter;
    private int page = 1;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message_center);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new MessageCenterAdapter(this);
        adapter.onFinishLoadMore(true);
        binding.rvMessageCenter.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessageCenter.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(onClick);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getMessageCenterData();
            }
        });
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void initData() {
        Config.putString(Constants.SHOW_WHITE_POINT, "disable");
        binding.srvRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * onClick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            LoadingHelper.showMaterLoading(v.getContext(), "加载中");
        }
    };

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("消息中心");
        binding.setHeader(header);
    }

    @Override
    public void onRefresh() {
        page = 1;
        adapter.onFinishLoadMore(true);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMessageCenterData();
            }
        }, 500);
    }

    /**
     * 获取消息中心数据
     */
    private void getMessageCenterData() {
        Call<MessageCenterDTO> call = ApiUtil.getOtherDataService().getMessageCenterData(page, Constants.PAGE_SIZE);
        call.enqueue(new APICallback<MessageCenterDTO>() {
            @Override
            public void onSuccess(MessageCenterDTO messageCenterDTO) {
                handlerMessageCenterData(messageCenterDTO);
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(MessageCenterActivity.this, message);
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 填充消息中心数据
     */
    private void handlerMessageCenterData(MessageCenterDTO dto) {
        if (dto == null) {
            return;
        }
        if (Collections.isEmpty(dto.getList())) {
            UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_messgae, false);
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, dto.getList(), page);
        adapter.addAll(dto.getList());
        adapter.notifyDataSetChanged();
        page++;
    }

    @Override
    public void onBackPressed() {
        Activity mainActivity = AppManager.getActivity(MainActivity.class);
        if (mainActivity == null) {
            LiangCeUtil.isActionLockActivity(this);
        } else {
            finish();
        }
        return;
    }

}
