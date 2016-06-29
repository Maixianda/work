package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityBrokeCollectionBinding;
import com.gzliangce.dto.CollectionBrokeDTO;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.event.RefreshMyCollecttionBrokerEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.BrokeAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 经纪人收藏页面
 * Created by aaron on 1/18/16.
 */
public class BrokerColletionActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private final Logger logger = LoggerFactory.getLogger(BrokerColletionActivity.class);
    private ActivityBrokeCollectionBinding binding;
    private BrokeAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_broke_collection);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new BrokeAdapter(this, new PlaceAnOrder());
        adapter.setButtonStatus(ButtonStatus.OrderDirectBtn);
        adapter.onFinishLoadMore(true);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.rvBrokeList.setLayoutManager(linearLayoutManager);
        binding.rvBrokeList.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(onClick);
        binding.rvBrokeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                binding.srvRefresh.setEnabled(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    @Override
    public void initData() {
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
            onRefresh();
        }
    };

    @Override
    public void onBackClicked() {
        finish();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("收藏的金融经纪");
        header.setRightBackground(0);
        binding.setHeader(header);
    }


    /**
     * 获取收藏经纪人列表
     */
    private void getCollectionList() {
        Call<CollectionBrokeDTO> call = ApiUtil.getUserCenterService().getCollectionBrokerList("mortgage");
        call.enqueue(new APICallback<CollectionBrokeDTO>() {
            @Override
            public void onSuccess(CollectionBrokeDTO collectionBrokeDTO) {
                handlerrCollectionData(collectionBrokeDTO);
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                        binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(BrokerColletionActivity.this, message + "");
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 处理收藏经纪人数据
     */
    private void handlerrCollectionData(CollectionBrokeDTO collectionBrokeDTO) {
        if (collectionBrokeDTO == null) {
            return;
        }
        if (collectionBrokeDTO.getList().size() <= 0) {
            UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                    binding.srvRefresh, R.string.no_collection, false);
        }
        adapter.clear();
        adapter.addAll(collectionBrokeDTO.getList());
        adapter.notifyDataSetChanged();
    }


    /**
     * 刷新数据
     */
    @Override
    public void onRefresh() {
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCollectionList();
            }
        }, 500);
    }


    /**
     * 接收广播删除照片
     */
    @Subscribe
    public void onRefreshMyCollecttionEvent(RefreshMyCollecttionBrokerEvent event) {
        onRefresh();
    }
}
