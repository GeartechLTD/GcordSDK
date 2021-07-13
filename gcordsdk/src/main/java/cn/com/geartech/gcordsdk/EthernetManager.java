package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class EthernetManager {
    private final static String INET = "inet ";
    private static EthernetManager instance;

    static {
        instance = new EthernetManager();
    }

    private EthernetManager() {

    }

    protected static EthernetManager getInstance() {
        return instance;
    }

    /**
     * 获取以太网子网掩码
     *
     * @return 子网掩码
     */
    @SuppressWarnings("unused")
    public String getEthernetMask() {
        return getEthernetIPV4OrMask(1);
    }

    /**
     * 获取以太网ipv4地址
     *
     * @return ipv4 address
     */
    @SuppressWarnings("unused")
    public String getEthernetIPV4Address() {
        return getEthernetIPV4OrMask(0);
    }


    /**
     * @param type 1:get mask;0-getIPv4 Address
     * @return mask or ip
     */

    private String getEthernetIPV4OrMask(int type) {
        if (type > 1 || type < 0) {
            return "";
        }

        String result = exeCommandAndGetResult("ip address show dev eth0");
        if (!isStringNullOrEmpty(result)) {
            if (result.contains(INET)) {
                result = result.substring(result.indexOf(INET));
                result = result.replace(INET, "");
                String str = result.split(" ")[0];
                String[] strArr = str.split("/");
                if (strArr.length > 1) {
                    if (type == 0) {
                        return strArr[0];
                    } else {
                        String mask = strArr[1];
                        return cidr2Decimalism(Integer.parseInt(mask));
                    }
                }
            }
        }
        return "";

    }

    private static boolean isStringNullOrEmpty(String str) {
        return (str == null || TextUtils.isEmpty(str.trim()));
    }

    private static String cidr2Decimalism(int mask) {
//        byte[] bytes = new byte[4];
        if (mask >= 32) {
            return "255.255.255.255";
        } else if (mask <= 0) {
            return "";
        } else {
            mask = 32 - mask;
            long max = 0xffffffffL;
            int maxByte = 0xff;
            long[] bytes = new long[4];
            max <<= mask;
            bytes[3] = max & maxByte;
            max >>= 8;
            bytes[2] = max & maxByte;
            max >>= 8;
            bytes[1] = max & maxByte;
            max >>= 8;
            bytes[0] = max & maxByte;
            return (bytes[0] + "." + bytes[1] + "." + bytes[2] + "." + bytes[3]);
        }

    }

    private static Process execCommand(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String exeCommandAndGetResult(String command) {

        Process p = execCommand(command);
        if (p != null) {
            try {
                p.waitFor();
                return getExecResult(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static String getExecResult(Process p) {
        try {
            InputStream inputStream = p.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                result.append(tmp);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            tmp = result.toString();
            Log.e("result", tmp);
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @SuppressWarnings("unused")
    protected void init(Application application) {

    }

    /**
     * 获取以太网网关
     *
     * @return gateway
     */
    @SuppressWarnings("unused")
    public String getGateWay() {
        String queryCmd = "ip route show";
        String line = exeCommandAndGetResult(queryCmd);
        if (!isStringNullOrEmpty(line)) {
            line = line.trim();
            if (line.contains("default via")) {
                String[] lines = line.split(" ");
                if (lines.length > 3) {
                    return lines[2];
                }
            }
        }
        return "";
    }

    /**
     * 获取以太网dns
     *
     * @return dns
     */
    public String[] getDns() {
        String[] dns = new String[]{"", ""};
        String queryCmd = "getprop net.dns1";
        String result = exeCommandAndGetResult(queryCmd);
        if (isStringNullOrEmpty(result)) {
            queryCmd = "getprop dhcp.eth0.dns1";
            result = exeCommandAndGetResult(queryCmd);
            if (isStringNullOrEmpty(result)) {
                result = "";
            }
        }
        dns[0] = result.trim();

        queryCmd = "getprop net.dns2";
        result = exeCommandAndGetResult(queryCmd);
        if (isStringNullOrEmpty(result)) {
            queryCmd = "getprop dhcp.eth0.dns2";
            result = exeCommandAndGetResult(queryCmd);
            if (isStringNullOrEmpty(result)) {
                result = "";
            }
        }
        dns[1] = result.trim();
        return dns;
    }
}
