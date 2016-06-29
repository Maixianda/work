package com.gzliangce.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.FragmentMessageBinding;
import com.gzliangce.db.ConversationHistory;
import com.gzliangce.db.DBManager;
import com.gzliangce.dto.ChatInfoDTO;
import com.gzliangce.dto.StaffDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.ChatConversation;
import com.gzliangce.entity.MembersInfo;
import com.gzliangce.enums.UserType;
import com.gzliangce.event.ImMessagePushEvent;
import com.gzliangce.event.ImMessageSendEvent;
import com.gzliangce.event.InChatEvent;
import com.gzliangce.event.LoginEvent;
import com.gzliangce.event.LogoutEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.MainActivity;
import com.gzliangce.ui.adapter.AVIMCoversationListAdapter;
import com.gzliangce.ui.base.BaseFragmentBinding;
import com.gzliangce.util.AVImClientManagerUtil;
import com.gzliangce.util.AdapterUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.UiUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.ui.fragment.BaseFragment;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 聊天界面
 * <p>
 * Created by Aaron on 8/21/14.
 */
public class MessageFragment extends BaseFragmentBinding implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static Logger logger = LoggerFactory.getLogger(MessageFragment.class);
    private FragmentMessageBinding binding;
    private AVIMCoversationListAdapter adapter;
    private AccountInfo userInfo;
    private ChatInfoDTO chatInfo;

    private List<MembersInfo> membersInfos = new ArrayList<>();
    private String ownsId, staffId;
    private boolean hasStaff = false;
    private boolean inList = false;
    private boolean needShowredPoint = false;
    private int iconPosition = 0;
    private String lastMessage;
    private HashMap<String, Object> attributes = new HashMap<>();
    private List<ConversationHistory> conversationHistories = new ArrayList<>();
    private List<ChatConversation> coversationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView() {
        adapter = new AVIMCoversationListAdapter(getActivity());
        adapter.onFinishLoadMore(true);
        binding.rcvCoversationList.setAdapter(adapter);
        binding.rcvCoversationList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void initListener() {
        binding.srvRefresh.setOnRefreshListener(this);
        binding.hint.tvLoad.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (AppContext.me().isLogined()) {
            userInfo = AppContext.me().getUser();
            ownsId = String.valueOf(userInfo.getAccountId());
            postDelayRefresh();
        } else {
            LoadingHelper.hideMaterLoading();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        LoadingHelper.showMaterLoading(getContext(), "加载中");
        initData();
    }

    public void doEditModle() {
        adapter.toggleEditMode();
    }

    public void setCompleteModle() {
        if (adapter != null) {
            adapter.completeMode();
        }
    }

    public boolean getStatus() {
        return adapter.getEditStatus();
    }

    /**
     * 查询会话列表
     */
    private void getConversationListData() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        userInfo = AppContext.me().getUser();
        ownsId = String.valueOf(userInfo.getAccountId());
        AVImClientManagerUtil.getInstance().open(ownsId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    getCoversationList(client);
                } else {
                    logger.e("获取列表失败:登录失败:" + e.getMessage());
                    binding.srvRefresh.setRefreshing(false);
                    isSetHint(true, R.string.http_on_failed);
                    LoadingHelper.hideMaterLoading();
                }
            }
        });
    }

    /**
     * 请求服务端获取会话列表
     */
    private void getCoversationList(AVIMClient client) {
        AVIMConversationQuery query = client.getQuery();
        query.orderByDescending("updatedAt");
        query.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.limit(1000);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> convs, AVIMException e) {
                if (e == null) {
                    logger.e("convs： " + convs.size());
                    hasStaff = false;
                    coversationList.clear();
                    getCoversation(convs);
                } else {
                    isSetHint(true, R.string.http_on_failed);
                }
                LoadingHelper.hideMaterLoading();
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 遍历会话列表，取出有用数据
     */
    private void getCoversation(List<AVIMConversation> convs) {
        for (AVIMConversation conv : convs) {
            if (judgeDataValid(conv)) {
                setAdapterData(conv);
            } else {
                deleteCoversation(conv);
            }
        }
        //如果没有跟客服聊过天，创建conversation
        if (!hasStaff) {
            if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
                queryConversation(staffId);
            } else {
                adapter.clear();
                adapter.addAll(sortRooms(coversationList));
                adapter.notifyDataSetChanged();
            }
        } else {
            adapter.clear();
            adapter.addAll(sortRooms(coversationList));
            adapter.notifyDataSetChanged();
        }
        isSetHint(false, R.string.no_messgae);
        getUnread();
    }

    /**
     * 是否显示提示
     *
     * @param isFailed
     * @param strId
     */
    private void isSetHint(boolean isFailed, int strId) {
        UiUtil.isSetFailedHint(adapter.size(), binding.hint.tvLoad, binding.srvRefresh, strId, isFailed);
    }

    /**
     * 判断会话的有效性
     *
     * @param avimConversation
     * @return
     */
    private boolean judgeDataValid(AVIMConversation avimConversation) {
        for (String member : avimConversation.getMembers()) {
            if (Strings.isEquals(member, ownsId) && avimConversation.getMembers().size() > 1) {
                try {
                    lastMessage = avimConversation.getAttribute("lastmessage").toString();
                } catch (Exception e) {
                    lastMessage = "";
                }
                if (Strings.isNotEmpty(lastMessage)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是不是客服
     *
     * @param avimConversation
     * @return
     */
    private boolean judgeIsStaff(AVIMConversation avimConversation) {
        for (String member : avimConversation.getMembers()) {
            if (Strings.isEquals(member, staffId)) {
                hasStaff = true;
                return true;
            }
        }
        return false;
    }

    /**
     * setAdapterItem数据
     *
     * @param avimConversation
     */
    private void setAdapterData(AVIMConversation avimConversation) {
        //判断在线客服是在会话历史中
        if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
            if (judgeIsStaff(avimConversation)) {
                coversationList.add(0, new ChatConversation(avimConversation, true));
                return;
            }
        }
        adapter.add(new ChatConversation(avimConversation, false));
        isSetHint(false, R.string.no_messgae);
        coversationList.add(new ChatConversation(avimConversation, false));
    }

    /**
     * 根据用户的id获取聊天窗口信息
     */
    private void getMortgageOrderList(final String memberId) {
        attributes.clear();
        Call<ChatInfoDTO> call = ApiUtil.getUserCenterService().getChatInfo(memberId);
        call.enqueue(new APICallback<ChatInfoDTO>() {
            @Override
            public void onSuccess(ChatInfoDTO chatInfoDTO) {
                chatInfo = chatInfoDTO;
                //获取会话历史
                getConversationListData();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }

    private void initAttributes(String memberId, String lastMessage, ChatInfoDTO chatInfoDTO) {
        attributes.clear();
        attributes.put(Constants.LAST_MESSAGE, lastMessage);
        membersInfos.add(newMembersInfo(memberId, chatInfoDTO.getIcon(), chatInfoDTO.getRealName()));
        membersInfos.add(newMembersInfo(String.valueOf(userInfo.getAccountId()), userInfo.getIcon(), userInfo.getRealName()));
        attributes.put(Constants.MEMBERS_INFO, membersInfos);
    }

    /**
     * 获取 conversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 conversation
     * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
     */
    private void queryConversation(final String memberId) {
        final AVIMClient client = AVImClientManagerUtil.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e == null) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    if (null != list && list.size() > 0) {
                        coversationList.add(0, new ChatConversation(list.get(0), true));
                        adapter.clear();
                        adapter.addAll(sortRooms(coversationList));
                        adapter.notifyDataSetChanged();
                    } else {
                        createStaffConversation(memberId, client);
                    }
                } else {
                    logger.d(e.toString());
                }
            }
        });
    }

    /**
     * 创建会话
     *
     * @param memberId
     * @param client
     */
    private void createStaffConversation(String memberId, AVIMClient client) {
        //为在线客服初始化Attributes
        initAttributes(staffId, "", chatInfo);

        client.createConversation(Arrays.asList(memberId), chatInfo.getRealName(), attributes, false, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (e == null) {
                    coversationList.add(0, new ChatConversation(avimConversation, true));
                    adapter.clear();
                    adapter.addAll(sortRooms(coversationList));
                    adapter.notifyDataSetChanged();
                } else {
                    ToastHelper.showMessage(getActivity(), "创建失败 : " + e.toString());
                    logger.d(e.toString());
                }
            }
        });
    }

    /**
     * 获取未读数
     */
    private void getUnread() {
        conversationHistories.clear();
        conversationHistories.addAll(DBManager.getInstance().query());
        for (ConversationHistory conversationHistory : conversationHistories) {
            for (int m = 0; m < adapter.size(); m++) {
                if (Strings.isEquals(conversationHistory.getConversationId(), adapter.get(m).getConversationId())) {
                    adapter.get(m).setUnReadCount(conversationHistory.getUnreadCount());
                    needShowredPoint = true;
                }
            }
        }
        if (needShowredPoint) {
            Config.putString(Constants.SHOW_RED_POINT, "enable");
        } else {
            Config.putString(Constants.SHOW_RED_POINT, "disable");
        }
        ((MainActivity) getActivity()).showRedPoint();
        adapter.notifyDataSetChanged();
    }

    /**
     * 创建Attributes的memberinfo
     *
     * @param memberId
     * @param icon
     * @param realName
     * @return
     */
    private MembersInfo newMembersInfo(String memberId, String icon, String realName) {
        MembersInfo membersInfo = new MembersInfo();
        membersInfo.setId(memberId);
        membersInfo.setAvatar(icon);
        membersInfo.setNickname(realName);
        return membersInfo;
    }

    /**
     * 接收推送的消息Event
     */
    @Subscribe
    public void onPushMessageEvent(ImMessagePushEvent event) {
        inList = false;
        logger.d("接收");
        String sendId = event.getConversation().getConversationId();
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (Strings.isEquals(adapter.get(i).getConversationId(), sendId)) {
                inList = true;
                adapter.get(i).setLastMessage(event.getMessage().getContent() + "");
                adapter.get(i).setUnReadCount(adapter.get(i).getUnReadCount() + 1);
                ChatConversation tempChatConversation = adapter.get(i);
                adapter.remove(i);
                //判断是否有客服的，需要将新消息置顶
                if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
                    adapter.add(1, tempChatConversation);
                } else {
                    adapter.add(0, tempChatConversation);
                }
                adapter.notifyDataSetChanged();
                break;
            }
        }
        if (!inList) {
            DBManager.getInstance().add(event.getConversation(), 1);
            Tasks.handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    postDelayRefresh();
                }
            }, 800);

        }
    }

    /**
     * 接收Conversation的反馈
     */
    @Subscribe
    public void onInChatCleanUnRead(InChatEvent event) {
        logger.d("接受clean红点");
        String sendId = event.getConversation().getConversationId();
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (Strings.isEquals(adapter.get(i).getConversationId(), sendId)) {
                adapter.get(i).setUnReadCount(0);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 接收自己发送出的消息Event
     */
    @Subscribe
    public void onSendMessageEvent(ImMessageSendEvent event) {
        inList = false;
        logger.d("发送");
        String sendId = event.getConversation().getConversationId();
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (Strings.isEquals(adapter.get(i).getConversationId(), sendId)) {
                inList = true;
                adapter.get(i).setLastMessage(event.getMessage());
                adapter.get(i).setTime(event.getConversation().getLastMessageAt().getTime());
                ChatConversation tempChatConversation = adapter.get(i);
                adapter.remove(i);
                //判断是否有客服的，需要将新消息置顶
                if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
                    adapter.add(1, tempChatConversation);
                } else {
                    adapter.add(0, tempChatConversation);
                }
                adapter.notifyDataSetChanged();
                break;
            }
        }
        if (!inList) {
            postDelayRefresh();
        }
    }

    /**
     * 延时刷新数据
     */
    private void postDelayRefresh() {
        binding.srvRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.srvRefresh.setRefreshing(true);
            }
        });
        binding.srvRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 500);
    }


    /**
     * 点击删除按钮
     */
    public void deleteConversation() {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (adapter.get(i).isChecked()) {
                if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
                    if (i == 0) {
                        ToastHelper.showMessage(getActivity(), "在线客服不能删除!");
                        break;
                    }
                }
                postDeleteCoversation(i);
            }
        }
    }

    /**
     * 请求服务端删除会话
     *
     * @param position
     */
    private void postDeleteCoversation(final int position) {
        adapter.get(position).getConversation().kickMembers(Arrays.asList(ownsId), new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    adapter.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    logger.d("删除会话失败" + e.toString());
                }
                binding.srvRefresh.setRefreshing(false);
                isSetHint(false, R.string.no_messgae);
            }
        });
    }

    /**
     * 请求服务端删除会话
     */
    private void deleteCoversation(AVIMConversation avimConversation) {
        avimConversation.kickMembers(Arrays.asList(ownsId), new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    logger.d("删除会话成功");
                    isSetHint(false, R.string.no_messgae);
                }
                isSetHint(false, R.string.no_messgae);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Strings.isEmpty(staffId)) {
            getOnlineStaff();
            return;
        }
        if (AVImClientManagerUtil.getInstance().getClient() == null) {
            getConversationListData();
        } else {
            getCoversationList(AVImClientManagerUtil.getInstance().getClient());
        }
    }


    /**
     * 获取在线客服Id
     */
    private void getOnlineStaff() {
        Call<StaffDTO> call = ApiUtil.getUserCenterService().getOnlineStaff();
        call.enqueue(new APICallback<StaffDTO>() {
            @Override
            public void onSuccess(StaffDTO staffDTO) {
                staffId = staffDTO.getStaff();
                if (staffId.startsWith(Constants.SYSTEM)) {
                    staffId = staffId.replace(Constants.SYSTEM, "");
                }
                getMortgageOrderList(staffId);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
                isSetHint(true, R.string.http_on_failed);
                LoadingHelper.hideMaterLoading();
            }

            @Override
            public void onFinish() {
                binding.srvRefresh.setRefreshing(false);
            }
        });
    }


    /**
     * 根据用户的id获取聊天窗口信息
     */
    private void getMemberInfo(ChatConversation chatConversation, final int position) {
        iconPosition++;
        Call<ChatInfoDTO> call = ApiUtil.getUserCenterService().getChatInfo(chatConversation.getMemberId());
        call.enqueue(new APICallback<ChatInfoDTO>() {
            @Override
            public void onSuccess(ChatInfoDTO chatInfoDTO) {
                if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
                    if (position == 0) {
                        return;
                    }
                }
                if (!Strings.isEquals(adapter.get(position).getIcon(), chatInfoDTO.getIcon()) ||
                        !Strings.isEquals(adapter.get(position).getName(), chatInfoDTO.getRealName())) {
                    adapter.get(position).setIcon(chatInfoDTO.getIcon());
                    adapter.get(position).setName(chatInfoDTO.getRealName());
                    updateConversation(adapter.get(position), chatInfoDTO);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getActivity(), message);
            }

            @Override
            public void onFinish() {
                if (iconPosition == adapter.getData().size()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * update conversation
     */
    private void updateConversation(ChatConversation chatConversation, ChatInfoDTO chatInfoDTO) {
        initAttributes(chatConversation.getMemberId(), chatConversation.getLastMessage(), chatInfoDTO);
        chatConversation.getConversation().setAttributes(attributes);
        chatConversation.getConversation().setName(chatInfoDTO.getRealName());
        chatConversation.getConversation().updateInfoInBackground(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    logger.d("update success");
                } else {
                    logger.d("update faile");
                }
            }
        });
    }


    /**
     * 接收登录Event,切换Fragment
     */
    @Subscribe
    public void onLogInEvent(LoginEvent event) {
        if (event != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            initData();
        }
    }


    /**
     * 接收退出账号Event,切换Fragment
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        if (event != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            isSetHint(false, R.string.no_messgae);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshIcon();
        }
    }

    /**
     * 刷新icon
     */
    public void refreshIcon() {
        iconPosition = 0;
        if (adapter.getData().size() < 1) {
            onRefresh();
            return;
        }
        for (int i = 0; i < adapter.getData().size(); i++) {
            getMemberInfo(adapter.get(i), i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.hideRedPoint();
    }

    /**
     * 按最后发消息时间排序
     *
     * @param chatList
     * @return
     */
    private List<ChatConversation> sortRooms(List<ChatConversation> chatList) {
        Collections.sort(chatList, new Comparator<ChatConversation>() {
            @Override
            public int compare(ChatConversation lhs, ChatConversation rhs) {

                long value = lhs.getTime() - rhs.getTime();
                if (value > 0) {
                    return -1;
                } else if (value < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        //判断是否有客服的，需要将新消息置顶
        if (!LiangCeUtil.judgeUserType(UserType.mortgage)) {
            // 将客服移到第一个item
            ChatConversation temp = null;
            for (int i = 0; i < chatList.size(); i++) {
                for (String member : chatList.get(i).getConversation().getMembers()) {
                    if (Strings.isEquals(member, staffId)) {
                        temp = chatList.get(i);
                        break;
                    }
                }
            }
            if (temp != null) {
                chatList.remove(temp);
                chatList.add(0, temp);
            }
        }
        return chatList;
    }
}