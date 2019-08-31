package edu.gatech.pecssinbuiltsensors;

import java.util.ArrayList;
import java.util.List;

public class commonData {

    public static long recordingStartTime = 0L;
//    public static List<appUsageInformation> appInfoList;

    public static void setTime(long recordingStartTime1){
        recordingStartTime = recordingStartTime1;
    }
    public static long getTime(){
        return recordingStartTime;
    }
//
//
//    public static void setList(List appInfoList1){
//        appInfoList = new ArrayList<appUsageInformation>(appInfoList1);
////        appInfoList = appInfoList1;
//    }
//    public static List getList(){
//        return appInfoList;
//    }



}
