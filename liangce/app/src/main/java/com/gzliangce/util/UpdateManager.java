package com.gzliangce.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.dto.VersionDTO;
import com.gzliangce.entity.AppBeanInfo;
import com.gzliangce.entity.VersionInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.dialog.UpDateHintDialog;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.UpdateManagerListener;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.cache.CacheManager;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

/**
 * 应用更新管理类
 * <p/>
 */
public class UpdateManager {
    private static final Logger logger = LoggerFactory.getLogger(UpdateManager.class);
    private static UpdateManager updateManager;

    public static UpdateManager getInstance() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        return updateManager;
    }

    /**
     * 获取更新接口回调
     *
     * @param activity
     */
    public UpdateManagerListener getUpdateListener(final Activity activity) {
        UpdateManagerListener listener = null;
        if (activity == null) {
            return listener;
        }
        listener = createListener(activity);
        return listener;
    }


    /**
     * 创建更新回调接口
     *
     * @param activity
     */
    private UpdateManagerListener createListener(final Activity activity) {
        return new UpdateManagerListener() {
            @Override
            public void onUpdateAvailable(final String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                logger.e("AppBean:" + appBean.toString());
                if (appBean == null) {
                    return;
                }
                upDateHintDialog(activity, appBean).show();
            }

            @Override
            public void onNoUpdateAvailable() {
            }
        };
    }


    /**
     * 更新提示弹窗
     *
     * @param activity
     * @param appBean
     */
    public MaterialDialog upDateHintDialog(final Activity activity, final AppBean appBean) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title("更新")
                .cancelable(false)
                .content(appBean.getReleaseNote())
                .positiveColorRes(R.color.colorPrimaryDark)
                .negativeColorRes(R.color.gray_6e)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AppBeanInfo info = AppBeanInfo.copyAppBean(appBean);
                        logger.e("AppBeanInfo:" + info.toString());
                        CacheManager.disk(activity).put(Constants.APP_UPDATE_NKOTE, info);
                        startDownloadTask(
                                activity,
                                appBean.getDownloadURL());
                    }
                })
                .build();
        return dialog;
    }


    /**
     * 是否显示更新日志
     */
    public void isShowUpdateHint(Context context) {
        AppBeanInfo bean = (AppBeanInfo) CacheManager.disk(context).get(Constants.APP_UPDATE_NKOTE);
        if (bean == null) {
            getVersion(context);
            return;
        }
        logger.e("bean:" + bean.toString());
        logger.e("bean:Systems:" + Systems.getVersionCode(context));
        logger.e("bean:getVersionCode" + bean.getVersionCode());
        if (Systems.getVersionCode(context) < Integer.parseInt(bean.getVersionCode())) {
            return;
        }
        if (!Strings.isEquals(bean.getVersionName(), Systems.getVersionName(context))) {
            return;
        }
        showUpDateDialog(context, bean.getVersionName(), bean.getReleaseNote());
    }

    /**
     * 更新日志弹窗显示
     */
    public void showUpDateDialog(Context context, String version, String note) {
        UpDateHintDialog dialog = new UpDateHintDialog(context, version, note);
        dialog.show();
    }


    /**
     * 获取服务端版本信息
     *
     * @param context
     */
    private void getVersion(final Context context) {
        Call<VersionDTO> call = ApiUtil.getOtherDataService().getVersion();
        call.enqueue(new APICallback<VersionDTO>() {
            @Override
            public void onSuccess(VersionDTO versionDTO) {
                logger.e("versionDTO:" + versionDTO.toString());
                handlerVersionData(context, versionDTO);
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 判断处理服务器返回的版本信息
     *
     * @param context
     * @param dto
     */
    private void handlerVersionData(Context context, VersionDTO dto) {
        if (dto == null || dto.getVersion() == null) {
            return;
        }
        if (Strings.isEmpty(dto.getVersion().getVersion()) || Strings.isEmpty(dto.getVersion().getNote())) {
            return;
        }
        //服务器版本与安装版本不一致，直接忽略
        if (!Strings.isEquals(dto.getVersion().getVersion(), Systems.getVersionName(context))) {
            return;
        }
        //不为null，则说明之前已经提示过了
        VersionInfo version = (VersionInfo) CacheManager.disk(context).get(dto.getVersion().getVersion());
        if (version != null) {
            return;
        }
        CacheManager.disk(context).put(dto.getVersion().getVersion(), dto.getVersion());
        showUpDateDialog(context, dto.getVersion().getVersion(), dto.getVersion().getNote());
    }

}
