package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivitySearchBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.NewsInfo;
import com.gzliangce.entity.OrderInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.UserType;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.BrokeAdapter;
import com.gzliangce.ui.adapter.MyDataAdapter;
import com.gzliangce.ui.adapter.MyOrderAdapter;
import com.gzliangce.ui.adapter.NewsDetailedAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 搜索界面 - 复用
 */
public class SearchActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener, TextWatcher {
    private Logger logger = LoggerFactory.getLogger(SearchActivity.class);
    private ActivitySearchBinding binding;
    private LoadMoreAdapter adapter;
    private PlaceAnOrder placeAnOrder;
    private int searchType;
    private int page = 1;
    private ButtonStatus buttonStatus;

    @Override
    public void beforeInitView() {
        buttonStatus = (ButtonStatus) getIntent().getSerializableExtra(Constants.REQUEST_BUTTON_STATUS);
        searchType = getIntent().getIntExtra(Constants.SEARCH_RESULT_TYPE, 1);
        placeAnOrder = (PlaceAnOrder) getIntent().getSerializableExtra(Constants.PLACE_ON_ORDER_PARM);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
    }

    @Override
    public void initView() {
        switch (searchType) {
            case 1:
                adapter = new MyOrderAdapter(this, 1, SearchActivity.class);
                break;
            case 2:
                adapter = new MyDataAdapter(this);
                break;
            case 3:
                adapter = new BrokeAdapter(this, placeAnOrder);
                ((BrokeAdapter) adapter).setButtonStatus(buttonStatus);
                break;
            case 4:
                adapter = new NewsDetailedAdapter(this);
                break;
        }
        binding.rvResultList.setAdapter(adapter);
        adapter.onFinishLoadMore(true);
        binding.rvResultList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initListener() {
        binding.leftHeader.setOnClickListener(this);
        binding.tvSearch.setOnClickListener(this);
        binding.etSearchContent.addTextChangedListener(this);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                seachData();
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_header:
                finish();
                break;
            case R.id.tv_search:
                Systems.hideKeyboard(this);
                page = 1;
                seachData();
                break;
        }
    }

    /**
     * 获取搜索结果
     */
    private void seachData() {
        if (Strings.isEmpty(binding.etSearchContent.getText() + "")) {
            ToastHelper.showMessage(this, "关键字不能为空");
            return;
        }
        if (page == 1) {
            LoadingHelper.showMaterLoading(this, "搜索中");
        }
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postSeachData();
            }
        }, 500);
    }

    /**
     * 提交搜索请求
     */
    private void postSeachData() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(page));
        map.put(Constants.SIZE, String.valueOf(Constants.PAGE_SIZE));
        switch (searchType) {
            case 2:
                map.put(Constants.KEYWORD, binding.etSearchContent.getText().toString());
                documentSearch(map);
                break;
            case 1:
                map.put(Constants.KEYWORD, binding.etSearchContent.getText().toString());
                userTypeSwitchOrder(map);
                break;
            case 3:
                if (placeAnOrder != null) {
                    map.put(Constants.AREAI_D, placeAnOrder.getAreaId());
                }
                map.put(Constants.NAME, binding.etSearchContent.getText().toString());
                getMortgageUserList(map);
                break;
            case 4:
                map.put(Constants.KEYWORD, binding.etSearchContent.getText().toString());
                getNewListData(map);
                break;
        }
    }

    /**
     * 文档资料搜索
     *
     * @param map
     */
    private void documentSearch(Map<String, String> map) {
        Call<ListDTO<OrderInfo>> call = ApiUtil.getOrderService().getDocumentList(map);
        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleSeachData(orderInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SearchActivity.this, message);
            }

            @Override
            public void onFinish() {
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 根据用户的type调用不同API
     *
     * @param map
     */
    private void userTypeSwitchOrder(Map<String, String> map) {
        Call<ListDTO<OrderInfo>> call;

        if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            // 中介获取我的订单数据列表
            call = ApiUtil.getOrderService().getMediatorMyOrderList(map);
        } else if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            // 按揭获取我的订单数据列表
            call = ApiUtil.getOrderService().getMortgageMyOrderList(map);
        } else {
            call = ApiUtil.getOrderService().getSimpleUserMyOrderList(map);
        }
        call.enqueue(new APICallback<ListDTO<OrderInfo>>() {
            @Override
            public void onSuccess(ListDTO<OrderInfo> orderInfoListDTO) {
                handleSeachData(orderInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SearchActivity.this, message);
            }

            @Override
            public void onFinish() {
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 获取金融经纪列表
     */
    private void getMortgageUserList(Map<String, String> map) {
        Call<ListDTO<BrokeInfo>> call = ApiUtil.getUserCenterService().getMortgageUserList(map);
        call.enqueue(new APICallback<ListDTO<BrokeInfo>>() {
            @Override
            public void onSuccess(ListDTO<BrokeInfo> brokeInfoListDTO) {
                handleSeachData(brokeInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SearchActivity.this, message);
            }

            @Override
            public void onFinish() {
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 获取资讯列表
     */
    private void getNewListData(Map<String, String> map) {
        Call<ListDTO<NewsInfo>> call = ApiUtil.getOtherDataService().getNewsListData(map);
        call.enqueue(new APICallback<ListDTO<NewsInfo>>() {
            @Override
            public void onSuccess(ListDTO<NewsInfo> newsInfoListDTO) {
                handleSeachData(newsInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(SearchActivity.this, message);
            }

            @Override
            public void onFinish() {
                adapter.hideLoadMore();
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 网络请求完成数据处理
     *
     * @param dto
     */
    private void handleSeachData(ListDTO dto) {
        if (dto == null || Collections.isEmpty(dto.getList())) {
            setSeachHint();
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, dto.getList(), page);
        adapter.addAll(dto.getList());
        adapter.notifyDataSetChanged();
        setSeachHint();
        page++;

    }


    /**
     * 空数据提示
     */
    private void setSeachHint() {
        if (adapter.size() > 0) {
            binding.rvResultList.setVisibility(View.VISIBLE);
            binding.tvSearchEmpty.setVisibility(View.GONE);
        } else {
            binding.rvResultList.setVisibility(View.GONE);
            binding.tvSearchEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (Strings.isEmpty(s.toString())) {
            adapter.clear();
            adapter.onFinishLoadMore(true);
            adapter.notifyDataSetChanged();
            setSeachHint();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
