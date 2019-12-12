package uk.ac.shef.oak.com4510.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "loc_and_sensor_data")
public class LocAndSensorData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id = 0;

    private float preasure_value;
    private int preasure_vaule_accurany;

    private double longitude;
    private double latitude;
    private int tripId;
    private String tripName;

    public LocAndSensorData(float preasure_value,int preasure_vaule_accurany,double latitude,double longitude, int tripId,String tripName){
        this.preasure_value = preasure_value;
        this.preasure_vaule_accurany = preasure_vaule_accurany;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tripId = tripId;
        this.tripName = tripName;
    }

    public float getPreasure_value() {
        return preasure_value;
    }

    public int getId() {
        return id;
    }

    public int getPreasure_vaule_accurany() {
        return preasure_vaule_accurany;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPreasure_value(float preasure_value) {
        this.preasure_value = preasure_value;
    }

    public void setPreasure_vaule_accurany(int preasure_vaule_accurany) {
        this.preasure_vaule_accurany = preasure_vaule_accurany;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "latitude: "+latitude+",longitude: "+longitude+",barometer: "+preasure_value+",tripId: "+tripId+",trip name: "+tripName;
    }
}
