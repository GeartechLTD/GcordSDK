package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import cn.com.geartech.gcordsdk.R;

/**
 * Created by cuizhy on 18/1/10.
 */

public class ClockView extends LinearLayout {

    public ClockView(Context context) {
        super(context);
        init(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context mContext;
    private TextView tvHour;
    private TextView tvDot;
    private TextView tvMin;
    private Handler mHandler;
    private boolean shouldShowDot = true;

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_clock, this);
        tvHour = (TextView) view.findViewById(R.id.tvHour);
        tvDot = (TextView) view.findViewById(R.id.tvDot);
        tvMin = (TextView) view.findViewById(R.id.tvMin);
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        start();
    }

    public void start() {
        mHandler.post(mTimeRefresher);
    }

    public void stop() {
        mHandler.removeCallbacks(mTimeRefresher);
    }


    private final Runnable mTimeRefresher = new Runnable() {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            tvHour.setText(String.format("%1$02d", hour));
            tvMin.setText(String.format("%1$02d", minute));

            if (shouldShowDot) {
                tvDot.setText(":");
            } else {
                tvDot.setText("");
            }
            shouldShowDot = !shouldShowDot;
            mHandler.postDelayed(this, 1000l);

        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stop();
    }
}
