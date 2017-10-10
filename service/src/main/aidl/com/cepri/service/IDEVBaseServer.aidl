        // IDEVBaseServer.aidl
  package com.cepri.service;

   import com.cepri.service.ApnInfo;

    interface IDEVBaseServer {
    boolean setDateTime(String dateTime);
    boolean addApn(in ApnInfo apnInfo);
    boolean deleteApn(String apnName);
    ApnInfo getCurrApn();
    boolean setCurrApn(String apnName);
    List<ApnInfo> getAllApn();
    String getDeviceModel();
    String getDeviceSn();
    String getOSVersionCode();
    String getDeviceHardVersionCode();
    String getProductionDate();
    boolean setUSBMode(int mode);
    int getUSBMode();
    boolean OSUpdate(String filePath);
    int getOSVerifyPercent();
    String getServiceVersionCode();
    boolean setHomeKeyEnabled(boolean status);
    boolean getHomeKeyEnabled();
    boolean setStatusBarPullEnabled(boolean status);
    }
