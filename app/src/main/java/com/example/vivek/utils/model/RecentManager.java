package com.example.vivek.utils.model;

/**
 * Created by vIvEk on 08-01-2016.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trey Robinson on 8/3/14.
 * Copyright 2014 MindMine LLC.
 */
public class RecentManager {

    private static String[] RecentArray = {"Australia", "China", "Italy", "Japan", "United Kingdom", "United States"};
    private static String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
            "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private static RecentManager mInstance;
    private List<Recent> recents;

    public static RecentManager getInstance() {
        if (mInstance == null) {
            mInstance = new RecentManager();
        }

        return mInstance;
    }

    public List<Recent> getrecents() {
        if (recents == null) {
            recents = new ArrayList<Recent>();

            for (String recentName : RecentArray) {
                Recent recent = new Recent();
                recent.name = recentName;
                recent.description = loremIpsum;
                recent.imageName = recentName.replaceAll("\\s+", "").toLowerCase();
                recents.add(recent);
            }
        }

        return recents;
    }

}