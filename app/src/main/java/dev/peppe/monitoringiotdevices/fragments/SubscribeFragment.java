package dev.peppe.monitoringiotdevices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.helpers.SubscriptionArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Subscription;

public class SubscribeFragment extends Fragment {
    private OnSubscribeInteractionListener mListener;
    ArrayList<Subscription> list;
    private Bundle savedState = null;

    public SubscribeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("bundle");
        }
        if(savedState != null) {
            list = (ArrayList<Subscription>) savedState.getSerializable("listSubscription");
            savedState = null;
        }
        else
            list = new ArrayList<>();
        final ListView listview = view.findViewById(R.id.subscriptionList);
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                final NewSubscriptionDialog dialog=new NewSubscriptionDialog();
                dialog.onButtonOk=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String device = dialog.device.getText().toString();
                        String topic = dialog.topic.getSelectedItem().toString();
                        int qos = Integer.parseInt(dialog.qos.getSelectedItem().toString());
                        Subscription subscript = new Subscription(device,topic,qos);
                        subscript.setTopicPath();
                        boolean exists = false;
                        for(Subscription item : list){
                            if(item.getTopicPath().equals(subscript.getTopicPath())) {
                                exists = true;
                                break;
                            }
                        }
                        if (mListener != null && !exists) {
                            if(mListener.onSubscribeButtonClicked(subscript)){
                                list.add(0,subscript);
                                listview.invalidateViews();
                                listview.refreshDrawableState();
                            }
                        }
                        dialog.dismiss();
                        if(exists)
                            Toast.makeText(getContext(), "Subscription already exists", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        savedState = saveState();
    }

    private Bundle saveState() { /* called either from onDestroyView() or onSaveInstanceState() */
        Bundle state = new Bundle();
        state.putSerializable("listSubscription",list);
        return state;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("bundle", (savedState != null) ? savedState : saveState());
    }

    public interface OnSubscribeInteractionListener {
        boolean onSubscribeButtonClicked(Subscription sub);
    }
}