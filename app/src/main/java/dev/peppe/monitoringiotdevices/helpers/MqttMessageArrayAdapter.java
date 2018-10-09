package dev.peppe.monitoringiotdevices.helpers;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.R;
import dev.peppe.monitoringiotdevices.utils.ReceivedMessage;

public class MqttMessageArrayAdapter extends ArrayAdapter<ReceivedMessage> implements Filterable {
    private ArrayList<ReceivedMessage> filteredList;
    private ArrayList<ReceivedMessage> originalList;
    CustomFilter filter;

    public MqttMessageArrayAdapter(Context context, int textViewResourceId,
                              ArrayList<ReceivedMessage> objects) {
        super(context, textViewResourceId, objects);
        originalList = objects;
        filteredList = objects;
        getFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from user list
     * @param i item index
     * @return list item
     */
    @Override
    public ReceivedMessage getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mqttmessage_listitem, null);

        TextView topicName = convertView.findViewById(R.id.topicPathText);
        TextView messageValue = convertView.findViewById(R.id.messageValue);
        TextView timestampValue = convertView.findViewById(R.id.timestampValue);
        ReceivedMessage message = getItem(position);
        topicName.setText(message.getTopic());
        messageValue.setText(message.getMessage().toString());
        timestampValue.setText(DateFormat.format("yyyy-MM-dd hh:mm:ss a",message.getTimestamp()));
        return convertView;
    }

    @Override
    public Filter getFilter() {

        if (filter == null){
            filter = new MqttMessageArrayAdapter.CustomFilter();
        }
        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();
                ArrayList<ReceivedMessage> listFiltered = new ArrayList<>();
                //get specific items
                for(ReceivedMessage mess : originalList)
                {
                    if(mess.getTopic().toUpperCase().contains(constraint))
                        listFiltered.add(mess);
                }
                results.values = listFiltered;
                results.count = listFiltered.size();
            }
            else
            {
                results.values = originalList;
                results.count = originalList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = ((ArrayList<ReceivedMessage>)results.values);
            notifyDataSetChanged();
        }

    }
}
