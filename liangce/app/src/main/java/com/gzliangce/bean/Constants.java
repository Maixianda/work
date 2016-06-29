package com.gzliangce.bean;

/**
 * 常量
 * <p>
 * Created by Tony on 11/10/15.
 */
public class Constants {
    public final static String TAG_GG_API = "GG_API";

    //验证码计时时间
    public static final long ILLIS_IN_FUTURE = 60000;
    public static final long COUNT_DOWN_INTERVAL = 1000;
    //手势解锁密码key
    public final static String GESTURE_LOCK_PASSWORD_KEY = "GESTURE_LOCK_PASSWORD_KEY_KEY_MAP";
    //PIN码解锁密码key
    public final static String PIN_PASSWORD_KEY = "PIN_PASSWORD_KEY_MAP";
    // LockSetting ->GestureLockActivity 传递是否设置手势密码状态
    public final static String IS_GESTURE_LOCK_MODE = "IS_GESTURE_LOCK_MODE_KEY_MAP";
    //LockSetting -> 解锁界面成功后需要跳转的界面class
    public final static String IS_UNLOCK_JUM_ACTIVITY_NAME = "IS_UNLOCK_JUM_ACTIVITY_NAME_KEY_MAP";
    //判断跳转来自哪个activity
    public final static String ACTIVITY_FROM_TYPE = "ACTIVITY_FROM_TYPE";
    //AboutAppActivity -> 文本相关activity 需要加载的链接
    public final static String ABOUT_TEXT_URL = "ABOUT_TEXT_UEL_KEY_MAP";
    //AboutAppActivity -> 文本相关界面title
    public final static String ABOUT_TEXT_TITLE = "ABOUT_TEXT_TITLE_KEY_MAP";
    //图片预览界面
    public final static String PREVIEW_IMAGE_ACTIVITY_DATA = "PREVIEW_IMAGE_ACTIVITY_DATA_KEY_MAP";
    //图片预览界面index
    public final static String PREVIEW_IMAGE_ACTIVITY_INDEX_DATA = "PREVIEW_IMAGE_ACTIVITY_INDEX_DATA_KEY_MAP";
    //缓存Metadata key
    public final static String METADATA_DATA_KEY = "METADATA_DATA_KEY_MAP";
    //缓存物业区域 key
    public final static String PROPERTY_AREA_KEY = "property_area_key";
    //用户登录信息
    public final static String CONFIG_LOGIN_USER = "CONFIG_LOGIN_USER";
    //用户手机号码
    public final static String USER_PHONE_NUMBER = "USER_PHONE_NUMBER_KEY_MAP";
    //用户短信验证码
    public final static String USER_SMS_CODE = "USER_SMS_CODE_KEY_MAP";
    //推荐人手机号
    public final static String USER_REFERRER_PHONE_NUMBER = "USER_REFERRER_PHONE_KEY_MAP";
    //登录成功后是否要跳转到主界面
    public final static String IS_ACTION_MANIN_ACTIVITY = "IS_ACTION_MANIN_ACTIVITY_KEY_MAP";
    //搜索界面搜索结果
    public final static String SEARCH_RESULT_TYPE = "search_result_type";
    //搜索界面搜索结果类型
    //1.订单类型。2.资料类型。3.经纪人类型.4.资讯类型
    public static final int[] SEARCH_TYPE = {1, 2, 3, 4};
    //我的订单页面订单状态
    public static final String ORDER_STATUS = "order_status";
    //跳转到产品详情Activity
    public final static String ACTION_PRODUCT_DETAIL_ACTIVITY = "ACTION_PRODUCT_DETAIL_ACTIVITY_KEY_MAP";
    //请求定位权限
    public final static int REQUEST_ACCESS_COARSE_LOCATION = 1;
    //传递经纪人id数据
    public final static String REQUEST_BROKE_INFO_ID = "REQUEST_BROKE_ID_INFO_KEY_MAP";
    //传递按钮显示状态
    public final static String REQUEST_BUTTON_STATUS = "REQUEST_BUTTON_STATUSB_KEY_MAP";
    //订单状态
    public final static String INTENT_ORDER_STATE = "INTENT_ORDER_STATE_KEY_MAP";
    //下订参数
    public final static String PLACE_ON_ORDER_PARM = "place_on_order_parm";
    //按揭员列表数据
    public final static String PLACE_ON_ORDER_PARM_LIST = "place_on_order_parm_list";
    //是否是从下单界面跳转到产品详情
    public final static String REQUEST_ACTIVITY_PLACE_ORDER = "request_activity_place_orde";
    //订单信息
    public final static String ORDER_INFO = "order_info";
    //传递房款总价到首付比例界面
    public final static String REQUEST_ACTIVITY_HOUSER_PRICE = "REQUEST_ACTIVITY_HOUSER_PRICE_KEY_MAP";
    //传递首付比例到计算器界面
    public final static String REQUEST_ACTIVITY_HOUSER_SCALE = "REQUEST_ACTIVITY_HOUSER_SCALE_KEY_MAP";
    //计算类型
    public final static String REQUEST_CALCULATION_TYPE = "REQUEST_CALCULATION_TYPE_KEY_MAP";
    //订单号
    public final static String ORDER_NUMBER = "order_number";
    //订单图片
    public final static String ORDER_PHOTO = "order_photo";
    //是不是签约拍照
    public final static String IS_SIGN_PHOTO = "is_sign_photo";
    //贷款数据
    public final static String LOAN_INFO = "LoanInfo";
    //文档资料类型
    public final static String DOCUMENT_DATA_TYPE = "document_data_type";
    //是否需要删除按钮
    public final static String IS_NEED_DELETE = "is_need_delete";
    //跳转到月供详情界面需要传递的数据
    public final static String INTENT_DETAIL_MONTH_ACTIVITY_INFO = "DetailMonthActivity_info";
    //fragment所在界面位置
    public final static String FRAGMENT_INDEX = "fragment_index_data";
    //利率比例
    public final static String INTENT_DETAIL_INERESTEATE_INFO = "InterestrateInfo_info";
    //item_position
    public final static String ITEM_POSITION = "item_position";
    //首付比例RequestCode
    public static final int MORTGAGE_RATE_REQUST_CODE = 1101;
    //利率比例RequestCode
    public static final int INTERESTRATE_REQUST_CODE = 1102;
    //新房税费
    public final static String CALCUATION_RESULT_NEW_HOUSE = "CalcuationResult_new_house";
    //二手房税费
    public final static String CALCUATION_RESULT_OLD_HOUSE = "CalcuationResult_old_house";
    //聊天相关
    public static final String CHAT_MEMBER_ID = "chat_member_id";
    public static final String CHAT_CONVERSATION_ID = "chat_conversation_id";
    public static final String CHAT_CONVERSATION_IS_STAFF = "chat_conversation_is_staff";
    //签约拍照详情数据对象
    public static final String SIGN_NEW_PHOTO_INFO = "SignNewPhotoInfo";
    //利率数据保存到本地数据key
    public static final String RATE_LIST_DATA = "rateList_data";
    //是否是公积金利率
    public static final String IS_FUND_RATE = "isFundRate";
    //传递资讯相关数据
    public final static String NEWS_DETAILED_DATA = "news_detailed_data";
    //学院数据
    public final static String COLLEGE_COURSE_DATA = "college_course_data";
    //学院列表item数据
    public final static String COLLEGE_COURSE_LIST_ITEM = "college_course_list_item";
    //导师id key
    public final static String TEACHER_ID = "teacher_id";
    //在线客服id key
    public final static String STAFF_ID = "staff_id";
    //需要清除对应conversation为读数 key
    public static final String IS_NEED_CLEAN_RED_POINT = "is_need_clean_red_point";
    //im红点状态 key
    public static final String SHOW_RED_POINT = "show_red_point";
    //消息中心红点状态 key
    public static final String SHOW_WHITE_POINT = "show_white_point";
    //收藏界面直接下单需要用到的按揭员用户id
    public static final String DIRECT_ORDER_MEMBERID = "direct_order_memberId";
    //保存未登录时下单信息
    public static final String SAVE_OREDER_INFO = "save_order_info";
    //是否是直接下单
    public static final String IS_DIRECT = "is_direct";
    //保存app更新日志
    public static final String APP_UPDATE_NKOTE = "app_update_note";
    //月供详情数据
    public static final String PAYMENT_MONTHLY = "payment_monthly";


    /**
     * request key
     */
    public static final int PAGE_SIZE = 20;
    public static final String PAGE = "page";
    public static final String TYPE = "type";
    public static final String SIZE = "size";
    public static final String STATUS = "status";
    public static final String KEYWORD = "keyword";
    public static final String NAME = "name";
    public static final String AREAI_D = "areaId";
    public static final String INDEX = "index";
    public static final String SYSTEM = "system-";
    public static final String COLLEGE = "college";
    public static final String WAIT = "wait";
    public static final String LAST_MESSAGE = "lastmessage";
    public static final String MEMBERS_INFO = "membersInfo";
    public static final String CLIENT_ID="ClientId";
}
