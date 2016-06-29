package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ActivityAllProductBinding;
import com.gzliangce.dto.CollectionProductDTO;
import com.gzliangce.event.RefreshMyCollecttionProductEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.AllProductAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/1/29.
 * 我收藏的产品
 */
public class MyCollectionProductActivity extends BaseSwipeBackActivityBinding {
    private ActivityAllProductBinding binding;
    private AllProductAdapter collectionProductAdapter;


    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_product);
        setHeader();
    }

    @Override
    public void initView() {
        collectionProductAdapter = new AllProductAdapter(this);
        binding.rvAllProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllProducts.setAdapter(collectionProductAdapter);
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


    /**
     * onClick
     */
    private OnSingleClickListener onClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            onRefresh();
        }
    };

    /**
     * 获取收藏的产品列表
     */
    private void getCollectionList() {
        Call<CollectionProductDTO> call = ApiUtil.getUserCenterService().getCollectionProductList("product");
        call.enqueue(new APICallback<CollectionProductDTO>() {
                         @Override
                         public void onSuccess(CollectionProductDTO collectionProductDTO) {
                             handlerCollectionData(collectionProductDTO);
                         }

                         @Override
                         public void onFailed(String message) {
                             UiUtil.isSetFailedHint(collectionProductAdapter.size(), binding.hint.tvLoad,
                                     binding.rvAllProducts, R.string.http_on_failed, true);
                             ToastHelper.showMessage(MyCollectionProductActivity.this, message + "");
                         }

                         @Override
                         public void onFinish() {
                             LoadingHelper.hideMaterLoading();
                         }
                     }
        );
    }

    /**
     * 处理收藏数据
     */
    private void handlerCollectionData(CollectionProductDTO dto) {
        if (dto == null) {
            return;
        }
        collectionProductAdapter.clear();
        collectionProductAdapter.addAll(dto.getList());
        collectionProductAdapter.notifyDataSetChanged();
        UiUtil.isSetFailedHint(collectionProductAdapter.size(), binding.hint.tvLoad,
                binding.rvAllProducts, R.string.no_collection, false);
    }


    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("收藏的产品");
        binding.setHeader(header);
    }

    /**
     * 刷新数据
     */
    public void onRefresh() {
        LoadingHelper.showMaterLoading(this, "加载中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCollectionList();
            }
        }, 500);
    }


    /**
     * 接收广播删除照片
     */
    @Subscribe
    public void onRefreshMyCollecttionProductEvent(RefreshMyCollecttionProductEvent event) {
        getCollectionList();
    }
}
