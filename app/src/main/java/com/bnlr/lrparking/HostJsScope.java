package com.bnlr.lrparking;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bnlr.lrparking.bean.WXOrderBean;
import com.bnlr.lrparking.constant.AppConstant;
import com.bnlr.lrparking.util.WXPayUtils;
import com.bnlr.lrparking.util.XmlUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;

/**
 * HostJsScope中需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
 */

public class HostJsScope {


    /**
     * 微信登录
     */
    public static void doWXLogin() {
        LogUtils.i("doWXLogin");
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!MyApp.mWxApi.isWXAppInstalled()) {
            LogUtils.i("isWXAppInstalled");
            ToastUtils.showShort("您还未安装微信客户端");
            return;
        }
        //微信登录
        SendAuth.Req req = new SendAuth.Req();
        req.scope = AppConstant.WX.WX_SCOPE;
        req.state = AppConstant.WX.WX_STATE;
        //向微信发送请求
        MyApp.mWxApi.sendReq(req);
    }


    /**
     * 微信支付
     * 使用的是微信官方demo中的服务器签名
     */
    public static void doWXPayWithServerSign() {
        LogUtils.i("doWXPay");
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!MyApp.mWxApi.isWXAppInstalled()) {
            LogUtils.i("isWXAppInstalled");
            ToastUtils.showShort("您还未安装微信客户端");
            return;
        }

        // 微信支付
        // 微信demo中给的服务器订单地址
        String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        ToastUtils.showShort("获取订单中...");
        OkGo.<String>get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        WXOrderBean wxOrderBean = gson.fromJson(response.body(), WXOrderBean.class);
                        PayReq payReq = new PayReq();
                        payReq.appId = wxOrderBean.getAppid();
                        payReq.partnerId = wxOrderBean.getPartnerid();
                        payReq.prepayId = wxOrderBean.getPrepayid();
                        payReq.packageValue = wxOrderBean.getPackageX();
                        payReq.nonceStr = wxOrderBean.getNoncestr();
                        payReq.timeStamp = wxOrderBean.getTimestamp();
                        payReq.sign = wxOrderBean.getSign();

                        MyApp.mWxApi.sendReq(payReq);

                    }
                });
    }


    /**
     * 微信支付
     * 使用自身签名生成订单
     */
    public static void doWXPayWithAppSign() {
        LogUtils.i("doWXPayWithAppSign");
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (!MyApp.mWxApi.isWXAppInstalled()) {
            LogUtils.i("isWXAppInstalled");
            ToastUtils.showShort("您还未安装微信客户端");
            return;
        }

        // 生成预支付订单
        HashMap<String, String> map = new HashMap<>();
        map.put("appid", AppConstant.WX.APP_ID);
        map.put("mch_id", AppConstant.WX.MCH_ID);
        map.put("nonce_str", WXPayUtils.genNonceStr());
        map.put("body", "利人停车--贡献值充值");
        map.put("out_trade_no", "201508061253878165614");
        map.put("total_fee", "1");
        map.put("spbill_create_ip", "42.236.200.212");
        map.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
        map.put("trade_type", "APP");

        String sign = null;
        try {
            sign = WXPayUtils.generateSignature(map, AppConstant.WX.API_KEY);
            map.put("sign", sign);
        } catch (Exception e) {
            LogUtils.e("签名生成错误！ ", e.getMessage());
        }

        String xml = null;
        try {
            xml = XmlUtil.mapToXml(map);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("xml参数转换错误！ ", e.getMessage());
        }

        OkGo.<String>post(AppConstant.WX.UNIFIED_ORDER)
                .upString(xml)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.i("response", response.body());
                        String data = response.body();
                        data = data.replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "");
                        LogUtils.i("data",data);
                        HashMap<String, String> hashMap = null;
                        try {
                            hashMap = (HashMap<String, String>) XmlUtil.xmlToMap(data);
                            LogUtils.i(hashMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        PayReq payReq = new PayReq();
                        payReq.appId = AppConstant.WX.APP_ID;
                        payReq.partnerId = AppConstant.WX.MCH_ID;
                        payReq.prepayId = hashMap.get("prepay_id");
                        payReq.packageValue = AppConstant.WX.WX_KEY_PACKAGEVALUE;
                        payReq.nonceStr = hashMap.get("nonce_str");
                        payReq.timeStamp = String.valueOf(WXPayUtils.genTimeStamp());

                        HashMap<String, String> value = new HashMap<>();
                        value.put("appid", payReq.appId);
                        value.put("partnerid", payReq.partnerId);
                        value.put("prepayid", payReq.prepayId);
                        value.put("package", payReq.packageValue);
                        value.put("noncestr", payReq.nonceStr);
                        value.put("timestamp", payReq.timeStamp);

                        try {
                            payReq.sign = WXPayUtils.generateSignature(value, AppConstant.WX.API_KEY);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LogUtils.i("payReq",payReq.toString());
                        MyApp.mWxApi.sendReq(payReq);

                    }
                });


    }
}