package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.UnifiedPhoneCall;

/**
 * @author xujiahui
 * @since 2017/2/8
 */

public class FlashButton extends CallBaseView {
    public FlashButton(Context context) {
        super(context);

        autoHide();
    }

    public FlashButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        autoHide();
    }

    public FlashButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        autoHide();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        GcordSDK.getInstance().getPhoneAPI().flash(null);
    }

    void autoHide()
    {


    }
}
