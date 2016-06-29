package com.gzliangce.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ItemSelectBrokerBinding;
import com.gzliangce.entity.BrokeInfo;
import com.gzliangce.entity.PlaceAnOrder;
import com.gzliangce.enums.ButtonStatus;
import com.gzliangce.enums.UserType;
import com.gzliangce.ui.activity.order.SelectBrokerMapActivity;
import com.gzliangce.ui.callback.IRemindDialogCallback;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.IntentUtil;
import com.gzliangce.util.LiangCeUtil;

import java.util.List;

import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;


/**
 * Created by zzwdream on 1/25/16.
 * 地图经纪人列表
 */
public class SelectBroberAdapter extends PagerAdapter {
    private Logger logger = LoggerFactory.getLogger(SelectBroberAdapter.class);
    private SelectBrokerMapActivity activity;
    private LayoutInflater layoutInflater;
    private List<BrokeInfo> brokeInfoList;
    private PlaceAnOrder placeAnOrder;

    public SelectBroberAdapter(SelectBrokerMapActivity activity, List<BrokeInfo> brokeInfoList, PlaceAnOrder placeAnOrder) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        this.brokeInfoList = brokeInfoList;
        this.placeAnOrder = placeAnOrder;
    }

    @Override
    public int getCount() {
        return brokeInfoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_select_broker, null);
        ItemSelectBrokerBinding binding = ItemSelectBrokerBinding.bind(view);
        binding.setData(brokeInfoList.get(position));
        binding.llyItemBroker.setOnClickListener(clickListen);
        binding.tvJoinOrder.setOnClickListener(clickListen);
        if (Strings.isEquals(brokeInfoList.get(position).getAccountId() + "", AppContext.me().getUserId())) {
            binding.tvJoinOrder.setVisibility(View.GONE);
        }
        container.addView(binding.getRoot());
        return view;
    }

    View.OnClickListener clickListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BrokeInfo info = brokeInfoList.get(activity.binding.vpBroker.getCurrentItem());
            switch (v.getId()) {
                case R.id.lly_item_broker:
                    IntentUtil.actionPersonalDetailsActivity(info, placeAnOrder, ButtonStatus.OrderDoBtn);
                    break;
                case R.id.tv_join_order:
                    joinOrder(info);
                    break;
            }
        }
    };

    /**
     * 转单
     */
    private void joinOrder(BrokeInfo info) {
        int accoutId = info.getAccountId();
        boolean isPlaceOrder;
        if (LiangCeUtil.judgeUserType(UserType.mortgage)) {
            isPlaceOrder = false;
        } else {
            isPlaceOrder = true;
        }
        showDoPlaceOrderDialog(isPlaceOrder, accoutId);
    }


    /**
     * 或者转单提示框
     *
     * @param accoutId
     */
    private void showDoPlaceOrderDialog(final boolean isPlaceOrder, final int accoutId) {
        String string = activity.getResources().getString(R.string.Order_place_text);
        if (!isPlaceOrder) {
            string = activity.getResources().getString(R.string.Order_change_text);
        }
        DialogUtil.getMaterialDialog(activity, string, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                placeAnOrder.setMortgageId(accoutId + "");
                if (isPlaceOrder) {
                    ApiDataUtil.doPlaceAnOrder(placeAnOrder, activity);
                } else {
                    ApiDataUtil.changeOrder(placeAnOrder, activity);
                }
            }
        }).show();
    }
}
