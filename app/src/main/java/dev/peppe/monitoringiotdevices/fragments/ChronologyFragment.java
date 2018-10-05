package dev.peppe.monitoringiotdevices.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.MainActivity;
import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.MqttMessageArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.ReceivedMessage;

public class ChronologyFragment extends Fragment implements MainActivity.OnMessageArrivedListener {

    ListView listview;
    ArrayList<ReceivedMessage> list;
    MqttMessageArrayAdapter adapter;
    private Bundle savedState = null;

    public ChronologyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setOnMessageArrivedListener(ChronologyFragment.this);
        return inflater.inflate(R.layout.fragment_chronology, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        listview = view.findViewById(R.id.listMqttMessage);
        if(savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("bundle");
        }
        if(savedState != null) {
            list = (ArrayList<ReceivedMessage>) savedState.getSerializable("listMessages");
            savedState = null;
        }
        else
            list = new ArrayList<>();
        adapter = new MqttMessageArrayAdapter(this.getContext(),R.layout.mqttmessage_listitem,list);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putSerializable("listMessages",list);
        return state;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("bundle", (savedState != null) ? savedState : saveState());
    }

    @Override
    public void onMessageArrived(ReceivedMessage message){
        list.add(message);
        listview.invalidateViews();
        listview.refreshDrawableState();
        adapter.notifyDataSetChanged();

    }
}
