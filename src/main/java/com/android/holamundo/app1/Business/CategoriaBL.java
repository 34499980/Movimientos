package com.android.holamundo.app1.Business;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.widget.Toast;

import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.Entities.Tipo;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CategoriaBL {

     public static boolean IngresarCategoria(String nombre,long imagen, SQLiteDatabase db) {
         try {
                 if (!getCategoria(nombre, db).equals(nombre)) {
                     Usuario user = Usuario.getUsuario();

                    int id = DataBase.getLastId(Constants.SQLTABLES.CATEGORIA, Constants.SQLCOLUMNS.IdCategoria, db);
                     id++;
                     String query = "INSERT INTO CATEGORIA VALUES( " + id + ", '" + nombre.toLowerCase() + "','"+imagen+"',0 ,"+ user.Id +")";

                     DataBase.Ejecutar(query, db);
                     Categoria categoria = Categoria.getCategoria();
                     categoria.Nombre = nombre.toLowerCase();
                     categoria.Id = id;
                     categoria.Imagen = imagen;
                     return true;
                 } else {
                     throw new Exception("Ya existe la categoria");
                 }

         } catch (Exception ex) {
            return false;
         }
     }
    public static Categoria getCategoriaByName(String nombre,SQLiteDatabase db){
        try{
            Usuario user = Usuario.getUsuario();
            Categoria categoria=null;
            String query = "SELECT * FROM CATEGORIA WHERE Descripcion= '"+nombre.toLowerCase()+"' AND IdUsuario="+user.Id;
            Cursor c = DataBase.Traer(query,db);
            if(c.moveToNext()){
                categoria = Categoria.getCategoria();
                categoria.Id = Integer.valueOf(c.getString(0));
                categoria.Nombre = c.getString(1).substring(0, 1).toUpperCase() + c.getString(1).substring(1, c.getString(1).length()).toLowerCase();
                categoria.Imagen = c.getInt(2);
                categoria.Cant = c.getInt(3);
            }
            return  categoria;


        }catch(Exception ex){
            throw ex;
        }
    }
    public static String getCategoria(String nombre,SQLiteDatabase db){
         try{
             Usuario user = Usuario.getUsuario();
             String query = "SELECT * FROM CATEGORIA WHERE Descripcion= '"+nombre.toLowerCase()+"' AND IdUsuario="+user.Id;
             Cursor c = DataBase.Traer(query,db);

             return c.moveToNext()==false? "": c.getString(1).toString();
         }catch(Exception ex){
             throw ex;
         }
    }
    public static List<Categorias> getAllCategorias(SQLiteDatabase db) {
        List<Categorias> listCategoria = new ArrayList<>();
        Usuario user = Usuario.getUsuario();
        String query = "SELECT * FROM " + Constants.SQLTABLES.CATEGORIA + " WHERE IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query, db);
        Categorias categoria;
        if (c.moveToNext()) {
            do {
                //  if (!c.getString(0).equals("0")) {
                categoria = new Categorias();
                categoria.Id = Integer.valueOf(c.getString(0));
                categoria.nombre = c.getString(1).substring(0, 1).toUpperCase() + c.getString(1).substring(1, c.getString(1).length()).toLowerCase();
                categoria.Imagen = c.getString(2);
                categoria.Cant = c.getInt(3);
                listCategoria.add(categoria);
                // }


            } while (c.moveToNext());
        }
        return listCategoria;
    }
    public static void ActualizarImagen(String nombre, Long imagen,SQLiteDatabase db){
         try {
             Usuario user = Usuario.getUsuario();
             String query = "UPDATE CATEGORIA SET Imagen= '"+imagen+"' WHERE Descripcion='"+nombre.toLowerCase()+"' AND IdUsuario="+user.Id;
             DataBase.Ejecutar(query, db);
         }catch(Exception ex){
             throw ex;
         }
    }
    public static void ActualizarPorNombre(String nombreNuevo,String nombreViejo,Long imagen,SQLiteDatabase db){
         try{
             Usuario user = Usuario.getUsuario();
        if (!getCategoria(nombreNuevo, db).equals(nombreNuevo)) {

          //  int id = DataBase.getLastId(Constants.SQLTABLES.CATEGORIA, Constants.SQLCOLUMNS.IdCategoria, db);
           // id++;
            String query = "UPDATE CATEGORIA SET Imagen= '"+imagen+"' ,Descripcion= '" + nombreNuevo.toLowerCase() + "' WHERE Descripcion='"+nombreViejo.toLowerCase()+"' AND IdUsuario="+user.Id;
            DataBase.Ejecutar(query, db);

        } else {
            throw new Exception("Ya existe la categoria");
        }

        } catch (Exception ex) {

        }
    }
    public static void BorrarCategoria(String nombre,SQLiteDatabase db){
         try {
             if(!GetCategoriaUse(db,nombre)) {
                 Usuario user = Usuario.getUsuario();
                 String query = "DELETE FROM CATEGORIA WHERE Descripcion='" + nombre.toLowerCase() + "' AND IdUsuario=" + user.Id;
                 DataBase.Ejecutar(query, db);
             }else{
                 throw new UnsupportedOperationException("La categoria esta en uso.");
             }
         }catch(Exception ex){
            throw ex;
         }
    }
    public static List<Tipo> getTipos(SQLiteDatabase db){
         try {
             List<Tipo> listTipos = new ArrayList<Tipo>();
             Tipo tipo;
             String query = "SELECT * FROM TIPO";
             Cursor c = DataBase.Traer(query, db);
             if(c.moveToNext()){
                 do{
                     tipo = new Tipo();
                     tipo.IdTipo = c.getInt(0);
                     tipo.Descripcion = c.getString(1);
                     listTipos.add(tipo);
                 }while (c.moveToNext());
             }
             return listTipos;
         }catch(Exception ex){
             throw  ex;
         }
    }
    public static Categorias getCategoriaById(int id,SQLiteDatabase db){
        try{
           Categorias categoria=null;
            String query = "SELECT * FROM CATEGORIA WHERE IdCategoria= "+id;
            Cursor c = DataBase.Traer(query,db);
            if(c.moveToNext()){
                categoria = new Categorias();
                categoria.Id = c.getInt(0);
                categoria.nombre =c.getString(1);
                categoria.Cant = c.getInt(3);
            }
            return categoria;
        }catch(Exception ex){
            throw ex;
        }
    }
    public static Tipo getTipoById(int id,SQLiteDatabase db){
        try{
            Tipo tipo=null;
            String query = "SELECT * FROM TIPO WHERE IdTipo= "+id;
            Cursor c = DataBase.Traer(query,db);
            if(c.moveToNext()){
                tipo = new Tipo();
                tipo.IdTipo = c.getInt(0);
                tipo.Descripcion =c.getString(1);
            }
            return tipo;
        }catch(Exception ex){
            throw ex;
        }
    }
    public static void UpdateCantidad(SQLiteDatabase db,int idCategoria,int cant,int idTipo){
        int result;
        try {
            String query = "SELECT * FROM CATEGORIA WHERE IdCategoria=" + idCategoria;
            Cursor c = DataBase.Traer(query, db);
            if (c.moveToNext()) {
                if (idTipo == Constants.Venta) {
                    result = c.getInt(3) - cant;

                } else {
                    result = c.getInt(3) + cant;
                }
                query = "UPDATE CATEGORIA SET Cant=" + result + " WHERE IdCategoria=" + idCategoria;
                DataBase.Ejecutar(query, db);
            }
        }catch(Exception ex){
            throw  ex;
        }
    }
    private static Boolean GetCategoriaUse(SQLiteDatabase db, String nombre){
         Usuario user = Usuario.getUsuario();
        int id=-1;
        String query = "SELECT * FROM CATEGORIA WHERE Descripcion='"+nombre.toLowerCase()+"' AND IdUsuario="+user.Id;
         Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()) {
           id  = c.getInt(0);
        }

         query = "SELECT * FROM MOVIMIENTOS WHERE IdCategoria="+ id;
         c = DataBase.Traer(query,db);
         if(c.moveToNext()) {
             return true;
         }else{
             return false;
         }
    }


}
