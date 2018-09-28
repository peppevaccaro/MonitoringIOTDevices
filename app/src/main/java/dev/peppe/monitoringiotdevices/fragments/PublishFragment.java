package dev.peppe.monitoringiotdevices.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.TopicArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        ListView listview = view.findViewById(R.id.topicsList);
        Button publishButt = view.findViewById(R.id.publishButton);
        Spinner topics = view.findViewById(R.id.topics);
        Spinner qosLevels = view.findViewById(R.id.qosLevels);

        final ArrayList<Topic> list = new ArrayList<Topic>();
            Topic topic1 = new Topic("temperature",0,false);
            list.add(topic1);


        TopicArrayAdapter adapter = new TopicArrayAdapter(this.getContext(),R.layout.topic_listitem,list);
        listview.setAdapter(adapter);

        publishButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
