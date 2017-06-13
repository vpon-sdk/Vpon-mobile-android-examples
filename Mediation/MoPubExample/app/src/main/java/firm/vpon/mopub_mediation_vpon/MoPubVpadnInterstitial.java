package firm.vpon.mopub_mediation_vpon;

import android.content.Context;
import android.util.Log;
import android.app.Activity;
import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnInterstitialAd;
import com.mopub.mobileads.CustomEventInterstitial;
import java.util.Map;

import static com.mopub.mobileads.MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
import static com.mopub.mobileads.MoPubErrorCode.NETWORK_NO_FILL;


public class MoPubVpadnInterstitial extends CustomEventInterstitial {
    /*
     * These keys are intended for MoPub internal use. Do not modify.
     */
    private static final String AD_UNIT_ID_KEY = "adUnitID";
   
    private Activity activity;
    private CustomEventInterstitialListener mInterstitialListener;
    private VpadnInterstitialAd mVponInterstitialAd;

    @Override
    protected void loadInterstitial(
            final Context context,
            final CustomEventInterstitialListener customEventInterstitialListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mInterstitialListener = customEventInterstitialListener;
        String adUnitId = null;
        
        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
			adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
		}
        
        if(adUnitId == null){
        	Log.e("VPADN","adUnitId == null");
        	mInterstitialListener.onInterstitialFailed(ADAPTER_CONFIGURATION_ERROR);
        	return;
        }else{
        	Log.d("VPADN","adUnitId is "+adUnitId);
        }
        
        if(context instanceof Activity){
        	activity = (Activity) context;
        }else{
        	mInterstitialListener.onInterstitialFailed(ADAPTER_CONFIGURATION_ERROR);
        	Log.e("VPADN","context instanceof Activity is FALSE");
        	return;
        }
        
        mVponInterstitialAd = new VpadnInterstitialAd(activity,adUnitId,"TW");
        mVponInterstitialAd.setAdListener(new InterstitialAdListener());
       
        final VpadnAdRequest adRequest = new VpadnAdRequest();
        mVponInterstitialAd.loadAd(adRequest);
        
    }

    @Override
    protected void showInterstitial() {
        if (mVponInterstitialAd.isReady()) {
        	mVponInterstitialAd.show();
        } else {
            Log.d("VPADN", "Tried to show a VPON interstitial ad before it finished loading. Please try again.");
        }
    }

    @Override
    protected void onInvalidate() {
        if (mVponInterstitialAd != null) {
        	mVponInterstitialAd.setAdListener(null);
        }
    }

    

    private class InterstitialAdListener implements VpadnAdListener {

		@Override
		public void onVpadnDismissScreen(VpadnAd arg0) {
          Log.d("VPADN", "VPON interstitial ad dismissed.");
          if (mInterstitialListener != null) {
              mInterstitialListener.onInterstitialDismissed();
          }
			
		}

		@Override
		public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnErrorCode arg1) {
          Log.d("VPADN", "VPON interstitial ad failed to load.");
          if (mInterstitialListener != null) {
              mInterstitialListener.onInterstitialFailed(NETWORK_NO_FILL);
          }
		}

		@Override
		public void onVpadnLeaveApplication(VpadnAd arg0) {
          Log.d("VPADN", "VPON interstitial ad clicked.");
          if (mInterstitialListener != null) {
              mInterstitialListener.onInterstitialClicked();
          }
		}

		@Override
		public void onVpadnPresentScreen(VpadnAd arg0) {
          Log.d("VPADN", "Showing VPON interstitial ad.");
          if (mInterstitialListener != null) {
              mInterstitialListener.onInterstitialShown();
          }
			
		}

		@Override
		public void onVpadnReceiveAd(VpadnAd arg0) {
          Log.d("VPADN", "VPON interstitial ad loaded successfully.");
          if (mInterstitialListener != null) {
              mInterstitialListener.onInterstitialLoaded();
          }
		}
       
    }

    
}