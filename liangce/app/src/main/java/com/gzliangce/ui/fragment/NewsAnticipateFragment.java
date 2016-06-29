package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.FragmentAnticipatesNewsBinding;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.CommonFPagerAdapter;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 资讯界面
 * <p>
 * Created by leo on 8/21/14.
 */
public class NewsAnticipateFragment extends BaseFragmentBinding implements View.OnClickListener {
    private static Logger logger = LoggerFactory.getLogger(NewsAnticipateFragment.class);
    private FragmentAnticipatesNewsBinding binding;
    private CommonFPagerAdapter commonFPagerAdapter;
    private List<BaseFragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnticipatesNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_anticipates_news;
    }

    @Override
    public void initView() {
        commonFPagerAdapter = new CommonFPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        binding.vpNews.setAdapter(commonFPagerAdapter);
        binding.pagerTabs.setCustomTabView(R.layout.item_tab, android.R.id.text1);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.red_fa));
        binding.vpNews.setOffscreenPageLimit(3);
    }

    @Override
    public void initListener() {
        binding.hint.tvLoad.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getNewsType();
    }

    @Override
    public void onClick(View v) {
        LoadingHelper.showMaterLoading(getContext(), "加载中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNewsType();
            }
        }, 500);
    }


    /**
     * 添加Fragment数据
     */
    private void addFragment(ListDTO<NewsTypeInfo> dto) {
        if (dto == null) {
            return;
        }
        fragments.clear();
        for (int i = 0; i < dto.getList().size(); i++) {
            fragments.add(NewsDetailedFragment.getInstance(dto.getList().get(i).getTypeId(), dto.getList().get(i).getTypeName()));
        }
        binding.pagerTabs.setBackgroundColor(getResources().getColor(R.color.white));
        binding.vpNews.setAdapter(commonFPagerAdapter);
        binding.pagerTabs.setViewPager(binding.vpNews);
        UiUtil.isSetFailedHint(fragments.size(), binding.hint.tvLoad, binding.mainContent, R.string.no_data, false);
    }


    /**
     * 获取用户资讯类型数据
     */
    private void getNewsType() {
        Call<ListDTO<NewsTypeInfo>> call = ApiUtil.getOtherDataService().getNoLoginNewsType();
        if (AppContext.me().isLogined()) {
            call = ApiUtil.getOtherDataService().getLoginNewsType();
        }
        call.enqueue(new APICallback<ListDTO<NewsTypeInfo>>() {
            @Override
            public void onSuccess(ListDTO<NewsTypeInfo> newsTypeInfoListDTO) {
                addFragment(newsTypeInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
                UiUtil.isSetFailedHint(fragments.size(), binding.hint.tvLoad, binding.mainContent, R.string.http_on_failed, true);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 接收退出账号Event,刷新资讯类型
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        if (event != null) {
            getNewsType();
        }
    }


    /**
     * 接收登录Event,刷新资讯类型
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (event != null) {
            getNewsType();
        }
    }

}