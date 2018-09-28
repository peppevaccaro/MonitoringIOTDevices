package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class TopicArrayAdapter extends ArrayAdapter<Topic> {
    public TopicArrayAdapter(Context context, int textViewResourceId,
                             ArrayList<Topic> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.topic_listitem, null);
        TextView topicPath = convertView.findViewById(R.id.top);
        TextView qos = convertView.findViewById(R.id.qosValue);
        TextView retain = convertView.findViewById(R.id.retainValue);
        return convertView;
    }
}
