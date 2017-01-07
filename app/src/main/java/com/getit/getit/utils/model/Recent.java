package com.getit.getit.utils.model;

/**
 * Created by vIvEk on 08-01-2016.
 */

import android.content.Context;

/**
 * Created by Trey Robinson on 8/3/14.
 * Copyright 2014 MindMine LLC.
 */
public class Recent {

    public String name;
    public String description;
    public String imageName;


    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}