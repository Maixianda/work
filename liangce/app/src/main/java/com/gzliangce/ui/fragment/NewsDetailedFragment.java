package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentNewsDetailedBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.NewsInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.NewsDetailedAdapter;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/3/9.
 * 资讯列表
 */
public class NewsDetailedFragment extends BaseFragmentBinding implements SwipeRefreshLayout.OnRefreshListener {
    private Logger logger = LoggerFactory.getLogger(NewsDetailedFragment.class);
    private int type;
    private FragmentNewsDetailedBinding binding;
    private NewsDetailedAdapter adapter;
    private int page = 1;

    public static NewsDetailedFragment getInstance(int index, String title) {
        NewsDetailedFragment fragment = new NewsDetailedFragment();
        fragment.setType(index);
        fragment.setFragmentTitle(title);
        return fragment;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsDetailedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_news_detailed;
    }

    @Override
    public void initView() {
        binding.hint.tvLoad.setOnClickListener(onClick);
        adapter = new NewsDetailedAdapter(getActivity());
        adapter.onFinishLoadMore(true);
        binding.rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvNews.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getNewListData();
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

    /**
     * 获取资讯列表
     */
    private void getNewListData() {
        Call<ListDTO<NewsInfo>> call = ApiUtil.getOtherDataService().getNewsListData(type, page, Constants.PAGE_SIZE);
        call.enqueue(new APICallback<ListDTO<NewsInfo>>() {
            @Override
            public void onSuccess(ListDTO<NewsInfo> newsInfoListDTO) {
                handlerListData(newsInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(getActivity(), message);
            }

            @Override
            public void onFinish() {
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.hideLoadMore();
                        binding.srvRefresh.setRefreshing(false);
                        LoadingHelper.hideMaterLoading();
                    }
                });
            }
        });
    }


    /**
     * 处理资讯列表数据
     */
    private void handlerListData(ListDTO<NewsInfo> dto) {
        if (dto == null) {
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, dto.getList(), page);
        adapter.addAll(dto.getList());
        adapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, R.string.no_news, false);
        page++;
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        adapter.onFinishLoadMore(true);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNewListData();
            }
        }, 500);
    }
}
