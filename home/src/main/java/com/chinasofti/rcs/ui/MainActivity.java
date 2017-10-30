package com.chinasofti.rcs.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.chinasofti.common.utils.log.YLog;
import com.chinasofti.rcs.R;
import com.chinasofti.rcs.entity.TabEntity;
import com.chinasofti.rcs.tab.CommonTabLayout;
import com.chinasofti.rcs.tab.listener.CustomTabEntity;
import com.chinasofti.rcs.tab.listener.OnTabSelectListener;
import com.chinasofti.rcs.tab.utils.UnreadMsgUtils;
import com.chinasofti.rcs.tab.widget.MsgView;
import com.chinasofti.rcs.utils.ViewFindUtils;
import com.didi.virtualapk.PluginManager;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
import dalvik.system.DexClassLoader;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private Context mContext;


    Random mRandom = new Random();
    private String[] mTitles = {"消息", "通讯录", "通话", "我"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};


    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout;

    private Fragment messageFragment;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_layout);
        requestPermissions();
    }

    private void initUI() {
        mContext = this;
        for (int i = 0; i < mTitles.length; i++) {
            if (i == 0) {
                mFragments.add(messageFragment);
            } else {
                mFragments.add(SimpleFragment.getInstance(mTitles[i]));
            }
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.vp);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        /** with ViewPager */
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl);

        initViewPager();
    }

    private void initViewPager() {
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mTabLayout.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);

        //角标数字
        initAngleNumber();
    }

    private void initAngleNumber() {
        //两位数
        mTabLayout.showMsg(0, 55);
        mTabLayout.setMsgMargin(0, -5, 5);

        //三位数
        mTabLayout.showMsg(1, 100);
        mTabLayout.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        mTabLayout.showDot(2);
        MsgView rtv_2 = mTabLayout.getMsgView(2);
        if (rtv_2 != null) {
            UnreadMsgUtils.setSize(rtv_2, dp2px(7.5f));
        }

        //设置未读消息背景
        mTabLayout.showMsg(3, 5);
        mTabLayout.setMsgMargin(3, 0, 5);
        MsgView rtv_3 = mTabLayout.getMsgView(3);
        if (rtv_3 != null) {
            rtv_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private int count = 0;

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            YLog.e(TAG, permission.name + " is granted.");
                            count++;
                            if (count == 2) {
                                loadPlugin(MainActivity.this);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            YLog.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            YLog.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    private void loadPlugin(Context base) {
        PluginManager pluginManager = PluginManager.getInstance(base);
        File plugin_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File apk_origin = new File(plugin_dir, "message.apk");
        YLog.i(TAG, "apk:" + apk_origin);
        if (apk_origin.exists()) {
            try {
                long startTime = System.currentTimeMillis();
                pluginManager.loadPlugin(apk_origin);
                YLog.e("###loadplugin time = " + (System.currentTimeMillis() - startTime) + "ms");

                Intent intent = new Intent();
                intent.setClassName("com.chinasofti.message", "com.chinasofti.message.ImageService");
                startService(intent);

                Context ctx = null;
                try {
                    File optimizedApkPath = getDir("plugin",Context.MODE_PRIVATE);
                    DexClassLoader classLoader = new DexClassLoader(plugin_dir + "/message.apk",
                            optimizedApkPath.getAbsolutePath(),
                            null,
                            getClassLoader());
                    Class mLoadClass = classLoader.loadClass("com.chinasofti.message.MessageFragment");
                    Constructor constructor = mLoadClass.getConstructor(new Class[]{});
                    Object obj = constructor.newInstance(new Object[]{});
                    messageFragment = (Fragment) obj;
                    initUI();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            YLog.e(TAG, "apk不存在...");
        }
    }
}
