package dev.peppe.monitoringiotdevices.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.SubscriptionArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Subscription;

;

public class SubscribeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fabAddSub);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listview = view.findViewById(R.id.subscriptionList);

        final ArrayList<Subscription> list = new ArrayList<Subscription>();
        Subscription subscription1 = new Subscription("topic1",0);
        list.add(subscription1);


        SubscriptionArrayAdapter adapter = new SubscriptionArrayAdapter(this.getContext(),R.layout.subscription_listitem,list);
        listview.setAdapter(adapter);
    }
}



