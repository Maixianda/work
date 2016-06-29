package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemCoversationListBinding;
import com.gzliangce.db.DBManager;
import com.gzliangce.db.DatabaseHelper;
import com.gzliangce.entity.ChatConversation;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.activity.chat.ConversationActivity;
import com.gzliangce.ui.activity.usercenter.MessageCenterActivity;
import com.gzliangce.ui.fragment.MessageFragment;

import io.ganguo.library.Config;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/1/11.
 * 金融经纪列表
 */
public class AVIMCoversationListAdapter extends LoadMoreAdapter<ChatConversation, ItemCoversationListBinding> {
    private Activity activity;
    private boolean isEditMode = false;
    private boolean hasCheck = false;

    public AVIMCoversationListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemCoversationListBinding> vh, int position) {
        ItemCoversationListBinding binding = vh.getBinding();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.llyCheck.getLayoutParams();
        if (isEditMode) {
            layoutParams.setMargins(0, 0, getDimenPixelSize(R.dimen.dp_n10), 0);
            binding.llyCheck.setVisibility(View.VISIBLE);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
            binding.llyCheck.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_coversation_list;
    }

    @Override
    protected void onItemClick(BaseAdapter adapter, BaseViewHolder vh, View view) {
        switch (view.getId()) {
            case R.id.rly_item_coversation:
                doItemOnclick(vh);
                break;
        }
    }

    public void toggleEditMode() {
        isEditMode = !isEditMode;
        if (isEditMode) {
            setUnSelect();
        }
        notifyDataSetChanged();
    }

    public void completeMode() {
        setUnSelect();
        isEditMode = false;
        notifyDataSetChanged();
    }

    private int getDimenPixelSize(int dimenValues) {
        return activity.getResources().getDimensionPixelSize(dimenValues);
    }

    public boolean getEditStatus() {
        return isEditMode;
    }

    private void doItemOnclick(BaseViewHolder vh) {
        if (isEditMode) {
            if (get(vh.getAdapterPosition()).isChecked()) {
                setUnSelect();
            } else {
                setUnSelect();
                get(vh.getAdapterPosition()).setChecked(true);
            }
            HasCheckBoxSelect();
        } else {
            get(vh.getAdapterPosition()).setUnReadCount(0);
            hideRedPoint();
            //数据库中删除该会话的数据
            DBManager.getInstance().deleteOldConversation(get(vh.getAdapterPosition()).getConversationId());

            Intent intent = new Intent(activity, ConversationActivity.class);
            intent.putExtra(Constants.CHAT_MEMBER_ID, get(vh.getAdapterPosition()).getMemberId());
            intent.putExtra(Constants.CHAT_CONVERSATION_IS_STAFF, get(vh.getAdapterPosition()).isStaff());
            activity.startActivity(intent);
            notifyDataSetChanged();
        }
    }

    private void HasCheckBoxSelect() {
        notifyDataSetChanged();
        for (int i = 0; i < getData().size(); i++) {
            if (get(i).isChecked()) {
                hasCheck = true;
                break;
            }
            if (i + 1 == getData().size()) {
                if (get(i).isChecked()) {
                    hasCheck = true;
                } else {
                    hasCheck = false;
                }
                break;
            }
        }
        if (hasCheck) {
            ((MainActivity) activity).setHeaderRightText("删除");
        } else {
            ((MainActivity) activity).setHeaderRightText("完成");
        }
    }

    private void setUnSelect() {
        for (ChatConversation ChatConversation : getData()) {
            ChatConversation.setChecked(false);
        }
    }

    public void hideRedPoint() {
        for (ChatConversation chatConversation : getData()) {
            if (chatConversation.getUnReadCount() != 0) {
                Config.putString(Constants.SHOW_RED_POINT, "enable");
                return;
            }
        }
        Config.putString(Constants.SHOW_RED_POINT, "disable");
        ((MainActivity) activity).showRedPoint();
    }
}
