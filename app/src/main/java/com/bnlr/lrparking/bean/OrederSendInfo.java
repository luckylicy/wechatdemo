package com.bnlr.lrparking.bean;


/**
 * description: 微信统一下单接口请求bean
 * OrederSendInfo class
 *
 * @author : Licy
 * @date : 2018/12/18
 */
public class OrederSendInfo {


    /**
     * <xml>
     *    <appid>wx2421b1c4370ec43b</appid>
     *    <attach>支付测试</attach>
     *    <body>APP支付测试</body>
     *    <mch_id>10000100</mch_id>
     *    <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
     *    <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
     *    <out_trade_no>1415659990</out_trade_no>
     *    <spbill_create_ip>14.23.150.211</spbill_create_ip>
     *    <total_fee>1</total_fee>
     *    <trade_type>APP</trade_type>
     *    <sign>0CB01533B8C1EF103065174F50BCA001</sign>
     * </xml>
     */

    /**
     * 应用ID
     * 微信开放平台审核通过的应用APPID
     */
    private String appid;
    /**
     * 商户号
     * 微信支付分配的商户号
     */
    private String mch_id;
    /**
     * 随机字符串
     * 随机字符串，不长于32位
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;

    /**
     * 商品描述
     * 商品描述交易字段格式根据不同的应用场景按照以下格式：
     * APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
     */
    private String body;
    /**
     * 商户订单号
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
     */
    private String appout_trade_noid;
    /**
     * 总金额
     * 订单总金额，单位为分
     */
    private String total_fee;

    /**
     * 终端IP
     * 用户端实际ip
     */
    private String spbill_create_ip;
    /**
     * 通知地址
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     */
    private String notify_url;
    /**
     * 交易类型
     * 一般默认 APP
     */
    private String trade_type;

    public OrederSendInfo(String appid, String mch_id, String nonce_str, String body, String appout_trade_noid, String total_fee, String spbill_create_ip, String notify_url, String trade_type) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.body = body;
        this.appout_trade_noid = appout_trade_noid;
        this.total_fee = total_fee;
        this.spbill_create_ip = spbill_create_ip;
        this.notify_url = notify_url;
        this.trade_type = trade_type;
    }

    @Override
    public String toString() {
        return "OrederSendInfo{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", body='" + body + '\'' +
                ", appout_trade_noid='" + appout_trade_noid + '\'' +
                ", total_fee=" + total_fee +
                ", spbill_create_ip='" + spbill_create_ip + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", trade_type='" + trade_type + '\'' +
                '}';
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAppout_trade_noid() {
        return appout_trade_noid;
    }

    public void setAppout_trade_noid(String appout_trade_noid) {
        this.appout_trade_noid = appout_trade_noid;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
