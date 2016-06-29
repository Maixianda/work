package com.gzliangce.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.ui.custom.NetworkImageHolderView;

import java.util.List;

import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.util.Tasks;

/**
 * Created by leo on 16/6/6.
 * UI相关工具类
 */
public class UiUtil {

    /**
     * set 下拉刷新控件颜色
     */
    public static void bindSwipeRefreshView(Context context, SwipeRefreshLayout refreshLayout) {
        if (context == null || refreshLayout == null) {
            return;
        }
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }


    /**
     * 是否显示提示加载失败提示
     *
     * @param hintView
     * @param isFailed
     * @param count
     */
    public static void isSetFailedHint(int count, TextView hintView, View listView, int strId, boolean isFailed) {
        if (count <= 0) {
            hintView.setVisibility(View.VISIBLE);
            setHintText(hintView, listView, strId, isFailed);
        } else {
            hintView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置提示文字
     *
     * @param hintView
     * @param isFailed
     */
    private static void setHintText(TextView hintView, View listView, int strId, boolean isFailed) {
        if (isFailed) {
            hintView.setEnabled(true);
            listView.setVisibility(View.GONE);
            hintView.setTextColor(hintView.getContext().getResources().getColor(R.color.red_fa));
        } else {
            hintView.setEnabled(false);
            listView.setVisibility(View.VISIBLE);
            hintView.setTextColor(hintView.getContext().getResources().getColor(R.color.gray_95));
        }
        hintView.setText(AppContext.me().getResources().getText(strId));
    }


    /**
     * UI线程刷新Adapter
     *
     * @param adapter
     */
    public static void runOnUiNotifyAdapter(final BaseAdapter adapter) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 广告banner 初始化代码
     *
     * @param activity
     * @param bannerView
     * @param data
     * @param listener
     */
    public static void initBannerView(Activity activity, ConvenientBanner bannerView, List data, OnItemClickListener listener) {
        bannerView.setPages(CreateCBViewHolderCreator(activity), data);
        bannerView.setPageIndicator(new int[]{R.drawable.ic_white_dot, R.drawable.ic_red_dot}, activity.getResources().getDimensionPixelSize(R.dimen.dp_7));
        bannerView.setOnItemClickListener(listener);
        if (data.size() >= 2) {
            bannerView.setPointViewVisible(true);
            bannerView.startTurning(5000);
            bannerView.setCanLoop(true);
        } else {
            bannerView.setPointViewVisible(false);
            bannerView.stopTurning();
            bannerView.setCanLoop(false);
        }
    }


    /**
     * banner ViewHolder
     *
     * @param activity
     */
    private static CBViewHolderCreator CreateCBViewHolderCreator(final Activity activity) {
        return new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView(activity);
            }
        };
    }

}
