package dev.peppe.monitoringiotdevices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.SubscriptionArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Subscription;

public class SubscribeFragment extends Fragment {
    private OnSubscribeInteractionListener mListener;
    ArrayList<Subscription> list;

    public SubscribeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        super.onViewCreated(view, savedInstanceState);

        final ListView listview = view.findViewById(R.id.subscriptionList);
        list = new ArrayList<Subscription>();
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                final NewSubscriptionDialog dialog=new NewSubscriptionDialog();
                dialog.onButtonOk=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String topic = dialog.topic.getSelectedItem().toString();
                        int qos = Integer.parseInt(dialog.qos.getSelectedItem().toString());
                        Subscription subscript = new Subscription(topic,qos);
                        if (mListener != null) {
                            if(mListener.onSubscribeButtonClicked(subscript)){
                                list.add(subscript);
                                listview.invalidateViews();
                                listview.refreshDrawableState();
                            }
                        }
                        dialog.dismiss();
                    }
                };
                dialog.show(getActivity().getSupportFragmentManager(),null);
            }}
        );

        SubscriptionArrayAdapter adapter = new SubscriptionArrayAdapter(this.getContext(),R.layout.subscription_listitem,list);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubscribeInteractionListener) {
            mListener = (OnSubscribeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSubscribeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSubscribeInteractionListener {
        boolean onSubscribeButtonClicked(Subscription sub);
    }
}



