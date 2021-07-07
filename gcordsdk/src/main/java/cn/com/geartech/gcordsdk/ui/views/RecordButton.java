package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.PhoneAPI;
import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;

/**
 * @author xujiahui
 * @since 2017/2/8
 */

public class RecordButton extends CallBaseView {

    public RecordButton(Context context) {
        super(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        setSelected(!isSelected());
        if (isSelected()) {
            Toast.makeText(mActivity, R.string.start_recording, Toast.LENGTH_SHORT).show();

            UnifiedPhoneController.getInstance().startRecording(new UnifiedPhoneController.UnifiedPhoneRecordCallback() {
                @Override
                public void onRecordSuccess(String audioPath) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, R.string.record_is_saved, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onRecordFailed(int errorCode,final String errorMessage) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else {
            UnifiedPhoneController.getInstance().stopRecording();
        }
    }

}
