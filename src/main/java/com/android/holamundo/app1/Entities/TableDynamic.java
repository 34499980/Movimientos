package com.android.holamundo.app1.Entities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Deudas;
import com.android.holamundo.app1.Helper.Components;

import java.util.ArrayList;

public class TableDynamic {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private String[] data;
    private TableRow tableRow;
    private TextView textCell;
    private int indexC;
    private int indexR;
    private boolean multiColor=false;
    int firstColor, secondColor;
    int rows;
    private int TableSize;
    private int headerSize;



    public TableDynamic(TableLayout tablelayout, Context context) {
        this.tableLayout = tablelayout;
        this.context = context;

    }
    public void addHeader(String[] header){
        this.header = header;
        createHeader();
    }
    public void addData(String[] data){
        this.data = data;
       createDataTable();
    }
    private void newRow(){
        tableRow = new TableRow(context);

    }
    private void newCell(){
        textCell = new TextView(context);
        textCell.setGravity(Gravity.CENTER);
        textCell.setTextSize(TableSize);
        textCell.setTextColor(Color.WHITE);
    }
    private void createHeader(){
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
            textCell.setText(header[indexC++]);
            textCell.setTextSize(headerSize);
            tableRow.addView(textCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }
    private TableRow getRow(int index){
        return(TableRow)tableLayout.getChildAt(index);
    }
    private TextView getCell(int rowIndex, int columIndex){
        tableRow = getRow(rowIndex);
        return tableRow!=null? (TextView) tableRow.getChildAt(columIndex):null;
    }
    public void backgroundHeader(int color){
        indexC = 0;
        while(indexC<header.length){
         textCell = getCell(0,indexC++);
         textCell.setBackgroundColor(color);
        }

    }
    public void reColoring(){
        indexC = 0;
        indexR=0;
        while (indexR < rows) {
            multiColor = !multiColor;
            while (indexC < header.length) {
                textCell = getCell(indexR, indexC++);
                textCell.setBackgroundColor(multiColor ? firstColor : secondColor);
            }
            indexR++;
        }

    }
    public void backgroundData(int firstColor, int secondColor){

         for (indexR =1;indexR <= rows;indexR++){
        multiColor =! multiColor;
        for (indexC =0;indexC <header.length;indexC++){
            textCell = getCell(indexR,indexC);
            if(textCell != null)
            textCell.setBackgroundColor(multiColor?firstColor:secondColor);
        }

         }
         this.firstColor = firstColor;
         this.secondColor = secondColor;
    }
    public void createDataTable(){
        String info;
       // for (indexR =1;indexR <= header.length;indexR++){
        indexR =1;
            newRow();
            for (indexC =0;indexC <header.length;indexC++){
                newCell();
                String cell = data[indexC];
             //   info = (indexC<cell.length)?cell[indexC]:"";
                textCell.setText(cell);
                tableRow.addView(textCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
       // }
    }
   public void addItem(String[] item){
        String info;
      //  for (String row: item) {


        //data.add(row);
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
           // info=(indexC>row.length())?row[indexC]:"";
            textCell.setText(item[indexC]);
            tableRow.addView(textCell,newTableRowParams());
            indexC++;

        }
        tableLayout.addView(tableRow);
        reColoring();
      //  }
    }
    public void addItemDeuda(String[] item){
        String info;
        //  for (String row: item) {


        //data.add(row);
        indexC = 0;
        newRow();
        while(indexC<header.length){
            newCell();
            // info=(indexC>row.length())?row[indexC]:"";
            textCell.setText(item[indexC]);
            tableRow.addView(textCell,newTableRowParams());
            indexC++;

        }
        tableLayout.setTag(indexR);
        tableLayout.addView(tableRow);
        reColoring();
        //  }
    }
    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight=1;
        return params;
    }
    public void setRows(int rows){
        this.rows = rows;
    }
    public void removeAll(){
        //for (int i=0;i<tableLayout.getChildCount();i++){
          //  TableRow tr=(TableRow)tableLayout.getChildAt(i);
            tableLayout.removeAllViews();
        //}
        indexR=1;
    }
    public void setSize(int header,int table){
        TableSize = table;
        headerSize = header;
    }
}
