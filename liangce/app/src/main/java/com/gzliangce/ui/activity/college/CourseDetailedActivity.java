package com.gzliangce.ui.activity.college;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.AppContext;
import com.gzliangce.AppEnv;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCourseDetailedBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.IsEnrollDTO;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.event.CourseEnrollEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.attestation.LoginActivity;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.ui.widget.WebChromeClient;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;
import com.leo.gesturelibray.util.StringUtils;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/3/15.
 * 课程详情界面 - 无视频
 */
public class CourseDetailedActivity extends BaseSwipeBackActivityBinding implements WebChromeClient.OnProgressChangedListener, MaterialDialog.SingleButtonCallback {
    private ActivityCourseDetailedBinding binding;
    private CourseInfo courseInfo;
    private boolean isEnroll;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_detailed);
    }

    @Override
    public void initView() {
        LiangCeUtil.initWebView(binding.wbSignUp);
    }

    @Override
    public void initListener() {
        binding.wbSignUp.setWebChromeClient(new WebChromeClient(this));
        binding.wbSignUp.setWebViewClient(webViewClient);
        binding.tvSignUp.setOnClickListener(onSingleClickListener);
    }

    @Override
    public void initData() {
        binding.prProgress.setProgressDrawable(getResources().getDrawable(R.drawable.selector_progress_bg));
        courseInfo = (CourseInfo) getIntent().getSerializableExtra(Constants.COLLEGE_COURSE_LIST_ITEM);
        setHeader();
        if (courseInfo != null) {
            checkCourseEnroll();
            binding.wbSignUp.loadUrl(courseInfo.getUrl());
        }
    }


    /**
     * 避免打开网页时调用系统浏览器
     */
    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 处理自定义scheme
            if (!StringUtils.isEquals(url, courseInfo.getUrl())) {
                actionTeacherIntroduceActivity(url);
                return true;
            }
            if (!url.startsWith("http")) {
                openSchemeUrl(url);
                return true;
            }
            return false;
        }
    };

    /**
     * 跳转到导师详情界面
     */
    private void actionTeacherIntroduceActivity(String url) {
        String id;
        if (url.startsWith(AppEnv.BASE_URL)) {
            return;
        }
        id = url.substring(url.length() - 1);
        Intent intent = new Intent(this, TeacherIntroduceActivity.class);
        intent.putExtra(Constants.TEACHER_ID, id);
        startActivity(intent);
    }


    /**
     * 打开不是http开头的链接
     */
    private void openSchemeUrl(String url) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        if (courseInfo != null) {
            header.setMidTitle(courseInfo.getCourseName());
        }
        header.setRightBackground(0);
        header.onBackClickListener();
        binding.setHeader(header);
    }


    /**
     * 网页监听加载进度
     */
    @Override
    public void onProgress(int newProgress) {
        if (binding.prProgress.getVisibility() == View.GONE) {
            binding.prProgress.setVisibility(View.VISIBLE);
        }
        binding.prProgress.setProgress(newProgress);
    }

    /**
     * 网页加载完成
     */
    @Override
    public void onProgressFinish(String title) {
        binding.prProgress.setProgress(100);
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.prProgress.setVisibility(View.GONE);
            }
        }, 500);
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            isEnroll();
        }
    };

    /**
     * 判断是提交 取消报名 or 报名
     */
    private void isEnroll() {
        if (courseInfo == null) {
            return;
        }
        if (isEnroll) {
            showUnEnrollDialog("确认取消报名？");
        } else {
            showUnEnrollDialog("确认报名本课程？");
        }
    }


    /**
     * 取消报名 or 报名 确认提示框
     */
    private void showUnEnrollDialog(String message) {
        if (!AppContext.me().isLogined()) {//判断是否已经登录
            IntentUtil.actionActivity(this, LoginActivity.class);
            return;
        }
        DialogUtil.getMaterialDialog(this, message, this).show();
    }

    /**
     * 查询是否已经报名课程
     */
    private void checkCourseEnroll() {
        if (!AppContext.me().isLogined()) {//判断是否已经登录
            return;
        }
        LoadingHelper.showMaterLoading(this, "加载中");
        Call<IsEnrollDTO> call = ApiUtil.getOtherDataService().getCourseCheckEnroll(courseInfo.getCourseId());
        call.enqueue(new APICallback<IsEnrollDTO>() {
            @Override
            public void onSuccess(IsEnrollDTO dto) {
                logger.e("IsEnrollDTO" + dto.toString());
                handlerCheckData(dto);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getBaseContext(), message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 处理查询结果
     */
    private void handlerCheckData(IsEnrollDTO dto) {
        if (dto == null) {
            return;
        }
        isEnroll = dto.isEnroll();
        if (isEnroll) {
            binding.tvSignUp.setText("取消报名");
        } else {
            binding.tvSignUp.setText("立即报名");
        }

    }

    /**
     * 提交报名请求
     */
    private void postCourseEnroll() {
        Call<BaseDTO> call = ApiUtil.getOtherDataService().postCourseEnroll(courseInfo.getCourseId());
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                isEnroll = true;
                binding.tvSignUp.setText("取消报名");
                EventHub.post(new CourseEnrollEvent(courseInfo, true));
                ToastHelper.showMessage(getBaseContext(), "报名成功");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getBaseContext(), message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 取消报名请求
     */
    private void postCourseUnEnroll() {
        Call<BaseDTO> call = ApiUtil.getOtherDataService().postCourseUnEnroll(courseInfo.getCourseId());
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                isEnroll = false;
                binding.tvSignUp.setText("立即报名");
                EventHub.post(new CourseEnrollEvent(courseInfo, false));
                ToastHelper.showMessage(getBaseContext(), "取消成功");
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getBaseContext(), message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    /**
     * Dialog 确定
     */
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        LoadingHelper.showMaterLoading(this, "处理中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isEnroll) {
                    postCourseUnEnroll();
                } else {
                    postCourseEnroll();
                }
            }
        }, 500);
    }

}
