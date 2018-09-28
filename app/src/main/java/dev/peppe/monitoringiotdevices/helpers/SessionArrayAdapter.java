package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.Session;

public class SessionArrayAdapter extends ArrayAdapter<Session> {
    public SessionArrayAdapter(Context context, int textViewResourceId,
                             ArrayList<Session> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.session_listitem, null);
        TextView serverUri = convertView.findViewById(R.id.serverUriText);
        TextView clientId = convertView.findViewById(R.id.clientIdValue);
        return convertView;
    }
}
