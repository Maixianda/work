package com.gzliangce.ui.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gzliangce.R;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.ui.fragment.BaseFragment;

/**
 * Created by leo on 16/6/6.
 * Fragment 基类
 */
public abstract class BaseFragmentBinding extends BaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        UiUtil.bindSwipeRefreshView(getContext(), (SwipeRefreshLayout) getView().findViewById(R.id.srv_refresh));
        super.onActivityCreated(savedInstanceState);
    }
}
