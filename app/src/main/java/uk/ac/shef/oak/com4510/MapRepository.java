package uk.ac.shef.oak.com4510;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.shef.oak.com4510.database.LocAndSensorData;
import uk.ac.shef.oak.com4510.database.MyDAO;
import uk.ac.shef.oak.com4510.database.MyRoomDatabase;

public class MapRepository extends ViewModel {
    private final MyDAO mDBDao;

    public MapRepository(Application application){
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        mDBDao = db.myDao();
    }

    public void insertOneData(@NonNull LocAndSensorData locAndSensorData){
        new insertAsyncTask(mDBDao).execute(locAndSensorData);
    }

    public LiveData<LocAndSensorData> getLatestData() {
        return mDBDao.retrieveOneData();
    }

    private static class insertAsyncTask extends AsyncTask<LocAndSensorData, Void, Void> {
        private MyDAO mAsyncTaskDao;
        private LiveData<LocAndSensorData> numberData;

        insertAsyncTask(MyDAO dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final LocAndSensorData... params) {
            mAsyncTaskDao.insert(params[0]);
            Log.i("MyRepository", params[0].toString());
            return null;
        }
    }

}
