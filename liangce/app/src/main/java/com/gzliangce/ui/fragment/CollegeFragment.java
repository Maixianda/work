package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentCollegeBinding;
import com.gzliangce.dto.BannerDTO;
import com.gzliangce.dto.TypeListDTO;
import com.gzliangce.entity.BannerImageInfo;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.CollegeAdapter;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Files;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by aaron on 16/3/11.
 * 学院 - fragment
 */
public class CollegeFragment extends BaseFragmentBinding implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(CollegeFragment.class);
    private FragmentCollegeBinding binding;
    private List<BannerImageInfo> bannerImageInfoList = new ArrayList<>();
    private List<NewsTypeInfo> newsTypeInfoList = new ArrayList<>();
    private List<NewsTypeInfo> moreNewsTypeInfos = new ArrayList<>();
    private CollegeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollegeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_college;
    }

    @Override
    public void initView() {
        binding.rvCourseList.setLayoutManager(createrLayoutManager());
        adapter = new CollegeAdapter(getActivity(), bannerImageInfoList, newsTypeInfoList, moreNewsTypeInfos);
        binding.rvCourseList.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(this);
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
     * LayoutManager
     */
    private RecyclerView.LayoutManager createrLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == R.layout.item_college_banner_knowledge) {
                    return 2;
                }
                return 1;
            }
        });
        return layoutManager;
    }

    @Override
    public void onRefresh() {
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.stopBanner();
                getBannerData();
            }
        }, 1000);
    }

    /**
     * 获取广告数据
     */
    private void getBannerData() {
        String port = getPort();
        String index = Constants.COLLEGE;
        Call<BannerDTO> call = ApiUtil.getOtherDataService().getBannerData(port, index);
        call.enqueue(new APICallback<BannerDTO>() {
            @Override
            public void onSuccess(BannerDTO bannerDTO) {
                handlerBannerData(bannerDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
                isSetLoadHint(true, R.string.http_on_failed);
                hideLoading();
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 获取请求广告的用户类型
     */
    private String getPort() {
        String port = UserType.mediator.toString();
        if (AppContext.me().isLogined()) {
            port = LiangCeUtil.getUserType().toString();
        }
        return port;
    }


    /**
     * 处理广告数据
     */
    private void handlerBannerData(final BannerDTO bannerDTO) {
        if (bannerDTO != null) {
            if (!Collections.isEmpty(bannerDTO.getList())) {
                bannerImageInfoList.clear();
                bannerImageInfoList.addAll(bannerDTO.getList());
            }
        }
        getHomeCourse();
    }

    /**
     * 获取课程数据
     */
    private void getHomeCourse() {
        Call<TypeListDTO> call = ApiUtil.getOtherDataService().getUnLoginCollegeHome();
        if (AppContext.me().isLogined()) {
            call = ApiUtil.getOtherDataService().getLoginCollegeHome();
        }
        call.enqueue(new APICallback<TypeListDTO>() {
            @Override
            public void onSuccess(TypeListDTO typeListDTO) {
                handlerCouuseData(typeListDTO);
            }

            @Override
            public void onFailed(String message) {
                isSetLoadHint(true, R.string.http_on_failed);
                ToastHelper.showMessage(getActivity(), message);
            }

            @Override
            public void onFinish() {
                hideLoading();
            }
        });
    }

    /**
     * 隐藏Loading
     */
    private void hideLoading() {
        binding.srvRefresh.setRefreshing(false);
        LoadingHelper.hideMaterLoading();
    }


    /**
     * 是否显示重新加载
     */
    private void isSetLoadHint(boolean isFailed, int strId) {
        int size = adapter.size();
        UiUtil.isSetFailedHint(size, binding.hint.tvLoad, binding.srvRefresh, strId, isFailed);
    }

    /**
     * 处理课程数据
     */
    private void handlerCouuseData(final TypeListDTO typeListDTO) {
        if (typeListDTO == null) {
            return;
        }
        adapter.clear();
        adapter.add(new CourseInfo());
        newsTypeInfoList.clear();
        moreNewsTypeInfos.clear();
        handlerTypeListData(typeListDTO);
        handlerCourseData(typeListDTO);
        adapter.notifyDataSetChanged();
        isSetLoadHint(false, R.string.no_college);
    }


    /**
     * 处理课程分类数据
     */
    private void handlerTypeListData(TypeListDTO typeListDTO) {
        if (typeListDTO == null || Collections.isEmpty(typeListDTO.getTypeList())) {
            return;
        }
        if (typeListDTO.getTypeList().size() < 1) {
            return;
        }
        if (typeListDTO.getTypeList().size() < 8) {
            newsTypeInfoList.addAll(typeListDTO.getTypeList());
            return;
        }
        replaceTypeData(typeListDTO.getTypeList());
        newsTypeInfoList.add(new NewsTypeInfo("更多分类", Files.getAssetFile("ic_more_classify.png")));
    }


    /**
     * 分割课程菜单
     */
    private void replaceTypeData(List<NewsTypeInfo> typeInfos) {
        for (int i = 0; i < typeInfos.size(); i++) {
            if (i < 7) {
                newsTypeInfoList.add(typeInfos.get(i));
            } else {
                moreNewsTypeInfos.add(typeInfos.get(i));
            }
        }
    }


    /**
     * 处理课程推荐数据
     */
    private void handlerCourseData(final TypeListDTO typeListDTO) {
        if (Collections.isEmpty(typeListDTO.getCourseList())) {
            return;
        }
        adapter.addAll(typeListDTO.getCourseList());
    }


    /**
     * 接收退出账号Event,刷新资讯类型
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        if (event != null) {
            binding.rvCourseList.smoothScrollToPosition(0);
            onRefresh();
        }
    }


    /**
     * 接收登录Event,刷新资讯类型
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (event != null) {
            binding.rvCourseList.smoothScrollToPosition(0);
            onRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        LoadingHelper.showMaterLoading(getContext(), "加载中");
        onRefresh();
    }

    @Override
    public void onResume() {
        adapter.startBaner();
        super.onResume();
    }

    @Override
    public void onPause() {
        adapter.stopBanner();
        super.onPause();
    }
}
