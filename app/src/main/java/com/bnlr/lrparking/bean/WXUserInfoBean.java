package com.bnlr.lrparking.bean;

import java.io.Serializable;

/**
 * package: com.yiying.xxd.bean
 * filename:
 * author: Licy
 * date: 2018/11/6
 * description: TODO
 */
public class WXUserInfoBean implements Serializable {

    private String country; //国家
    private String province; //省
    private String city; //市
    private String nickname; //用户名
    private int sex; // 性别 1为男性，2为女性
    private String headimgurl; //头像url

    public WXUserInfoBean() {
        this.country = "xxxxxx";
    }

    @Override
    public String toString() {
        return "WXUserInfoBean{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", headimgurl='" + headimgurl + '\'' +
                '}';
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
