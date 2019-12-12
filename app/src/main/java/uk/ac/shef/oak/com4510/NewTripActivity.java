package uk.ac.shef.oak.com4510;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class NewTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_trip);

        Thread thread=new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView=(TextView)findViewById(R.id.timeText);
                                long date=System.currentTimeMillis();
                                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss a");
                                String dateString=sdf.format(date);
                                textView.setText(dateString);
                            }
                        });
                    }
                }catch(InterruptedException e){

                }
            }
        };
        thread.start();

        //button
        final Button button=(Button) findViewById(R.id.button);
        final EditText tripNameInput = (EditText) findViewById(R.id.tripName);
        final TextView error_mess = (TextView) findViewById(R.id.errorText);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String mess = tripNameInput.getText().toString();
                if(mess.length() != 0) {
                    error_mess.setText("");
                    Intent intent = new Intent(NewTripActivity.this, MapsActivity.class);
                    intent.putExtra("tripName", mess);
                    startActivity(intent);
                }else {
                    error_mess.setText("Please input a trip name.");
                }
            }
        });
    }
}
