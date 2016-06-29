package com.gzliangce.ui.activity.product;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityAllProductBinding;
import com.gzliangce.dto.AllProductDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.AllProductAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.UiUtil;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/1/21.
 * 全部产品界面（与选择产品复用）
 */
public class AllProductActivity extends BaseSwipeBackActivityBinding {
    private ActivityAllProductBinding binding;
    private AllProductAdapter allProductAdapter;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_product);
        setHeader();
    }

    @Override
    public void initView() {
        allProductAdapter = new AllProductAdapter(this);
        binding.rvAllProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllProducts.setAdapter(allProductAdapter);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.hint.tvLoad.setOnClickListener(onClick);
    }

    @Override
    public void initData() {
        onRefresh();
    }

    private void onRefresh() {
        LoadingHelper.showMaterLoading(this, "加载中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllProducts();
            }
        }, 500);
    }


    /**
     * onClick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            onRefresh();
        }
    };


    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("全部产品");
        binding.setHeader(header);
    }

    /**
     * 获取全部产品数据
     */
    private void getAllProducts() {
        ApiDataUtil.getAllProducts(new APICallback<AllProductDTO>() {
            @Override
            public void onSuccess(AllProductDTO allProductDTO) {
                handlerAllProduct(allProductDTO);
            }

            @Override
            public void onFailed(String message) {
                UiUtil.isSetFailedHint(allProductAdapter.size(), binding.hint.tvLoad, binding.rvAllProducts, R.string.http_on_failed, true);
                ToastHelper.showMessage(AllProductActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 处理全部产品数据
     */
    private void handlerAllProduct(AllProductDTO allProductDTO) {
        if (allProductDTO == null) {
            return;
        }
        allProductAdapter.clear();
        allProductAdapter.addAll(allProductDTO.getList());
        allProductAdapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(allProductAdapter.size(), binding.hint.tvLoad, binding.rvAllProducts, R.string.no_product, false);
    }

}
