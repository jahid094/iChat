package com.example.ichat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    int[] flags;
    String[] countryNames;
    String[] countryCode;
    Context context;
    private LayoutInflater layoutInflater;

    CustomAdapter(Context context, int[] flags, String[] countryNames, String[] countryCode) {
        this.context = context;
        this.flags = flags;
        this.countryNames = countryNames;
        this.countryCode = countryCode;
    }

    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.sample_view,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.countryImageViewId);
        TextView countryNametextView = convertView.findViewById(R.id.countryNametextViewId);
        TextView countryCodetextView = convertView.findViewById(R.id.countryCodetextViewId);

        imageView.setImageResource(flags[position]);
        countryNametextView.setText(countryNames[position]);
        countryCodetextView.setText(countryCode[position]);
        return convertView;
    }
}
