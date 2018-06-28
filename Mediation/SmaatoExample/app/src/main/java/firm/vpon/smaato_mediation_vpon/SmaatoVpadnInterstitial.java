package firm.vpon.smaato_mediation_vpon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.bannerutilities.constant.Values;
import com.smaato.soma.mediation.MediationEventInterstitial;

import java.util.Map;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnInterstitialAd;

public class SmaatoVpadnInterstitial extends MediationEventInterstitial {
    /*
     * These keys are intended for Smaato internal use. Do not modify.
     */
    private static final String AD_UNIT_ID_KEY = "BANNER_ID";

    private Activity activity;
    private MediationEventInterstitial.MediationEventInterstitialListener mInterstitialListener;
    private VpadnInterstitialAd mVponInterstitialAd;
    int width = 0;
    int height = 0;

    /**
     * The Method name could be changed as per the name given in the Smaato SPX portal, but the params should be fixed.
     */
    public void loadCustomInterstitial(Context context, MediationEventInterstitialListener mediationEventInterstitialListener, Map<String, String> serverBundle) {

        mInterstitialListener = mediationEventInterstitialListener;
        String adUnitId = null;

        if (!mediationInputsAreValid(serverBundle)) {
            mInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        if (serverBundle.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverBundle.get(AD_UNIT_ID_KEY);
        }

        if (adUnitId == null) {
            Log.e("VPADN", "adUnitId == null");
            mInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        } else {
            Log.d("VPADN", "adUnitId is " + adUnitId);
        }

        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            mInterstitialListener.onInterstitialFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            Log.e("VPADN", "context instanceof Activity is FALSE");
            return;
        }

        mVponInterstitialAd = new VpadnInterstitialAd(activity, adUnitId, "TW");
        mVponInterstitialAd.setAdListener(new InterstitialAdListener());

        final VpadnAdRequest adRequest = new VpadnAdRequest();
        mVponInterstitialAd.loadAd(adRequest);

    }

    @Override
    public void showInterstitial() {
        if (mVponInterstitialAd.isReady()) {
            mVponInterstitialAd.show();
        } else {
            Log.d("VPADN", "Tried to show a VPON interstitial ad before it finished loading. Please try again.");
        }
    }

    @Override
    public void onInvalidate() {
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
                mInterstitialListener.onInterstitialFailed(ErrorCode.NETWORK_NO_FILL);
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

    private boolean mediationInputsAreValid(Map<String, String> serverBundle) {
        try {
            if (serverBundle == null) return false;

            try {
                // Check and update whether widht and Height are needed for your custom Adapter
                if (serverBundle.get(Values.MEDIATION_WIDTH) != null && serverBundle.get(Values.MEDIATION_HEIGHT) != null) {
                    width = Integer.valueOf(serverBundle.get(Values.MEDIATION_WIDTH));
                    height = Integer.valueOf(serverBundle.get(Values.MEDIATION_HEIGHT));
                }
            } catch (Exception ex) {
                // check if width ht params are mandatory return false;
            }

            // ### Needs to be updated as per Custom Network Mandatory Fields
            if (serverBundle != null && !serverBundle.get(AD_UNIT_ID_KEY).isEmpty())
                return true;
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

}
