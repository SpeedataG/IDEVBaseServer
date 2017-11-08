package com.cepri.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cepri.service.ApnInfo;
import com.cepri.service.DEVBaseServer;

/**
 * @author :Reginer in  2017/10/20 14:36.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DEVBaseServer mService;
    private static final String TAG = "Reginer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mService = DEVBaseServer.getInstance();
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_set_time).setOnClickListener(this);
        findViewById(R.id.btn_add_apn).setOnClickListener(this);
        findViewById(R.id.btn_delete_apn).setOnClickListener(this);
        findViewById(R.id.btn_get_current_apn).setOnClickListener(this);
        findViewById(R.id.btn_set_default_apn).setOnClickListener(this);
        findViewById(R.id.btn_get_all_apn).setOnClickListener(this);
        findViewById(R.id.btn_get_device_model).setOnClickListener(this);
        findViewById(R.id.btn_get_device_sn).setOnClickListener(this);
        findViewById(R.id.btn_get_version_name).setOnClickListener(this);
        findViewById(R.id.btn_get_hardware_name).setOnClickListener(this);
        findViewById(R.id.btn_get_production_date).setOnClickListener(this);
        findViewById(R.id.btn_set_usb_mode).setOnClickListener(this);
        findViewById(R.id.btn_get_usb_mode).setOnClickListener(this);
        findViewById(R.id.btn_os_update).setOnClickListener(this);
        findViewById(R.id.btn_os_verify_percent).setOnClickListener(this);
        findViewById(R.id.btn_get_service_version_name).setOnClickListener(this);
        findViewById(R.id.btn_set_home_enable).setOnClickListener(this);
        findViewById(R.id.btn_get_home_enable).setOnClickListener(this);
        findViewById(R.id.btn_set_status_bar_enable).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_time:
                Log.d(TAG, String.valueOf(mService.setDateTime("2016-10-28 16:34:45")));
                break;
            case R.id.btn_add_apn:
                ApnInfo apnInfo = new ApnInfo();
                apnInfo.setApn("apn");
                apnInfo.setApnName("apnName");
                Log.d(TAG, String.valueOf(mService.addApn(apnInfo)));
                break;
            case R.id.btn_delete_apn:
                Log.d(TAG, String.valueOf(mService.deleteApn("apnName")));
                break;
            case R.id.btn_get_current_apn:
                Log.d(TAG, mService.getCurrApn().getApnName());
                break;
            case R.id.btn_set_default_apn:
                Log.d(TAG, String.valueOf(mService.setCurrApn("apnName")));
                break;
            case R.id.btn_get_all_apn:
                Log.d(TAG, "apnInfoS size :" + String.valueOf(mService.getAllApn() == null ? 0 : mService.getAllApn().size()));
                break;
            case R.id.btn_get_device_model:
                Log.d(TAG, mService.getDeviceModel());
                break;
            case R.id.btn_get_device_sn:
                Log.d(TAG, mService.getDeviceSn());
                break;
            case R.id.btn_get_version_name:
                Log.d(TAG, mService.getOSVersionCode());
                break;
            case R.id.btn_get_hardware_name:
                Log.d(TAG, mService.getDeviceHardVersionCode());
                break;
            case R.id.btn_get_production_date:
                Log.d(TAG, mService.getProductionDate());
                break;
            case R.id.btn_set_usb_mode:
                Log.d(TAG, String.valueOf(mService.setUSBMode(0x0001)));
                break;
            case R.id.btn_get_usb_mode:
                Log.d(TAG, String.valueOf(mService.getUSBMode()));
                break;
            case R.id.btn_os_update:
                Log.d(TAG, String.valueOf(mService.OSUpdate("这里填路径")));
                break;
            case R.id.btn_os_verify_percent:
                Log.d(TAG, String.valueOf(mService.getOSVerifyPercent()));
                break;
            case R.id.btn_get_service_version_name:
                Log.d(TAG, String.valueOf(mService.getServiceVersionCode()));
                break;
            case R.id.btn_set_home_enable:
                Log.d(TAG, String.valueOf(mService.setHomeKeyEnabled(false)));
                break;
            case R.id.btn_get_home_enable:
                Log.d(TAG, String.valueOf(mService.getHomeKeyEnabled()));
                break;
            case R.id.btn_set_status_bar_enable:
                Log.d(TAG, String.valueOf(mService.setStatusBarPullEnabled(false)));
                break;
            case R.id.btn_start:
                startActivity(new Intent(this, NativeActivity.class));
                break;
            default:
                break;
        }
    }
}
