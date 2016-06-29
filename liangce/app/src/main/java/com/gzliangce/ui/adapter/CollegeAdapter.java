package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemCollegeBannerKnowledgeBinding;
import com.gzliangce.databinding.ItemCollegeCourseBinding;
import com.gzliangce.entity.BannerImageInfo;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.activity.college.CollegeListActivity;
import com.gzliangce.ui.activity.college.CourseDetailedActivity;
import com.gzliangce.util.UiUtil;
import com.gzliangce.util.XmlUtil;

import java.util.List;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/20.
 * 产品分类列表adapter
 */
public class CollegeAdapter extends ListAdapter<CourseInfo, ViewDataBinding> implements OnItemClickListener {
    private Logger logger = LoggerFactory.getLogger(CollegeAdapter.class);
    private Activity activity;
    private ItemCollegeBannerKnowledgeBinding knowledgeBinding;

    private List<BannerImageInfo> bannerImageInfoList;
    private List<NewsTypeInfo> newsTypeInfoList;
    private List<NewsTypeInfo> moreNewsTypeInfos;

    public CollegeAdapter(Activity activity, List<BannerImageInfo> bannerImageInfoList, List<NewsTypeInfo> newsTypeInfoList, List<NewsTypeInfo> moreNewsTypeInfos) {
        super(activity);
        this.activity = activity;
        this.bannerImageInfoList = bannerImageInfoList;
        this.newsTypeInfoList = newsTypeInfoList;
        this.moreNewsTypeInfos = moreNewsTypeInfos;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        switch (vh.getItemViewType()) {
            case R.layout.item_college_banner_knowledge:
                knowledgeBinding = (ItemCollegeBannerKnowledgeBinding) vh.getBinding();
                initHeader(knowledgeBinding);
                break;
            case R.layout.item_college_course:
                initCourse(vh);
                break;
        }
    }


    /**
     * 初始化Header数据
     */
    private void initHeader(ItemCollegeBannerKnowledgeBinding knowledgeBinding) {
        initBannerData(knowledgeBinding);
        initKnowledge(knowledgeBinding);
        knowledgeBinding.tvMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                NewsTypeInfo info = new NewsTypeInfo();
                info.setTypeId(-1);
                info.setTypeName("全部课程");
                Intent intent = new Intent(getContext(), CollegeListActivity.class);
                intent.putExtra(Constants.COLLEGE_COURSE_DATA, info);
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 初始化广告banner数据
     */
    private void initBannerData(ItemCollegeBannerKnowledgeBinding binding) {
        if (newsTypeInfoList.size() < 1) {
            binding.rvKnowledgeList.setVisibility(View.GONE);
        } else {
            binding.rvKnowledgeList.setVisibility(View.VISIBLE);
        }
        if (bannerImageInfoList.size() < 1) {
            stopBanner();
            binding.cbBanner.setVisibility(View.GONE);
            return;
        } else {
            startBaner();
            binding.cbBanner.setVisibility(View.VISIBLE);
        }
        UiUtil.initBannerView(activity, binding.cbBanner, bannerImageInfoList, this);
    }


    /**
     * 停止广告轮播
     */
    public void stopBanner() {
        if (knowledgeBinding == null || knowledgeBinding.cbBanner == null) {
            return;
        }
        knowledgeBinding.cbBanner.stopTurning();
    }


    /**
     * 启动广告轮播
     */
    public void startBaner() {
        if (knowledgeBinding == null || knowledgeBinding.cbBanner == null) {
            return;
        }
        if (bannerImageInfoList == null || bannerImageInfoList.size() < 2) {
            return;
        }
        knowledgeBinding.cbBanner.startTurning(5000);
    }




    /**
     * 初始化知识item数据
     */
    private void initKnowledge(ItemCollegeBannerKnowledgeBinding binding) {
        KnowledgeAdapter knowledgeAdapter = null;
        if (binding.rvKnowledgeList.getLayoutManager() == null) {
            binding.rvKnowledgeList.setLayoutManager(new GridLayoutManager(activity, 4));
        }
        if (binding.rvKnowledgeList.getAdapter() == null) {
            knowledgeAdapter = new KnowledgeAdapter(activity, true);
            knowledgeAdapter.setMoreNewsTypeInfos(moreNewsTypeInfos);
            knowledgeAdapter.addAll(newsTypeInfoList);
            binding.rvKnowledgeList.setAdapter(knowledgeAdapter);
            return;
        }
        ((KnowledgeAdapter) binding.rvKnowledgeList.getAdapter()).clear();
        ((KnowledgeAdapter) binding.rvKnowledgeList.getAdapter()).addAll(newsTypeInfoList);
        binding.rvKnowledgeList.getAdapter().notifyDataSetChanged();
    }

    /**
     * 初始化课程item数据
     */
    private void initCourse(final BaseViewHolder vh) {
        ItemCollegeCourseBinding courseBinding = (ItemCollegeCourseBinding) vh.getBinding();
        courseBinding.llAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Class className;
                if (XmlUtil.displayVedioIcon(get(vh.getAdapterPosition()).getForm())) {
                    className = WebActivity.class;
                } else {
                    className = CourseDetailedActivity.class;
                }
                Intent intent = new Intent(getContext(), className);
                intent.putExtra(Constants.COLLEGE_COURSE_LIST_ITEM, get(vh.getAdapterPosition()));
                intent.putExtra(Constants.ABOUT_TEXT_URL, get(vh.getAdapterPosition()).getUrl());
                intent.putExtra(Constants.ABOUT_TEXT_TITLE, get(vh.getAdapterPosition()).getCourseName());
                activity.startActivity(intent);
            }
        });
        if (XmlUtil.displayVedioIcon(get(vh.getAdapterPosition()).getForm())) {
            courseBinding.ivVideo.setVisibility(View.VISIBLE);
        } else {
            courseBinding.ivVideo.setVisibility(View.GONE);
        }
    }


    @Override
    protected int getItemLayoutId(int position) {
        if (position == 0) {
            return R.layout.item_college_banner_knowledge;
        }
        return R.layout.item_college_course;
    }

    @Override
    public void onItemClick(int i) {
        BannerImageInfo imageInfo = bannerImageInfoList.get(i);
        if (Strings.isEmpty(imageInfo.getUrl())) {
            return;
        }
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setCourseId(imageInfo.getId());
        courseInfo.setUrl(imageInfo.getUrl());
        courseInfo.setCourseName(imageInfo.getTitle());
        Intent intent = new Intent(getContext(), CourseDetailedActivity.class);
        intent.putExtra(Constants.COLLEGE_COURSE_LIST_ITEM, courseInfo);
        intent.putExtra(Constants.ABOUT_TEXT_URL, imageInfo.getUrl());
        intent.putExtra(Constants.ABOUT_TEXT_TITLE, imageInfo.getTitle());
        activity.startActivity(intent);
    }
}
