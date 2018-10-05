package dev.peppe.monitoringiotdevices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.TopicArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishFragment extends Fragment {
    private OnPublishInteractionListener mListener;
    ListView listview;

    public PublishFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        listview = view.findViewById(R.id.topicsList);
        Button publishButt = view.findViewById(R.id.publishButton);
        final EditText deviceValue = view.findViewById(R.id.deviceValue);
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
                int qosLevel = Integer.parseInt(qosLevels.getSelectedItem().toString());
                boolean retain = retainValue.isChecked();
                String device = deviceValue.getText().toString();
                Topic topic = new Topic(device,topicText,qosLevel,retain);
                topic.setTopicPath();
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

    public interface OnPublishInteractionListener {
        boolean onPublishButtonClicked(Topic topic);
    }

}
