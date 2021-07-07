package cn.com.geartech.gcordsdk.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import cn.com.geartech.app.ui.components.LoopViewPager;
import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.HandleManager;
import cn.com.geartech.gcordsdk.HomeKeyManager;
import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.WallPaperManager;
import cn.com.geartech.gcordsdk.ui.views.ClockView;

public class WallPaperActivity extends Activity {

    public static WallPaperActivity wallPaperActivity = null;
    private LoopViewPager pager;
    private SharedPreferences pref;
    private WallPaperManager wallPaperManager;
    private ClockView text_large_time;
    public static final String HIDE_WALL_PAPER_ACTION = "cn.com.geartech.ACTION_HIDE_WALL_PAPER";

    protected int getContentViewRes() {
        return R.layout.activity_wall_paper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(GcordSDK.getInstance().getPowerManager().isScreenOnAllDay()){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            delayMillis = GcordSDK.getInstance().getWallPaperManager().getWallpaperSwitchInterval();
        }catch (Throwable e) {
            e.printStackTrace();
        }

        setContentView(getContentViewRes());

        wallPaperActivity = this;
        pref = GcordSDK.getInstance().getContext().getSharedPreferences(WallPaperManager.PREF_WALL_PAPER_TIME_IN_GCORD_SDK, Context.MODE_PRIVATE);
        nextHandler = new Handler(Looper.getMainLooper());
        wallPaperManager = GcordSDK.getInstance().getWallPaperManager();
        imageLoader.init(ImageLoaderConfiguration.createDefault(GcordSDK.getInstance().getContext()));
//        timeTextY = pref.getFloat(PREF_KEY_LARGE_TIMER_COORD_Y, 100.f);
        initViews();
        IntentFilter intentFilter = new IntentFilter(HIDE_WALL_PAPER_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (HIDE_WALL_PAPER_ACTION.equals(intent.getAction())) {
                hideWallPaper();
            }
        }
    };

    private HandleManager.HandleEventListener handleEventListener = new HandleManager.HandleEventListener() {
        @Override
        public void onHandlePickedUp() {
            hideWallPaper();
        }

        @Override
        public void onHandlePutDown() {
            hideWallPaper();
        }
    };

    private void initViews() {
        initTimeView();
        pager = (LoopViewPager) findViewById(R.id.pager);
        initPager();
        if (photosItems.size() > 1 && nextHandler != null) {
            nextHandler.postDelayed(nextRunnable, delayMillis);
        }
    }

//    private float timeTextX = -1f;
//    private final String PREF_KEY_LARGE_TIMER_COORD_Y = "PREF_KEY_LARGE_TIMER_COORD_Y";
//
//
//    private float getTimeTextY() {
//        return timeTextY;
//    }
//
//    private void setTimeTextY(float timeTextY) {
//
//        this.timeTextY = timeTextY;
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putFloat(PREF_KEY_LARGE_TIMER_COORD_Y, timeTextY);
//
//        editor.apply();
//    }

//    private float timeTextY = 0.0f;
    private HomeKeyManager.HOME_KEY_ACTION_TYPE homeKeyMode;

    @Override
    protected void onResume() {
        super.onResume();
        homeKeyMode = GcordSDK.getInstance().getHomeKeyManager().getHomeKeyActionType();
        GcordSDK.getInstance().getHomeKeyManager().setHomeKeyActionType(HomeKeyManager.HOME_KEY_ACTION_TYPE.CUSTOM_KEY_EVENT);
        GcordSDK.getInstance().getHomeKeyManager().addHomeKeyEventListener(homeKeyListener);
        GcordSDK.getInstance().getHandleManager().addHandleEventListener(handleEventListener);
    }

