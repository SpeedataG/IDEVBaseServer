package com.cepri.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :Reginer in  2017/10/10 14:15.
 *         联系方式:QQ:282921012
 *         功能描述:
 */
public class ApnInfo implements Parcelable {
    private String apnName;
    private String apn;
    private String userName;
    private String password;
    private String proxy;
    private String Port;

    public String getApnName() {
        return apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.apnName);
        dest.writeString(this.apn);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.proxy);
        dest.writeString(this.Port);
    }

    public ApnInfo() {
    }

    protected ApnInfo(Parcel in) {
        this.apnName = in.readString();
        this.apn = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.proxy = in.readString();
        this.Port = in.readString();
    }

    public static final Creator<ApnInfo> CREATOR = new Creator<ApnInfo>() {
        @Override
        public ApnInfo createFromParcel(Parcel source) {
            return new ApnInfo(source);
        }

        @Override
        public ApnInfo[] newArray(int size) {
            return new ApnInfo[size];
        }
    };
}
