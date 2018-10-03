package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.Session;

public class SessionArrayAdapter extends ArrayAdapter<Session> {

    private ArrayList<Session> list;
    private MQTTHelper mqttHelper;
    public SessionArrayAdapter(Context context, int textViewResourceId,
                             ArrayList<Session> objects,MQTTHelper mqttHelper) {
        super(context, textViewResourceId, objects);
        list = objects;
        this.mqttHelper = mqttHelper;
    }

    public void setMqttHelper(MQTTHelper mqttHelper){
        this.mqttHelper = mqttHelper;
    }

    @Override
    public Session getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.session_listitem, null);
        TextView serverUri = convertView.findViewById(R.id.serverUriText);
        TextView clientId = convertView.findViewById(R.id.clientIdValue);
        Session item = getItem(position);
        serverUri.setText(item.serverUri);
        clientId.setText(item.clientId);

        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mqttHelper.disconnect();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
