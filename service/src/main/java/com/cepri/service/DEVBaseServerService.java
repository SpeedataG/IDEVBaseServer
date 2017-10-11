package com.cepri.service;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Telephony;
import android.support.annotation.NonNull;

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
        contentValues.put(Telephony.Carriers.NAME, apnInfo.getApnName());
        contentValues.put(Telephony.Carriers.APN, apnInfo.getApn());
        contentValues.put(Telephony.Carriers.USER, apnInfo.getUserName());
        contentValues.put(Telephony.Carriers.PASSWORD, apnInfo.getPassword());
        contentValues.put(Telephony.Carriers.PROXY, apnInfo.getProxy());
        contentValues.put(Telephony.Carriers.PORT, apnInfo.getPort());
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
    public boolean deleteApn(@NonNull String apnName) throws RemoteException {
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
            apnName = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.NAME));
            apn = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.APN));
            username = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.USER));
            password = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PASSWORD));
            proxy = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PROXY));
            port = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PORT));
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
            String apnName = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.NAME));
            String apn = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.APN));
            String username = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.USER));
            String password = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PASSWORD));
            String proxy = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PROXY));
            String port = mCursor.getString(mCursor.getColumnIndex(Telephony.Carriers.PORT));
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
        return null;
    }

    @Override
    public String getDeviceSn() throws RemoteException {
        return null;
    }

    @Override
    public String getOSVersionCode() throws RemoteException {
        return null;
    }

    @Override
    public String getDeviceHardVersionCode() throws RemoteException {
        return null;
    }

    @Override
    public String getProductionDate() throws RemoteException {
        return null;
    }

    @Override
    public boolean setUSBMode(int mode) throws RemoteException {
        return false;
    }

    @Override
    public int getUSBMode() throws RemoteException {
        return 0;
    }

    @Override
    public boolean OSUpdate(String filePath) throws RemoteException {
        return false;
    }

    @Override
    public int getOSVerifyPercent() throws RemoteException {
        return 0;
    }

    @Override
    public String getServiceVersionCode() throws RemoteException {
        return null;
    }

    @Override
    public boolean setHomeKeyEnabled(boolean status) throws RemoteException {
        return false;
    }

    @Override
    public boolean getHomeKeyEnabled() throws RemoteException {
        return false;
    }

    @Override
    public boolean setStatusBarPullEnabled(boolean status) throws RemoteException {
        return false;
    }

}
