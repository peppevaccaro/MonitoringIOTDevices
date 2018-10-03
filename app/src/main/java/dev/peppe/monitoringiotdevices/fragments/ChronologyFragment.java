package dev.peppe.monitoringiotdevices.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.peppe.monitoringiotdevices.R;

public class ChronologyFragment extends Fragment  {


    private SensorManager manager;
    private SensorEventListener listener;
    private TextView tempValueText;
    private TextView humidityValueText;
    private TextView lightValueText;
    private TextView accelerometerValueText;

    public ChronologyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chronology, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        manager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);
        tempValueText = view.findViewById(R.id.temperatureValue);
        humidityValueText = view.findViewById(R.id.humidityValue);
        lightValueText = view.findViewById(R.id.lightValue);
        accelerometerValueText = view.findViewById(R.id.accelerometerValue);

        listener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor arg0, int arg1) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                Sensor sensor = event.sensor;
                float currentValue = event.values[0];
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerValueText.setText(String.valueOf(currentValue));

                }
                else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {

                }
                else if (sensor.getType() == Sensor.TYPE_LIGHT) {
                    lightValueText.setText(String.valueOf(currentValue));

                }
                else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    humidityValueText.setText(String.valueOf(currentValue));
                }

            }
        };
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
    }
}
