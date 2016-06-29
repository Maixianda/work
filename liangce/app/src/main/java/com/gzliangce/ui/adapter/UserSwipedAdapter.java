package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemSwipeBinding;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.ui.activity.usercenter.UserSwipeActivity;
import com.gzliangce.ui.dialog.DeleteDialog;

import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;


/**
 * Created by aaron on 10/20/15.
 */
public class UserSwipedAdapter extends ListAdapter<String, ItemSwipeBinding> {
    private Activity activity;
    private int fromType;

    public UserSwipedAdapter(Activity activity, int fromType) {
        super(activity);
        this.activity = activity;
        this.fromType = fromType;
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_swipe;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemSwipeBinding> vh, int position) {

    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        switch (view.getId()) {
            case R.id.lly_remove:
                if (size() == 1) {
                    if (fromType == 1) {
                        ToastHelper.showMessage(activity, "必须要有一门语言");
                    } else {
                        ToastHelper.showMessage(activity, "必须要有一项职能");
                    }
                } else {
                    DeleteDialog deleteDialog = new DeleteDialog(activity, this, vh, fromType);
                    deleteDialog.show();
                }
                break;
        }
    }
}
