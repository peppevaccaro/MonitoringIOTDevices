package dev.peppe.monitoringiotdevices.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import dev.peppe.monitoringiotdevices.R;

public class NewSubscriptionDialog extends DialogFragment {

    public View.OnClickListener onButtonOk;
    public EditText device;
    public Spinner topic;
    public Spinner qos;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = LayoutInflater.from(builder.getContext());
        View view = li.inflate(R.layout.dialog_subscribe, null);
        Button buttonSub = view.findViewById(R.id.buttonSub);
        topic = view.findViewById(R.id.topic);
        qos = view.findViewById(R.id.qos);
        device = view.findViewById(R.id.deviceInput);
        buttonSub.setOnClickListener(onButtonOk);
         builder.setView(view);
         return builder.create();
    }
}
