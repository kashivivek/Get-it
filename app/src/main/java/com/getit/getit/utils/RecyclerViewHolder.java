package com.getit.getit.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.getit.getit.yes.R;

/**
 * Created by kashivivek on 06-14-2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public ImageView imageview;




    public RecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.title = (TextView) view
                .findViewById(R.id.title);
        this.imageview = (ImageView) view
                .findViewById(R.id.image);


    }



}