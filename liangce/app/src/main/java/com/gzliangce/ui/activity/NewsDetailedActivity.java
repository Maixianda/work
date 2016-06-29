package com.gzliangce.ui.activity;

import android.databinding.DataBindingUtil;
import android.text.Html;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityNewDetailedBinding;
import com.gzliangce.dto.NewsDetailDTO;
import com.gzliangce.entity.NewsInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;

import io.ganguo.library.common.ToastHelper;
import retrofit.Call;

/**
 * Created by leo on 16/3/18.
 * 新闻详细页面
 */
public class NewsDetailedActivity extends BaseSwipeBackActivityBinding {
    private ActivityNewDetailedBinding binding;
    private NewsInfo newsInfo;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_detailed);
        setHeader();
    }

    @Override
    public void initView() {
        binding.tvContent.setVisibility(View.GONE);
        binding.tvTitle.setVisibility(View.GONE);
        binding.tvTime.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        newsInfo = getIntent().getParcelableExtra(Constants.NEWS_DETAILED_DATA);
        if (newsInfo != null) {
            getNewsDetailed(newsInfo.getId());
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("文章详情");
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 获取资讯详情
     */
    private void getNewsDetailed(long id) {
        Call<NewsDetailDTO> call = ApiUtil.getOtherDataService().getNewsDetail(id);
        call.enqueue(new APICallback<NewsDetailDTO>() {
            @Override
            public void onSuccess(NewsDetailDTO newsDetailDTO) {
                handlerNewsData(newsDetailDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(NewsDetailedActivity.this, message);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    /**
     * 处理资讯详情
     */
    private void handlerNewsData(NewsDetailDTO dto) {
        if (dto == null) {
            return;
        }
        binding.tvTitle.setText(dto.getDetail().getTitle());
        binding.tvTime.setText(dto.getDetail().getCreateTime());
        binding.tvContent.setText(Html.fromHtml(dto.getDetail().getContent()));
        binding.tvContent.setVisibility(View.VISIBLE);
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.tvTime.setVisibility(View.VISIBLE);
    }
}
