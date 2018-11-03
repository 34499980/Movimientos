package com.android.holamundo.app1.Helper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Components {
    public static Button CreateButton(Context context, int id, String texto, int colorTexto, int colorFondo, int ubicacion,int width, int height){
        Button button = new Button(context);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(width,height);
        button.setId(id);
        //texto = texto.substring(0,1).toUpperCase()+ texto.substring(1,texto.length()).toLowerCase();
        button.setText(texto);
        button.setTextColor(colorTexto);
        button.setBackgroundColor(colorFondo);
        button.setGravity(ubicacion);
        button.setOnClickListener((View.OnClickListener) context);
        ll.gravity = ubicacion;
        ll.setMargins(10,10,10,10);
        button.setAllCaps(false);
        button.setLayoutParams(ll);
        button.setHeight(height);
        return button;
    }
    public static AlertDialog.Builder CreateDialog(Context context, Map<String,Object> controls, String titulo)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        TextView Enter;
        alertDialog.setTitle(titulo);
        alertDialog.setMessage("\n");
        LinearLayout lp = new LinearLayout(context);
        if(controls != null){
        for (Map.Entry<String,Object> item: controls.entrySet()) {

            switch (item.getKey()) {
                case "EditText":
                    EditText edit = (EditText) item.getValue();
                    lp.setGravity(Gravity.CENTER);
                    alertDialog.setMessage("\n");
                    lp.addView(edit);

                    break;
                case "TextView":
                    TextView text = (TextView) item.getValue();
                    lp.addView(text);
                    break;
                case "listText":
                  for (TextView itemList: (List<TextView>)item.getValue()) {
                        Enter = new TextView(context);
                        Enter.setText("\n");
                        lp.setGravity(Gravity.CENTER);
                       // alertDialog.setMessage("\n");
                        lp.addView(Enter);
                        alertDialog.setView(lp);
                        lp.addView(itemList);
                        alertDialog.setView(lp);
                    }

                    break;
                case "list":
                    for (Object itemList: (List<Object>)item.getValue()) {
                        String clase = itemList.getClass().getName().substring(itemList.getClass().getName().indexOf(".",itemList.getClass().getName().indexOf("widget"))+1,itemList.getClass().getName().length());
                      if(clase.equals("TextView")){
                          Enter = new TextView(context);
                          Enter.setText("\n");
                          lp.setGravity(Gravity.CENTER);
                          // alertDialog.setMessage("\n");
                          lp.addView(Enter);
                          alertDialog.setView(lp);
                          lp.addView((TextView)itemList);
                          alertDialog.setView(lp);
                      }else{
                          Enter = new EditText(context);
                          Enter.setText("\n");
                          lp.setGravity(Gravity.CENTER);
                          // alertDialog.setMessage("\n");
                          lp.addView(Enter);
                          alertDialog.setView(lp);
                          lp.addView((EditText)itemList);
                          alertDialog.setView(lp);
                      }
                        /*  Enter = new TextView(context);
                        Enter.setText("\n");
                        lp.setGravity(Gravity.CENTER);
                        // alertDialog.setMessage("\n");
                        lp.addView(Enter);
                        alertDialog.setView(lp);
                        lp.addView(itemList);
                        alertDialog.setView(lp);*/
                    }

                    break;
                case"ImageView":
                    ImageView image = (ImageView) item.getValue();
                    if(image.getParent()!=null)
                        ((ViewGroup)image.getParent()).removeView(image);

                    lp.addView(image);
                    alertDialog.setMessage("\n");
                    break;
                case "GridView":
                    GridView grid = (GridView) item.getValue();
                    lp.addView(grid);
                    break;


            }
            alertDialog.setView(lp);
        }


        }
        return alertDialog;

    }
    public static EditText CreateEditText(Context context, String hint){
        EditText control = new EditText(context);
        control.setHint(hint);
        return control;
    }
    public static TextView CreateText(Context context,String text){
        TextView texto = new TextView(context);
        texto.setText(text);
        return texto;
    }
    public static ImageView CreateImageView(Context context,int id){
        ImageView image = new ImageView(context);
        image.setImageResource(id);
        return  image;
    }
}
