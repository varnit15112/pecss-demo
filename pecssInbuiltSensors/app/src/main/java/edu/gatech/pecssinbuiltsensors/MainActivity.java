package edu.gatech.pecssinbuiltsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private long lastTime = 0L;
    private long diff ;
    private Button startButton;
    private commonData commonDataObject;
    private Button startLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLocationButton = findViewById(R.id.location_activity_btn);

        startButton = findViewById(R.id.round_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                diff = System.currentTimeMillis() - lastTime;
                lastTime = System.currentTimeMillis();

                Log.i("testing","MainActivity");
                Log.i("testing",String.valueOf(diff/1000.0));

                commonDataObject.setTime(lastTime);

                Intent activityChangeIntent;
                activityChangeIntent = new Intent(MainActivity.this, recordingPlayer.class);
                activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                MainActivity.this.startActivity(activityChangeIntent);
                finish();

            }

            //Change activity to play that recording

        });

        startLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LocationActivity.class));
            }
        });
    }



}
