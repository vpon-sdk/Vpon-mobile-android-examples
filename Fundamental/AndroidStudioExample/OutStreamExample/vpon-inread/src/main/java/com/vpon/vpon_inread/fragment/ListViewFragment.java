package com.vpon.vpon_inread.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnInReadAd;
import com.vpon.vpon_inread.R;
import com.vpon.vpon_inread.fragment.adapter.MyListAdapter;

public class ListViewFragment extends BaseFragment {

    public static ListViewFragment newInstance(Bundle args) {
        ListViewFragment fragment = new ListViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = view.findViewById(R.id.listView);
        MyListAdapter adapter = new MyListAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.e("ListViewFragment","position("+position+") click invoked!!");
                Log.e("ListViewFragment","id("+id+") click invoked!!");
            }
        });

        if (getActivity() != null) {
            inReadAd = new VpadnInReadAd(getActivity(), licenseKey);
            VpadnAdRequest adRequest = new VpadnAdRequest().setAdContainer(listView);
            adRequest.setAdPosition(AD_POSITION);
            inReadAd.setAdListener(new MyAdListener());
            inReadAd.loadAd(adRequest);
        }
    }

}
