package com.gzliangce.ui.activity.college;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityMyCourseBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.event.CourseEnrollEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.MyCourseAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.LoadMoreListener;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/3/14.
 * 我的课程界面
 */
public class MyCourseActivity extends BaseSwipeBackActivityBinding implements SwipeRefreshLayout.OnRefreshListener {
    private ActivityMyCourseBinding binding;
    private MyCourseAdapter myCourseAdapter;
    private int page = 1;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_course);
        setHeader();
    }

    @Override
    public void initView() {
        myCourseAdapter = new MyCourseAdapter(this);
        binding.rclMyCourse.setLayoutManager(new LinearLayoutManager(this));
        myCourseAdapter.onFinishLoadMore(true);
        binding.rclMyCourse.setAdapter(myCourseAdapter);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.hint.tvLoad.setOnClickListener(onClick);
        binding.srvRefresh.setOnRefreshListener(this);
        myCourseAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadData();
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
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("我的课程");
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        myCourseAdapter.onFinishLoadMore(true);
        page = 1;
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 刷新课程列表
     */
    @Subscribe
    public void onCourseEnrollEvent(CourseEnrollEvent event) {
        if (event.getCourseInfo() == null) {
            return;
        }
        if (event.isEnroll()) {
            myCourseAdapter.add(0, event.getCourseInfo());
            isDataNotify(false);
        } else {
            deleteCourse(event.getCourseInfo());
        }
    }


    /**
     * 加载列表数据
     */
    public void showCourseListData(ListDTO<CourseInfo> dto) {
        if (dto == null || dto.getList() == null) {
            return;
        }
        if (page == 1) {
            myCourseAdapter.clear();
        }
        myCourseAdapter.addAll(dto.getList());
        isDataNotify(false);
        page++;
    }


    /**
     * 隐藏加载Loading
     */
    public void hideLoading() {
        binding.srvRefresh.setRefreshing(false);
        myCourseAdapter.hideLoadMore();
        LoadingHelper.hideMaterLoading();
    }


    /**
     * 加载列表数据
     */
    public void loadData() {
        Call<ListDTO<CourseInfo>> call = ApiUtil.getOtherDataService().getMyCourseListData(page, Constants.PAGE_SIZE);
        call.enqueue(new APICallback<ListDTO<CourseInfo>>() {
            @Override
            public void onSuccess(ListDTO<CourseInfo> courseInfoListDTO) {
                if (courseInfoListDTO != null) {
                    showCourseListData(courseInfoListDTO);
                }
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(myCourseAdapter.size(), binding.hint.tvLoad,
                        binding.srvRefresh, R.string.http_on_failed, true);
                ToastHelper.showMessage(MyCourseActivity.this, message);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }


    /**
     * 删除对应的课程数据
     */
    public void deleteCourse(final CourseInfo courseInfo) {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                removeData(courseInfo);
            }
        });
    }

    /**
     * 遍历删除
     */
    private void removeData(CourseInfo courseInfo) {
        List<CourseInfo> list = myCourseAdapter.getData();
        for (CourseInfo info : list) {
            if (courseInfo.getCourseId() == info.getCourseId()) {
                myCourseAdapter.remove(info);
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isDataNotify(false);
                    }
                });
                break;
            }
        }
    }

    /**
     * 界面数据刷新
     */
    private void isDataNotify(boolean isFaild) {
        AdapterUtil.setAdapterIsLoadMore(myCourseAdapter, myCourseAdapter.getData(), page);
        myCourseAdapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(myCourseAdapter.size(), binding.hint.tvLoad,
                binding.srvRefresh, R.string.no_college, isFaild);
    }

}
