// IGTAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

import cn.com.geartech.gtplatform.model.aidl.IPrinterAidlCallback;

// Declare any non-default types here with import statements

interface IPrinterAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void printString(String content);

    void setPrintSize(int scaleWidth, int scaleHeight);

    void setBold(boolean isBold);

    void setUnderline(boolean isUnderline);

    void clearSetting();

    void setLineSpacing(int n);

    void setAlignment(int n);

    void printImage(String imagePath);

    void printQRCode(String url);

    void printQRCodeFitSize(String url, int width, int height);

    void reset();

    void printTestPage();

    void printBlank(int n);

    int getPrinterConnectionState();

    void registerCallBack(IPrinterAidlCallback callback, String packageName);

    boolean hasPaper();

}
