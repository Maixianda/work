package com.gzliangce.util;

import android.view.View;

import com.gzliangce.bean.Constants;

import java.util.List;

import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.util.Collections;

/**
 * Created by leo on 16/5/17.
 */
public class AdapterUtil {

    public static void setAdapterIsLoadMore(LoadMoreAdapter adapter, List list, int page) {
        if (Collections.isEmpty(list)) {
            adapter.onFinishLoadMore(true);
            return;
        }
        if (page == 1 && list.size() < Constants.PAGE_SIZE) {
            adapter.onFinishLoadMore(true);
        } else {
            adapter.onFinishLoadMore(false);
        }
    }


    /**
     * 是否显示空数据提示
     */
    public static void setHint(LoadMoreAdapter adapter, View view) {
        if (adapter.size() <= 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
