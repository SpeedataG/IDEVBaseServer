package com.cepri.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cepri.service.ApnInfo;
import com.cepri.service.IDEVBaseServer;

/**
 * @author :Reginer in  2017/11/2 11:52.
 *         联系方式:QQ:282921012
 *         功能描述:client
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private IDEVBaseServer iServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_connect).setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                bindService(new Intent("com.cepri.use"), serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_set_time:
                try {
                    Log.d(TAG, String.valueOf(iServer.setDateTime("2016-10-28 16:34:45")));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_add_apn:
                ApnInfo apnInfo = new ApnInfo();
                apnInfo.setApn("apn");
                apnInfo.setApnName("apnName");
                try {
                    Log.d(TAG, String.valueOf(iServer.addApn(apnInfo)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_delete_apn:
                try {
                    Log.d(TAG, String.valueOf(iServer.deleteApn("apnName")));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_current_apn:
                try {
                    Log.d(TAG, iServer.getCurrApn().getApnName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_set_default_apn:
                try {
                    Log.d(TAG, String.valueOf(iServer.setCurrApn("apnName")));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_all_apn:
                try {
                    Log.d(TAG, "apnInfoS size :" + String.valueOf(iServer.getAllApn() == null ? 0 : iServer.getAllApn().size()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_device_model:
                try {
                    Log.d(TAG, iServer.getDeviceModel());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_device_sn:
                try {
                    Log.d(TAG, iServer.getDeviceSn());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_version_name:
                try {
                    Log.d(TAG, iServer.getOSVersionCode());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_hardware_name:
                try {
                    Log.d(TAG, iServer.getDeviceHardVersionCode());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_production_date:
                try {
                    Log.d(TAG, iServer.getProductionDate());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_set_usb_mode:
                try {
                    Log.d(TAG, String.valueOf(iServer.setUSBMode(0x0001)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_usb_mode:
                try {
                    Log.d(TAG, String.valueOf(iServer.getUSBMode()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_os_update:
                try {
                    Log.d(TAG, String.valueOf(iServer.OSUpdate("这里填路径")));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_os_verify_percent:
                try {
                    Log.d(TAG, String.valueOf(iServer.getOSVerifyPercent()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_service_version_name:
                try {
                    Log.d(TAG, String.valueOf(iServer.getServiceVersionCode()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_set_home_enable:
                try {
                    Log.d(TAG, String.valueOf(iServer.setHomeKeyEnabled(false)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_get_home_enable:
                try {
                    Log.d(TAG, String.valueOf(iServer.getHomeKeyEnabled()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_set_status_bar_enable:
                try {
                    Log.d(TAG, String.valueOf(iServer.setStatusBarPullEnabled(false)));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iServer = null;
            Log.d(TAG, "连接断开了");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iServer = IDEVBaseServer.Stub.asInterface(service);
            Log.d(TAG, "连接成功了");
        }
    };
}
