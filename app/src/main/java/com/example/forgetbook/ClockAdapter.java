package com.example.forgetbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ClockAdapter extends ArrayAdapter<iteminclude> {
    private int newposition;
    public ClockAdapter(Context context, int position, List<iteminclude> clocklist){
        super(context,position,clocklist);
        newposition =position;
    }
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        iteminclude item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(newposition,parent,false);
        TextView title = view.findViewById(R.id.title);
        TextView time = view.findViewById(R.id.time);
        TextView things = view.findViewById(R.id.things);
        title.setText(item.getTitle());
        time.setText(item.getTime());
        things.setText(item.getThings());
        return view;
    }
}