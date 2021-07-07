package cn.com.geartech.gcordsdk.ui.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.HandleManager;
import cn.com.geartech.gcordsdk.PhoneAPI;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.CallStatusItem;

/**
 * @author xujiahui
 * @since 2017/2/7
 */

public class HandsFreeButton extends CallBaseView {

    public HandsFreeButton(Context context) {
        super(context);
    }

    public HandsFreeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HandsFreeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    PhoneAPI.PhoneEventListener mPhoneEventListener;

    HandleManager.HandleEventListener mHandleEventListener;

    @Override
    public void init() {
        super.init();



        if(!isInEditMode()){

            mHandleEventListener = new HandleManager.HandleEventListener() {
                @Override
                public void onHandlePickedUp() {
                    setSelected(false);
                }

                @Override
                public void onHandlePutDown() {
                    setSelected(true);
                }
            };

//            mPhoneEventListener = new PhoneAPI.PhoneEventListener() {
//                @Override
//                public void onPickUp(PhoneAPI.PICKUP_STATE state) {
//                    setSelected(state == PhoneAPI.PICKUP_STATE.HANDS_FREE);
//                }
//
//                @Override
//                public void onInComingCall() {
//
//                }
//
//                @Override
//                public void onRingEnd() {
//
//                }
//
//                @Override
//                public void onPhoneNumberReceived(String phoneNumber) {
//
//                }
//
//                @Override
//                public void onSwitchPhoneState(PhoneAPI.PICKUP_STATE state) {
//                    setSelected(state == PhoneAPI.PICKUP_STATE.HANDS_FREE);
//                }
//
//                @Override
//                public void onHangOff() {
//
//                }
//            };
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSelected(UnifiedPhoneController.getInstance().isHandsFreeOn());
                }
            },500);

//            GcordSDK.getInstance().getPhoneAPI().addPhoneEventListener(mPhoneEventListener);

            GcordSDK.getInstance().getHandleManager().addHandleEventListener(mHandleEventListener);
            UnifiedPhoneController.getInstance().addPhoneEventListener(unifiedPhoneEventListener);
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
            if(seconds == 0)
            {
                setSelected(UnifiedPhoneController.getInstance().isHandsFreeOn());
            }
        }

        @Override
        public void onCallTerminated() {

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

    @Override
    public void onClick(View v) {
        super.onClick(v);
//        GcordSDK.getInstance().getPhoneAPI().switchPhoneState(
//                isSelected() ? PhoneAPI.PICKUP_STATE.HANDS_FREE
//                        : PhoneAPI.PICKUP_STATE.HANDLE);
        if(UnifiedPhoneController.getInstance().isHandsFreeOn())
        {
            UnifiedPhoneController.getInstance().switchToHandle();
            setSelected(false);
        }
        else
        {
            UnifiedPhoneController.getInstance().switchToHandsFree();
            setSelected(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        UnifiedPhoneController.getInstance().removePhoneEventListener(unifiedPhoneEventListener);
        GcordSDK.getInstance().getPhoneAPI().removePhoneEventListener(mPhoneEventListener);
    }
}
