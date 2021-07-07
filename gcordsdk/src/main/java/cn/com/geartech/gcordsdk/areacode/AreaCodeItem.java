package cn.com.geartech.gcordsdk.areacode;

/**
 * Created by dinner on 14-8-1.
 */
public class AreaCodeItem {

    String mobileArea;
    String mobileType;
    String areaCode = "";

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    String provider;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    String mark;

    public String getMobileArea() {
        return mobileArea;
    }

    public String getProvince() {
        if (mobileArea == null || mobileArea.length() == 0) {
            return null;
        }

        try {
            int index = mobileArea.indexOf(' ');
            if (index > 0) {
                return mobileArea.substring(0, index);
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }

    }

    public String getCity() {
        if (mobileArea == null || mobileArea.length() == 0) {
            return null;
        }


        try {
            int index = mobileArea.indexOf(' ');
            if (index > 0) {
                return mobileArea.substring(index + 1);
            } else {
                return mobileArea;
            }
        } catch (Exception e) {
            return null;
        }

    }


    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString(){
        return " getMark()=" + getMark()
                + " getProvince()=" + getProvince()
                + " getCity()=" + getCity();
    }
}
