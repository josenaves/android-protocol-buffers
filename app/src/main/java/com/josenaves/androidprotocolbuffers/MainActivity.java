package com.josenaves.androidprotocolbuffers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.josenaves.androidprotocolbuffers.data.Image;
import com.josenaves.androidprotocolbuffers.data.realm.ImageRealm;
import com.josenaves.androidprotocolbuffers.data.sqlite.ImagesDataSource;
import com.josenaves.androidprotocolbuffers.data.Utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject Sensors sensors;

    private ImagesDataSource datasource;

    private Realm realmDatasource;

    private TextView textAccelerometer;
    private TextView textTemperature;
    private TextView textLight;
    private TextView textProximity;

    private Button buttonSaveSQLite;
    private Button buttonSaveRealm;

    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate...");

        datasource = new ImagesDataSource(this);

        realmDatasource = ((ProtocolBuffersApplication)getApplication()).getRealm();

        ((ProtocolBuffersApplication)getApplication()).getComponent().inject(this);

        textAccelerometer = (TextView) findViewById(R.id.text_accelerometer);
        textTemperature = (TextView) findViewById(R.id.text_temperature);
        textLight = (TextView) findViewById(R.id.text_light);
        textProximity = (TextView) findViewById(R.id.text_proximity);

        buttonSaveSQLite = (Button) findViewById(R.id.button_save_sqlite);
        buttonSaveSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Image image = new Image();
                        image.name = "Test";
                        image.date = Utils.getCurrentDateTime();
                        image.imageData = Utils.getImageData(MainActivity.this);

                        datasource.open();
                        datasource.createImage(image);
                        datasource.close();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Created new image in database", Toast.LENGTH_SHORT).show();

                                datasource.open();
                                Log.d(TAG, datasource.getAllImages().toString());
                                datasource.close();
                            }
                        });
                    }
                }).start();
            }
        });

        buttonSaveRealm = (Button) findViewById(R.id.button_save_realm);
        buttonSaveRealm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All writes must be wrapped in a transaction to facilitate safe multi threading
                realmDatasource.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // Add a person
                        ImageRealm image = realm.createObject(ImageRealm.class);
                        image.setId(UUID.randomUUID().toString());
                        image.setName("Test");
                        image.setDate(Utils.getCurrentDateTime());
                        image.setImageData(Utils.getImageData(MainActivity.this));

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Created new image in database", Toast.LENGTH_SHORT).show();

                                RealmResults<ImageRealm> images = realmDatasource.where(ImageRealm.class).findAll();
                                Log.d(TAG, images.toString());
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume...");
        //application.onResume();

        sensors.registerListeners();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textAccelerometer.setText("Accelerometer: " + sensors.getAccelerometerValues());
                        textTemperature.setText("Temperature: " + sensors.getTemperatureValues());
                        textLight.setText("Light: " + sensors.getLightValues());
                        textProximity.setText("Proximity: " + sensors.getProximityValues());
                    }
                });
            }
        }, 1000, 5000);  // after one second, run periodically each 5 seconds
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause...");

        //application.onPause();

        sensors.unregisterListeners();
        myTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmDatasource.close();
    }
}