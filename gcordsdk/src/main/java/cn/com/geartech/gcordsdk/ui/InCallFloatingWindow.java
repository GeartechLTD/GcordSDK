package cn.com.geartech.gcordsdk.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.CallStatusItem;

/**
 * @author xujiahui
 * @since 12/05/2017
 */
public class InCallFloatingWindow {

    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    LinearLayout mFloatLayout;
    TextView tvTime;

    long mCallTime;
    String mStrTime;

    private static boolean isActive = false;

    public static boolean isActive() {
        return isActive;
    }

    Handler mHandler = new Handler();
    Runnable callTimeRunnable = new Runnable() {
        @Override
        public void run() {
            mStrTime = "";
            long count = mCallTime;
            mCallTime++;
            long hour = count / 3600;
            if (hour > 0) {
                mStrTime += hour + ":";
                count = count % 3600;
            }

            long minute = count / 60;
            mStrTime += minute < 10 ? "0" + minute + ":" : minute + ":";

            long second = count % 60;
            mStrTime += second < 10 ? "0" + second : second;

            tvTime.setText(mStrTime);
            mHandler.postDelayed(this, 1000);
        }
    };

    private static InCallFloatingWindow sInstance;

    public static void show(Context context, long callTime, String callNum, boolean recordSelected) {
        if (sInstance == null) {
            sInstance = new InCallFloatingWindow();
        }
        isActive = true;
        sInstance.open(context, callTime, callNum, recordSelected);
    }

    public void open(final Context context, long callTime, final String callNum, final boolean recordSelected) {
        mCallTime = callTime;
        if (mWindowManager == null) {
            UnifiedPhoneController.getInstance().addPhoneEventListener(unifiedPhoneEventListener);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wmParams = new WindowManager.LayoutParams();
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

            wmParams.width = 116; // 图片宽度
            wmParams.height = 143; // 图片高度
            wmParams.x = 300;
            wmParams.y = -400;
            wmParams.format = PixelFormat.RGBA_8888;

            mFloatLayout = (LinearLayout) LayoutInflater.from(context)
                    .inflate(R.layout.layout_incall_floating_window, null);
            mWindowManager.addView(mFloatLayout, wmParams);
            tvTime = (TextView) mFloatLayout.findViewById(R.id.tv_time);
            if (callTime != 0) {
                mHandler.post(callTimeRunnable);
            }

            mFloatLayout.setOnTouchListener(new View.OnTouchListener() {
                int initialX, initialY;
                float initialTouchX, initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = wmParams.x;
                            initialY = wmParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            wmParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            wmParams.y = initialY + (int) (event.getRawY() - initialTouchY);

                            wmParams.x = wmParams.x > 300 ? 300 : wmParams.x;
                            wmParams.x = wmParams.x < -300 ? -300 : wmParams.x;
                            wmParams.y = wmParams.y > 400 ? 400 : wmParams.y;
                            wmParams.y = wmParams.y < -400 ? -400 : wmParams.y;
                            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                            return true;
                        case MotionEvent.ACTION_UP:
                            if ((event.getRawX() - initialTouchX) < 5 && (event.getRawY() - initialTouchY) < 5) {
                                close();
                                Intent i = new Intent(context, InCallActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("phoneNumber", callNum);
                                i.putExtra("callTime", mCallTime);
                                i.putExtra("recordSelected", recordSelected);
                                i.putExtra("show_back_button", true);
                                context.startActivity(i);
                            }
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    public void close() {
        try {
            isActive = false;
            if (mWindowManager != null) {
                UnifiedPhoneController.getInstance().removePhoneEventListener(unifiedPhoneEventListener);
                mWindowManager.removeView(mFloatLayout);
                mWindowManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    UnifiedPhoneController.UnifiedPhoneEventListener unifiedPhoneEventListener = new UnifiedPhoneController.UnifiedPhoneEventListener() {
        @Override
        public void onIncomingCall() {

        }

        @Override
        public void onGetPhoneNumber(String number) {

        }

        @Override
        public void onTicking(int seconds) {
            mCallTime = seconds;
            mHandler.removeCallbacks(callTimeRunnable);
            mHandler.post(callTimeRunnable);
        }

        @Override
        public void onCallTerminated() {
            close();
        }

        @Override
        public void onDialFinish(String number) {

        }

        @Override
        public void onBusyTone() {

        }

        @Override
        public void onResumeInCallStatus(CallStatusItem item) {

        }
    };
}
