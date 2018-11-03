package com.android.holamundo.app1.Business;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;

public class ConfiguracionGeneralBL {

    public static int GetTableSize(SQLiteDatabase db){
        Usuario user =Usuario.getUsuario();
        int size = 12;
        String query="SELECT * FROM ConfiguracionGeneral WHERE Key='TablaSize' AND IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            size = c.getInt(2);
        }
        return size;
    }
    public static int GetTableHeaderSize(SQLiteDatabase db){
        Usuario user =Usuario.getUsuario();
        int size = 15;
        String query="SELECT * FROM ConfiguracionGeneral WHERE Key='TablaHeaderSize' AND IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            size = c.getInt(2);
        }
        return size;
    }
    public static void UpdateTableHeaderSize(SQLiteDatabase db,int valor){
        Usuario user = Usuario.getUsuario();
        String query = "UPDATE ConfiguracionGeneral SET Value="+valor+" WHERE Key='TablaHeaderSize' AND IdUsuario="+user.Id;
        DataBase.Ejecutar(query,db);
    }
    public static void UpdateTableSize(SQLiteDatabase db,int valor){
        Usuario user = Usuario.getUsuario();
        String query = "UPDATE ConfiguracionGeneral SET Value="+valor+" WHERE Key='TablaSize' AND IdUsuario="+user.Id;
        DataBase.Ejecutar(query,db);
    }
    public static void InsertarConfiguracionInicial(SQLiteDatabase db){
        Usuario user = Usuario.getUsuario();
        int id = DataBase.getLastId(Constants.SQLTABLES.ConfiguracionGeneral,Constants.SQLCOLUMNS.IdConfiguracion,db)+1;
        String query = "INSERT INTO ConfiguracionGeneral VALUES("+id+",'Usabilidad','Categoria',"+user.Id+")";
        DataBase.Ejecutar(query,db);
        id++;
        query = "INSERT INTO ConfiguracionGeneral VALUES("+id+",'TablaSize',12,"+user.Id+")";
        DataBase.Ejecutar(query,db);
        id++;
        query = "INSERT INTO ConfiguracionGeneral VALUES("+id+",'TablaHeaderSize',15,"+user.Id+")";
        DataBase.Ejecutar(query,db);
    }
    public static String GetUsabilidad(SQLiteDatabase db){
        Usuario user =Usuario.getUsuario();
        String usabilidad = "Categoria";
        String query="SELECT * FROM ConfiguracionGeneral WHERE Key='Usabilidad' AND IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            usabilidad = c.getString(2);
        }
        return usabilidad;
    }
    public static void SetUsabilidad(SQLiteDatabase db,String valor){
        Usuario user =Usuario.getUsuario();
        String query = "UPDATE ConfiguracionGeneral SET Value='"+valor+"' WHERE Key='Usabilidad' AND IdUsuario="+user.Id;
        DataBase.Ejecutar(query,db);
    }
}
