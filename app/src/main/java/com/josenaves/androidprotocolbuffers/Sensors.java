package com.josenaves.androidprotocolbuffers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Class responsible for get sensor data
 */
public final class Sensors implements SensorEventListener {

    private static final String TAG = Sensors.class.getSimpleName();

    private final Context context;
    private final SensorManager sensorManager;

    private final Sensor accelerometer;
    private final Sensor temperature;
    private final Sensor light;
    private final Sensor proximity;

    private String accelerometerValues = "-";
    private String temperatureValues = "-";
    private String lightValues = "-";
    private String proximityValues = "-";

    public Sensors(Context context) {
        this.context = context;
        this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        this.temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        this.sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);

        this.light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);

        this.proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        this.sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String values = getValuesFromEvents(event);
        String s = event.sensor.toString();
        switch (event.sensor.getType()) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                this.temperatureValues = values;
                Log.d(TAG, String.format("onSensorChanged (temperature): sensor %s, %s", temperatureValues, s));
                break;

            case Sensor.TYPE_ACCELEROMETER:
                this.accelerometerValues = values;
                Log.v(TAG, String.format("onSensorChanged (accelerometer) : sensor %s, %s", accelerometerValues, s));
                break;

            case Sensor.TYPE_LIGHT:
                this.lightValues = values;
                Log.v(TAG, String.format("onSensorChanged (light) : sensor %s, %s", lightValues, s));
                break;

            case Sensor.TYPE_PROXIMITY:
                this.proximityValues = values;
                Log.v(TAG, String.format("onSensorChanged (proximity) : sensor %s, %s", proximityValues, s));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, String.format("onAccuracyChanged: %s, %d", sensor.toString(), accuracy));
    }

    public void registerListeners() {
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        this.sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        sensorManager.unregisterListener(this);
    }

    private String getValuesFromEvents(SensorEvent event) {
        StringBuffer values = new StringBuffer();
        for (float f : event.values) {
            values.append(String.valueOf(f)).append(" ");
        }
        return values.toString();
    }

    public String getAccelerometerValues() {
        return accelerometerValues;
    }

    public String getTemperatureValues() {
        return temperatureValues;
    }

    public String getLightValues() {
        return lightValues;
    }

    public String getProximityValues() {
        return proximityValues;
    }
}
