package edu.gatech.pecssinbuiltsensors;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class showResults extends AppCompatActivity {

    private commonData commonDataObject;
//    private List<appUsageInformation> appInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        TextView resultsText = (TextView)findViewById(R.id.results);

        String appUsage = "";

        for (int i = 0; i < recordingPlayer.appInfoList.size(); i++) {
            System.out.println(recordingPlayer.appInfoList.get(i));
            appUsageInformation aui = recordingPlayer.appInfoList.get(i);

            appUsage += aui.packageName;
            appUsage += " : ";
            appUsage += String.valueOf(aui.timeInForeground/1000.0);
            appUsage += "\n";
//
//            System.out.println(aui.timeInForeground);
//            System.out.println(aui.packageName);

        }

        appUsage += "\n\n\n";


        for (int i = 0; i < recordingPlayer.accListNoteTimes.size(); i++) {

            appUsage += recordingPlayer.accListNoteTimes.get(i);
            appUsage += " , ";
//
//            System.out.println(aui.timeInForeground);
//            System.out.println(aui.packageName);

        }


        appUsage += "\n\n\n";


        resultsText.setText(appUsage);


        writeToFile(appUsage,this.getApplicationContext());



//        appInfoList = commonData.getList();

//        appInfoList = new ArrayList<appUsageInformation>(commonData.getList());

//        printUsage();

    }

//
//    private void printUsage(){
//
//        System.out.println("LooooooooooooooooL");
//
//        for (int i = 0; i < appInfoList.size(); i++) {
//            System.out.println(appInfoList.get(i));
//            appUsageInformation aui = appInfoList.get(i);
//            System.out.println(aui.timeInForeground);
//            System.out.println(aui.packageName);
//
//        }
//
//
//    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("results-ptsd.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
