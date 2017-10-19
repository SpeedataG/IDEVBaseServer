package com.cepri.service;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.app.StatusBarManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author :Reginer in  2017/10/10 14:13.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class DEVBaseServerService extends IDEVBaseServer.Stub {

    // APN列表资源
    private static final Uri APN_LIST_URI = Uri.parse("content://telephony/carriers");

    // 默认APN资源
    private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    private Context mContext;

    private String filePath;

    private static final String ENABLE_HOME = "widget_enable_home";

    public DEVBaseServerService(Context context) {
        mContext = context;
    }

    @Override
    public boolean setDateTime(String dateTime) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            long time = dateFormat.parse(dateTime).getTime();
            SystemClock.setCurrentTimeMillis(time);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addApn(ApnInfo apnInfo) throws RemoteException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", apnInfo.getApnName());
        contentValues.put("apn", apnInfo.getApn());
        contentValues.put("user", apnInfo.getUserName());
        contentValues.put("password", apnInfo.getPassword());
        contentValues.put("proxy", apnInfo.getProxy());
        contentValues.put("port", apnInfo.getPort());
        return setDefaultApn(createApn(contentValues));
    }

    @Override
    public int createApn(ContentValues values) throws RemoteException {
        int apnId = -1;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = null;
        try {
            Uri newRow = resolver.insert(APN_LIST_URI, values);
            if (newRow != null) {
                c = resolver.query(newRow, null, null, null, null);
                int index;
                if (c != null) {
                    index = c.getColumnIndex("_id");
                    c.moveToFirst();
                    apnId = c.getShort(index);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (c != null)
            c.close();
        return apnId;
    }

    @Override
    public boolean setDefaultApn(int apnId) throws RemoteException {
        boolean res = false;
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("apn_id", apnId);
        try {
            resolver.update(PREFERRED_APN_URI, values, null, null);
            Cursor c = resolver.query(PREFERRED_APN_URI, new String[]{"name",
                    "apn"}, "_id=" + apnId, null, null);
            if (c != null) {
                res = true;
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int getApnId(String apnName) throws RemoteException {
        int apnId = -1;
        Cursor mCursor = mContext.getContentResolver().query(APN_LIST_URI, null,
                "apn like '%hnydz.ha%'", null, null);
        while (mCursor != null && mCursor.moveToNext()) {
            apnId = mCursor.getShort(mCursor.getColumnIndex("_id"));
            String name = mCursor.getString(mCursor.getColumnIndex("name"));
            if (apnName.equals(name)) {
                return apnId;
            } else {
                apnId = -1;
            }
        }
        if (mCursor != null) {
            mCursor.close();
        }
        return apnId;
    }

    @Override
    public boolean deleteApnById(int apnId) throws RemoteException {
        if (apnId == -1) {
            return false;
        }
        ContentResolver resolver = mContext.getContentResolver();
        Uri deleteIdUri = ContentUris.withAppendedId(APN_LIST_URI, apnId);
        try {
            resolver.delete(deleteIdUri, null, null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean deleteApn(String apnName) throws RemoteException {
        return deleteApnById(getApnId(apnName));
    }

    @Override
    public ApnInfo getCurrApn() throws RemoteException {
        ApnInfo apnInfo = new ApnInfo();
        Cursor mCursor = mContext.getContentResolver().query(PREFERRED_APN_URI,
                null, null, null, null);
        if (mCursor == null) {
            return null;
        }
        String apnName = "";
        String apn = "";
        String username = "";
        String password = "";
        String proxy = "";
        String port = "";
        while (mCursor.moveToNext()) {
            apnName = mCursor.getString(mCursor.getColumnIndex("name"));
            apn = mCursor.getString(mCursor.getColumnIndex("apn"));
            username = mCursor.getString(mCursor.getColumnIndex("user"));
            password = mCursor.getString(mCursor.getColumnIndex("password"));
            proxy = mCursor.getString(mCursor.getColumnIndex("proxy"));
            port = mCursor.getString(mCursor.getColumnIndex("port"));
        }
        mCursor.close();
        apnInfo.setApnName(apnName);
        apnInfo.setApn(apn);
        apnInfo.setUserName(username);
        apnInfo.setPassword(password);
        apnInfo.setProxy(proxy);
        apnInfo.setPort(port);
        return apnInfo;
    }

    @Override
    public boolean setCurrApn(String apnName) throws RemoteException {
        return setDefaultApn(getApnId(apnName));
    }

    @Override
    public List<ApnInfo> getAllApn() throws RemoteException {
        Cursor mCursor = mContext.getContentResolver().query(APN_LIST_URI, null,
                null, null, null);
        List<ApnInfo> apnInfoList = new ArrayList<>();
        while (mCursor != null && mCursor.moveToNext()) {
            String apnName = mCursor.getString(mCursor.getColumnIndex("name"));
            String apn = mCursor.getString(mCursor.getColumnIndex("apn"));
            String username = mCursor.getString(mCursor.getColumnIndex("user"));
            String password = mCursor.getString(mCursor.getColumnIndex("password"));
            String proxy = mCursor.getString(mCursor.getColumnIndex("proxy"));
            String port = mCursor.getString(mCursor.getColumnIndex("port"));
            ApnInfo apnInfo = new ApnInfo();
            apnInfo.setApnName(apnName);
            apnInfo.setApn(apn);
            apnInfo.setUserName(username);
            apnInfo.setPassword(password);
            apnInfo.setProxy(proxy);
            apnInfo.setPort(port);
            apnInfoList.add(apnInfo);
        }
        if (mCursor != null) {
            mCursor.close();
        }
        if (apnInfoList.size() > 0) {
            return apnInfoList;
        } else {
            return null;
        }
    }

    @Override
    public String getDeviceModel() throws RemoteException {
        return Build.MODEL;
    }

    @Override
    public String getDeviceSn() throws RemoteException {
        return SystemProperties.get("ro.serialno");
    }

    @Override
    public String getOSVersionCode() throws RemoteException {
        return Build.DISPLAY;
    }

    @Override
    public String getDeviceHardVersionCode() throws RemoteException {
        return SystemProperties.get("persist.sys.hardware");;
    }

    @Override
    public String getProductionDate() throws RemoteException {
        return SystemProperties.get("persist.sys.time");;
    }

    @Override
    public boolean setUSBMode(int mode) throws RemoteException {
        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        if (mode == 0) {
            //关闭usb功能
            mUsbManager.setCurrentFunction(UsbManager.USB_FUNCTION_CHARGING_ONLY, true);
            return true;
        }
        if (mode == 0x0001) {
            //打开adb调试
            Settings.Secure.putInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 1);
            return true;
        }
        if (mode == 0x0010) {
            //打开mtp方式
            mUsbManager.setCurrentFunction(UsbManager.USB_FUNCTION_MTP, true);
            return true;
        }
        if (mode == 0x0100) {
            //可以同时开始adb，mtp和大容量存储模式的一种或者几种通信模式
            Settings.Secure.putInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 1);
            mUsbManager.setCurrentFunction(UsbManager.USB_FUNCTION_MTP, true);
            return true;
        }
        return false;
    }

    @Override
    public int getUSBMode() throws RemoteException {
        UsbManager mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        boolean enableAdb = (Settings.Secure.getInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0);
        String function = mUsbManager.getDefaultFunction();
        if (enableAdb && (UsbManager.USB_FUNCTION_MTP.equals(function) ||
                UsbManager.USB_FUNCTION_PTP.equals(function) ||
                UsbManager.USB_FUNCTION_MASS_STORAGE.equals(function)
        )) {
            return 0x0100;
        }
        if (UsbManager.USB_FUNCTION_MTP.equals(function)) {
            return 0x0010;
        }
        if (enableAdb) {
            return 0x0001;
        }
        return 0;
    }

    @Override
    public boolean OSUpdate(String filePath) throws RemoteException {
        this.filePath = filePath;
        try {
            RecoverySystem.installPackage(mContext, new File(filePath));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public int getOSVerifyPercent() throws RemoteException {
        final int[] verifyPercent = new int[1];
        try {
            RecoverySystem.verifyPackage(new File(filePath), new RecoverySystem.ProgressListener() {
                @Override
                public void onProgress(int progress) {
                    verifyPercent[0] = progress;
                }
            }, null);
        } catch (Exception e) {
            return 0;
        }
        return verifyPercent[0];
    }

    @Override
    public String getServiceVersionCode() throws RemoteException {
        return "1.0";
    }

    @Override
    public boolean setHomeKeyEnabled(boolean status) throws RemoteException {
        SystemProperties.set(ENABLE_HOME, String.valueOf(status));
        return true;
    }

    @Override
    public boolean getHomeKeyEnabled() throws RemoteException {
        return "true".equals(SystemProperties.get(ENABLE_HOME));
    }

    @Override
    public boolean setStatusBarPullEnabled(boolean status) throws RemoteException {
        StatusBarManager mStatusBarManager = (StatusBarManager) mContext.getSystemService(Context.STATUS_BAR_SERVICE);
        //禁止下拉mStatusBarManager点disable(StatusBarManager.DISABLE_NONE);
        mStatusBarManager.disable(StatusBarManager.DISABLE_EXPAND);
        return true;
    }

}
