package com.gzliangce.entity;

import android.view.View;

import com.gzliangce.R;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.OnItemClickListener;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.ui.adapter.v7.ViewHolder.LayoutId;

/**
 * Created by Tony on 10/22/15.
 */
public class Contributor implements LayoutId {
    private String login;
    private int contributions;

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public OnItemClickListener onClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
                adapter.notifyItemRemoved(vh.getAdapterPosition());
            }
        };
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_contributor;
    }
}
