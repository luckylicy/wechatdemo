package com.bnlr.lrparking.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bnlr.lrparking.MyApp;
import com.bnlr.lrparking.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @BindView(R.id.button4)
    Button button4;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wxpay);
        ButterKnife.bind(this);

        api = MyApp.mWxApi;
        //这句没有写,是不能执行回调的方法的
        api.handleIntent(getIntent(), this);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        LogUtils.i("resp", resp);
        LogUtils.i("resp.errCode", resp.errCode);
        switch (resp.errCode) {
            // 支付成功
            case 0:
                LogUtils.i("成功  展示成功页面");
                ToastUtils.showShort("成功  展示成功页面");
                break;
            // 错误
            case -1:
                LogUtils.i("可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。");
                ToastUtils.showShort("可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。");
                finish();
                break;
            // 用户取消
            case -2:
            default:
                LogUtils.i("无需处理。发生场景：用户不支付了，点击取消，返回APP。");
                ToastUtils.showShort("无需处理。发生场景：用户不支付了，点击取消，返回APP。");
                finish();
                break;
        }

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("支付结果是" + String.valueOf(resp.errCode));
            builder.show();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }
}