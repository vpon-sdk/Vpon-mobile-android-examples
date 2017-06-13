package com.example.finnvpon.newadmob;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;



public class MainActivity extends Activity {

    // This sample present banner and interstitial ads by button clicks
    // doBannerAd and doInterstitial methods are just for our convenience
    // You can include the code inside doMethods to anywhere as you like
    private AdView adView;
    private InterstitialAd interstitial;
    private Button button1;
    private Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adView.
        adView = new AdView(this);
        adView.setAdUnitId("SET_YOUR_AD_UNIT_ID_HERE");
        adView.setAdSize(AdSize.BANNER);

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout".
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);

        // Add the adView to it.
        layout.addView(adView);

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //do banner functoin
                doBannerAd();
            }
        });

        //press button to make an interstitial
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
            //do interstitial function
            doInterstitialAd();
            }
        });
    }

    public void doBannerAd(){
        // Initiate a generic request.
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("984DCE0208DDBFAD7C2C135BF5524ABA").build();
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load the adView with the ad request.
        adView.loadAd(adRequest);
    }


    private void doInterstitialAd() {
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("SET_YOUR_AD_UNIT_ID_HERE");
        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                Log.d("Vpon_Interstitial", "onAdLoaded");
                interstitial.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String message = String.format("onAdFailedToLoad (%s)", getErrorReason(errorCode));
                Log.d("Vpon_Interstitial", message);

            }
        });
        interstitial.loadAd(adRequest);
    }


    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    /** Gets a string error reason from an error code. */
    private String getErrorReason(int errorCode) {
        String errorReason = "";
        switch(errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    }

}
