package com.vpon.vpon_inread.fragment.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vpon.vpon_inread.R;
import com.vpon.vpon_inread.fragment.BaseFragment;

public class MyListAdapter extends BaseAdapter {

    private static final String LT = "MyListAdapter";

    static class ViewHolder {
        public TextView title;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null && viewGroup.getContext() != null) {
            convertView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.item_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d(LT, "viewHolder is null ? "+(holder == null));
        if(position == BaseFragment.AD_POSITION -1){
            holder.title.setText("AD shows below");
        }else if(position == BaseFragment.AD_POSITION){
            holder.title.setText("AD shows above");
        }else{
            holder.title.setText("Hello Vpon(" + (position + 1) + ")");
        }
        Log.d(LT, "convertView.LayoutParam is null ? "+(convertView.getLayoutParams() == null));
        convertView.setBackgroundColor(Color.YELLOW);
        convertView.getLayoutParams().height = 100;
        convertView.setLayoutParams(convertView.getLayoutParams());
        return convertView;
    }




}
