package com.gzliangce.ui.activity.order;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySelectBrokerListBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.BrokeAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.IntentUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import retrofit.Call;

/**
 * Created by leo on 16/1/23.
 * 选择金融经纪人 - 列表界面
 */
public class SelectBrokerListActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private ActivitySelectBrokerListBinding binding;
    private BrokeAdapter mBrokerAdapter;
    private int page = 1;
    private ButtonStatus buttonStatus;
    private PlaceAnOrder placeAnOrder;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_broker_list);
    }

    @Override
    public void initView() {
        placeAnOrder = (PlaceAnOrder) getIntent().getSerializableExtra(Constants.PLACE_ON_ORDER_PARM);
        buttonStatus = (ButtonStatus) getIntent().getSerializableExtra(Constants.REQUEST_BUTTON_STATUS);
        mBrokerAdapter = new BrokeAdapter(this, placeAnOrder);
        mBrokerAdapter.setButtonStatus(buttonStatus);
        mBrokerAdapter.onFinishLoadMore(true);
        binding.rvBroker.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBroker.setAdapter(mBrokerAdapter);
    }

    @Override
    public void initListener() {
        binding.ibtnBlack.setOnClickListener(onSingleClickListener);
        binding.ibtnMap.setOnClickListener(onSingleClickListener);
        binding.ibtnSeach.setOnClickListener(onSingleClickListener);
        binding.srvRefresh.setOnRefreshListener(this);
        mBrokerAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getMortgageUserList();
            }
        });
    }

    @Override
    public void initData() {
        addBrokerData();
    }


    /**
     * 处理列表数据
     */
    private void addBrokerData() {
        List<BrokeInfo> list = (List<BrokeInfo>) getIntent().getSerializableExtra(Constants.PLACE_ON_ORDER_PARM_LIST);
        if (!Collections.isEmpty(list)) {
            mBrokerAdapter.addAll(list);
            binding.srvRefresh.setEnabled(false);
            mBrokerAdapter.notifyDataSetChanged();
            binding.tvTitle.setText("选择金融经纪");
            setHint();
        } else {
            binding.tvTitle.setText("金融经纪人");
            binding.ibtnMap.setVisibility(View.GONE);
            binding.srvRefresh.post(new Runnable() {
                @Override
                public void run() {
                    binding.srvRefresh.setRefreshing(true);
                    onRefresh();
                }
            });
        }
    }

    /**
     * 设置提示文字
     */
    private void setHint() {
        if (mBrokerAdapter.size() <= 0) {
            binding.tvHint.setVisibility(View.VISIBLE);
        } else {
            binding.tvHint.setVisibility(View.GONE);
        }
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_map:
                    onBackPressed();
                    break;
                case R.id.ibtn_seach:
                    IntentUtil.intentSearchActivity(SelectBrokerListActivity.this, Constants.SEARCH_TYPE[2], placeAnOrder, buttonStatus);
                    break;
                case R.id.ibtn_black:
                    finish();
                    break;
            }
        }
    };

    /**
     * 获取金融经纪列表
     */
    private void getMortgageUserList() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        Call<ListDTO<BrokeInfo>> call = ApiUtil.getUserCenterService().getMortgageUserList(map);
        call.enqueue(new APICallback<ListDTO<BrokeInfo>>() {
            @Override
            public void onSuccess(ListDTO<BrokeInfo> brokeInfoListDTO) {
                handlerMortgageData(brokeInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
            }

            @Override
            public void onFinish() {
                mBrokerAdapter.hideLoadMore();
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 处理金融经纪数据
     */
    private void handlerMortgageData(ListDTO<BrokeInfo> dto) {
        if (dto == null) {
            return;
        }
        if (Collections.isEmpty(dto.getList())) {
            AdapterUtil.setHint(mBrokerAdapter, binding.tvHint);
            return;
        }
        if (page == 1) {
            mBrokerAdapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(mBrokerAdapter, dto.getList(), page);
        mBrokerAdapter.addAll(dto.getList());
        mBrokerAdapter.notifyDataSetChanged();
        page++;
    }

    @Override
    public void onRefresh() {
        page = 1;
        mBrokerAdapter.onFinishLoadMore(true);
        binding.srvRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMortgageUserList();
            }
        }, 500);
    }
}
