package com.bnlr.lrparking.constant;

/**
 * description: TODO
 * AppConstant class
 *
 * @author : Licy
 * @date : 2018/12/15
 */
public class AppConstant {

    public static final String Url = "http://zlh.guoxifc.cn/app/index.php?i=2&c=entry&m=ewei_shopv2&do=mobile&r=diypage&id=22";

    public static class AppUrls{

        public static final String WX_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String WX_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    }

    public static class WX {
        /**
         * 1、 登录
         * 2、 分享
         */
        public static final int RETURN_MSG_TYPE_LOGIN = 1;
        public static final int RETURN_MSG_TYPE_SHARE = 2;


        /**
         * 微信统一下单接口
         */
        public static final String UNIFIED_ORDER ="https://api.mch.weixin.qq.com/pay/unifiedorder";

        /**
         * 微信开放平台 申请的唯一的应用id
         */
        public static final String APP_ID = "";
        /**
         *
         */
        public static final String APP_SERECET = "";
        /**
         * 微信支付商户号
         */
        public static final String MCH_ID = "";
        /**
         * 微信支付API密钥
         */
        public static final String API_KEY = "";

        /**
         * 应用授权作用域
         * 取用户个人信息使用 snsapi_userinfo
         */
        public static final String WX_SCOPE = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方。
         * 该参数可用于防止csrf攻击（跨站请求伪造攻击），
         * 建议第三方带上该参数，可设置为简单的随机数加session进行校验
         */
        public static final String WX_STATE = "wechat_sdk_demo";

        /**
         * 微信登录 KEY
         */
        public static final String WX_KEY_APPID = "appid";
        public static final String WX_KEY_SECRET = "secret";
        public static final String WX_KEY_CODE = "code";
        public static final String WX_KEY_GRANT_TYPE = "grant_type";

        /**
         * 微信登录 value
         */
        public static final String WX_VALUE_GRANT_TYPE = "authorization_code";

        public static final String FIELD_SIGN = "sign";
        public static final String FIELD_SIGN_TYPE = "sign_type";

        public static final String WX_KEY_PACKAGEVALUE = "Sign=WXPay";


    }


    public static class SP{

        /**
         * shared preferences name
         */
        public static final String SP_NAME = "userinfo";
        /**
         * 国家
         */
        public static final String SP_KEY_COUNTRY = "country";
        /**
         * 省
         */
        public static final String SP_KEY_PROVINCE = "province";
        /**
         * 市
         */
        public static final String SP_KEY_CITY = "city";
        /**
         * 用户名
         */
        public static final String SP_KEY_NICKNAME = "nickname";
        /**
         * 性别 1为男性，2为女性
         */
        public static final String SP_KEY_SEX = "sex";
        /**
         * 头像url
         */
        public static final String SP_KEY_HEADIMGURL = "headimgurl";

    }

}
