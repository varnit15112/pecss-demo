package edu.gatech.pecssinbuiltsensors;

import android.graphics.drawable.Drawable;

public class appUsageInformation {

    Drawable appIcon;
    String appName, packageName;
    long timeInForeground;
    int launchCount;

    appUsageInformation(String pName) {
        this.packageName=pName;
    }
}
