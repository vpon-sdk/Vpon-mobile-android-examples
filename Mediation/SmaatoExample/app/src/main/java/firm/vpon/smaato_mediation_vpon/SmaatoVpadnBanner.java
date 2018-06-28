package firm.vpon.smaato_mediation_vpon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.bannerutilities.constant.Values;
import com.smaato.soma.mediation.MediationEventBanner;
import com.smaato.soma.mediation.Views;

import java.util.Map;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

public class SmaatoVpadnBanner extends MediationEventBanner {
    /*
     * These keys are intended for Smaato internal use. Do not modify.
     */
    private static final String AD_UNIT_ID_KEY = "BANNER_ID";


    private MediationEventBannerListener mBannerListener;

    private Activity activity;
    private VpadnBanner vponBanner;
    int width = 0;
    int height = 0;

    /**
     * The Method name could be changed as per the name given in the Smaato SPX portal, but the params should be fixed.
     */
    public void loadCustomBanner(Context context, MediationEventBannerListener mediationEventBannerListener, Map<String, String> serverBundle) {

        mBannerListener = mediationEventBannerListener;
        String adUnitId = null;

        if (!mediationInputsAreValid(serverBundle)) {
            mBannerListener.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        if (serverBundle.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverBundle.get(AD_UNIT_ID_KEY);
        }

        if (adUnitId == null) {
            Log.e("VPADN", "adUnitId == null");
            mBannerListener.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        } else {
            Log.d("VPADN", "adUnitId is " + adUnitId);
        }

        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            Log.e("VPADN", "context instanceof Activity is FALSE");
            mBannerListener.onBannerFailed(ErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        vponBanner = new VpadnBanner(activity, adUnitId, VpadnAdSize.SMART_BANNER, "TW");
        vponBanner.setAdListener(new AdViewListener());
        final VpadnAdRequest adRequest = new VpadnAdRequest();
        vponBanner.loadAd(adRequest);
    }

    @Override
    public void onInvalidate() {
        Views.removeFromParent(vponBanner);
        if (vponBanner != null) {
            vponBanner.setAdListener(null);
            vponBanner.destroy();
        }
    }

    private class AdViewListener implements VpadnAdListener {

        @Override
        public void onVpadnDismissScreen(VpadnAd arg0) {
        }

        @Override
        public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnErrorCode arg1) {
            Log.d("VPADN", "Vpon banner ad failed to load.");
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(ErrorCode.NETWORK_NO_FILL);
            }
        }

        @Override
        public void onVpadnLeaveApplication(VpadnAd arg0) {
        }

        @Override
        public void onVpadnPresentScreen(VpadnAd arg0) {
            Log.d("VPADN", "Vpon banner ad clicked.");
            if (mBannerListener != null) {
                mBannerListener.onBannerClicked();
            }
        }

        @Override
        public void onVpadnReceiveAd(VpadnAd arg0) {
            Log.d("VPADN", "Vpon banner ad loaded successfully. Showing ad...");
            if (mBannerListener != null) {
                mBannerListener.onReceiveAd(vponBanner);
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
