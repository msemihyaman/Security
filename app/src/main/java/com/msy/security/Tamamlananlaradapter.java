package com.msy.security;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import java.util.ArrayList;

public class Tamamlananlaradapter extends ArrayAdapter <tamamlama> {

    private final Context mContext;
    int mResource;


    public Tamamlananlaradapter(Context context, int resource, ArrayList <tamamlama> objects) {
        super(context, resource, objects);
        mContext=context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String tc = getItem(position).getTc();
        String durum = getItem(position).getDurum();

        tamamlama tamamlama = new tamamlama(tc,durum);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);
        TextView tctext = (TextView)  convertView.findViewById(R.id.list_textview1);
        TextView durumtext = (TextView)  convertView.findViewById(R.id.list_textview2);
        tctext.setText(tc);
        durumtext.setText(durum);

        return convertView;
    }
}
