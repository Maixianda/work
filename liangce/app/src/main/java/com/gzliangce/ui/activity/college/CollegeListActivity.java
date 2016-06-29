package com.gzliangce.ui.activity.college;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCollegeListBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.CollegeListAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by aaron on 16/3/15.
 * 学院内容列表
 */
public class CollegeListActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private Logger logger = LoggerFactory.getLogger(CollegeListActivity.class);
    private ActivityCollegeListBinding binding;
    private CollegeListAdapter adapter;

    private NewsTypeInfo newsTypeInfo;
    private int page = 1;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_college_list);
    }

    @Override
    public void initView() {
        adapter = new CollegeListAdapter(this);
        adapter.onFinishLoadMore(true);
        binding.rcvCollegeList.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvCollegeList.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.hint.tvLoad.setOnClickListener(onClick);
        binding.srvRefresh.setOnRefreshListener(this);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                getCourseInfoList();
            }
        });
    }

    @Override
    public void initData() {
        newsTypeInfo = (NewsTypeInfo) getIntent().getSerializableExtra(Constants.COLLEGE_COURSE_DATA);
        setHeader();
        binding.srvRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    /**
     * 设置header
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
        header.setMidTitle(newsTypeInfo.getTypeName());
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    /**
     * 获取课程列表数据
     */
    private void getCourseInfoList() {
        Map<String, String> map = new HashMap<>();
        if (newsTypeInfo.getTypeId() != -1) {
            map.put(Constants.TYPE, String.valueOf(newsTypeInfo.getTypeId()));
        }
        map.put(Constants.PAGE, String.valueOf(page));
        Call<ListDTO<CourseInfo>> call = ApiUtil.getOtherDataService().getMyCourseListNoLoginData(map);
        if (AppContext.me().isLogined()) {
            call = ApiUtil.getOtherDataService().getMyCourseListData(map);
        }
        call.enqueue(new APICallback<ListDTO<CourseInfo>>() {
            @Override
            public void onSuccess(ListDTO<CourseInfo> courseInfoListDTO) {
                handlerData(courseInfoListDTO.getList());
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                        binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(CollegeListActivity.this, message + "");
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
     * 处理课程数据
     */
    private void handlerData(List<CourseInfo> courseInfoList) {
        if (courseInfoList == null) {
            return;
        }
        if (page == 1) {
            adapter.clear();
        }
        AdapterUtil.setAdapterIsLoadMore(adapter, courseInfoList, page);
        adapter.addAll(courseInfoList);
        adapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad,
                binding.srvRefresh, R.string.no_college, false);
        page++;
    }


    @Override
    public void onRefresh() {
        adapter.onFinishLoadMore(true);
        page = 1;
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCourseInfoList();
            }
        }, 500);
    }

}
