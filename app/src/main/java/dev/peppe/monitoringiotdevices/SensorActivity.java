package dev.peppe.monitoringiotdevices;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.helpers.SensorArrayAdapter;

public class SensorActivity extends AppCompatActivity {

    ListView listview;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listview = findViewById(R.id.sensorsList);
        ArrayList<Sensor> sensorList  =  new ArrayList<Sensor>(mSensorManager.getSensorList(Sensor.TYPE_ALL));
        final SensorArrayAdapter adapter = new SensorArrayAdapter(this,R.layout.sensor_listitem,sensorList);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
