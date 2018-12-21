package com.bnlr.lrparking.bean;

import com.google.gson.annotations.SerializedName;

/**
 * description: TODO
 * WXOrderBean class
 *
 * @author : Licy
 * @date : 2018/12/18
 */
public class WXOrderBean {


    /**
     * appid : wxb4ba3c02aa476ea1
     * partnerid : 1900006771
     * package : Sign=WXPay
     * noncestr : e834121bca48ec25337068c624d31bd9
     * timestamp : 1545122577
     * prepayid : wx18164257063628bdb32c64ba4327748632
     * sign : 5CCF59C1FB3CEB4613B5F3ADE513E384
     */

    private String appid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String prepayid;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
