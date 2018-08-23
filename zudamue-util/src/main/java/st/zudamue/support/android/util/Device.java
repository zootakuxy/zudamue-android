package st.zudamue.support.android.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Device implements Serializable  {

    private String branch;
    private String model;
    private String version;
    private String imei;
    private String size;
    private String mac;

    public Device( Context context ) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE );
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        this .mac = loadMac(context);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi=( double ) width/( double )dens;
        double hi=( double ) height/( double )dens;

        double x = Math.pow( wi, 2 );
        double y = Math.pow( hi, 2 );
        double polegada = Math.sqrt(x+y);
        polegada = Math.round(polegada);

        /**
         * Using these device identifiers is not recommended other than for high value fraud prevention and advanced telephony use-cases. For advertising use-cases, use AdvertisingIdClient$Info#getId and for analytics, use InstanceId#getId.
         */
        this.branch = android.os.Build.MANUFACTURER+" "+android.os.Build.DEVICE;
        this.model = android.os.Build.MODEL;
        this.version = android.os.Build.VERSION.RELEASE;

        try{
            this.imei = telephonyManager.getDeviceId();
        }catch ( Exception ex ){
            ex.printStackTrace();
            try {
                this.imei = AdvertisingIdClient.getAdvertisingIdInfo( context ).getId();
            }catch ( Exception aex ){
                aex.printStackTrace();
            }
        }
        this.size = polegada+"";
    }

    public Device(String branch, String model, String version, String imei, String size, String mac) {
        this.branch = branch;
        this.model = model;
        this.version = version;
        this.imei = imei;
        this.size = size;
        this.mac = mac;
    }

    private String loadMac(Context context) {
        String mac = "02:00:00:00:00:00";
//        if( Build.VERSION.SDK_INT  < Build.VERSION_CODES.M ){
//            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService( Context.WIFI_SERVICE );
//            wifiManager.setWifiEnabled( true );
//            WifiInfo wInfo = wifiManager.getConnectionInfo();
//
//
//            if( wInfo.getMacAddress() != null)  {
//                mac = wInfo.getMacAddress();
//            }
//        } else {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//        }

        return mac;

    }

    public String getBranch() {
        return branch;
    }

    public String getImei() {
        return imei;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getVersion() {
        return version;
    }


    public String getMac() {
        return mac;
    }

    public String getSize() {
        return size;
    }


    @Override
    public String toString() {
        return "Device [branch=" + branch + ", model=" + model + ", version="
                + version + ", imei=" + imei + ", size=" + size
                + ", mac=" + mac + "]";
    }


}