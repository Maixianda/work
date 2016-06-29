package com.gzliangce.entity;

import com.gzliangce.R;
import io.ganguo.library.ui.adapter.v7.ViewHolder.LayoutId;

/**
 * Created by Tony on 11/10/15.
 */
public class Issue implements LayoutId{

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_issue;
    }
}
