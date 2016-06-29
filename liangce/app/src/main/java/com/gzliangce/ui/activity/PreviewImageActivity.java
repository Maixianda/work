package com.gzliangce.ui.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityPreviewImageBinding;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.QualificationImageInfo;
import com.gzliangce.enums.AttestationType;
import com.gzliangce.event.DeletePreviewImageEvent;
import com.gzliangce.ui.adapter.PreviewImageAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IonClickCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AnimUtil;
import com.gzliangce.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnPageChangeAdapter;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/15.
 * 预览图片
 */
public class PreviewImageActivity extends BaseSwipeBackActivityBinding implements MaterialDialog.SingleButtonCallback, IonClickCallback {
    private Logger logger = LoggerFactory.getLogger(PreviewImageActivity.class);
    private ActivityPreviewImageBinding binding;
    private List<QualificationImageInfo> imageInfoList = new ArrayList<>();
    private PreviewImageAdapter previewImageAdapter;
    private int mPosition = 0;
    private boolean isTitleHide = false;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview_image);
        setHeader();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        header.onMenuClickListener();
        header.onBackClickListener();
        binding.vpPreviewImage.addOnPageChangeListener(onPageChangeListener);
    }


    @Override
    public void initData() {
        setMenuVisibility();
        previewImageAdapter = new PreviewImageAdapter(imageInfoList, this, this);
        binding.vpPreviewImage.setAdapter(previewImageAdapter);
        getIntentImageData();
        header.setRightIcon(R.drawable.ic_delete);
    }


    /**
     * 判断删除按钮是否显示，审核中得状态不能编辑删除照片，只能预览
     */
    private void setMenuVisibility() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        AccountInfo info = AppContext.me().getUser();
        if (Strings.isEquals(info.getInfo().getStatus(), AttestationType.check.toString())) {
            binding.icdHeader.rightHeader.setVisibility(View.INVISIBLE);
            binding.icdHeader.rightHeader.setEnabled(false);
        }
    }


    @Override
    public void onMenuClicked() {
        DialogUtil.getMaterialDialog(this,
                getResources().getString(R.string.m_action_delete_image),
                this).show();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    /**
     * 获取传递过来的图片数据
     */
    private void getIntentImageData() {
        List<QualificationImageInfo> list = (List<QualificationImageInfo>) getIntent().getSerializableExtra(Constants.PREVIEW_IMAGE_ACTIVITY_DATA);
        int index = getIntent().getIntExtra(Constants.PREVIEW_IMAGE_ACTIVITY_INDEX_DATA, 0);
        if (list != null) {
            imageInfoList.addAll(list);
            previewImageAdapter.notifyDataSetChanged();
        }
        if (list.size() > 0 && index < list.size()) {
            binding.vpPreviewImage.setCurrentItem(index);
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }


    /**
     * 滑动监听回调
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new OnPageChangeAdapter() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mPosition = position;
            final QualificationImageInfo info = imageInfoList.get(position);
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.icdHeader.midHeader.setText(info.getImageType());
                }
            });
        }
    };


    @Override
    public void onClick(View v) {
        if (isTitleHide) {
            isTitleHide = false;
            AnimUtil.getYAppendAnim(binding.icdHeader.rlyHeader, null).start();
        } else {
            isTitleHide = true;
            AnimUtil.getYSubtractAnim(binding.icdHeader.rlyHeader).start();
        }
    }

    /**
     * Dialog确定
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventHub.post(new DeletePreviewImageEvent(imageInfoList.get(mPosition)));
                imageInfoList.remove(mPosition);
                if (imageInfoList.size() <= 0) {
                    finish();
                } else {
                    PreviewImageAdapter adapter = new PreviewImageAdapter(imageInfoList, PreviewImageActivity.this, PreviewImageActivity.this);
                    binding.vpPreviewImage.setAdapter(adapter);
                }
            }
        });
    }
}
