package cn.com.geartech.gcordsdk.ui.views;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;

/**
 * @author xujiahui
 * @since 2017/2/8
 */

public class InCallDialpad extends LinearLayout implements View.OnClickListener {

    private final Object mToneGeneratorLock = new Object();
    private ToneGenerator mToneGenerator = null;

    /**
     * The length of DTMF tones in milliseconds
     */
    private static final int TONE_LENGTH_MS = 150;

    /**
     * Stream type used to play the DTMF tones off call, and mapped to the volume control keys
     */
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_MUSIC;

    /**
     * The DTMF tone volume relative to other sounds in the stream
     */
    private static final int TONE_RELATIVE_VOLUME = 80;


    public interface InCallDialPadListener{
        public void onNumberDialed(String number);
    }

    InCallDialPadListener inCallDialPadListener;

    public void setInCallDialPadListener(InCallDialPadListener inCallDialPadListener) {
        this.inCallDialPadListener = inCallDialPadListener;
    }

    public InCallDialpad(Context context) {
        super(context);
        init();
    }

    public InCallDialpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InCallDialpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    Button flashBtn ;

    private void init() {

        if(!isInEditMode()){
            initTone();
        }

        View dialPad = inflate(getContext(), R.layout.incall_dial_pad, this);

        dialPad.findViewById(R.id.one).setOnClickListener(this);
        dialPad.findViewById(R.id.two).setOnClickListener(this);
        dialPad.findViewById(R.id.three).setOnClickListener(this);
        dialPad.findViewById(R.id.four).setOnClickListener(this);
        dialPad.findViewById(R.id.five).setOnClickListener(this);
        dialPad.findViewById(R.id.six).setOnClickListener(this);
        dialPad.findViewById(R.id.seven).setOnClickListener(this);
        dialPad.findViewById(R.id.eight).setOnClickListener(this);
        dialPad.findViewById(R.id.nine).setOnClickListener(this);
        dialPad.findViewById(R.id.zero).setOnClickListener(this);
        dialPad.findViewById(R.id.star).setOnClickListener(this);
        dialPad.findViewById(R.id.pound).setOnClickListener(this);

        //flashBtn = findViewById(R.id.)
    }

    private void initTone() {

        // if the mToneGenerator creation fails, just continue without it.  It is
        // a local audio signal, and is not as important as the dtmf tone itself.

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    // we want the user to be able to control the volume of the dial tones
                    // outside of a call, so we use the stream type that is also mapped to the
                    // volume control keys for this activity
                    mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
                    ((Activity) getContext()).setVolumeControlStream(DIAL_TONE_STREAM_TYPE);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mToneGenerator = null;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int keyCode = -1;
        int tone = -1;
        if (id == R.id.one) {
            keyCode = KeyEvent.KEYCODE_1;
            tone = ToneGenerator.TONE_DTMF_1;
        } else if (id == R.id.two) {
            keyCode = KeyEvent.KEYCODE_2;
            tone = ToneGenerator.TONE_DTMF_2;
        } else if (id == R.id.three) {
            keyCode = KeyEvent.KEYCODE_3;
            tone = ToneGenerator.TONE_DTMF_3;
        } else if (id == R.id.four) {
            keyCode = KeyEvent.KEYCODE_4;
            tone = ToneGenerator.TONE_DTMF_4;
        } else if (id == R.id.five) {
            keyCode = KeyEvent.KEYCODE_5;
            tone = ToneGenerator.TONE_DTMF_5;
        } else if (id == R.id.six) {
            keyCode = KeyEvent.KEYCODE_6;
            tone = ToneGenerator.TONE_DTMF_6;
        } else if (id == R.id.seven) {
            keyCode = KeyEvent.KEYCODE_7;
            tone = ToneGenerator.TONE_DTMF_7;
        } else if (id == R.id.eight) {
            keyCode = KeyEvent.KEYCODE_8;
            tone = ToneGenerator.TONE_DTMF_8;
        } else if (id == R.id.nine) {
            keyCode = KeyEvent.KEYCODE_9;
            tone = ToneGenerator.TONE_DTMF_9;
        } else if (id == R.id.zero) {
            keyCode = KeyEvent.KEYCODE_0;
            tone = ToneGenerator.TONE_DTMF_0;
        } else if (id == R.id.pound) {
            keyCode = KeyEvent.KEYCODE_POUND;
            tone = ToneGenerator.TONE_DTMF_P;
        } else if (id == R.id.star) {
            keyCode = KeyEvent.KEYCODE_STAR;
            tone = ToneGenerator.TONE_DTMF_S;
        }

        if (keyCode != -1) {
            keyPressed(keyCode);
        }

        if (tone != -1) {
            playTone(tone);
        }
    }

