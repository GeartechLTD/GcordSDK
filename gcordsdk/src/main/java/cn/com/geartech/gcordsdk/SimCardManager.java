package cn.com.geartech.gcordsdk;

public final class SimCardManager {

    /**
     * sim卡index 1
     * 任何时候 sim卡index只可能是1，或者2；
     * 1是左边的小卡，2是右边的大卡
     */
    public static final int SIM_INDEX_1 = 1;

    /**
     * sim卡index 2
     * 任何时候 sim卡index只可能是1，或者2；
     * 1是左边的小卡，2是右边的大卡
     */
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

    /**
     * 获取默认的用于拨号的sim卡index
     *
     * @return 1 or 2
     */
    public int getDefaultPhoneSimIndex() {
        return getDefaultPhoneIndexImpl();
    }

    /**
     * 获取默认的用于数据使用的sim卡index
     *
     * @return 1 or 2
     */
    public int getDefaultDataSimIndex() {
        return getDefaultDataIndexImpl();
    }

    /**
     * 设置默认的用于拨号的sim卡index
     *
     * @param index 只接受1 or 2
     * @return 是否成功
     */
    public boolean setDefaultPhoneSim(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return setDefaultPhoneImpl(index);
    }

    /**
     * 设置默认的用于数据使用的sim卡index
     *
     * @param index 只接受1 or 2
     * @return 是否成功
     */
    public boolean setDefaultDataSim(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return setDefaultDataSimImpl(index);
    }

    /**
     * 设置是否开启数据（即sim卡上网）
     *
     * @param enabled boolean
     * @return 是否成功
     */
    public boolean setDataEnabled(boolean enabled) {
        return setDataEnabledImpl(enabled);
    }

    /**
     * 获取是否开启数据
     * @return 是/否
     */
    public boolean isDataEnabled() {
        return isDataEnabledImpl();
    }

    /**
     * 获取该index的sim卡是否已准备就绪
     * 注：开机后，android可能需要最多1分钟去准备sim卡
     * @param index 只接受1 or 2
     * @return 是/否
     */
    public boolean isSimReady(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return false;
        }
        return isSimReadyImpl(index);
    }

    /**
     * 是否有sim卡已准备就绪
     * @return boolean
     */
    public boolean isAnySimReady() {
        return isSimReady(SIM_INDEX_1) || isSimReady(SIM_INDEX_2);
    }

    /**
     * 是否2张sim卡均已准备就绪
     * @return boolean
     */
    public boolean isDualSimReady() {
        return isSimReady(SIM_INDEX_1) && isSimReady(SIM_INDEX_2);
    }

    /**
     * 获取该index的sim卡运营商名字（如：中国联通、中国电信）
     * @param index 只接受1 or 2
     * @return 中国联通、中国电信 等
     */
    public String getSimOperator(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return null;
        }
        return getSimOperatorImpl(index);
    }

    /**
     * 获取用于默认通话的sim卡运营商名字
     * @return 中国联通、中国电信 等
     */
    public String getSimOperator() {
        return getSimOperatorImpl(getDefaultPhoneSimIndex());
    }

    /**
     * 获取用于默认通话的sim卡的电话号码。如果无法获得号码，则为空（与android系统显示是一致的）
     * @return 电话号码
     */
    public String getPhoneNumber() {
        return getPhoneNumberImpl(getDefaultPhoneSimIndex());
    }

    /**
     * 获取该index的sim卡的电话号码。如果无法获得号码，则为空（与android系统显示是一致的）
     * @param index 只接受1 or 2
     * @return 电话号码
     */
    public String getPhoneNumber(int index) {
        if (index != SIM_INDEX_1 && index != SIM_INDEX_2) {
            return null;
        }
        return getPhoneNumberImpl(index);
    }

    /**
     * 获取正在通话的sim卡的index，比如：如果sim卡1在通话（这个“通话”指正在拨号，正在进行通话，
     *      正在挂断，或者来电时正在响铃等状态），则返回1，无sim卡在通话则返回-1
     * 可用于插入双sim卡时，获取用户是打电话给哪一张sim卡
     * @return 1 or 2
     */
    public int getIncallSimIndex() {
        return getIncallSimIndexImpl();
    }

    /**
     * 默认拨号sim卡{@link #getDefaultPhoneSimIndex()}是否打开
     * @return boolean
     */
    public boolean isRadioOn() {
        return isRadioOnImpl();
    }

    private boolean isRadioOnImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isRadioOn();
        }catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开关默认拨号的sim卡{@link #getDefaultPhoneSimIndex()}
     * @param on boolean
     */
    public void setRadioOn(boolean on) {
        setRadioOnImpl(on);
    }

    private void setRadioOnImpl(boolean on) {
        try {
            GcordPreference.getInstance().getAIDL().setRadioOn(on);
        }catch (Throwable e) {
            e.printStackTrace();
        }
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
