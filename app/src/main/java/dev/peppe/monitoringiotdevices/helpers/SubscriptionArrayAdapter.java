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
import dev.peppe.monitoringiotdevices.utils.Subscription;

public class SubscriptionArrayAdapter extends ArrayAdapter<Subscription> {

    private ArrayList<Subscription> list;
    public SubscriptionArrayAdapter(Context context, int textViewResourceId,
                               ArrayList<Subscription> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.subscription_listitem, null);

        TextView topic = convertView.findViewById(R.id.topicText);
        TextView qos = convertView.findViewById(R.id.qosValue);
        Subscription item = getItem(position);
        topic.setText(item.topicSubscription);
        qos.setText(String.valueOf(item.qosSubscription));

        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
