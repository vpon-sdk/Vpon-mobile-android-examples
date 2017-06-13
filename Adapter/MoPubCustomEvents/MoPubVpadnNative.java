package murrray.shay.mopub_mediation_vpon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnNativeAd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

public class MoPubVpadnNative extends CustomEventNative {

    private static final String AD_UNIT_ID_KEY = "adUnitID";

    // CustomEventNative implementation
    @Override
    protected void loadNativeAd(final Context context,
            final CustomEventNativeListener customEventNativeListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {

        final String placementId;
        if (extrasAreValid(serverExtras)) {
            placementId = serverExtras.get(AD_UNIT_ID_KEY);
            Log.d("VPADN","adUnitId is " + placementId);
        } else {
            Log.e("VPADN","adUnitId == null");
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final VponStaticNativeAd vponStaticNativeAd = new VponStaticNativeAd(
                context,
                new VpadnNativeAd((Activity)context, placementId),
                customEventNativeListener);

        vponStaticNativeAd.loadAd();
    }


    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        final String placementId = serverExtras.get(AD_UNIT_ID_KEY);
        return (placementId != null && placementId.length() > 0);
    }

    /**
     * This class will implement a MoPub Native Ad
     * using a native ad from another network.
     */
    static class VponStaticNativeAd extends StaticNativeAd implements VpadnAdListener {

        private static final double MIN_STAR_RATING = 0;
        private static final double MAX_STAR_RATING = 5;
        private static final String SOCIAL_CONTEXT_FOR_AD = "socialContextForAd";

        private final Context mContext;
        private final VpadnNativeAd mNativeAd;
        private final CustomEventNativeListener mCustomEventNativeListener; //不知道是幹嘛得

        VponStaticNativeAd(final Context context,
                           final VpadnNativeAd nativeAd,
                           final CustomEventNativeListener customEventNativeListener) {
            mContext = context.getApplicationContext();
            mNativeAd = nativeAd;
            mCustomEventNativeListener = customEventNativeListener;
        }

        void loadAd(){
            mNativeAd.setAdListener(this);
            /** Request Test Ad*/
            VpadnAdRequest adRequest = new VpadnAdRequest();
            HashSet<String> testDeviceImeiSet = new HashSet<String>();
            testDeviceImeiSet.add("f68c6091-698a-49df-8989-3bb1649840f7"); //insert your device's gaid
            adRequest.setTestDevices(testDeviceImeiSet);
            mNativeAd.loadAd(adRequest);
        }

        // VpadnAdListener
        @Override
        public void onVpadnReceiveAd(VpadnAd ad) {
            if (mNativeAd == null || mNativeAd != ad) {
                Log.e("VPADN", "Race condition, load() called again before last ad was displayed");
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                return;
            }

            setTitle(mNativeAd.getAdTitle());
            setText(mNativeAd.getAdBody());

            final VpadnNativeAd.Image coverImage = mNativeAd.getAdCoverImage();
            setMainImageUrl(coverImage == null ? null : coverImage.getUrl());

            final VpadnNativeAd.Image icon = mNativeAd.getAdIcon();
            setIconImageUrl(icon == null ? null : icon.getUrl());

            setCallToAction(mNativeAd.getAdCallToAction());
            setStarRating(getDoubleRating(mNativeAd.getAdStarRating()));

            addExtra(SOCIAL_CONTEXT_FOR_AD, mNativeAd.getAdSocialContext());

            final List<String> imageUrls = new ArrayList<String>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(getMainImageUrl());
            }
            final String iconUrl = getIconImageUrl();
            if (iconUrl != null) {
                imageUrls.add(getIconImageUrl());
            }

            preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
                @Override
                public void onImagesCached() {
                    Log.d("VPADN", "Vpon banner ad loaded successfully. Showing ad...");
                    mCustomEventNativeListener.onNativeAdLoaded(VponStaticNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(NativeErrorCode errorCode) {
                    Log.d("VPADN", "Vpon Native ad failed to load because call onImagesFailedToCache");
                    mCustomEventNativeListener.onNativeAdFailed(errorCode);
                }
            });
        }

        @Override
        public void onVpadnFailedToReceiveAd(VpadnAd vpadnAd, VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
            Log.d("VPADN", "Vpon Native ad failed to load.");
            if (vpadnErrorCode == null || vpadnErrorCode.equals("INTERNAL_ERROR")) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
            } else if (vpadnErrorCode.equals("NO_FILL")) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
            } else if (vpadnErrorCode.equals("INVALID_REQUEST")) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_REQUEST_URL);
            } else if (vpadnErrorCode.equals("NETWORK_ERROR")) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
            } else {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
            }
        }

        @Override
        public void onVpadnPresentScreen(VpadnAd vpadnAd) {
            Log.d("VPADN", "Vpon native ad clicked.");
            notifyAdClicked();

        }

        @Override
        public void onVpadnDismissScreen(VpadnAd vpadnAd) {}

        @Override
        public void onVpadnLeaveApplication(VpadnAd vpadnAd) {}

        private Double getDoubleRating(final VpadnNativeAd.Rating rating) {
            if (rating == null) {
                return null;
            }

            return MAX_STAR_RATING * rating.getValue() / rating.getScale();
        }

        // BaseForwardingNativeAd
        @Override
        public void prepare(final View view) {
            mNativeAd.registerViewForInteraction(view);
        }

        @Override
        public void clear(final View view) {
            mNativeAd.unregisterView();
        }

        @Override
        public void destroy() {
            mNativeAd.destroy();
        }
    }

}
