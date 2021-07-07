package cn.com.geartech.gcordsdk.dataType;

/**
 * Created by pangyuning on 17/5/8.
 */

/**
 * 软件更新信息
 */
public class PackageUpgradeItem {

    /**
     * 软件包名
     */
    private String packageName;
    /**
     * 最新版本
     */
    private String versionName;
    private String info;

    private String changes;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }
}
