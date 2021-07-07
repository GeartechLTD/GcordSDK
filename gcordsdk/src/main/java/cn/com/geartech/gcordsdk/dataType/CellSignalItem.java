package cn.com.geartech.gcordsdk.dataType;

public class CellSignalItem {

    Integer dbm;
    Integer asu;
    Integer level;
    String networkTypeName;
    String dataStateString;
    String networkOperatorName;

    public Integer getDbm() {
        return dbm;
    }

    public void setDbm(Integer dbm) {
        this.dbm = dbm;
    }

    public Integer getAsu() {
        return asu;
    }

    public void setAsu(Integer asu) {
        this.asu = asu;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNetworkTypeName() {
        return networkTypeName;
    }

    public void setNetworkTypeName(String networkTypeName) {
        this.networkTypeName = networkTypeName;
    }

    public String getDataStateString() {
        return dataStateString;
    }

    public void setDataStateString(String dataStateString) {
        this.dataStateString = dataStateString;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }
}
