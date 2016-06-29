package com.gzliangce.ui.activity.chat;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCoversationBinding;
import com.gzliangce.dto.ChatInfoDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.MembersInfo;
import com.gzliangce.event.ImMessagePushEvent;
import com.gzliangce.event.ImMessageSendEvent;
import com.gzliangce.event.InChatEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.adapter.CoversationAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.AVImClientManagerUtil;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.MetadataUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.AppManager;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Files;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * 聊天界面
 */
public class ConversationActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ActivityCoversationBinding binding;
    private CoversationAdapter adapter;
    private AVIMConversation imConversation;
    private ChatInfoDTO chatInfo;
    private HashMap<String, Object> attributes = new HashMap<>();
    private List<MembersInfo> membersInfos = new ArrayList<>();
    private String lastMessage;
    private AccountInfo userInfo;
    private int page = 0;
    private String memberId, ownId;
    private boolean isStaff = false, needCleanRedPoint = false;
    private String staffId;
    private AVPush push;
    private AVQuery<AVInstallation> query;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coversation);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new CoversationAdapter(this);
        binding.rcvChat.setAdapter(adapter);
        binding.rcvChat.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tasks.handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollToBottom();
                    }
                }, 400);
                return false;
            }
        });
        binding.tvSendMessage.setOnClickListener(this);
    }

    @Override
    public void initData() {
        memberId = getIntent().getStringExtra(Constants.CHAT_MEMBER_ID);
        isStaff = getIntent().getBooleanExtra(Constants.CHAT_CONVERSATION_IS_STAFF, false);
        needCleanRedPoint = getIntent().getBooleanExtra(Constants.IS_NEED_CLEAN_RED_POINT, false);
        if (Strings.isEmpty(memberId)) {
            ToastHelper.showMessage(this, "用户ID为空");
            return;
        }
        staffId = MetadataUtil.getStaffCache(this);
        if (Strings.isNotEmpty(staffId)) {
            if (Strings.isEquals(memberId, staffId)) {
                isStaff = true;
            }
        }
        //测试
        //memberId="171";
        userInfo = AppContext.me().getUser();
        onRefresh();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_message:
                sendMessage();
                break;
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        if (Strings.isEmpty(binding.etMessage.getText().toString())) {
            ToastHelper.showMessage(this, "输入不能为空");
            return;
        }
        if (binding.srvRefresh.isRefreshing()) {
            ToastHelper.showMessage(ConversationActivity.this, "获取聊天记录中，请稍候再发");
            return;
        }
        doSendMessage(binding.etMessage.getText().toString());
        binding.etMessage.setText("");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackClicked() {
        Systems.hideKeyboard(this);
        onBackPressed();
    }

    @Override
    public void onMenuClicked() {
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("");
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    private void judgeClientOpen(final String memberId) {
        if (AVImClientManagerUtil.getInstance().getClient() != null) {
            getConversation(memberId);
            return;
        }
        if (!AppContext.me().isLogined()) {
            return;
        }
        ownId = String.valueOf(AppContext.me().getUserId());
        AVImClientManagerUtil.getInstance().open(ownId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    getConversation(memberId);
                } else {
                    logger.e("登录失败");
                }
            }
        });
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
     */
    private void getConversation(final String memberId) {
        final AVIMClient client = AVImClientManagerUtil.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e == null) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    getMemberInfo(memberId, client, list);
                }
            }
        });
    }

    /**
     * 根据用户的id获取聊天窗口信息
     */
    private void getMemberInfo(final String memberId, final AVIMClient client, final List<AVIMConversation> list) {
        Call<ChatInfoDTO> call = ApiUtil.getUserCenterService().getChatInfo(memberId);
        call.enqueue(new APICallback<ChatInfoDTO>() {
            @Override
            public void onSuccess(ChatInfoDTO chatInfoDTO) {
                chatInfo = chatInfoDTO;
                handleChatData(memberId, client, chatInfoDTO, list);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ConversationActivity.this, message);
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
            }

        });
    }

    private void handleChatData(String memberId, AVIMClient client, ChatInfoDTO chatInfoDTO, List<AVIMConversation> list) {
        if (isStaff) {
            adapter.setMemberIcon(Files.getAssetFile("ic_staff.png"));
            header.setMidTitle("在线客服");
        } else {
            adapter.setMemberIcon(chatInfoDTO.getIcon());
            header.setMidTitle(chatInfoDTO.getRealName());
        }
        binding.setHeader(header);

        //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
        if (null != list && list.size() > 0) {
            fetchMessages(list.get(0));
        } else {
            createConversation(memberId, client, chatInfoDTO);
        }

    }

    private void createConversation(String memberId, AVIMClient client, ChatInfoDTO chatInfoDTO) {
        initAttribute("");
        client.createConversation(Arrays.asList(memberId), chatInfoDTO.getRealName(), attributes, false, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                fetchMessages(avimConversation);
            }
        });
    }

    private MembersInfo newMembersInfo(String memberId, String icon, String realName) {
        MembersInfo membersInfo = new MembersInfo();
        membersInfo.setId(memberId);
        membersInfo.setAvatar(icon);
        membersInfo.setNickname(realName);
        return membersInfo;
    }

    /**
     * 拉取消息，必须加入 conversation 后才能拉取消息
     */
    private void fetchMessages(AVIMConversation conversation) {
        imConversation = conversation;
        if (needCleanRedPoint) {
            EventHub.post(new InChatEvent(imConversation));
        }
        conversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null) {
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    scrollToBottom();
                }
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }


    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void doSendMessage(String messageText) {
        if (null != imConversation && null != binding.etMessage.getText().toString()) {
            AVIMMessage message = new AVIMMessage();
            lastMessage = messageText;
            message.setContent(messageText);
            message.setMessageStatus(AVIMMessage.AVIMMessageStatus.AVIMMessageStatusSending);
            adapter.add(message);
            adapter.notifyItemChanged(adapter.size() - 1);
            scrollToBottom();
            imConversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (e == null) {
                        adapter.get(adapter.size() - 1).setMessageStatus(AVIMMessage.AVIMMessageStatus.AVIMMessageStatusReceipt);
                        adapter.notifyItemChanged(adapter.size() - 1);
                        updateConversation(lastMessage);
                        PushMessage(lastMessage);
                    } else {
                        adapter.get(adapter.size() - 1).setMessageStatus(AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed);
                        adapter.notifyItemChanged(adapter.size() - 1);
                        logger.e(e.toString());
                    }
                }
            });
        }
    }

    private void updateConversation(final String lastMessage) {
        initAttribute(lastMessage);
        imConversation.setAttributes(attributes);
        imConversation.updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                EventHub.post(new ImMessageSendEvent(lastMessage, imConversation));
                scrollToBottom();
            }
        });
    }

    private void initAttribute(String lastMessage) {
        membersInfos.clear();
        attributes.put(Constants.LAST_MESSAGE, lastMessage);
        membersInfos.add(newMembersInfo(memberId, chatInfo.getIcon(), chatInfo.getRealName()));
        membersInfos.add(newMembersInfo(userInfo.getAccountId() + "", userInfo.getIcon(), userInfo.getRealName()));
        attributes.put(Constants.MEMBERS_INFO, membersInfos);
    }

    /**
     * 接收推送的消息Event
     */
    @Subscribe
    public void onPushMessageEvent(ImMessagePushEvent event) {
        if (null != imConversation &&
                imConversation.getConversationId().equals(event.conversation.getConversationId())) {
            adapter.add(event.message);
            adapter.notifyDataSetChanged();
            scrollToBottom();
            EventHub.post(new InChatEvent(imConversation));
        }
    }

    private void scrollToBottom() {
        if (page == 1) {
            binding.rcvChat.scrollToPosition(adapter.getItemCount() - 1);
        } else {
            binding.rcvChat.scrollToPosition(0);
        }
    }

    @Override
    public void onRefresh() {
        page++;
        if (page == 1) {
            judgeClientOpen(memberId);
            return;
        }
        //返回的消息一定是时间增序排列，也就是最早的消息一定是第一个
        if (adapter.size() < 1) {
            binding.srvRefresh.setRefreshing(false);
            return;
        }
        AVIMMessage oldestMessage = adapter.get(0);
        imConversation.queryMessages(oldestMessage.getMessageId(), oldestMessage.getTimestamp(), 20, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e != null) {
                    return;
                }
                if (list.size() > 0) {
                    adapter.addAll(0, list);
                    adapter.notifyDataSetChanged();
                    binding.rcvChat.smoothScrollToPosition(list.size() - 1);
                }
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Activity mainActivity = AppManager.getActivity(MainActivity.class);
        if (mainActivity == null) {
            LiangCeUtil.isActionLockActivity(this);
        } else {
            finish();
        }
        return;
    }


    private void PushMessage(String message) {
        if (push == null) {
            push = new AVPush();
        }
        if (query == null) {
            query = AVInstallation.getQuery();
        }

        // 设置查询条件
        query.whereEqualTo("ClientId", memberId);
        push.setQuery(query);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "UserMessage");
        if (isStaff) {
            jsonObject.put("alert", "在线客服：" + message);
        } else {
            jsonObject.put("alert", chatInfo.getRealName() + "：" + message);
        }
        jsonObject.put("sender-id", AppContext.me().getUserId());

        push.setData(jsonObject);

        // 推送
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    logger.e("successful: push message");
                } else {
                    logger.e("failed: push message" + e.toString());
                }
            }
        });
    }
}
