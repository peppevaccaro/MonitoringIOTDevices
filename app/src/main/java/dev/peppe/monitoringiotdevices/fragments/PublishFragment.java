package dev.peppe.monitoringiotdevices.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.broadcast_receiver.BatteryBroadcastReceiver;
import dev.peppe.monitoringiotdevices.helpers.TopicArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishFragment extends Fragment implements SensorEventListener {
    private OnPublishInteractionListener mListener;
    ListView listview;
    private SensorManager mSensorManager;
    private Sensor mSensorTemperature;
    private Sensor mSensorLight;
    public float currentLight;

    public PublishFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);
        mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);

        listview = view.findViewById(R.id.topicsList);
        Button publishButt = view.findViewById(R.id.publishButton);
        final Spinner topics = view.findViewById(R.id.topics);
        final Spinner qosLevels = view.findViewById(R.id.qosLevels);
        final CheckBox retainValue = view.findViewById(R.id.retainValue);

        final ArrayList<Topic> list = new ArrayList<Topic>();

        TopicArrayAdapter adapter = new TopicArrayAdapter(this.getContext(),R.layout.topic_listitem,list);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);

        publishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topicText = topics.getSelectedItem().toString();
                Sensor sensorSelected = getSensor(topicText);
                int qosLevel = Integer.parseInt(qosLevels.getSelectedItem().toString());
                boolean retain = retainValue.isChecked();

                if(topicText.equals("Battery")) {
                    BroadcastReceiver br = new BatteryBroadcastReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    v.getContext().registerReceiver(br, filter);
                }

                Topic topic = new Topic(topicText,qosLevel,retain);
                if (mListener != null) {
                    if(mListener.onPublishButtonClicked(topic)){
                        list.add(topic);
                        listview.invalidateViews();
                        listview.refreshDrawableState();
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPublishInteractionListener) {
            mListener = (OnPublishInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPublishInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        if(sensor.getType() == Sensor.TYPE_LIGHT)
        {

        }
    }

    public void onSensorChanged(SensorEvent event)
    {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            currentLight = event.values[0];
        }
    }

    public Sensor getSensor(String topic) {
        switch (topic) {
            case "temperature":
                return mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            case "light":
                return mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            default:
                return null;
        }
    }

    public interface OnPublishInteractionListener {
        boolean onPublishButtonClicked(Topic topic);
    }

}