    private void keyPressed(int keyCode) {

        String number = "";

        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                UnifiedPhoneController.getInstance().dialDTMF("0");
                number = "0";
                //GcordSDK.getInstance().getPhoneAPI().dial("0");
                break;
            case KeyEvent.KEYCODE_1:
                UnifiedPhoneController.getInstance().dialDTMF("1");
                number = "1";

                //GcordSDK.getInstance().getPhoneAPI().dial("1");
                break;
            case KeyEvent.KEYCODE_2:
                UnifiedPhoneController.getInstance().dialDTMF("2");
                number = "2";

                //GcordSDK.getInstance().getPhoneAPI().dial("2");
                break;
            case KeyEvent.KEYCODE_3:
                UnifiedPhoneController.getInstance().dialDTMF("3");
                number = "3";

                //GcordSDK.getInstance().getPhoneAPI().dial("3");
                break;
            case KeyEvent.KEYCODE_4:
                UnifiedPhoneController.getInstance().dialDTMF("4");
                number = "4";

                //GcordSDK.getInstance().getPhoneAPI().dial("4");
                break;
            case KeyEvent.KEYCODE_5:
                UnifiedPhoneController.getInstance().dialDTMF("5");
                number = "5";

                //GcordSDK.getInstance().getPhoneAPI().dial("5");
                break;
            case KeyEvent.KEYCODE_6:
                UnifiedPhoneController.getInstance().dialDTMF("6");
                number = "6";

                //GcordSDK.getInstance().getPhoneAPI().dial("6");
                break;
            case KeyEvent.KEYCODE_7:
                UnifiedPhoneController.getInstance().dialDTMF("7");
                number = "7";

                //GcordSDK.getInstance().getPhoneAPI().dial("7");
                break;
            case KeyEvent.KEYCODE_8:
                UnifiedPhoneController.getInstance().dialDTMF("8");
                number = "8";

                //GcordSDK.getInstance().getPhoneAPI().dial("8");
                break;
            case KeyEvent.KEYCODE_9:
                UnifiedPhoneController.getInstance().dialDTMF("9");
                number = "9";

                //GcordSDK.getInstance().getPhoneAPI().dial("9");
                break;
            case KeyEvent.KEYCODE_STAR:
                UnifiedPhoneController.getInstance().dialDTMF("*");
                number = "*";

                //GcordSDK.getInstance().getPhoneAPI().dial("*");
                break;
            case KeyEvent.KEYCODE_POUND:
                UnifiedPhoneController.getInstance().dialDTMF("#");
                number = "#";

                //GcordSDK.getInstance().getPhoneAPI().dial("#");
                break;
        }

        if(inCallDialPadListener != null)
        {
            inCallDialPadListener.onNumberDialed(number);
        }
    }

    void playTone(int tone) {

        AudioManager audioManager =
                (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT)
                || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.startTone(tone, TONE_LENGTH_MS);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mToneGenerator != null) {
            mToneGenerator.release();
        }
        super.onDetachedFromWindow();
    }
}
