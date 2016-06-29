package com.gzliangce.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemProductListBinding;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.ui.callback.ItemCallBack;

import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/5/23.
 * PopWindow 列表菜单相关Adapter
 */
public class PopupMenuListAdapter extends ListAdapter<ProductsInfo, ItemProductListBinding> {
    private ItemCallBack callBack;
    private int layoutId = -1;

    public PopupMenuListAdapter(Context context, ItemCallBack callBack, int layoutId) {
        super(context);
        this.callBack = callBack;
        this.layoutId = layoutId;
    }


    public void setCallBack(ItemCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemProductListBinding> vh, int position) {

    }

    @Override
    protected int getItemLayoutId(int position) {
        if (get(position).isBigType()) {
            return R.layout.item_big_type_list;
        }
        return R.layout.item_product_list;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        if (callBack != null) {
            callBack.onItemCallBack(get(vh.getAdapterPosition()), vh.getAdapterPosition());
        }
    }

}
