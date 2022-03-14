package com.bighao.project.common.uitls;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.MODIFY_PHONE_STATE;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import androidx.annotation.RequiresPermission;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * Created by Hao on 2022/3/14
 * 网络工具类
 */
@SuppressWarnings("all")
public class NetworkUtils {

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 打开WiFi设置
     */
    public static void openWirelessSettings() {
        AppUtils.getApp().startActivity(
                new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
    }

    /**
     * 网络是否连接
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 移动数据是否打开
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean getMobileDataEnabled() {
        try {
            TelephonyManager tm = (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.isDataEnabled();
            }
            @SuppressLint("PrivateApi")
            Method getMobileDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            SuperLog.e(e);
        }
        return false;
    }

    /**
     * 设置移动数据打开关闭
     */
    @RequiresPermission(MODIFY_PHONE_STATE)
    public static boolean setMobileDataEnabled(final boolean enabled) {
        try {
            TelephonyManager tm =
                    (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.setDataEnabled(enabled);
                return false;
            }
            Method setDataEnabledMethod =
                    tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            setDataEnabledMethod.invoke(tm, enabled);
            return true;
        } catch (Exception e) {
            SuperLog.e(e);
        }
        return false;
    }

    /**
     * 是否正在使用移动数据
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isMobileData() {
        NetworkInfo info = getActiveNetworkInfo();
        return null != info
                && info.isAvailable()
                && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 是否正在使用4G
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null
                && info.isAvailable()
                && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 获取WiFi是否打开
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) AppUtils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) {
            return false;
        }
        return manager.isWifiEnabled();
    }

    /**
     * 设置WiFi打开还是关闭
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    public static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) AppUtils.getApp().getSystemService(WIFI_SERVICE);
        if (manager == null) {
            return;
        }
        if (enabled == manager.isWifiEnabled()) {
            return;
        }
        manager.setWifiEnabled(enabled);
    }

    /**
     * 获取WiFi是否连接
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) AppUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取网络名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) AppUtils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }
        return tm.getNetworkOperatorName();
    }

    /**
     * 获取网络类型
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkType getNetworkType() {
        if (isEthernet()) {
            return NetworkType.NETWORK_ETHERNET;
        }
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.NETWORK_2G;

                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.NETWORK_3G;

                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.NETWORK_4G;

                    default:
                        String subtypeName = info.getSubtypeName();
                        if ("TD-SCDMA".equalsIgnoreCase(subtypeName)
                                || "WCDMA".equalsIgnoreCase(subtypeName)
                                || "CDMA2000".equalsIgnoreCase(subtypeName)) {
                            return NetworkType.NETWORK_3G;
                        }
                }
            }
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    /**
     * 是否可以ping地址
     *
     * @param ip ip地址
     */
    @RequiresPermission(INTERNET)
    public static boolean isAvailableByPing(String ip) {
        if (ip == null || ip.length() <= 0) {
            ip = "223.5.5.5";
        }
        ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            SuperLog.e("isAvailableByPing() called" + result.errorMsg);
        }
        if (result.successMsg != null) {
            SuperLog.e("isAvailableByPing() called" + result.successMsg);
        }
        return ret;
    }

    /**
     * 获取网络IP地址
     */
    @RequiresPermission(INTERNET)
    public static String getIPAddress(final boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            LinkedList<InetAddress> adds = new LinkedList<>();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp() || ni.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement());
                }
            }
            for (InetAddress add : adds) {
                if (!add.isLoopbackAddress()) {
                    String hostAddress = add.getHostAddress();
                    boolean isIPv4 = hostAddress.indexOf(':') < 0;
                    if (useIPv4) {
                        if (isIPv4) {
                            return hostAddress;
                        }
                    } else {
                        if (!isIPv4) {
                            int index = hostAddress.indexOf('%');
                            return index < 0
                                    ? hostAddress.toUpperCase()
                                    : hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取域地址
     */
    @RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取WiFi IP地址
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getIpAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) AppUtils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().ipAddress);
    }

    /**
     * 获取WiFi网关
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getGatewayByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) AppUtils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().gateway);
    }

    /**
     * Return the net mask by wifi.
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getNetMaskByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) AppUtils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().netmask);
    }

    /**
     * Return the server address by wifi.
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    public static String getServerAddressByWifi() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) AppUtils.getApp().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        return Formatter.formatIpAddress(wm.getDhcpInfo().serverAddress);
    }

    /**
     * 是否使用以太网
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static boolean isEthernet() {
        final ConnectivityManager cm =
                (ConnectivityManager) AppUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        final NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (info == null) {
            return false;
        }
        NetworkInfo.State state = info.getState();
        if (null == state) {
            return false;
        }
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING;
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) AppUtils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return null;
        }
        return cm.getActiveNetworkInfo();
    }

    public enum NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }
}
