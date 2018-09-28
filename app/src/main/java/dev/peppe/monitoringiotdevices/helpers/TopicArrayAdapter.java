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
import dev.peppe.monitoringiotdevices.utils.Topic;

public class TopicArrayAdapter extends ArrayAdapter<Topic> {
    private ArrayList<Topic> list;
    public TopicArrayAdapter(Context context, int textViewResourceId,
                             ArrayList<Topic> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.topic_listitem, null);

        TextView topic = convertView.findViewById(R.id.topicPathText);
        TextView qos = convertView.findViewById(R.id.qosValue);
        TextView retain = convertView.findViewById(R.id.retainValue);
        Topic item = getItem(position);
        topic.setText(item.topicPath);
        //qos.setText(item.qos);
        //retain.setText(String.valueOf(item.retain));

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