    HomeKeyManager.HomeKeyListener homeKeyListener = new HomeKeyManager.HomeKeyListener() {
        @Override
        public void onHomeClicked() {
            hideWallPaper();
        }

        @Override
        public void onResumedByHomeKey() {
            hideWallPaper();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        GcordSDK.getInstance().getHomeKeyManager().removeHomeKeyEventListener(homeKeyListener);
        GcordSDK.getInstance().getHomeKeyManager().setHomeKeyActionType(homeKeyMode);
        GcordSDK.getInstance().getHandleManager().removeHandleEventListener(handleEventListener);
    }

    private void initTimeView() {
        text_large_time = (ClockView) findViewById(R.id.text_large_time);
//        text_large_time.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_MOVE) {
//
//                    float x = event.getRawX() - v.getWidth() / 2.0f;
//                    float y = event.getRawY() - v.getHeight() / 2.0f;
//
//                    int h = text_large_time.getHeight();
//                    if (y > 0.0f && y + h / 2.0f + 50.0f < 1024.0f) {
//
//                        timeTextX = x;
//                        setTimeTextY(y);
//                        v.setY(y);
//                    }
//
//                }
//                return true;
//            }
//
//
//        });
//        if (getTimeTextY() != 0.0f) {
//
//            float x = timeTextX;
//            float y = getTimeTextY();
//
//            int h = text_large_time.getHeight();
////                    textTime.setX(x);
//            if (y > 0.0f && y + h / 2.0f + 50.0f < 1024.0f) {
//
//                timeTextX = x;
//                setTimeTextY(y);
//                text_large_time.setY(y);
//            }
//
//        } else {
//
//            text_large_time.setY(text_large_time.getY() + 100.0f);
//        }
    }

    private Runnable nextRunnable = new Runnable() {
        @Override
        public void run() {
            next();
        }
    };

    private ArrayList<String> photosItems = new ArrayList<>();
    private LoopPagerAdapter pagerAdapter = null;
    public static final String KEY_WALLPAPER_CURRENT_INDEX = "key_wallpaper_current_index";
    private Handler nextHandler = null;
    private long delayMillis = 15 * 1000;

    private void initPager() {
        try {
            this.photosItems = wallPaperManager.getPhotosItems();

            pagerAdapter = new LoopPagerAdapter(photosItems);

            if (photosItems.size() == 1) {
                pager.setPagingEnabled(false);
            } else {
                pager.setPagingEnabled(true);
            }

            pager.setBoundaryCaching(true);
            pager.setAdapter(pagerAdapter);
            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(final int position) {

                    pref.edit()
                            .putInt(KEY_WALLPAPER_CURRENT_INDEX, position).apply();

                    if (nextHandler != null) {
                        nextHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                postNext();
                            }
                        }, 500);

                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            pager.setCurrentItem(pref.getInt(KEY_WALLPAPER_CURRENT_INDEX, 0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void postNext() {

        if (nextHandler != null) {
            nextHandler.removeCallbacksAndMessages(null);
            nextHandler.postDelayed(nextRunnable, delayMillis);
        }

    }


    public void next() {
        try {
            if (pager != null) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        wallPaperActivity = null;
        pager = null;
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private class LoopPagerAdapter extends PagerAdapter {
        private List<String> photosItemList;

        LoopPagerAdapter(List<String> photosItemList) {
            if (photosItemList != null) {
                this.photosItemList = new ArrayList<>(photosItemList);
            } else {
                this.photosItemList = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return photosItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }

        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View v = LayoutInflater.from(GcordSDK.getInstance().getContext())
                    .inflate(R.layout.item_pager_image, container, false);
            ImageView image = (ImageView) v.findViewById(R.id.image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideWallPaper();
                }
            });

            if (photosItemList.size() > 0) {
                String path = photosItemList.get(position);
                try {
                    imageLoader.displayImage("file://" + path, image, options);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            container.addView(v, 0);
            return v;
        }
    }

    public void hideWallPaper() {
        try {
            if (!isFinishing()) {
                Intent intent = new Intent(WallPaperManager.ACTION_UNLOCK_SCREEN);
                intent.setPackage(GcordSDK.getInstance().getContext().getPackageName());
                sendBroadcast(intent);
                finish();
                wallPaperActivity = null;
            }
            wallPaperManager.onUserInteraction();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
