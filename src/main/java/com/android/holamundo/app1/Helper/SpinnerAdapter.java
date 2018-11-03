package com.android.holamundo.app1.Helper;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Categorias>{
    int groupId;
    Activity context;
    List<Categorias> listCategorias ;
    LayoutInflater inflanter;
    public SpinnerAdapter(Context context,List<Categorias> listCategorias){
        super(context,0,listCategorias);
        this.listCategorias=listCategorias;
        inflanter=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return initView(position,convertView,parent);

    }
    @Override
    public View getDropDownView(int position,View convertView,ViewGroup parent){
        return initView(position,convertView,parent);
    }
    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout,parent,false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.SpinnerImage);
        TextView textView = (TextView)convertView.findViewById(R.id.SpinnerText);
        imageView.setImageResource(Integer.valueOf(listCategorias.get(position).Imagen));
        textView.setText(listCategorias.get(position).nombre);
        return convertView;
    }
}
