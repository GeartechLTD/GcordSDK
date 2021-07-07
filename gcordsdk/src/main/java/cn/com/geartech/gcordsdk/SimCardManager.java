package cn.com.geartech.gcordsdk;

public final class SimCardManager {

    public static final int SIM_INDEX_1 = 1;
    public static final int SIM_INDEX_2 = 2;

    private static SimCardManager instance;

    protected SimCardManager() {

    }

    protected static SimCardManager getInstance() {
        if (instance == null) {
            instance = new SimCardManager();
        }
        return instance;
    }

    public int getDefaultPhoneSimIndex() {
        return getDefaultPhoneIndexImpl();
    }

    public int getDefaultDataSimIndex() {
        return getDefaultDataIndexImpl();
    }

    public boolean setDefaultPhoneSim(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return setDefaultPhoneImpl(index);
    }

    public boolean setDefaultDataSim(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return setDefaultDataSimImpl(index);
    }

    public boolean setDataEnabled(boolean enabled) {
        return setDataEnabledImpl(enabled);
    }

    public boolean isDataEnabled() {
        return isDataEnabledImpl();
    }

    public boolean isSimReady(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return isSimReadyImpl(index);
    }

    public boolean isAnySimReady() {
        return isSimReady(SIM_INDEX_1) || isSimReady(SIM_INDEX_2);
    }

    public boolean isDualSimReady() {
        return isSimReady(SIM_INDEX_1) && isSimReady(SIM_INDEX_2);
    }

    public String getSimOperator(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return null;
        }
        return getSimOperatorImpl(index);
    }

    public String getSimOperator() {
        return getSimOperatorImpl(getDefaultPhoneSimIndex());
    }

    public String getPhoneNumber() {
        return getPhoneNumberImpl(getDefaultPhoneSimIndex());
    }

    public String getPhoneNumber(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return null;
        }
        return getPhoneNumberImpl(index);
    }

    public int getIncallSimIndex() {
        return getIncallSimIndexImpl();
    }

    private boolean isSimReadyImpl(int index) {
        try {
            return GcordPreference.getInstance().getAIDL().isSimReady(index);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getSimOperatorImpl(int index) {
        try {
            return GcordPreference.getInstance().getAIDL().getSimOperator(index);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPhoneNumberImpl(int index) {
        try {
            return GcordPreference.getInstance().getAIDL().getPhoneNumber(index);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getDefaultPhoneIndexImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getDefaultPhoneIndex();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getDefaultDataIndexImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getDefaultDataIndex();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean setDefaultPhoneImpl(int index) {
        try {
            return GcordPreference.getInstance().getAIDL().setDefaultPhone(index);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setDefaultDataSimImpl(int index) {
        try {
            return GcordPreference.getInstance().getAIDL().setDefaultDataSim(index);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setDataEnabledImpl(boolean enabled) {
        try {
            return GcordPreference.getInstance().getAIDL().setDataEnabled(enabled);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isDataEnabledImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isDataEnabled();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getIncallSimIndexImpl() {
        try {
            if (CellPhoneManager.getInstance().isInCall())
                return GcordPreference.getInstance().getAIDL().getIncallSimIndex();
            else
                return -1;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }
}
