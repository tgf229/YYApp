package com.yy.yyapp.constant;

public class Constants
{
    /**
     * 是否显示引导页，版本控制,需要引导页时，版本号变大1
     */
    public static final int GUIDE_VERSION_CODE = 1;
    
    /**
     * 推出应用
     */
    public static final String EXIT_APP = "1";
    
    /**
     * 进入应用
     */
    public static final int IN_APP = 0;
    
    /**
     *  成功码
     */
    public static final String SUCESS_CODE = "000000";
    
    /**
     * 返回请求成功码
     */
    public static final String SUCESS_CODE_END = "000000";
    
    /**
     * 返回请求失败码
     */
    public static final String FAILED_CODE_END = "000001";
    
    /**
     * 未绑定任何房屋
     */
    public static final String NO_BIND_HOUSE = "000002";
    
    /**
     * 不加密  
     */
    public static final String ENCRYPT_NONE = "none";
    
    /**
     * 加密方式 - simple
     */
    public static final String ENCRYPT_SIMPLE = "simple";
    
    /**
     * sharedPreferences中保存用户的信息文件名
     */
    public static final String USER_INFO = "userInfo";
    
    /**
     * 手机类型   1 安卓  2 苹果
     */
    public static final String clientType = "1";
    
    /**
     * 小区默认id
     */
    //    public static String CID = "1";
    
    /**
     * 是否为游客进入应用
     */
    public static boolean TOURIST = false;
    
    /**
     * 魔鬼数字
     */
    public static final int NUM0 = 0;
    
    public static final int NUM1 = 1;
    
    public static final int NUM2 = 2;
    
    public static final int NUM3 = 3;
    
    public static final int NUM4 = 4;
    
    public static final int DOWNLOAD = 5;
    
    public static final int DOWNLOAD_FINISH = 6;
    
    public static final int NO_SD = 9;
    
    /**
     * 请求失败展示信息
     */
    public static final String ERROR_MESSAGE = "请求失败，请稍后再试";
    
    /**
     * 官方推荐
     */
    public static final String OFFICIAL_RECOMMENDATION = "1";
    
    /**
     * 热门社区
     */
    public static final String POPULAR_COMMUNITY = "2";
    
    /**
     * 我的加入的社区
     */
    public static final String MY_JOINED_COMMUNITY = "3";
    
    /**
     * 我管理的社区
     */
    public static final String MY_MANAGED_COMMUNITY = "4";
    
    /**
     * 他管理的社区
     */
    public static final String HE_MANAGED_COMMUNITY = "5";
    
    /**
     * 注册返回码1
     */
    public static final int Register_one_code = 1001;
    
    /**
     * 注册返回码2
     */
    public static final int Register_two_code = 1002;
    
    /**
     * 注册返回码3
     */
    public static final int Register_three_code = 1003;
    
    /**
     * 忘记密码返回码
     */
    public static final int Forget_password_code = 1004;
    
    /**
     * 个人中心登录请求返回码
     */
    public static final int Person_center_login_code = 1005;
    
    /**
     * 选择小区
     */
    public static final int Select_community_cid = 1015;
    
    /**
     * 移动话题
     */
    public static final int move_topic = 1016;
    
    /**
     * 普通话题
     */
    public static final String TOPIC_TYPE_NOMAL = "1";
    
    /**
     * 投票话题
     */
    public static final String TOPIC_TYPE_VOTE = "2";
    
    /**
     * 取消赞标志位
     */
    public static final String CANCEL_PRAISE = "0";
    
    /**
     * 赞标志位
     */
    public static final String PRAISE = "1";
    
    /**
     * 分享
     */
    public static final String SHARE = "2";
    
    /**
     * 评论
     */
    public static final String COMMENT = "3";
    
    /**
     * 未投票
     */
    public static final String NO_VOTE = "0";
    
    /**
     * 已投赞成
     */
    public static final String AGREE = "1";
    
    /**
     * 已投反对
     */
    public static final String DISAGREE = "2";
    
    /**
     * 圈子详情&&登陆名为圈主
     */
    public static final String COMMUNITY_DETAIL_OWNER = "1";
    
    /**
     * 圈子详情
     */
    public static final String COMMUNITY_DETAIL = "2";
    
    /**
     * 圈子-个人中心
     */
    public static final String COMMUNITU_PERSON_CENTER = "3";
    
    /**
     * 其他
     */
    public static final String OTHER = "0";
    
    /**
     * 是否置顶   0否  1是
     */
    public static final String NOTTOP = "0";
    
    public static final String ISTOP = "1";
    
    /**
     * 话题类型  1 普通话题    2 投票话题
     */
    public static final String NORMAL = "1";
    
    public static final String VOTE = "2";
    
    /**
     * 已关注：1
     */
    public static final String ADDED_GROUP = "1";
    
    /**
     * 未关注：0
     */
    public static final String QUITED_GROUP = "0";
    
    /**
     * 创建社区or编辑社区    
     * 创建社区 1
     * 编辑社区 2
     */
    public static final String CREATE_COMMUNITY = "1";
    
    public static final String EDIT_COMMUNITY = "2";
    
    /**
     * 1 置顶
     * 2 取消置顶
     * 3 删除
     */
    public static final String GROUP_UP = "1";
    
    public static final String GROUP_CANCEL_UP = "2";
    
    public static final String GROUP_DELETE = "3";
    
    /**
     * 圈子
     */
    public static final String PERSON_INFO_EDIT_GROUP = "1";
    
    /**
     * 0普通用户
     * 1大V
     * 2超级管理员
     */
    public static final String LEVEL_NORMAL = "0";
    
    public static final String LEVEL_VIP = "1";
    
    public static final String LEVEL_SUPER = "2";
    
