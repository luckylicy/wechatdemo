package com.bnlr.lrparking;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bnlr.lrparking.bean.WXUserInfoBean;
import com.bnlr.lrparking.constant.AppConstant;
import com.bnlr.lrparking.event.WXUserInfoEvent;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.button3)
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

//        onGetWXUserInfo(new WXUserInfoEvent());

        WebSettings webSettings = webview.getSettings();

        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        webSettings.setJavaScriptEnabled(true);
        // 允许网页弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webview.loadUrl("file:///android_asset/javascript.html");

        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());

    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            LogUtils.i("拦截到事件！");
            Uri uri = Uri.parse(message);
            LogUtils.i("uri" + uri.toString());
            if ("js".equals(uri.getScheme())) {
                LogUtils.i("第一层 js");
                if ("webview".equals(uri.getAuthority())) {
                    LogUtils.i("第二层 webview");
                    if ("WXLogin".equals(uri.getQueryParameter("arg1"))) {
                        LogUtils.i("第三层 WXLogin");
                        ToastUtils.showShort("开始微信登录");
                        HostJsScope.doWXLogin();
                    } else if ("WXPay".equals(uri.getQueryParameter("arg1"))) {
                        LogUtils.i("第三层 WXPay");
                        ToastUtils.showShort("开始微信支付");
                        HostJsScope.doWXPayWithServerSign();
                    }
                }
            }

            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

            Uri uri = Uri.parse(message);
            if ("js".equals(uri.getScheme())) {
                if ("webview".equals(uri.getAuthority())) {
                    if ("WXLogin".equals(uri.getQueryParameter("arg1"))) {
                        ToastUtils.showShort("开始微信登录");
                        HostJsScope.doWXLogin();
                    }
                }
            }

            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }


    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if ("js".equals(uri.getScheme())) {
                if ("webview".equals(uri.getAuthority())) {
                    if ("WXLogin".equals(uri.getQueryParameter("arg1"))) {
                        ToastUtils.showShort("开始微信登录");
                        HostJsScope.doWXLogin();
                    }
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @OnClick({R.id.button, R.id.button2, R.id.button3})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                HostJsScope.doWXLogin();
                break;
            case R.id.button2:
                HostJsScope.doWXPayWithServerSign();
                break;
            case R.id.button3:
                HostJsScope.doWXPayWithAppSign();
                break;
            default:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetWXUserInfo(WXUserInfoEvent event) {
        WXUserInfoBean wxUserInfoBean = new WXUserInfoBean();
        wxUserInfoBean.setCountry(SPUtils.getInstance(AppConstant.SP.SP_NAME).getString(AppConstant.SP.SP_KEY_COUNTRY));
        wxUserInfoBean.setProvince(SPUtils.getInstance(AppConstant.SP.SP_NAME).getString(AppConstant.SP.SP_KEY_PROVINCE));
        wxUserInfoBean.setCity(SPUtils.getInstance(AppConstant.SP.SP_NAME).getString(AppConstant.SP.SP_KEY_CITY));
        wxUserInfoBean.setNickname(SPUtils.getInstance(AppConstant.SP.SP_NAME).getString(AppConstant.SP.SP_KEY_NICKNAME));
        wxUserInfoBean.setSex(SPUtils.getInstance(AppConstant.SP.SP_NAME).getInt(AppConstant.SP.SP_KEY_SEX));
        wxUserInfoBean.setHeadimgurl(SPUtils.getInstance(AppConstant.SP.SP_NAME).getString(AppConstant.SP.SP_KEY_HEADIMGURL));
        textView.setText(wxUserInfoBean.toString());

        Glide.with(this).load(wxUserInfoBean.getHeadimgurl()).into(image);
    }


}
