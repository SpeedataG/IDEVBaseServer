package com.cepri.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cepri.device.utils.SecurityUnit;

/**
 * @author :Reginer in  2017/11/7 16:19.
 *         联系方式:QQ:282921012
 *         功能描述:测试native功能
 */
public class NativeActivity extends AppCompatActivity implements View.OnClickListener {
    SecurityUnit mSecurityUnit = new SecurityUnit();
    private static final String TAG = "Reginer";
    private TextView mResult;

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
        mResult = findViewById(R.id.result);
    }

    @SuppressLint("SetTextI18n")
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
                Log.d(TAG, String.valueOf(mSecurityUnit.Config(115200, 8, 0, 1, 1)));
                mResult.setText("设置的参数为:115200, 8, 0, 1, 1");
                break;
            case R.id.send:
                byte[] t1 = new byte[]{(byte) 0xE9, 0x00, 0x02, 0x00, 0x01, (byte) 0xEC, (byte) 0xE6};
                StringBuilder textSend = new StringBuilder();
                for (byte aT1 : t1) {
                    textSend.append(String.format("%02x ", aT1));
                }
                mResult.setText("发送的数据为:" + textSend);
                Log.d(TAG, String.valueOf(mSecurityUnit.SendData(t1, 0, 7)));
                break;
            case R.id.recData:
                byte[] aa = new byte[255];
                int length = (mSecurityUnit.RecvData(aa, 0, 255));
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    text.append(String.format("%02x ", aa[i]));
                }
                mResult.setText("接收的数据为:" + text);
                break;
            case R.id.setTimeOut:
                Log.d(TAG, String.valueOf(mSecurityUnit.SetTimeOut(0, 800)));
                Log.d(TAG, String.valueOf(mSecurityUnit.SetTimeOut(1, 800)));
                break;
            default:
                break;
        }
    }
}
