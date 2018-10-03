package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;

public class SensorArrayAdapter extends ArrayAdapter<Sensor> {
    private ArrayList<Sensor> list;
    public SensorArrayAdapter(Context context, int textViewResourceId,
                             ArrayList<Sensor> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.sensor_listitem, null);

        TextView sensorName = convertView.findViewById(R.id.sensorText);
        TextView sensorType = convertView.findViewById(R.id.sensorTypeValue);
        Sensor sensor = getItem(position);
        sensorName.setText(sensor.getName());
        sensorType.setText(sensor.getStringType());

        return convertView;
    }
}
