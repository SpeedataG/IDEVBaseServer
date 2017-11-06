package com.cepri.server;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.cepri.service.ApnInfo;
import com.cepri.service.DEVBaseServer;
import com.cepri.service.IDEVBaseServer;

import java.util.List;

/**
 * @author :Reginer in  2017/11/2 10:56.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class DevService extends Service {
    private DEVBaseServer mService;

    public DevService() {
        mService = DEVBaseServer.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IDEVBaseServer.Stub stub = new IDEVBaseServer.Stub() {
        @Override
        public boolean setDateTime(String dateTime) throws RemoteException {
            return mService.setDateTime("2016-10-28 16:34:45");
        }

        @Override
        public boolean addApn(ApnInfo apnInfo) throws RemoteException {
            return mService.addApn(apnInfo);
        }

        @Override
        public int createApn(ContentValues values) throws RemoteException {
            return mService.createApn(values);
        }

        @Override
        public boolean setDefaultApn(int apnId) throws RemoteException {
            return mService.setDefaultApn(apnId);
        }

        @Override
        public int getApnId(String apnName) throws RemoteException {
            return mService.getApnId(apnName);
        }

        @Override
        public boolean deleteApnById(int apnId) throws RemoteException {
            return mService.deleteApnById(apnId);
        }

        @Override
        public boolean deleteApn(String apnName) throws RemoteException {
            return mService.deleteApn(apnName);
        }

        @Override
        public ApnInfo getCurrApn() throws RemoteException {
            return mService.getCurrApn();
        }

        @Override
        public boolean setCurrApn(String apnName) throws RemoteException {
            return mService.setCurrApn(apnName);
        }

        @Override
        public List<ApnInfo> getAllApn() throws RemoteException {
            return mService.getAllApn();
        }

        @Override
        public String getDeviceModel() throws RemoteException {
            return mService.getDeviceModel();
        }

        @Override
        public String getDeviceSn() throws RemoteException {
            return mService.getDeviceSn();
        }

        @Override
        public String getOSVersionCode() throws RemoteException {
            return mService.getOSVersionCode();
        }

        @Override
        public String getDeviceHardVersionCode() throws RemoteException {
            return mService.getDeviceHardVersionCode();
        }

        @Override
        public String getProductionDate() throws RemoteException {
            return mService.getProductionDate();
        }

        @Override
        public boolean setUSBMode(int mode) throws RemoteException {
            return mService.setUSBMode(mode);
        }

        @Override
        public int getUSBMode() throws RemoteException {
            return mService.getUSBMode();
        }

        @Override
        public boolean OSUpdate(String filePath) throws RemoteException {
            return mService.OSUpdate(filePath);
        }

        @Override
        public int getOSVerifyPercent() throws RemoteException {
            return mService.getOSVerifyPercent();
        }

        @Override
        public String getServiceVersionCode() throws RemoteException {
            return mService.getServiceVersionCode();
        }

        @Override
        public boolean setHomeKeyEnabled(boolean status) throws RemoteException {
            return mService.setHomeKeyEnabled(status);
        }

        @Override
        public boolean getHomeKeyEnabled() throws RemoteException {
            return mService.getHomeKeyEnabled();
        }

        @Override
        public boolean setStatusBarPullEnabled(boolean status) throws RemoteException {
            return mService.setStatusBarPullEnabled(status);
        }
    };

}
