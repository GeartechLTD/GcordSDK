package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeTickTextView extends TextView{
    public TimeTickTextView(Context context) {
        super(context);
    }

    public TimeTickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTickTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler handler;
    private long startTimeMillis = 0;
    private long timeElapsed = 0;

    private final Runnable timeRefresher = new Runnable() {
        @Override
        public void run() {

            timeElapsed = (System.currentTimeMillis() - startTimeMillis)/1000;
            setText(DateUtils.formatElapsedTime(timeElapsed));
            handler.postDelayed(this, REFRESH_DELAY);
        }
    };

    private static final int REFRESH_DELAY = 1000;

    public void startTick(){

        timeElapsed = 0;

        startTimeMillis = System.currentTimeMillis();

        if(handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        handler = new Handler(Looper.getMainLooper());

        handler.post(timeRefresher);
    }

    public long getElapsedMillis(){

        return timeElapsed;
    }

    public void stopTick(){

        if(handler != null)
            handler.removeCallbacksAndMessages(null);
        handler = null;
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stopTick();
    }
}
