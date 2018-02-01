package com.vpon.vpadnnativesingle;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnMediaView;
import com.vpadn.ads.VpadnNativeAd;

import java.util.Arrays;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements VpadnAdListener {

    protected final static String LT = MainActivity.class.getSimpleName();
    private static final String NATIVEID = "YOUR PLACEMENT ID"; // kelly
    private LinearLayout native_Ad_Container;
    private LinearLayout nativeAdView;
    protected VpadnNativeAd nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNativeUI();
        nativeAd = new VpadnNativeAd(this, NATIVEID, "TW");
        nativeAd.setAdListener(this);

        /** Request Test Ad*/
        VpadnAdRequest adRequest = new VpadnAdRequest();
        HashSet<String> testDeviceImeiSet = new HashSet<String>();
        testDeviceImeiSet.add("YOUR GOOGLE ADVERTISING ID");
        adRequest.setTestDevices(testDeviceImeiSet);

        nativeAd.loadAd(adRequest);
    }

    private void loadNativeUI() {
        native_Ad_Container = (LinearLayout) findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        nativeAdView = (LinearLayout) inflater.inflate(R.layout.native_ad_unit, native_Ad_Container, false);
        native_Ad_Container.addView(nativeAdView);
    }

    @Override
    public void onVpadnReceiveAd(VpadnAd ad) {
        if (nativeAd == null || nativeAd != ad) {
            Log.e(LT, "Race condition, load() called again before last ad was displayed");
            return;
        }

        if (ad == nativeAd) {

            nativeAd.unregisterView();
            inflateAd(nativeAd, nativeAdView, this);
            nativeAd.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        switch (view.getId()) {
                            case R.id.nativeAdCallToAction:
                                Log.e(LT, "nativeAdCallToAction");
                                Toast.makeText(getBaseContext(), "nativeAdCallToAction Clicked", Toast.LENGTH_SHORT).show();
                                break;
//                            case R.id.nativeAdImage:
                            case R.id.native_ad_media:
                                Log.e(LT, "nativeAdImage");
                                Toast.makeText(getBaseContext(), "nativeAdCallToAction Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.d(LT, "Other ad component clicked");
                                Toast.makeText(getBaseContext(), "Other ad component Clicked", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });

            native_Ad_Container.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVpadnFailedToReceiveAd(VpadnAd vpadnAd, VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
        Log.e(LT, "CALL NativeAd onVpadnFailedToReceiveAd, " + "errorCode : " + vpadnErrorCode );
    }

    @Override
    public void onVpadnPresentScreen(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnPresentScreen");
    }

    @Override
    public void onVpadnDismissScreen(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnDismissScreen");
    }

    @Override
    public void onVpadnLeaveApplication(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnLeaveApplication");
    }

    protected static void inflateAd(VpadnNativeAd nativeAd, View nativeAdView, Activity mContext) {
        //Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) nativeAdView.findViewById(R.id.nativeAdIcon);
        TextView nativeAdTitle = (TextView) nativeAdView.findViewById(R.id.nativeAdTitle);
        TextView nativeAdBody = (TextView) nativeAdView.findViewById(R.id.nativeAdBody);
//        ImageView nativeAdImage = (ImageView) nativeAdView.findViewById(R.id.nativeAdImage);
        VpadnMediaView nativeAdMedia = (VpadnMediaView) nativeAdView.findViewById(R.id.native_ad_media);
        RatingBar nativeAdStarRating = (RatingBar) nativeAdView.findViewById(R.id.nativeAdStarRating);
        TextView nativeAdSocialContext = (TextView) nativeAdView.findViewById(R.id.nativeAdSocialContext);
        Button nativeAdCallToAction = (Button) nativeAdView.findViewById(R.id.nativeAdCallToAction);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        VpadnNativeAd.Rating rating = nativeAd.getAdStarRating();
        if (rating != null) {
            nativeAdStarRating.setNumStars((int) rating.getScale());
            nativeAdStarRating.setRating((float) rating.getValue());
        } else {
            nativeAdStarRating.setVisibility(View.GONE);
        }

        // Downloading and setting the ad icon.
        VpadnNativeAd.Image adIcon = nativeAd.getAdIcon();
        VpadnNativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        VpadnNativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        int bannerWidth = adCoverImage.getWidth();
        int bannerHeight = adCoverImage.getHeight();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
//        nativeAdImage.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int) (((double) screenWidth / (double) bannerWidth) * bannerHeight)));
//        VpadnNativeAd.downloadAndDisplayImage(adCoverImage, nativeAdImage);
        nativeAdMedia.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int) (((double) screenWidth / (double) bannerWidth) * bannerHeight)));
        nativeAdMedia.setNativedAd(nativeAd);

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable.
//		nativeAd.registerViewForInteraction(nativeAdView);

        // You can replace the above call with the following call to specify the clickable areas.
//        nativeAd.registerViewForInteraction(nativeAdView, Arrays.asList(nativeAdCallToAction, nativeAdImage));
        nativeAd.registerViewForInteraction(nativeAdView, Arrays.asList(nativeAdCallToAction, nativeAdMedia));


    }
}
