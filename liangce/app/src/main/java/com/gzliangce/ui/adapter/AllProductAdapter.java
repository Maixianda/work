package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemAllProductsBinding;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.enums.ProductType;
import com.gzliangce.ui.widget.FullyGridLayoutManager;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/1/21.
 * 全部产品Adapter
 */
public class AllProductAdapter extends ListAdapter<ProductsInfo, ItemAllProductsBinding> {
    private Activity activity;

    public AllProductAdapter(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemAllProductsBinding> vh, final int position) {
        ProductsInfo info = get(position);
        PhotoLoader.display(vh.getBinding().ibtnProductType, info.getIcon(), getContext().getResources().getDrawable(R.drawable.ic_product_loading));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        vh.getBinding().rvProduct.setLayoutManager(layoutManager);
        vh.getBinding().rvProduct.setEnabled(false);
        vh.getBinding().rvProduct.setSaveEnabled(false);
        vh.getBinding().rvProduct.setScrollContainer(false);
        if (info.getChildren() != null && info.getChildren().size() > 0) {
            vh.getBinding().rvProduct.setVisibility(View.VISIBLE);
            ProductsAdapter adapter = new ProductsAdapter(activity);
            vh.getBinding().rvProduct.setAdapter(adapter);
            adapter.addAll(get(position).getChildren());
            vh.getBinding().rvProduct.setAdapter(adapter);
        } else {
            vh.getBinding().rvProduct.setVisibility(View.GONE);
        }
        vh.getBinding().llyProductType.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                actionActivity(position);
            }
        });
    }

    /**
     * 点击事件
     */
    private void actionActivity(int position) {
        ProductsInfo info = get(position);
        info.setProductType(ProductType.BigType.toString());
        IntentUtil.actionProductsActivity(activity, info);
    }


    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_all_products;
    }
}
