package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by kya on 15/8/14.
 */
public class MiniTextView extends TextView{
    public MiniTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }
}
