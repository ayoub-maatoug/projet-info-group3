package com.example.faircorpapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.faircorpapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Cette classe permet de convertir notre list de map en adapter afin de créer la listview voulue
public class MapEntryListAdapter extends ArrayAdapter<Map.Entry<String, Integer>>
{
    public MapEntryListAdapter (Context context, List<Map.Entry<String, Integer>> entryList)
    {
        super (context, R.layout.new_activity_exemple, entryList);
    }

    @NonNull
    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        View currentItemView = convertView != null ? convertView :
                LayoutInflater.from (getContext()).inflate(
                        R.layout.new_activity_exemple, parent, false);

        Map.Entry<String, Integer> currentEntry = this.getItem(position);

        TextView textViewKey = currentItemView.findViewById(android.R.id.text1);
        TextView textViewValue = currentItemView.findViewById(android.R.id.text2);

        textViewKey.setText(currentEntry.getKey());
        textViewValue.setText(currentEntry.getValue().toString ());

        return currentItemView;
    }
}
