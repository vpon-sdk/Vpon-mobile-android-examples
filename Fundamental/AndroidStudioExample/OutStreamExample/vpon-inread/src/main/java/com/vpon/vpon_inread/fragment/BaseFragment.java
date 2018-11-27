package com.vpon.vpon_inread.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInReadAd;

public abstract class BaseFragment extends Fragment {

    protected String licenseKey = "{YOUR_LICENSE_KEY}";

    protected class MyAdListener implements VpadnAdListener {

        @Override
        public void onVpadnReceiveAd(VpadnAd vpadnAd) {

            if(getActivity() != null){
                Toast.makeText(getActivity(),"onVpadnReceiveAd invoked!!",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("BaseFragment", "getActivity is null !!" );
            }
        }

        @Override
        public void onVpadnFailedToReceiveAd(VpadnAd ad, VpadnAdRequest.VpadnErrorCode errorCode) {
            if(getActivity() != null){
                Toast.makeText(getActivity(),"onVpadnFailedToReceiveAd("+errorCode.name()+") invoked!!",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("BaseFragment", "getActivity is null !!" );
            }

        }

        @Override
        public void onVpadnPresentScreen(VpadnAd vpadnAd) {
            if(getActivity() != null){
                Toast.makeText(getActivity(),"onVpadnPresentScreen invoked!!",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("BaseFragment", "getActivity is null !!" );
            }
        }

        @Override
        public void onVpadnDismissScreen(VpadnAd vpadnAd) {
            if(getActivity() != null){
                Toast.makeText(getActivity(),"onVpadnDismissScreen invoked!!",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("BaseFragment", "getActivity is null !!" );
            }

        }

        @Override
        public void onVpadnLeaveApplication(VpadnAd vpadnAd) {

            if(getActivity() != null){
                Toast.makeText(getActivity(),"onVpadnLeaveApplication invoked!!",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("BaseFragment", "getActivity is null !!" );
            }
        }
    }

    protected VpadnInReadAd inReadAd = null;

    public static final int AD_POSITION = 15;

    @Override
    public void onResume() {
        super.onResume();
        Log.v("BaseFragment","onResume invoked!!");
        if(inReadAd != null){
            inReadAd.resume();
        }else{
            Log.v("BaseFragment", "inReadAd is null");
        }
    }

    @Override
    public void onPause() {
        Log.v("BaseFragment","onPause invoked!!");
        if(inReadAd != null){
            inReadAd.pause();
        }else{
            Log.v("BaseFragment", "inReadAd is null");
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.v("BaseFragment","onDestroy invoked!!");
        if(inReadAd != null){
            inReadAd.destroy();
        }else{
            Log.v("BaseFragment", "inReadAd is null");
        }
        super.onDestroy();
    }
}
