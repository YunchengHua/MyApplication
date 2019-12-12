package uk.ac.shef.oak.com4510.model;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

public class Temperature  {

    private long mSamplingRateInMSecs;
    private long mSamplingRateNano;
    private SensorEventListener mTemperatureListener = null;
    private SensorManager sensorManager;
    private Sensor temperature;
    private long timePhoneWasLastRebooted = 0;
    private long READING_FREQUENCY= 30000;
    private long lastReportTime = 0;

    private float latestValue;

    public Temperature(Context context){
        timePhoneWasLastRebooted = System.currentTimeMillis() - SystemClock.elapsedRealtime();

        mSamplingRateNano = (long) (READING_FREQUENCY) * 1000000;
        mSamplingRateInMSecs = (long) READING_FREQUENCY;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        latestValue = -1000;
        initTemperatureSensor();
    }

    public void initTemperatureSensor(){
        if(temperature == null){
            Log.i("Temp", "Standard Temperature sensor unavailable");
        }else{
            Log.i("temp","Using temperature sensor");
            mTemperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    long diff = event.timestamp - lastReportTime;
                    // time is in nanoseconds it represents the set reference times the first time we come here
                    // set event timestamp to current time in milliseconds
                    // see answer 2 at http://stackoverflow.com/questions/5500765/accelerometer-sensorevent-timestamp
                    // the following operation avoids reporting too many events too quickly - the sensor may always
                    // misbehave and start sending data very quickly
                    if (diff >= mSamplingRateNano) {
                        long actualTimeInMseconds = timePhoneWasLastRebooted + (long) (event.timestamp / 1000000.0);
                        latestValue = event.values[0];

                        Log.i("temp", Utilities.mSecsToString(actualTimeInMseconds) + ": current temperature pressure: " + latestValue);
                        lastReportTime = event.timestamp;
                    }
                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }
    }

    public String toString(){
        return "Current temperature is "+latestValue;
    }

    public void startTemperatureSensor(){
        if(temperature != null){
            Log.d("Standard Temperature", "starting listener");
            sensorManager.registerListener(mTemperatureListener, temperature, (int) (mSamplingRateInMSecs * 1000));
        }else{
            Log.i("Temp", "temperature unavailable or already active");
        }
    }

    public void stopTemperatureSensor(){
        if(temperature != null){
            Log.d("Standard Temperature", "Stopping listener");
            try {
                sensorManager.unregisterListener(mTemperatureListener);
            } catch (Exception e) {
                // probably already unregistered
            }
            latestValue = -1000;
            lastReportTime = 0;
        }
    }

    public float getLatestValue() {
        return latestValue;
    }
}
