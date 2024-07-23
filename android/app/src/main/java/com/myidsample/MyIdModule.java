package com.myidsample;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Locale;

import uz.myid.android.sdk.capture.MyIdBuildMode;
import uz.myid.android.sdk.capture.MyIdCameraShape;
import uz.myid.android.sdk.capture.MyIdClient;
import uz.myid.android.sdk.capture.MyIdConfig;
import uz.myid.android.sdk.capture.MyIdEntryType;
import uz.myid.android.sdk.capture.MyIdException;
import uz.myid.android.sdk.capture.MyIdResidentType;
import uz.myid.android.sdk.capture.MyIdResult;
import uz.myid.android.sdk.capture.MyIdResultListener;

public class MyIdModule extends ReactContextBaseJavaModule implements LifecycleEventListener, ActivityEventListener, MyIdResultListener {

    private final MyIdClient myIdClient = new MyIdClient();

    public MyIdModule(ReactApplicationContext reactContext) {
        super(reactContext);

        reactContext.addLifecycleEventListener(this);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "MyIdModule";
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent intent) {
        myIdClient.handleActivityResult(resultCode, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onSuccess(MyIdResult result) {
        WritableMap params = Arguments.createMap();
        params.putString("code", result.getCode());
        params.putDouble("comparison", result.getComparison());

        sendEvent("onSuccess", params);
    }

    @Override
    public void onError(MyIdException e) {
        WritableMap params = Arguments.createMap();
        params.putString("message", e.getMessage());
        params.putInt("code", e.getCode());

        sendEvent("onError", params);
    }

    @Override
    public void onUserExited() {
        WritableMap params = Arguments.createMap();
        params.putString("message", "User exited");

        sendEvent("onUserExited", params);
    }

    @ReactMethod
    public void startMyId() {
        MyIdConfig myIdConfig = new MyIdConfig.Builder("YOUR_CLIENT_ID")
                .withPassportData("AB1234567")
                .withBirthDate("01.09.1991")
                .withBuildMode(MyIdBuildMode.PRODUCTION)
                .withEntryType(MyIdEntryType.AUTH)
                .withResidency(MyIdResidentType.RESIDENT)
                .withLocale(new Locale("en"))
                .withCameraShape(MyIdCameraShape.CIRCLE)
                .withPhoto(false)
                .build();

        if (getCurrentActivity() != null) {
            myIdClient.startActivityForResult(getCurrentActivity(), 1, myIdConfig);
        }
    }

    private void sendEvent(
            String eventName,
            @Nullable WritableMap params
    ) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
