package com.cepri.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

import cepri.device.utils.SecurityUnit;

/**
 * @author :Reginer in  2017/11/7 16:19.
 *         联系方式:QQ:282921012
 *         功能描述:测试native功能
 */
public class NativeActivity extends AppCompatActivity implements View.OnClickListener {
    SecurityUnit mSecurityUnit = new SecurityUnit();
    private static final String TAG = "Reginer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        initView();
    }

    private void initView() {
        findViewById(R.id.init).setOnClickListener(this);
        findViewById(R.id.de_init).setOnClickListener(this);
        findViewById(R.id.clearSendCache).setOnClickListener(this);
        findViewById(R.id.clearRecvCache).setOnClickListener(this);
        findViewById(R.id.config).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        findViewById(R.id.recData).setOnClickListener(this);
        findViewById(R.id.setTimeOut).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init:
                Log.d(TAG, String.valueOf(mSecurityUnit.Init()));
                break;
            case R.id.de_init:
                Log.d(TAG, String.valueOf(mSecurityUnit.DeInit()));
                break;
            case R.id.clearSendCache:
                Log.d(TAG, String.valueOf(mSecurityUnit.ClearSendCache()));
                break;
            case R.id.clearRecvCache:
                Log.d(TAG, String.valueOf(mSecurityUnit.ClearRecvCache()));
                break;
            case R.id.config:
                Log.d(TAG, String.valueOf(mSecurityUnit.Config(9600, 8, 2, 1, 0)));
                break;
            case R.id.send:
                Log.d(TAG, String.valueOf(mSecurityUnit.SendData(new byte[]{0x16, 0x52}, 0, 2)));
                break;
            case R.id.recData:
                byte[] aa = new byte[2];
                Log.d(TAG, String.valueOf(mSecurityUnit.RecvData(aa, 0, 2)));
                Log.d(TAG, "recData: "+ Arrays.toString(aa));
                break;
            case R.id.setTimeOut:
                Log.d(TAG, String.valueOf(mSecurityUnit.SetTimeOut(0, 200)));
                Log.d(TAG, String.valueOf(mSecurityUnit.SetTimeOut(1, 200)));
                break;
            default:
                break;
        }
    }
}
