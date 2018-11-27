package com.vpon.vpon_inread.fragment.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vpon.adapter.AbsRecyclerAdapter;
import com.vpon.vpon_inread.R;
import com.vpon.vpon_inread.fragment.BaseFragment;

import java.util.List;

public class MyRecyclerAdapter extends AbsRecyclerAdapter<MyRecyclerAdapter.ViewHolder> {

    private List<String> letters;

    public MyRecyclerAdapter(List<String> letters) {
        this.letters = letters;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(@NonNull View convertView) {
            super(convertView);
            textView = convertView.findViewById(R.id.item_label);
        }
    }

    @Override
    protected void bindWrapViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView tv = viewHolder.textView;

        if(position == BaseFragment.AD_POSITION -1){
            tv.setText("AD shows below");
        }else if(position == BaseFragment.AD_POSITION){
            tv.setText("AD shows above");
        }else{
            tv.setText(letters.get(position));
        }
    }

    @Override
    protected ViewHolder onWrapViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item
                , viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    protected int getWrapItemCount() {
        return letters.size();
    }
}
