package com.oracle.kingdom.jpushdemo;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/12/22/022.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

    }

}
