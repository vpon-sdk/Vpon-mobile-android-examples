package com.example.vpadninterstitial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInterstitialAd;

//TODO: Implement VpadnAdListener
public class MainActivity extends Activity implements VpadnAdListener {

    private Button getInterstitialButton;
    private Button showInterstitialButton;
    // TODO: input Vpadn Banner ID (licenseKey)
    private String interstitialBannerId = "";
    private VpadnInterstitialAd interstitialAd;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remember to call the destroy() when your app is destroying.
        if (interstitialAd != null) {
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        getInterstitialButton.setEnabled(true);
        showInterstitialButton.setEnabled(false);

        getInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInterstitialButton.setEnabled(false);
                getInterstitialButton.setText("INTERSTITIAL LOADING...");
                interstitialAd = new VpadnInterstitialAd(MainActivity.this, interstitialBannerId, "TW");
                interstitialAd.setAdListener(MainActivity.this);
                interstitialAd.loadAd(new VpadnAdRequest());
            }
        });

        showInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInterstitialButton.setEnabled(true);
                showInterstitialButton.setEnabled(false);
                if(interstitialAd.isReady()) {
                    interstitialAd.show();
                }
            }
        });
    }

    private void findView() {
        getInterstitialButton = (Button) findViewById(R.id.getadlbtn);
        showInterstitialButton = (Button) findViewById(R.id.showadbtn);
    }

    // Interface VpadnAdListener method
    @Override
    public void onVpadnReceiveAd(VpadnAd ad) {
        if (ad == this.interstitialAd) {
            Log.d("Interstitial", "VpadnReceiveAd");
            showInterstitialButton.setEnabled(true);
            getInterstitialButton.setText("GET INTERSTITIAL");
        }
    }

    // Interface VpadnAdListener method
    @Override
    public void onVpadnDismissScreen(VpadnAd arg0) {
        Log.d("Interstitial", "VpadnDismissScreen");

    }

    // Interface VpadnAdListener method
    @Override
    public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnAdRequest.VpadnErrorCode arg1) {
        Log.d("Interstitial", "failed to receive ad (" + arg1 + ")");

    }

    // Interface VpadnAdListener method
    @Override
    public void onVpadnLeaveApplication(VpadnAd arg0) {
        Log.d("Interstitial", "VpadnLeaveApplication");

    }

    // Interface VpadnAdListener method
    @Override
    public void onVpadnPresentScreen(VpadnAd arg0) {
        Log.d("Interstitial", "VpadnPresentScreen");

    }

}
