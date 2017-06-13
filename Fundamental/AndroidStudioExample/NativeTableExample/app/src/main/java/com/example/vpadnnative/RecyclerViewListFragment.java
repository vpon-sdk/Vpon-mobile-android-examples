package com.example.vpadnnative;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnNativeAdsManager;

import java.util.Arrays;
import java.util.HashSet;

public class RecyclerViewListFragment extends Fragment implements VpadnNativeAdsManager.Listener{

    private static final String LT = RecyclerViewListFragment.class.getSimpleName();
    private static final String twNativeId = "your VPON native id"; //Dennis
    private static final String STATE_RECYCLERVIEW_PISITION = "RecyclerViewPosition";
    private static final String STATE_DATASET = "DataSet";
    private static final int DATASET_COUNT = 30;

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected String[] mDataset;
    protected int scrollPosition;

    private static Activity mContext;
    private VpadnNativeAdsManager nativeAdsManager;

    /**
     * Create a new instance of MyFragment that will be initialized
     * with the given arguments.
     */
    static RecyclerViewListFragment newInstance(String activityKey) {
        RecyclerViewListFragment f = new RecyclerViewListFragment();
        Bundle b = new Bundle();
        b.putString("activityKey", activityKey);
        f.setArguments(b);
        return f;
    }

    public RecyclerViewListFragment() {
        Log.e(LT,"Call RecyclerViewListFragment empty constructor");
    }

    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LT,"Call onCreate");
        Bundle args = getArguments();
        if (args != null) {
            String activityKey = args.getString("activityKey");
            StorageCenter sc = StorageCenter.instance();
            mContext = (Activity) sc.get(activityKey);
        }

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        if(savedInstanceState==null) {
            initDataset();
        } else {
            scrollPosition = savedInstanceState.getInt(STATE_RECYCLERVIEW_PISITION);
            mDataset = savedInstanceState.getStringArray(STATE_DATASET);
        }

        // Initialize native Ad dataset, this data would usually come from vpon server.
        nativeAdsManager = new VpadnNativeAdsManager(mContext, twNativeId, 3);
        nativeAdsManager.setListener(this);
        nativeAdsManager.loadAds();
        //if you want to see test ad, please open code after this line.
        nativeAdsManager.loadAds(requestTestAdvertising());
    }

    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(LT,"Call onCreateView");

        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(LT);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // you can also enable optimizations
        // if all item views are of the same height and width for significantly smoother scrolling
        mRecyclerView.setHasFixedSize(true);

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        // If a layout manager has already been set, get current scroll position.
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Swipe or Pull Refresh items
                refreshItems();
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e(LT,"Call onLoadMore");
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

        mAdapter = new CustomAdapter(mContext, mDataset);
        mAdapter.setNativeAdsManager(nativeAdsManager);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(LT,"Call onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(LT,"Call onStart");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(LT,"Call onDestroyView");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.e(LT,"Call onSaveInstanceState");
        int scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        savedInstanceState.putInt(STATE_RECYCLERVIEW_PISITION,scrollPosition);
        savedInstanceState.putStringArray(STATE_DATASET,mDataset);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "Your Customer View #" + i + " of Page:" + 0;
        }
    }

    private void refreshItems() {
        // Load items
        // ...
        nativeAdsManager.loadAds(requestTestAdvertising());
        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Append more data into the adapter.
     * This method probably sends out a network request and appends new data items to your adapter.
     */
    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes
        int oldLength = mDataset.length;
        int newLength = mDataset.length + 30;

        mDataset = Arrays.copyOf(mDataset,newLength);
        for( ;oldLength < mDataset.length; oldLength++){
            mDataset[oldLength] = "Your Customer View #" + oldLength + " of Page:" + page;
        }

        mAdapter.setDataSet(mDataset);

        nativeAdsManager.loadAds(requestTestAdvertising());
    }

    private VpadnAdRequest requestTestAdvertising() {
        VpadnAdRequest adRequest = new VpadnAdRequest();
        HashSet<String> testDeviceImeiSet = new HashSet<String>();
        testDeviceImeiSet.add("your device advertising id");
        adRequest.setTestDevices(testDeviceImeiSet);
        return adRequest;
    }

    @Override
    public void onVpadnReceiveAds() {
        Log.e(LT,"Call NativeAd onVpadnReceiveAds count:" + nativeAdsManager.getUniqueNativeAdCount());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVpadnFailedToReceiveAds(VpadnAdRequest.VpadnErrorCode vpadnErrorCode) {
        Log.e(LT, "CALL NativeAd onVpadnFailedToReceiveAds: " + vpadnErrorCode.toString());
    }
}