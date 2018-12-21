package com.bnlr.lrparking.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bnlr.lrparking.constant.AppConstant;
import com.bnlr.lrparking.MyApp;
import com.bnlr.lrparking.bean.WXTokenResponse;
import com.bnlr.lrparking.bean.WXUserInfoBean;
import com.bnlr.lrparking.bean.WXUserResponse;
import com.bnlr.lrparking.event.WXUserInfoEvent;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * description: TODO
 * WXEntryActivity class
 *
 * @author : Licy
 * @date : 2018/12/18
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {



    private static final String TAG = WXEntryActivity.class.getSimpleName();

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //这句没有写,是不能执行回调的方法的
        MyApp.mWxApi.handleIntent(getIntent(), this);

        wxUserInfoBean = new WXUserInfoBean();
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     *
     * @param baseReq
     */
    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * app发送消息给微信，处理返回消息的回调
     *
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {

        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + baseResp.errCode);
        //类型：分享还是登录
        int type = baseResp.getType();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ToastUtils.showShort("拒绝授权微信登录");
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == AppConstant.WX.RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == AppConstant.WX.RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                ToastUtils.showShort(message);
                // 跳转回登录页面
                finish();
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == AppConstant.WX.RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.i(TAG, "code:------>" + code);

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    getAccessToken(code);

                } else if (type == AppConstant.WX.RETURN_MSG_TYPE_SHARE) {
                    ToastUtils.showShort("微信分享成功");
                }
                break;
            default:
                break;
        }

    }

    private WXTokenResponse wxTokenResponse;

    /**
     * 获取access_token
     *
     * @param code 用户或取access_token的code，仅在ErrCode为0时有效
     */
    private void getAccessToken(final String code) {
        Map<String, String> params = new HashMap<>(4);
        params.put(AppConstant.WX.WX_KEY_APPID, AppConstant.WX.APP_ID);
        params.put(AppConstant.WX.WX_KEY_SECRET, AppConstant.WX.APP_SERECET);
        params.put(AppConstant.WX.WX_KEY_CODE, code);
        params.put(AppConstant.WX.WX_KEY_GRANT_TYPE, AppConstant.WX.WX_VALUE_GRANT_TYPE);

        OkGo.<String>get(AppConstant.AppUrls.WX_ACCESS_TOKEN)
                .params(params)
                .execute(new StringCallback() {

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {

                        Gson gson = new Gson();
                        wxTokenResponse = gson.fromJson(response.body(), WXTokenResponse.class);

                        Log.i(TAG, wxTokenResponse.toString());

                        getWXUserInfo(wxTokenResponse.access_token, wxTokenResponse.openid, wxTokenResponse.unionid);
                    }
                });

//
//        HttpUtils.getWXAccessTokenBean(URLConstant.URL_WX_BASE, params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<WXAccessTokenBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i(TAG, "onCompleted:-------->");
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.i(TAG, "onError:-------->" + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(WXAccessTokenBean wxAccessTokenBean) {
//                        Log.i(TAG, "onNext: ----->");
//                        String access_token = wxAccessTokenBean.getAccess_token(); //接口调用凭证
//                        String openid = wxAccessTokenBean.getOpenid(); //授权用户唯一标识
//                        //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
//                        String unionid = wxAccessTokenBean.getUnionid();
//                        Log.i(TAG, "access_token:----->" + access_token);
//                        Log.i(TAG, "openid:----->" + openid);
//                        Log.i(TAG, "unionid:----->" + unionid);
//                        getWXUserInfo(access_token, openid, unionid);
//                    }
//                });
    }


    private WXUserInfoBean wxUserInfoBean;

    /**
     * 获取微信登录，用户授权后的个人信息
     *
     * @param accessToken
     * @param openid
     * @param unionid
     */
    private void getWXUserInfo(final String accessToken, final String openid, final String unionid) {

        OkGo.<String>get(AppConstant.AppUrls.WX_USERINFO)
                .params("access_token", accessToken)
                .params("openid", openid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        WXUserResponse wxUserResponse = gson.fromJson(response.body(), WXUserResponse.class);

                        wxUserInfoBean.setCountry(wxUserResponse.country);
                        wxUserInfoBean.setProvince(wxUserResponse.province);
                        wxUserInfoBean.setCity(wxUserResponse.city);
                        wxUserInfoBean.setNickname(wxUserResponse.nickname);
                        wxUserInfoBean.setSex(wxUserResponse.sex);
                        wxUserInfoBean.setHeadimgurl(wxUserResponse.headimgurl);

                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_COUNTRY, wxUserResponse.country);
                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_PROVINCE, wxUserResponse.province);
                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_CITY, wxUserResponse.city);
                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_NICKNAME, wxUserResponse.nickname);
                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_SEX, wxUserResponse.sex);
                        SPUtils.getInstance(AppConstant.SP.SP_NAME).put(AppConstant.SP.SP_KEY_HEADIMGURL, wxUserResponse.headimgurl);

                        Log.i(TAG, wxUserInfoBean.toString());

                        EventBus.getDefault().post(new WXUserInfoEvent());
                        finish();

                        // TODO 待完善 跳转回登录页面
//                        Intent intent = new Intent(mContext, LoginActivity.class);
//                        intent.putExtra("wxUser", wxUserInfoBean);
//                        startActivity(intent);
//                        finish();


                    }
                });


//
//        HttpUtils.getWXUserInfoBean(URLConstant.URL_WX_BASE, params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<WXUserInfoBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i(TAG, "getWXUserInfo:--------> onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.i(TAG, "getWXUserInfo:--------> onError" + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(WXUserInfoBean wxUserInfoBean) {
//                        Log.i(TAG, "getWXUserInfo:--------> onNext");
//                        String country = wxUserInfoBean.getCountry(); //国家
//                        String province = wxUserInfoBean.getProvince(); //省
//                        String city = wxUserInfoBean.getCity(); //市
//                        String nickname = wxUserInfoBean.getNickname(); //用户名
//                        int sex = wxUserInfoBean.getSex(); //性别
//                        String headimgurl = wxUserInfoBean.getHeadimgurl(); //头像url
//                        Log.i(TAG, "country:-------->" + country);
//                        Log.i(TAG, "province:-------->" + province);
//                        Log.i(TAG, "city:-------->" + city);
//                        Log.i(TAG, "nickname:-------->" + nickname);
//                        Log.i(TAG, "sex:-------->" + sex);
//                        Log.i(TAG, "headimgurl:-------->" + headimgurl);
//                    }
//                });
    }



}
