package com.gzliangce.ui.dialog;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.DialogCollegeBinding;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.ui.adapter.KnowledgeAdapter;

import java.util.List;

import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/3/16.
 * 更多课程Dialog
 */
public class CollegeDialog extends BaseDialog implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(CollegeDialog.class);
    private Activity activity;
    private DialogCollegeBinding binding;
    private KnowledgeAdapter adapter;
    private List<NewsTypeInfo> newsTypeInfoList;

    public CollegeDialog(Activity activity, List<NewsTypeInfo> newsTypeInfoList) {
        super(activity);
        this.activity = activity;
        this.newsTypeInfoList = newsTypeInfoList;
    }

    @Override
    public void beforeInitView() {
        binding = DialogCollegeBinding.inflate(activity.getLayoutInflater(), null, false);
        setCanceledOnTouchOutside(false);
        setContentView(binding.getRoot());
        setWidthSize(Systems.getScreenWidth(activity));
        setLocation(Gravity.CENTER);
        setAnim(R.style.customDialog);
    }

    @Override
    public void initView() {
        binding.rcvDialogCollege.setLayoutManager(new GridLayoutManager(activity, 4));
        adapter = new KnowledgeAdapter(activity, false);
        binding.rcvDialogCollege.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        binding.ibtnClose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter.clear();
        if (!Collections.isEmpty(newsTypeInfoList)){
            adapter.addAll(newsTypeInfoList);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
