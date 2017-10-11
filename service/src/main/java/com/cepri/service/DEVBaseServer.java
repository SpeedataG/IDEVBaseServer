package com.cepri.service;


import android.content.ContentValues;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.util.List;

/**
 * @author :Reginer in  2017/10/10 14:09.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class DEVBaseServer {
    private IDEVBaseServer idevBaseServer;

    public DEVBaseServer() {
        idevBaseServer = IDEVBaseServer.Stub.asInterface(ServiceManager.getService(Context.DEV_SERVER));
    }

    public DEVBaseServer(Context mContext, IDEVBaseServer idevBaseServer) {
        this.idevBaseServer = idevBaseServer;
    }

    private static class DEVBaseServerHolder {
        private static final DEVBaseServer INSTANCE = new DEVBaseServer();
    }

    /**
     * 获取实例.
     *
     * @return DEVBaseServer
     */
    public static DEVBaseServer getInstance() {
        return DEVBaseServerHolder.INSTANCE;
    }

    /**
     * 时间和日期设置.
     *
     * @param dateTime 设置日期时间值的字符串yyyy-MM-dd HH:mm:ss
     *                 如2016-10-28 16:34:45
     * @return true    设置成功 false	设置失败
     */
    public boolean setDateTime(String dateTime) {
        try {
            return idevBaseServer.setDateTime(dateTime);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 添加APN并设置为当前
     *
     * @param apnInfo Apn设置信息对象
     * @return 设置是否成功
     */
    public boolean addApn(ApnInfo apnInfo) {
        try {
            return idevBaseServer.addApn(apnInfo);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int createApn(ContentValues values) {
        try {
            return idevBaseServer.createApn(values);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setDefaultApn(int apnId) {
        try {
            return idevBaseServer.setDefaultApn(apnId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getApnId(String apnName) {
        try {
            return idevBaseServer.getApnId(apnName);
        } catch (RemoteException e) {
           return -1;
        }
    }

    /**
     * 删除APN.
     *
     * @param apnName apn名称
     * @return 是否成功删除
     */
    public boolean deleteApn(String apnName) {
        try {
            return idevBaseServer.deleteApn(apnName);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 获取当前APN的设置信息 .
     *
     * @return ApnInfo对象，ApnInfo定义：
     * 类型	说明
     * apnName	String	apn的名称
     * apn	String	apn的具体字符串值
     * userName	String	用户名，可为空
     * password	String	密码，可为空
     */
    public ApnInfo getCurrApn() {
        try {
            return idevBaseServer.getCurrApn();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 设置已有apn为当前的APN.
     *
     * @param apnName apn名称
     * @return 是否设置成功
     */
    public boolean setCurrApn(String apnName) {
        try {
            return idevBaseServer.setCurrApn(apnName);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 获取所有APN的信息.
     *
     * @return apn列表
     */
    public List<ApnInfo> getAllApn() {
        try {
            return idevBaseServer.getAllApn();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 获取设备型号.
     *
     * @return 设备型号
     */
    public String getDeviceModel() {
        try {
            return idevBaseServer.getDeviceModel();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 获取设备序列号.
     *
     * @return 设备序列号
     */
    public String getDeviceSn() {
        try {
            return idevBaseServer.getDeviceSn();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 获取设备系统版本号.
     *
     * @return 设备系统版本号
     */
    public String getOSVersionCode() {
        try {
            return idevBaseServer.getOSVersionCode();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 获取设备硬件版本号.
     *
     * @return 设备硬件版本号
     */
    public String getDeviceHardVersionCode() {
        try {
            return idevBaseServer.getDeviceHardVersionCode();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 获取设备生产日期.
     *
     * @return 设备生产日期
     */
    public String getProductionDate() {
        try {
            return idevBaseServer.getProductionDate();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 设置USB的传输方式.
     *
     * @param mode mode = 0：关闭USB通信
     *             mode = 0x0001：打开adb调试
     *             mode = 0x0010：打开mtp方式
     *             mode= 0x0100：打开大容量存储模式
     *             可以同时开始adb，mtp和大容量存储模式的一种或者几种通信模式
     * @return 设置是否成功
     */
    public boolean setUSBMode(int mode) {
        try {
            return idevBaseServer.setUSBMode(mode);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 获取当前USB的传输方式 .
     *
     * @return 0    关闭USB通信
     * 0x0001	adb调试
     * 0x0010	mtp方式
     * 0x0100	大容量存储模式
     */
    public int getUSBMode() {
        try {
            return idevBaseServer.getUSBMode();
        } catch (RemoteException e) {
            return -1;
        }
    }

    /**
     * 系统升级（可能会重启设备）.
     *
     * @param filePath 升级文件的绝对路径
     * @return 升级是否成功
     */
    public boolean OSUpdate(String filePath) {
        try {
            return idevBaseServer.OSUpdate(filePath);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 获取系统校验百分比 .
     *
     * @return 系统校验百分比
     */
    public int getOSVerifyPercent() {
        try {
            return idevBaseServer.getOSVerifyPercent();
        } catch (RemoteException e) {
            return -1;
        }
    }

    /**
     * 获取系统基础服务的版本号.
     *
     * @return 系统基础服务的版本号
     */
    public String getServiceVersionCode() {
        try {
            return idevBaseServer.getServiceVersionCode();
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * 设置Home键是否可用 .
     *
     * @param status 可用状态
     * @return 设置是否成功
     */
    public boolean setHomeKeyEnabled(boolean status) {
        try {
            return idevBaseServer.setHomeKeyEnabled(status);
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 获取Home键是否可用.
     *
     * @return 可用状态
     */
    public boolean getHomeKeyEnabled() {
        try {
            return idevBaseServer.getHomeKeyEnabled();
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * 设置状态栏是否可以下拉 .
     *
     * @param status 是否可下拉
     * @return 设置是否成功
     */
    public boolean setStatusBarPullEnabled(boolean status) {
        try {
            return idevBaseServer.setStatusBarPullEnabled(status);
        } catch (RemoteException e) {
            return false;
        }
    }
}
