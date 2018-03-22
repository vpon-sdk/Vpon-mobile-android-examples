package com.example.vpadnsplash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSplashListener;
import com.vpadn.ads.VpadnSplashAd;

import java.util.HashSet;

public class SplashActivity extends Activity implements VpadnAdSplashListener {
    private static final String LT = "SplashActivity";
    private static final String yourPlacementId = "8a8081825fdd0a88015fe19f394909a9";
    private static final String yourTestDeviceGAID = "Please fill in your GAID";
    private VpadnSplashAd vpadnSplashAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout adsParent = (RelativeLayout) findViewById(R.id.splashContainer);

        vpadnSplashAd = new VpadnSplashAd(this, yourPlacementId, adsParent);
        vpadnSplashAd.setAdListener(this);
        VpadnAdRequest adRequest =  new VpadnAdRequest();
        HashSet<String> testDeviceImeiSet = new HashSet<String>();
        testDeviceImeiSet.add(yourTestDeviceGAID);
        adRequest.setTestDevices(testDeviceImeiSet);
        vpadnSplashAd.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vpadnSplashAd.destroy();
    }

    @Override
    public void onVpadnReceiveAd(VpadnAd ad) {
        Log.d(LT, "Call onVpadnReceiveAd");
    }

    @Override
    public void onVpadnFailedToReceiveAd(VpadnAd ad, VpadnAdRequest.VpadnErrorCode errorCode) {
        Log.d(LT, "Call onVpadnFailedToReceiveAd");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onVpadnClickAd(VpadnAd ad) {
        Log.e(LT, "Call onVpadnClickAd");
    }

    @Override
    public void onVpadnLeaveApplication(VpadnAd ad) {
        Log.e(LT, "Call onVpadnLeaveApplication");
    }

    // Note: Will only be notified once !!
    @Override
    public void onVpadnAllowToDismissAd(VpadnAd ad) {
        Log.d(LT, "Call onVpadnAllowToDismissAd");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
