package com.example.vpadninterstitial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnInterstitialAd;

//TODO: Implement VpadnAdListener
public class MainActivity extends Activity implements VpadnAdListener {

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
		interstitialAd = new VpadnInterstitialAd(this, interstitialBannerId, "TW");
		interstitialAd.setAdListener(this);
		interstitialAd.loadAd(new VpadnAdRequest());
	}

	// Interface VpadnAdListener method
	@Override
	public void onVpadnReceiveAd(VpadnAd ad) {
		if (ad == this.interstitialAd) {
			Log.d("Interstitial", "VpadnReceiveAd");
			interstitialAd.show();	
		}
	}

	// Interface VpadnAdListener method
	@Override
	public void onVpadnDismissScreen(VpadnAd arg0) {
		Log.d("Interstitial", "VpadnDismissScreen");

	}

	// Interface VpadnAdListener method
	@Override
	public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnErrorCode arg1) {
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
