package uk.ac.shef.oak.com4510.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MyDAO {
    @Insert
    void insertAll(LocAndSensorData... locAndSensorData);

    @Insert
    void insert(LocAndSensorData locAndSensorData);

    @Delete
    void delete(LocAndSensorData locAndSensorData);

    // it selects a random element
    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC LIMIT 1")
    LiveData<LocAndSensorData> retrieveOneData();

    @Query("SELECT * FROM loc_and_sensor_data ORDER BY id DESC")
    LiveData<LocAndSensorData> retrieveAllData();

    @Delete
    void deleteAll(LocAndSensorData... locAndSensorData);
    @Query("SELECT COUNT(*) FROM loc_and_sensor_data")
    int howManyElements();
}
