package com.bnlr.lrparking.util;

import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bnlr.lrparking.MyApp;
import com.bnlr.lrparking.bean.OrederSendInfo;
import com.bnlr.lrparking.constant.AppConstant;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * description: TODO
 * WXPayUtils class
 *
 * @author : Licy
 * @date : 2018/12/18
 */
public class WXPayUtils {

    private static IWXAPI iwxapi;

    public static IWXAPI getWXAPI(){
        iwxapi = MyApp.mWxApi;
        return iwxapi;
    }

    //生成随机字符串
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    //获得时间戳
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(AppConstant.WX.FIELD_SIGN)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }

        }
        sb.append("key=").append(key);

        return MD5(sb.toString()).toUpperCase();
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        if (!data.containsKey(AppConstant.WX.FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(AppConstant.WX.FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }


}
