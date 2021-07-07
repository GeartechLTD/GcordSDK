package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;

/**
 * @author xujiahui
 * @since 2017/2/7
 */

public class RejectButton extends CallBaseView {
    private static final String TAG = "RejectButton";

    public RejectButton(Context context) {
        super(context);
    }

    public RejectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RejectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
      //  GcordSDK.getInstance().getPhoneAPI().reject();
        UnifiedPhoneController.getInstance().reject();
        try {
            mActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
