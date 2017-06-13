package firm.vpon.mopub_mediation_vpon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

public class NativeActivity extends AppCompatActivity implements MoPubNative.MoPubNativeNetworkListener {

    //your MoPub PlacementId
    private static final String mNativePlacementId = "a0f981f50fb043e3a2ed7f6f7b5b250d";
    private MoPubNative mMoPubNative;
    private LinearLayout nativeAdContainer;
    private ViewBinder viewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);

        mMoPubNative = new MoPubNative(this, mNativePlacementId, this);
        viewBinder = new ViewBinder.Builder(R.layout.native_ad_unit)
                .mainImageId(R.id.nativeAdImage)
                .iconImageId(R.id.nativeAdIcon)
                .titleId(R.id.nativeAdTitle)
                .textId(R.id.nativeAdBody)
                .callToActionId(R.id.nativeAdCallToAction)
                .addExtra("socialContextForAd", R.id.nativeAdSocialContext)
                .build();
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        mMoPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
        mMoPubNative.makeRequest();
    }

    @Override
    public void onNativeLoad(NativeAd nativeAd) {
        Toast.makeText(getApplicationContext(), "Native successfully loaded.", Toast.LENGTH_SHORT).show();

        nativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {

            }

            @Override
            public void onClick(View view) {
                //The user clicks the native advertising for the first time
            }
        });
        View view = nativeAd.createAdView(NativeActivity.this, nativeAdContainer);
        nativeAd.renderAdView(view);
        nativeAd.prepare(view);
        nativeAdContainer.addView(view);
    }

    @Override
    public void onNativeFail(NativeErrorCode errorCode) {
        Toast.makeText(getApplicationContext(), "Native Failed loaded.", Toast.LENGTH_SHORT).show();
    }
}
