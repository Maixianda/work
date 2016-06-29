package com.gzliangce.mvp.view;

import java.util.List;

/**
 * Created by leo on 16/3/25.
 * 列表相关的 MvpView
 */
public interface MvpView<C> {
    int getPage();

    void setPage();

    List<C> getListData();
}
