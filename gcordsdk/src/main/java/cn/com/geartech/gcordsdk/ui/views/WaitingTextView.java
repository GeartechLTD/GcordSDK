package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author xujiahui
 * @since 16-3-24
 */
public class WaitingTextView extends TextView {
    private static final String TAG = "WaitingTextView";

    private int point = 0;
    private String s = "";
    private String[] suffix = new String[]{"      ", " .    ", " . .  ", " . . ."};

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            point = (point + 1) % 4;
            setText(s);
        }
    };

    public WaitingTextView(Context context) {
        super(context);
    }

    public WaitingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public WaitingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text.length() == 0) {
            super.setText(text, type);
            return;
        }
        s = text.toString();
        s = s.replace(".", "");
        s = s.replace(" ", "");
        s = s + suffix[point];
        super.setText(s, type);

        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(mRunnable, 1000);
    }

    public void clear() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        setText("");
    }

}
