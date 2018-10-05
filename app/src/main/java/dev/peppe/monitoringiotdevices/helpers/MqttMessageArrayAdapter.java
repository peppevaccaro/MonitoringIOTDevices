package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.ReceivedMessage;

public class MqttMessageArrayAdapter extends ArrayAdapter<ReceivedMessage> {
    private ArrayList<ReceivedMessage> list;
    public MqttMessageArrayAdapter(Context context, int textViewResourceId,
                              ArrayList<ReceivedMessage> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mqttmessage_listitem, null);

        TextView topicName = convertView.findViewById(R.id.topicPathText);
        TextView messageValue = convertView.findViewById(R.id.messageValue);
        TextView timestampValue = convertView.findViewById(R.id.timestampValue);
        ReceivedMessage message = getItem(position);
        topicName.setText(message.getTopic());
        messageValue.setText(message.getMessage().toString());
        timestampValue.setText(DateFormat.format("yyyy-MM-dd hh:mm:ss a",message.getTimestamp()));
        return convertView;
    }
}
