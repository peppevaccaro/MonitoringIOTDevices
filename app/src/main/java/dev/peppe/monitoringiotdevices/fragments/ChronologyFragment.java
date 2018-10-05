package dev.peppe.monitoringiotdevices.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Date;

import dev.peppe.monitoringiotdevices.MainActivity;
import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.MqttMessageArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.ReceivedMessage;

public class ChronologyFragment extends Fragment implements MainActivity.OnMessageArrivedListener {

    ListView listview;
    ArrayList<ReceivedMessage> list;
    MqttMessageArrayAdapter adapter;

    public ChronologyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chronology, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        listview = view.findViewById(R.id.listMqttMessage);

        list = new ArrayList<>();
        ReceivedMessage message1 = new ReceivedMessage(new MqttMessage(),"Topic",new Date());
        list.add(message1);
        adapter = new MqttMessageArrayAdapter(this.getContext(),R.layout.mqttmessage_listitem,list);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }

    @Override
    public void onMessageArrived(ReceivedMessage message){
        list.add(message);
        listview.invalidateViews();
        listview.refreshDrawableState();
        adapter.notifyDataSetChanged();

    }
}
