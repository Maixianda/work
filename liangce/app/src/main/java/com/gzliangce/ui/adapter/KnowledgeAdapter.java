package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemClassifyBinding;
import com.gzliangce.databinding.ItemProductBinding;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.ui.activity.college.CollegeListActivity;
import com.gzliangce.ui.dialog.CollegeDialog;

import java.util.List;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by aaron on 16/1/20.
 * 知识列表adapter
 */
public class KnowledgeAdapter extends ListAdapter<NewsTypeInfo, ItemClassifyBinding> {
    private Activity activity;
    private Logger logger = LoggerFactory.getLogger(KnowledgeAdapter.class);
    private CollegeDialog collegeDialog;
    private List<NewsTypeInfo> moreNewsTypeInfos;
    private boolean isMain;

    public KnowledgeAdapter(Activity activity, boolean isMain) {
        super(activity);
        this.activity = activity;
        this.isMain = isMain;
    }

    public List<NewsTypeInfo> getMoreNewsTypeInfos() {
        return moreNewsTypeInfos;
    }

    public void setMoreNewsTypeInfos(List<NewsTypeInfo> moreNewsTypeInfos) {
        this.moreNewsTypeInfos = moreNewsTypeInfos;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemClassifyBinding> vh, int position) {
        NewsTypeInfo info = get(position);
        PhotoLoader.display(vh.getBinding().ibtnProductIcon, info.getLogo(), activity.getResources().getDrawable(R.drawable.ic_product_loading));
        vh.getBinding().llyProduct.setOnClickListener(new onClick(position));
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_classify;
    }

    /**
     * 点击事件监听
     */
    private class onClick extends OnSingleClickListener {
        private int position;

        public onClick(int position) {
            this.position = position;
        }

        @Override
        public void onSingleClick(View v) {
            if (position == getItemCount() - 1 && isMain && moreNewsTypeInfos.size() > 0) {
                showMoreDialog();
            } else {
                Intent intent = new Intent(activity, CollegeListActivity.class);
                intent.putExtra(Constants.COLLEGE_COURSE_DATA, get(position));
                activity.startActivity(intent);
            }
        }
    }


    /**
     * 显示更多产品Dialog
     */
    private void showMoreDialog() {
        if (collegeDialog == null) {
            collegeDialog = new CollegeDialog(activity, moreNewsTypeInfos);
        }
        collegeDialog.show();
    }

}
