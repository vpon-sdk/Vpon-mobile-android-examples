package com.vpon.vpon_inread.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInReadAd;
import com.vpon.vpon_inread.R;
import com.vpon.vpon_inread.fragment.adapter.MyRecyclerAdapter;
import com.vpon.vpon_inread.fragment.adapter.OrginalRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewFragment extends BaseFragment {

    private static String[] letters = new String[]{"One", "Two", "Three",
            "Four", "Five", "Six", "Seven", "Eight", "Nigh", "Ten", "One", "Two",
            "Three", "Four", "Five", "Six", "Seven", "Eight", "Nigh", "Ten", "One", "Two",
            "Three", "Four", "Five", "Six", "Seven", "Eight", "Nigh", "Ten", "One", "Two",
            "Three", "Four", "Five", "Six", "Seven", "Eight", "Nigh", "Ten", "One", "Two",
            "Three", "Four", "Five", "Six", "Seven", "Eight", "Nigh", "Ten"};

    public static RecyclerViewFragment newInstance(Bundle args) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView.LayoutManager mLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);

        List<String> letterList = new ArrayList<>(Arrays.asList(letters));

//        OrginalRecyclerAdapter mAdapter = new OrginalRecyclerAdapter(letterList);
        MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(letterList);
        mRecyclerView.setAdapter(mAdapter);

        if (getActivity() != null) {
            inReadAd = new VpadnInReadAd(getActivity(), licenseKey);
            VpadnAdRequest adRequest = new VpadnAdRequest().setAdContainer(mRecyclerView);
            adRequest.setAdPosition(AD_POSITION);
            inReadAd.setAdListener(new MyAdListener());
            inReadAd.loadAd(adRequest);
        }
    }
}
