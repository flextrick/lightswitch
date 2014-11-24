package com.flextrick.lightswitch;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class MainActivity extends Activity implements View.OnClickListener{

    private Button lightDesk1, lightDesk2, lightRegal1, lightRegal2, lightAll1, lightAll2, timeButton;
    private EditText timeHour, timeMinute;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        ActionBar ab = this.getActionBar();

        if(ab != null)
            ab.hide();

        lightDesk1 = (Button)findViewById(R.id.switch11);
        lightDesk2 = (Button)findViewById(R.id.switch12);
        lightRegal1 = (Button)findViewById(R.id.switch21);
        lightRegal2 = (Button)findViewById(R.id.switch22);
        lightAll1 = (Button)findViewById(R.id.switchAll1);
        lightAll2 = (Button)findViewById(R.id.switchAll2);
        timeButton = (Button)findViewById(R.id.buttonSaveTime);
        timeHour = (EditText)findViewById(R.id.editTextHour);
        timeMinute = (EditText)findViewById(R.id.editTextMinute);

        lightDesk1.setOnClickListener(this);
        lightDesk2.setOnClickListener(this);
        lightRegal1.setOnClickListener(this);
        lightRegal2.setOnClickListener(this);
        lightAll1.setOnClickListener(this);
        lightAll2.setOnClickListener(this);
        timeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch11:
                new SendCommands(mContext).execute("sudo sh oneON.sh");
                break;
            case R.id.switch12:
                new SendCommands(mContext).execute("sudo sh oneOFF.sh");
                break;
            case R.id.switch21:
                new SendCommands(mContext).execute("sudo sh twoON.sh");
                break;
            case R.id.switch22:
                new SendCommands(mContext).execute("sudo sh twoOFF.sh");
                break;
            case R.id.switchAll1:
                new SendCommands(mContext).execute("sudo sh one_twoON.sh");
                break;
            case R.id.switchAll2:
                new SendCommands(mContext).execute("sudo sh one_twoOFF.sh");
                break;
            case R.id.buttonSaveTime:
                String sHour = timeHour.getText().toString();
                String sMinute = timeMinute.getText().toString();
                if(!sHour.equals("") & !sMinute.equals("")) {
                    setAlarm(Integer.parseInt(sHour), Integer.parseInt(sMinute));
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setAlarm(int hour, int minute){
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

	    //check if time is already over
        if(currentHour > hour){
            day = day + 1;
        }

        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(year, month, day, hour, minute);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
