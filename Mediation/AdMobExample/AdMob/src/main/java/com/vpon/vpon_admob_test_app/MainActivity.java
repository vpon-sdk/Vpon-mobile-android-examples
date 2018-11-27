package com.vpon.vpon_admob_test_app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String LT = "Main-admob-nativeAd";

    private String ADMOB_AD_UNIT_ID = null;

    private UnifiedNativeAd localUnifiedNativeAd = null;

    private UnifiedNativeAd.OnUnifiedNativeAdLoadedListener unifiedNativeAdLoadedListener =
            new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {

                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    log("onUnifiedNativeAdLoaded invoked!!");
                    localUnifiedNativeAd = unifiedNativeAd;
                    if (adView != null) {
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    }
                }
            };

    private UnifiedNativeAdView adView = null;

    private TextView adConsole = null;

    private ScrollView logContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(getBaseContext(), getString(R.string.admob_app_id));
        setContentView(R.layout.activity_main);
        ADMOB_AD_UNIT_ID = getString(R.string.admob_native_adunit_id);


        refresh = findViewById(R.id.fire_native_ad);
        if (refresh != null) {
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshAd();
                }
            });
        }

        adView = findViewById(R.id.native_ad_container);
        adConsole = findViewById(R.id.ad_console);

        logContainer = findViewById(R.id.console_log_container);
    }

    private Button refresh = null;

    private void refreshAd() {
        log("refreshAd invoked!!");
        WeakReference<Activity>contextWeakReference = new WeakReference<Activity>(this);
            AdLoader.Builder builder = new AdLoader.Builder(contextWeakReference.get(), ADMOB_AD_UNIT_ID);
            builder.forUnifiedNativeAd(unifiedNativeAdLoadedListener)
                    .withAdListener(adListener);
            builder.build().loadAd(new AdRequest.Builder().build());

    }

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm:ss.SSS  ";
    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.TAIWAN);

    protected void log(final String message) {

        Log.e(LT, message);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adConsole != null) {
                        String sb;
                        if ("=============================".equals(message)) {
                            sb = message + "\n" + String.valueOf(adConsole.getText());
                        } else {
                            sb = sdf.format(calendar.getTime()) +
                                    message + "\n" + String.valueOf(adConsole.getText());

                        }
                        adConsole.setText(sb);
                    }
                    if (logContainer != null) {
                        logContainer.fullScroll(ScrollView.FOCUS_UP);
                    }
                }
            });
        }
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            log("onAdClosed invoked!!");
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            log("onAdFailedToLoad(" + errorCode + ") invoked!!");
            if (refresh != null) {
                refresh.setEnabled(true);
            }
            Toast.makeText(getBaseContext(), "Failed to load native ad: "
                    + errorCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLeftApplication() {
            log("onAdLeftApplication invoked!!");
        }

        @Override
        public void onAdOpened() {
            log("onAdOpened invoked!!");
        }

        @Override
        public void onAdLoaded() {
            log("onAdLoaded invoked!!");
            if(adView != null){
                adView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAdClicked() {
            log("onAdClicked invoked!!");
        }

        @Override
        public void onAdImpression() {
            Log.e(LT, "onAdImpression invoked!!");
        }
    };

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        MediaView mediaView = new MediaView(getApplicationContext());
        FrameLayout _container = adView.findViewById(R.id.ad_media_container);
        _container.addView(mediaView);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
