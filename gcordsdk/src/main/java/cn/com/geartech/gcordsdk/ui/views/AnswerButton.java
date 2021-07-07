package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.UnifiedPhoneCall;

/**
 * @author xujiahui
 * @since 2017/2/7
 */

public class AnswerButton extends CallBaseView {
    public AnswerButton(Context context) {
        super(context);
    }

    public AnswerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        UnifiedPhoneController.getInstance().answer();

//        if (GcordSDK.getInstance().getHandleManager().isHandlePickedUp()) {
//            GcordSDK.getInstance().getPhoneAPI().pickUpWithHandle();
//        } else {
//            GcordSDK.getInstance().getPhoneAPI().pickUpWithHandsFree();
//        }
    }
}
