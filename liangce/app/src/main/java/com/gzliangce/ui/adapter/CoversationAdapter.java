package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.databinding.ItemMessageReceiveBinding;
import com.gzliangce.databinding.ItemMessageSendBinding;
import com.gzliangce.ui.activity.chat.ConversationActivity;
import com.gzliangce.util.AVImClientManagerUtil;

import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.date.Date;
import io.ganguo.library.util.date.FriendlyDate;
import io.ganguo.library.util.log.Logger;


/**
 * Created by aaron on 10/20/15.
 */
public class CoversationAdapter extends ListAdapter<AVIMMessage, ViewDataBinding> {
    private Activity activity;
    private String memberIcon;
    private String ownIcon;

    public CoversationAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
        if (AppContext.me().isLogined()) {
            this.ownIcon = AppContext.me().getUser().getIcon();
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        if (get(position).getFrom().equals(AVImClientManagerUtil.getInstance().getClientId())) {
            return R.layout.item_message_send;
        } else {
            return R.layout.item_message_receive;
        }
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ViewDataBinding> vh, int position) {
        switch (vh.getItemViewType()) {
            case R.layout.item_message_send: {
                ItemMessageSendBinding binding = (ItemMessageSendBinding) vh.getBinding();
                binding.setIcon(ownIcon);
                handleDateVisibility(binding.tvChatTime, position);
                binding.ivSendResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (get(vh.getAdapterPosition()).getMessageStatus().getStatusCode() ==
                                AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed.getStatusCode()) {
                            String messageText = get(vh.getAdapterPosition()).getContent();
                            remove(vh.getAdapterPosition());
                            ((ConversationActivity) activity).doSendMessage(messageText);
                        }
                    }
                });
                break;
            }
            case R.layout.item_message_receive: {
                ItemMessageReceiveBinding binding = (ItemMessageReceiveBinding) vh.getBinding();
                binding.setIcon(memberIcon);
                handleDateVisibility(binding.tvChatTime, position);
                break;
            }
        }
    }

    private void handleDateVisibility(View view, int position) {
        if (position == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            Date date1 = new Date(get(position - 1).getTimestamp());
            Date date2 = new Date(get(position).getTimestamp());
            FriendlyDate.TimeSpan timeSpan = new FriendlyDate.TimeSpan(date1, date2);
            if (timeSpan.minutes < 5) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setMemberIcon(String memberIconUrl) {
        memberIcon = memberIconUrl;
    }

}
