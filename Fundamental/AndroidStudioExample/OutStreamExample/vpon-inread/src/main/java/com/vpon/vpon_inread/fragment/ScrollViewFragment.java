package com.vpon.vpon_inread.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInReadAd;
import com.vpon.vpon_inread.R;

public class ScrollViewFragment extends BaseFragment {

    private static final String LT = "ScrollViewFragment";

    public static ScrollViewFragment newInstance(Bundle args) {
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        Log.v(LT, "onCreateView invoked!!");
        return inflater.inflate(R.layout.fragment_scroll,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(LT, "onViewCreated invoked!!");
        FrameLayout frameLayout = view.findViewById(R.id.adContent);

        if(getActivity() != null && frameLayout != null){
            inReadAd = new VpadnInReadAd(getActivity(), licenseKey);
            VpadnAdRequest adRequest = new VpadnAdRequest();
            adRequest.setAdContainer(frameLayout);
            inReadAd.setAdListener(new MyAdListener());
            inReadAd.loadAd(adRequest);
        }
    }

}
