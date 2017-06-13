package com.example.vpadnbanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

public class MainActivity extends Activity {

	private RelativeLayout adBannerLayout;
	private VpadnBanner vpadnBanner = null;
	// TODO: input Vpadn Banner ID (licenseKey)
	private String bannerId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
		vpadnBanner = new VpadnBanner(this, bannerId, VpadnAdSize.SMART_BANNER,
				"TW");

		VpadnAdRequest adRequest = new VpadnAdRequest();
		adRequest.setEnableAutoRefresh(true);
		vpadnBanner.loadAd(adRequest);

		adBannerLayout.addView(vpadnBanner);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (vpadnBanner != null) {
			vpadnBanner.destroy();
			vpadnBanner = null;
		}
	}

}