    /**
     * 获取房屋返回码
     */
    public static final int Register_get_building = 1006;
    
    public static final int Register_get_unit = 1007;
    
    public static final int Register_get_num = 1008;
    
    /**
     * 设置新密码返回码
     */
    public static final int Forget_password_set_new = 1009;
    
    /**
     * 个人中心绑定房屋返回码
     */
    public static final int Register_binding_to_one = 1010;
    
    public static final int Register_binding_to_center = 1011;
    
    public static final int select_community = 1013;
    
    /**
     * 住户身份信息
     */
    public static final String ROLE_TENEMENT = "tenement";
    
    public static final String ROLE_OWNER = "owner";
    
    /**
     * 退出登录返回码
     */
    public static final int User_login_out = 1012;
    
    /**
     * 个人信息返回码
     */
    public static final int User_info_pic = 1014;
    
    /**
     * 手机验证码类型
     */
    public static final String CODE_REGISTER = "1";
    
    public static final String CODE_FORGET_PASSWORD = "2";
    
    public static final String CODE_GET_HOUSE = "3";
    
    /**
     * 倒计时时间
     */
    public static final int Countdown_start = 99000;
    
    public static final int Countdown_end = 1000;
    
    /**
     * 登录成功后发送广播
     */
    public static final String LOGIN_SUCCESS_BROADCAST = "com.yurun.jiarun.login.success";
    
    /**
     * 登出成功后发送广播
     */
    public static final String LOGINOUT_SUCCESS_BROADCAST = "com.yurun.jiarun.logout.success";
    
    /**
     * 个人中心添加房屋
     */
    public static final String ADD_HOUSE_BROADCAST = "com.yurun.jiarun.add.house";
    
    /**
     * 首页的选择小区
     */
    public static final String SELECT_NEW_COMMUNITY = "com.yurun.jiarun.select.new.community";
    
    /**
     * 个人中心删除房屋
     */
    public static final String DELETE_HOUSE_BROADCAST = "com.yurun.jiarun.delete_house";
    
    /**
     * 公告分享后数量加1
     */
    public static final String ADVERTISE_SUCCESS_BROADCAST = "com.yurun.jiarun.advertise.success";
    
    /**
     * 话题分享后数量加1
     */
    public static final String TOPIC_SUCCESS_BROADCAST = "com.yurun.jiarun.topic.success";
    
    /**
     * 点赞数量+1
     */
    public static final String COMMUNITY_PRAISE_COUNT = "com.yurun.jiarun.community_praise";
    
    /**
     * 评论数量+1
     */
    public static final String COMMUNITY_COMMENT_COUNT = "com.yurun.jiarun.community_comment";
    
    /**
     * 表扬数量+1
     */
    public static final String COMMUNITY_AGREE_COUNT = "com.yurun.jiarun.community_agree";
    
    /**
     * 反对数量+1
     */
    public static final String COMMUNITY_DISAGREE_COUNT = "com.yurun.jiarun.community_disagree";
    
    /**
     * 物业未读消息数量
     */
    public static final String COMMUNITY_MESSAGE_NUMBER_BROADCAST = "com.yurun.jiarun.login.community_message_number";
    
    /**
     * 发送本地图片
     */
    public static final int CHOISE_MORE_PHOTO_110 = 110;
    
    /**
     * 发送本地图片
     */
    public static final int CHOISE_MORE_PHOTO_111 = 111;
    
    /**
     * 发送本地图片
     */
    public static final int CHOISE_MORE_PHOTO_112 = 112;
    
    /**
     * 通告分享
     */
    public static final int ADVERTISE_SHARE = 113;
    
    /**
     * 话题分享
     */
    public static final int TOPOC_SHARE = 114;
    
    /**
     * 报修;不满意
     */
    public static final String PROPERTY_REPAIR = "1";
    
    /**
     * 投诉;一般
     */
    public static final String PROPERTY_COMPLAINT = "2";
    
    /**
     * 表扬;满意
     */
    public static final String PROPERTY_PRAISE = "3";
    
    /**
     * 求助
     */
    public static final String PROPERTY_HELP = "4";
    
    /**
     * 建议
     */
    public static final String PROPERTY_SUGGEST = "5";
    
    /**
     * 全部邮包
     */
    public static final String DELIVERTY_ALL = "0";
    
    /**
     * 待领取邮包;待处理
     */
    public static final String NO_TAKE_DELIVERTY = "1";
    
    /**
     * 已领取邮包;处理中
     */
    public static final String HAS_TAKE_DELIVERTY = "2";
    
    /**
     * 已缴费;已处理
     */
    public static final String HAVE_TO_PAY = "3";
    
    /**
     * 已完成
     */
    public static final String OVER = "4";
    
    /**
     * 不包含图片
     */
    public static final String NO_IMAGE_FLAG = "0";
    
    /**
     * 包含图片
     */
    public static final String HAVA_IMAGE_FLAG = "1";
    
    /**
     * 友盟
     */
    public static final String DESCRIPTOR = "com.umeng.share";
    
    /**
     *  微信分享app和appKey
     */
    public static final String WEIXINID = "wxd49ea46006558fc8";
    
    public static final String WEIXINKEY = "7a00aa5e164b138ae64012f3b66cfd3d";
    
    /**
     * QQ的appId
     */
    public static final String QQ_APP_ID = "1103559554";
    
    public static final String QQ_APP_KEY = "MV3lqRHkgo1rVZH7";
    
    /**
     * 人人网的appKey
     */
    public static final String RENREN_APP_ID = "201874";
    
    public static final String RENREN_APP_KEY = "28401c0964f04a72a14c812d6132fcef";
    
    public static final String RENREN_APP_SECRET = "3bf66e42db1e4fa9829b955cc300b737";
}
