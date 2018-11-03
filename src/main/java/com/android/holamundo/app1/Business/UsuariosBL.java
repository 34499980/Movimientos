package com.android.holamundo.app1.Business;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Entities.Usuarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsuariosBL {
    public static Usuario getUsuarioByNombre(Map<String,Object> datos, SQLiteDatabase db){
        Usuario user = null;
        try
        {
            String nombre = datos.get("Nombre").toString();
            String contraseña = datos.get("Contraseña").toString();
            String query = "SELECT * FROM USUARIOS WHERE NOMBRE='" + nombre+"'";
            Cursor c = DataBase.Traer(query,db);
            if(c.moveToFirst()) {
                user = Usuario.getUsuario();
                user.Id = c.getInt(0);
                user.Nombre = c.getString(1).substring(0,1).toUpperCase() +  c.getString(1).substring(1, c.getString(1).length()).toLowerCase();
                user.Contraseña = c.getString(2);
                user.FechaNacimiento = c.getString(4);
                user.Mail = c.getString(3);
            }
            return user;

        }catch(Exception ex)
        {
            throw ex;
        }
    }
    public static Usuario getUsuarioById(SQLiteDatabase db, int id){
        Usuario user = null;
        try
        {

            String query = "SELECT * FROM USUARIOS WHERE IdUsuario='" + id + "'";
            Cursor c = DataBase.Traer(query,db);
            if(c.moveToFirst()) {
                user = Usuario.getUsuario();
                user.Id = c.getInt(0);
                user.Nombre  = c.getString(1).substring(0,1).toUpperCase() +  c.getString(1).substring(1, c.getString(1).length()).toLowerCase();
                user.Contraseña = c.getString(2);
                user.FechaNacimiento = c.getString(4);
                user.Mail = c.getString(3);
            }
            return user;

        }catch(Exception ex)
        {
            throw ex;
        }
    }
    public static boolean InsertarUsuario(Constants.SQLQUERY tipo, Map<String, Object> datos, SQLiteDatabase db, Context context){
        boolean bInsertar = false;
        try {
            //DataBase.onCreat(db);
            Usuario user = getUsuarioByNombre(datos, db);
            if (user == null) {
                user = new Usuario();
                user.Nombre = datos.get("Nombre").toString();
                user.Contraseña = datos.get("Contraseña").toString();
                switch (tipo) {
                    case InsertarUsuario:
                        user.Id = (DataBase.getLastId(Constants.SQLTABLES.USUARIOS,Constants.SQLCOLUMNS.IdUsuario, db) + 1);
                        String query = "INSERT INTO USUARIOS VALUES(" + user.Id + ",'" + user.Nombre + "','" + user.Contraseña + "',null,null)";
                        DataBase.Ejecutar(query, db);
                        bInsertar = true;
                        break;
                }
            } else {
                bInsertar = false;
                Toast.makeText(context, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            }
            return bInsertar;
        }catch(Exception ex)
        {
            throw ex;
        }

    }
    public static List<Usuarios> getAllUsuarios(SQLiteDatabase db) {
        List<Usuarios> listUsuarios = new ArrayList<>();
        Usuarios user;

        try
        {
            String query = "SELECT * FROM USUARIOS";
            Cursor c = DataBase.Traer(query, db);
            if(c.moveToNext()) {
                do {
                  //  if (!c.getString(0).equals("0")) {
                        user = new Usuarios();
                        user.Id = Integer.valueOf(c.getString(0));
                        user.Nombre = c.getString(1).substring(0,1).toUpperCase() +  c.getString(1).substring(1, c.getString(1).length()).toLowerCase();
                        listUsuarios.add(user);
                   // }


                } while (c.moveToNext());
            }

            return listUsuarios;
        }catch(Exception ex){
            throw ex;
        }
    }
    public static void DeleteUserByNombre(String nombre, SQLiteDatabase db){
        String query = "DELETE FROM USUARIOS WHERE Nombre='" + nombre + "'";
        DataBase.Ejecutar(query,db);
    }
    public static void IngresarDatosPersonales(SQLiteDatabase db){
        try {
            Usuario user = Usuario.getUsuario();
            String query = "UPDATE USUARIOS SET FechaNacimiento='"+  user.FechaNacimiento + "',Mail='" + user.Mail + "' WHERE Nombre='"+user.Nombre.toLowerCase()+"'";
            DataBase.Ejecutar(query, db);
        }catch(Exception ex) {
            throw ex;
        }
    }
}
