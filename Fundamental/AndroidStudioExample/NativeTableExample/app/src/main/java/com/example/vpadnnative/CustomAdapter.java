package com.example.vpadnnative;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.vpadn.ads.VpadnNativeAdsManager;

import java.util.Arrays;

public class CustomAdapter extends RecyclerView.Adapter implements VpadnAdListener{
    private static final String LT = CustomAdapter.class.getSimpleName();
    private static final int AD_VIEW_INTERVAL = 4;
    private Activity mContext;
    private VpadnNativeAdsManager nativeAdsManager;
    private String[] mDataSet;
    public enum ViewType {
        ADVERTISING(0), CONTENT(1);
        private int value;
        private ViewType(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public CustomAdapter(Activity context, String[]dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                v=LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_unit, parent, false);
                viewHolder = new VponNativeViewHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
                viewHolder = new PublisherViewHolder(v);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Log.d(LT, "Element " + position + " set.");
        int offset = (position+1)/(AD_VIEW_INTERVAL +1);
        int yourDataSetIndex = position - offset;

        ViewType viewType = getViewType(AD_VIEW_INTERVAL,position);
        switch (viewType) {
            case ADVERTISING:
                VpadnNativeAd vpadnNativeAd = nativeAdsManager.nextNativeAd();
                if (vpadnNativeAd != null) {
                    inflateAd(vpadnNativeAd, (VponNativeViewHolder)viewHolder);
                    registerVpadnNativeAdOnTouchListener(vpadnNativeAd);
                }
                break;
            case CONTENT:
                // Get element from your dataset at this position and replace the contents of the view
                // with that element
                ((PublisherViewHolder)viewHolder).getTextView().setText(mDataSet[yourDataSetIndex]);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2(number) depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
//        return position % 4 ;
        ViewType viewType = getViewType(AD_VIEW_INTERVAL,position);
        switch (viewType){
            case ADVERTISING :
                return viewType.getValue();
            case CONTENT :
                return viewType.getValue();
            default:
                break;
        }
        return position;
    }

    private ViewType getViewType(int adViewInterval, int position){
        boolean isAdType = ((position%(adViewInterval+1))==adViewInterval)?true:false;
        if(isAdType){
            return ViewType.ADVERTISING;
        }else{
            return ViewType.CONTENT;
        }
    }
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class PublisherViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public PublisherViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LT, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public static class VponNativeViewHolder extends RecyclerView.ViewHolder {

        private final View nativeAdView;
        private final ImageView nativeAdIcon;
        private final TextView nativeAdTitle;
        private final TextView nativeAdBody;
//        private final ImageView nativeAdImage;
        private final VpadnMediaView nativeAdMedia;
        private final RatingBar nativeAdStarRating;
        private final TextView nativeAdSocialContext;
        private final Button nativeAdCallToAction;

        public VponNativeViewHolder(View nativeAdView) {
            super(nativeAdView);
            this.nativeAdView = nativeAdView;
            //Create native UI using the ad metadata.
            nativeAdIcon = (ImageView) nativeAdView.findViewById(R.id.nativeAdIcon);
            nativeAdTitle = (TextView) nativeAdView.findViewById(R.id.nativeAdTitle);
            nativeAdBody = (TextView) nativeAdView.findViewById(R.id.nativeAdBody);
//            nativeAdImage = (ImageView) nativeAdView.findViewById(R.id.nativeAdImage);
            nativeAdMedia = (VpadnMediaView) nativeAdView.findViewById(R.id.native_ad_media);
            nativeAdStarRating = (RatingBar) nativeAdView.findViewById(R.id.nativeAdStarRating);
            nativeAdSocialContext = (TextView) nativeAdView.findViewById(R.id.nativeAdSocialContext);
            nativeAdCallToAction = (Button) nativeAdView.findViewById(R.id.nativeAdCallToAction);
        }

        public View getNativeAdView() {
            return nativeAdView;
        }

        public ImageView getNativeAdIcon() {
            return nativeAdIcon;
        }

        public TextView getNativeAdTitle() {
            return nativeAdTitle;
        }

        public TextView getNativeAdBody() {
            return nativeAdBody;
        }

//        public ImageView getNativeAdImage() {
//            return nativeAdImage;
//        }

        public VpadnMediaView getNativeAdMedia() {
            return nativeAdMedia;
        }

        public RatingBar getNativeAdStarRating() {
            return nativeAdStarRating;
        }

        public TextView getNativeAdSocialContext() {
            return nativeAdSocialContext;
        }

        public Button getNativeAdCallToAction() {
            return nativeAdCallToAction;
        }

    }

    @Override
    public void onVpadnReceiveAd(VpadnAd ad) {
        Log.e(LT, "CALL NativeAd onVpadnReceiveAd");
    }

    @Override
    public void onVpadnFailedToReceiveAd(VpadnAd vpadnAd, VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
        Log.e(LT, "CALL NativeAd onVpadnFailedToReceiveAd, " + "errorCode : " + vpadnErrorCode );
    }

    @Override
    public void onVpadnPresentScreen(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnPresentScreen");
        Toast.makeText(mContext, "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVpadnDismissScreen(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnDismissScreen");

    }

    @Override
    public void onVpadnLeaveApplication(VpadnAd vpadnAd) {
        Log.e(LT, "CALL NativeAd onVpadnLeaveApplication");
    }

    private void inflateAd(VpadnNativeAd vpadnNativeAd, VponNativeViewHolder vponNativeViewHolder) {

        vponNativeViewHolder.getNativeAdTitle().setText("");
        vponNativeViewHolder.getNativeAdBody().setText("");
        vponNativeViewHolder.getNativeAdSocialContext().setText("");
        vponNativeViewHolder.getNativeAdCallToAction().setText("");
        vponNativeViewHolder.getNativeAdStarRating().setNumStars(5);
        vponNativeViewHolder.getNativeAdStarRating().setRating(0);
        vponNativeViewHolder.getNativeAdIcon().setImageBitmap(null);
//        vponNativeViewHolder.getNativeAdImage().setImageBitmap(null);
//        vponNativeViewHolder.getNativeAdMedia().setImageBitmap(null);

        if(vpadnNativeAd != null){
//            vpadnNativeAd.unregisterView();
            vpadnNativeAd.setAdListener(CustomAdapter.this);

            vponNativeViewHolder.getNativeAdTitle().setText(vpadnNativeAd.getAdTitle());
            vponNativeViewHolder.getNativeAdBody().setText(vpadnNativeAd.getAdBody());
            vponNativeViewHolder.getNativeAdSocialContext().setText(vpadnNativeAd.getAdSocialContext());
            vponNativeViewHolder.getNativeAdCallToAction().setText(vpadnNativeAd.getAdCallToAction());

            RatingBar nativeAdStarRating = vponNativeViewHolder.getNativeAdStarRating();
            VpadnNativeAd.Rating rating = vpadnNativeAd.getAdStarRating();
            if (rating != null) {
                nativeAdStarRating.setNumStars((int) rating.getScale());
                nativeAdStarRating.setRating((float) rating.getValue());
            } else {
                nativeAdStarRating.setVisibility(View.GONE);
            }

            // Downloading and setting the ad icon.
            ImageView nativeAdIcon = vponNativeViewHolder.getNativeAdIcon();
            VpadnNativeAd.Image adIcon = vpadnNativeAd.getAdIcon();
            VpadnNativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

            // Downloading and setting the cover image.
//            ImageView nativeAdImage = vponNativeViewHolder.getNativeAdImage();
            VpadnMediaView nativeAdMedia = vponNativeViewHolder.getNativeAdMedia();
            VpadnNativeAd.Image adCoverImage = vpadnNativeAd.getAdCoverImage();
            int bannerWidth = adCoverImage.getWidth();
            int bannerHeight = adCoverImage.getHeight();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
//            nativeAdImage.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int) (((double) screenWidth / (double) bannerWidth) * bannerHeight)));
//            VpadnNativeAd.downloadAndDisplayImage(adCoverImage, nativeAdImage);
            nativeAdMedia.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, (int) (((double) screenWidth / (double) bannerWidth) * bannerHeight)));
            nativeAdMedia.setNativedAd(vpadnNativeAd);

            // Wire up the View with the native ad, the whole nativeAdContainer will be clickable.
//		    vpadnNativeAd.registerViewForInteraction(vponNativeViewHolder.getNativeAdView());

            // You can replace the above call with the following call to specify the clickable areas.
//            vpadnNativeAd.registerViewForInteraction(vponNativeViewHolder.getNativeAdView(), Arrays.asList(vponNativeViewHolder.getNativeAdCallToAction(), nativeAdImage));
            vpadnNativeAd.registerViewForInteraction(vponNativeViewHolder.getNativeAdView(), Arrays.asList(vponNativeViewHolder.getNativeAdCallToAction(), nativeAdMedia));
        }
    }

    private void registerVpadnNativeAdOnTouchListener(VpadnNativeAd vpadnNativeAd) {
        vpadnNativeAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.nativeAdCallToAction:
                            Log.d(LT, "nativeAdCallToAction clicked");
                            Toast.makeText(mContext, "nativeAdCallToAction Clicked", Toast.LENGTH_SHORT).show();
                            break;
//                        case R.id.nativeAdImage:
                        case R.id.native_ad_media:
                            Log.d(LT, "nativeAdImage clicked");
                            Toast.makeText(mContext, "nativeAdImage Clicked", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.d(LT, "Other ad component clicked");
                    }
                }
                return false;
            }
        });
    }

    public void setDataSet(String[] dataSet){
        mDataSet = dataSet;
        this.notifyItemRangeInserted(mDataSet.length-30, mDataSet.length-1);
    }

    public void setNativeAdsManager(VpadnNativeAdsManager nativeAdsManager) {
        this.nativeAdsManager = nativeAdsManager;
    }
}