package com.cepri.service;

import android.content.Context;
import android.os.RemoteException;

import java.util.List;

/**
 * @author :Reginer in  2017/10/10 14:13.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class DEVBaseServerService extends IDEVBaseServer.Stub {

    private Context mContext;

    public DEVBaseServerService(Context context) {
        mContext = context;
    }

    @Override
    public boolean setDateTime(String dateTime) throws RemoteException {
        return false;
    }

    @Override
    public boolean addApn(ApnInfo apnInfo) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteApn(String apnName) throws RemoteException {
        return false;
    }

    @Override
    public ApnInfo getCurrApn() throws RemoteException {
        return null;
    }

    @Override
    public boolean setCurrApn(String apnName) throws RemoteException {
        return false;
    }

    @Override
    public List<ApnInfo> getAllApn() throws RemoteException {
        return null;
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
