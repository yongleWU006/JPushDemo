# 极光推送集成Demo
官方文档https://docs.jiguang.cn/jpush/client/Android/android_guide/

## 集成步骤
1. 注册极光推送开发者平台，创建应用，自动生成对应的AppKey
2. 下载SDK https://docs.jiguang.cn/jpush/resources/ 
3. 在自己的项目工程下创建与java文件夹同级的jniLibs文件夹，将arm文件copy到jinLibs下，将jcore和jpush两个jar包copy到lib下
4. gradle添加jcore和jpush两个jar包的依赖
```
compile fileTree(dir: 'libs', include: ['*.jar'])
或者
compile files('libs/jcore-android-1.1.8.jar')
compile files('libs/jpush-android-3.1.0.jar')
```
5. 在AndroidManifest配置极光服务，含有必需或required的都要配置，包名可用${applicationId}代替，前提是applicationId的value必需和manifest的package相同，可到build.gradle查看applicationId
```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="你的应用包名">

    <!-- Required 必需 -->
    <permission
        android:name="${applicationId}.permission.JPUSH_MESSAGE"
        android:protectionLevel="signature" />

    <!-- Required 必需 -->
    <uses-permission android:name="${applicationId}.permission.JPUSH_MESSAGE" />
    <uses-permission android:name="android.permission.RECEIVE_USER_PRESENT" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

    <!-- Optional. Required for location feature 必需-->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" /> <!-- 用于开启 debug 版本的应用在6.0 系统上 层叠窗口权限 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
    <uses-permission android:name="android.permission.GET_TASKS" />

    <application
        android:name="你的Application"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name="你的MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Required SDK 核心功能 必需-->
        <!-- 可配置android:process参数将PushService放在其他进程中 -->
        <service
            android:name="cn.jpush.android.service.PushService"
            android:enabled="true"
            android:exported="false" >
            <intent-filter>
                <action android:name="cn.jpush.android.intent.REGISTER" />
                <action android:name="cn.jpush.android.intent.REPORT" />
                <action android:name="cn.jpush.android.intent.PushService" />
                <action android:name="cn.jpush.android.intent.PUSH_TIME" />
            </intent-filter>
        </service>


        <!-- since 3.0.9 Required SDK 核心功能-->
        <provider
            android:authorities="${applicationId}.DataProvider"
            android:name="cn.jpush.android.service.DataProvider"
            android:exported="true"
            />

        <!-- since 1.8.0 option 可选项。用于同一设备中不同应用的JPush服务相互拉起的功能。 -->
        <!-- 若不启用该功能可删除该组件，将不拉起其他应用也不能被其他应用拉起 -->
        <service
            android:name="cn.jpush.android.service.DaemonService"
            android:enabled="true"
            android:exported="true">
            <intent-filter >
                <action android:name="cn.jpush.android.intent.DaemonService" />
                <category android:name="${applicationId}"/>
            </intent-filter>
        </service>

        <!-- Required SDK核心功能 必需-->
        <receiver
            android:name="cn.jpush.android.service.PushReceiver"
            android:enabled="true" >
            <intent-filter android:priority="1000">
                <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED_PROXY" />
                <category android:name="${applicationId}"/>
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.USER_PRESENT" />
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
            </intent-filter>
            <!-- Optional -->
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_ADDED" />
                <action android:name="android.intent.action.PACKAGE_REMOVED" />
                <data android:scheme="package" />
            </intent-filter>
        </receiver>

        <!-- Required SDK核心功能 必需-->
        <activity
            android:name="cn.jpush.android.ui.PushActivity"
            android:configChanges="orientation|keyboardHidden"
            android:theme="@android:style/Theme.NoTitleBar"
            android:exported="false" >
            <intent-filter>
                <action android:name="cn.jpush.android.ui.PushActivity" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="${applicationId}" />
            </intent-filter>
        </activity>
        <!-- SDK核心功能 必需-->
        <activity
            android:name="cn.jpush.android.ui.PopWinActivity"
            android:configChanges="orientation|keyboardHidden"
            android:exported="false"
            android:theme="@style/MyDialogStyle">
            <intent-filter>
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="${applicationId}" />
            </intent-filter>
        </activity>

        <!-- Required SDK核心功能 必需-->
        <service
            android:name="cn.jpush.android.service.DownloadService"
            android:enabled="true"
            android:exported="false" >
        </service>

        <!-- Required SDK核心功能 必需-->
        <receiver android:name="cn.jpush.android.service.AlarmReceiver" />

        <!-- Required since 3.0.7 必需-->
        <!-- 新的tag/alias接口结果返回需要开发者配置一个自定的广播 -->
        <!-- 该广播需要继承JPush提供的JPushMessageReceiver类, 并如下新增一个 Intent-Filter -->
        <receiver
            android:name="你的JpushReceiver"
            android:enabled="true" >
            <intent-filter>
                <action android:name="cn.jpush.android.intent.RECEIVE_MESSAGE" />
                <category android:name="${applicationId}" />
            </intent-filter>
        </receiver>

        <!-- User defined. 用户自定义的广播接收器-->
        <receiver
            android:name="你的BroadcastReceiver"
            android:enabled="true">
            <intent-filter>
                <!--Required 用户注册SDK的intent-->
                <action android:name="cn.jpush.android.intent.REGISTRATION" />
                <!--Required 用户接收SDK消息的intent-->
                <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" />
                <!--Required 用户接收SDK通知栏信息的intent-->
                <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" />
                <!--Required 用户打开自定义通知栏的intent-->
                <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" />
                <!-- 接收网络变化 连接/断开 since 1.6.3 -->
                <action android:name="cn.jpush.android.intent.CONNECTION" />
                <category android:name="${applicationId}" />
            </intent-filter>
        </receiver>

        <!-- Required. For publish channel feature 必需-->
        <!-- JPUSH_CHANNEL 是为了方便开发者统计APK分发渠道。-->
        <!-- 例如: -->
        <!-- 发到 Google Play 的APK可以设置为 google-play; -->
        <!-- 发到其他市场的 APK 可以设置为 xxx-market。 -->
        <meta-data android:name="JPUSH_CHANNEL" android:value="developer-default"/>
        <!-- Required. AppKey copied from Portal -->
        <meta-data android:name="JPUSH_APPKEY" android:value="你的AppKey"/>

    </application>

</manifest>
```
6. 在你自定义的application中调用
```
JPushInterface.setDebugMode(false);
JPushInterface.init(this);
```
7. 其他
```
  7.1 如果在极光官网推送消息不成功，弹出红色的消息框，则以上步骤配置漏缺
  7.2 如果在官网可以推送成功，但手机仍未收到推送，则是有些手机系统做了一些屏蔽操作，可到设置中的通知栏做处理
  7.3 该项目只是简单集成，若想实现更多功能可到官网查看
```
