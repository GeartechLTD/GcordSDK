package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;

/**
 * @author xujiahui
 * @since 2017/2/7
 */

public class HangOffButton extends CallBaseView {
    public HangOffButton(Context context) {
        super(context);
    }

    public HangOffButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HangOffButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //GcordSDK.getInstance().getPhoneAPI().hangOff();
        UnifiedPhoneController.getInstance().hangOff();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (!mActivity.isFinishing()) {
                        mActivity.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 800);
    }
}
