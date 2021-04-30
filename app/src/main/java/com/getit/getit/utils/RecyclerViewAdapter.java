package com.getit.getit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.getit.getit.pojo.DataModel;
import com.getit.getit.yes.R;

import java.util.ArrayList;

/**
 * Created by SONU on 25/09/15.
 */
public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<DataModel> arrayList;
    private Context context;

    public RecyclerViewAdapter(Context context,
                                ArrayList<DataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final DataModel model = arrayList.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into
        // bitmap

        // setting title
        mainHolder.title.setText(model.getTitle());
        mainHolder.title.setTag(model.getTag());
        mainHolder.imageview.setImageBitmap(image);



    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }

}