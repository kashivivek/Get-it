package com.getit.getit.utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getit.getit.utils.model.Recent;
import com.getit.getit.yes.R;

import java.util.List;


/**
 * Created by Trey Robinson on 8/3/14.
 * Copyright 2014 MindMine LLC.
 */
public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private List<Recent> recents;
    private int rowLayout;
    private Context mContext;

    public RecentAdapter(List<Recent> recents, int rowLayout, Context context) {
        this.recents = recents;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Recent recent = recents.get(i);
        viewHolder.recentName.setText(recent.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.recentImage.setImageDrawable(mContext.getDrawable(recent.getImageResourceId(mContext)));
        }
    }

    @Override
    public int getItemCount() {
        return recents == null ? 0 : recents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recentName;
        public ImageView recentImage;

        public ViewHolder(View itemView) {
            super(itemView);
            recentName = (TextView) itemView.findViewById(R.id.recentName);
            recentImage = (ImageView) itemView.findViewById(R.id.recentImage);
        }

    }
}