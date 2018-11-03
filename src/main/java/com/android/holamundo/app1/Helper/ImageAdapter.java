package com.android.holamundo.app1.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int icons[];
    private List<Categorias> listCategorias;
    private LayoutInflater inflater;
    private int icons2[];
    private String letters[];
    private int Cant[];
    private boolean bStock=false;
    View gridView= null;

    public ImageAdapter(Context context, int icons[],List<Categorias> categorias) {
        this.context = context;
        this.icons = icons;
        if(categorias != null) {
          //  icons2 = new int[categorias.size()];
            letters = new String[categorias.size()];
            Cant =  new int[categorias.size()];
            int i=0;
            this.listCategorias = categorias;
            for (Categorias item: categorias) {
               // icons2[i] = Integer.valueOf(item.Imagen);
                letters[i] = item.nombre;
                Cant[i] = item.Cant;
                if(item.Cant > 0){
                    bStock =true;
                }
                i++;
            }
        }

    }
    public ImageAdapter(Context context, List<Categorias> categorias) {
        this.context = context;
        this.icons = icons;
        this.listCategorias = categorias;

    }

    public int getCount() {
        return this.icons.length;
    }

    public Object getItem(int position) {
        return icons[position];
    }

    public long getItemId(int position) {
        return icons[position];
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        try {
            gridView = convertView;
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                gridView = inflater.inflate(R.layout.activity_grid_image, null);
            }
            if (listCategorias == null) {
                ImageView icon = (ImageView) gridView.findViewById(R.id.icons);
                icon.setImageResource(icons[i]);
            } else {
                    ImageView icon = (ImageView) gridView.findViewById(R.id.icons);
                    TextView letter = (TextView) gridView.findViewById(R.id.letters);
                     TextView stock = (TextView) gridView.findViewById(R.id.Stock);

                //if(i < icons2.length) {
                    icon.setImageResource(icons[i]);
                    letter.setText(letters[i]);
                    stock.setText(String.valueOf(bStock==true?Cant[i]:""));
              //  }


            }
            return gridView;
        }catch(Exception ex) {
         throw ex;
        }
    }


}