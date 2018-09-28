package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.Subscription;

public class SubscriptionArrayAdapter extends ArrayAdapter<Subscription> {
    public SubscriptionArrayAdapter(Context context, int textViewResourceId,
                               ArrayList<Subscription> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.subscription_listitem, null);
        TextView topic = convertView.findViewById(R.id.topicText);
        TextView qos = convertView.findViewById(R.id.qosValue);
        return convertView;
    }
}
