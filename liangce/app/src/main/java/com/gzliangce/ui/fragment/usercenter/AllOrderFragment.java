package com.gzliangce.ui.fragment.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzliangce.R;
import com.gzliangce.databinding.FragmentAllOrderBinding;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.util.LiangCeUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.ui.adapter.CommonFPagerAdapter;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/5/17.
 * 复用Fragment
 */
public class AllOrderFragment extends BaseFragmentBinding {
    private Logger logger = LoggerFactory.getLogger(AllOrderFragment.class);
    private CommonFPagerAdapter commonFPagerAdapter;
    private UserType userType;

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private FragmentAllOrderBinding binding;
    private Class mClass;

    public void setmClass(Class mClass) {
        this.mClass = mClass;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Created by leo on 16/5/17.
     * 复用Fragment
     */
    public static AllOrderFragment getInstance(UserType userType, Class mClass) {
        AllOrderFragment orderFragment = new AllOrderFragment();
        orderFragment.setUserType(userType);
        orderFragment.setmClass(mClass);
        return orderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_all_order;
    }

    @Override
    public void initView() {
        commonFPagerAdapter = new CommonFPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        binding.vpOrder.setOffscreenPageLimit(3);
        binding.vpOrder.setAdapter(commonFPagerAdapter);

        binding.pagerTabs.setCustomTabView(R.layout.item_tab, android.R.id.text1);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.red_fa));
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        addFragment();
        binding.pagerTabs.setViewPager(binding.vpOrder);
    }


    private void addFragment() {
        fragments.add(MyOrderFragment.getInstance(1, "未接单", userType, mClass));
        fragments.add(MyOrderFragment.getInstance(2, "已接单", userType, mClass));
        if (userType == UserType.mediator) {
            fragments.add(MyOrderFragment.getInstance(3, "已取消", userType, mClass));
        }
        fragments.add(MyOrderFragment.getInstance(4, "已签约", userType, mClass));
        commonFPagerAdapter.notifyDataSetChanged();
    }


    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (LiangCeUtil.getUserType() != UserType.mortgage) {
            return;
        }
        if (event != null) {
            for (int i = 0; i < fragments.size(); i++) {
                MyOrderFragment fragment = (MyOrderFragment) fragments.get(i);
                fragment.getRecyclerView().smoothScrollToPosition(0);
                fragment.onRefresh();
            }
        }
    }

}
