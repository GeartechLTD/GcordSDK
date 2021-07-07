package cn.com.geartech.gcordsdk.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author xujiahui
 * @since 2017/2/7
 */

public class CallBaseView extends TextView implements View.OnClickListener {
    Activity mActivity;

    public CallBaseView(Context context) {
        super(context);
        init();
    }

    public CallBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CallBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (getContext() instanceof Activity) {
            mActivity = ((Activity) getContext());
        }
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
