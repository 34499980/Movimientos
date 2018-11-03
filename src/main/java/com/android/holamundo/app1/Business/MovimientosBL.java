package com.android.holamundo.app1.Business;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;

import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Deudas;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Cuota;

import com.android.holamundo.app1.Entities.Deuda;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.Totales;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.General;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientosBL {
    public static String IngresarMovimiento(SQLiteDatabase db, Map<String,String> movimiento){
        try {
            String query = "";

            Usuario user = Usuario.getUsuario();
            Calendar cal = Calendar.getInstance();
            int mes = cal.get(Calendar.MONTH);
            int año = cal.get(Calendar.YEAR);
            String categoria = movimiento.get("Categoria");
            int Monto =  Integer.valueOf(movimiento.get("Monto"));

            int idCategoria = General.getIdByName(Constants.SQLTABLES.CATEGORIA, Constants.SQLCOLUMNS.IdCategoria, user.Id, movimiento.get("Categoria").toLowerCase(), db);
            int idTipo = General.getIdByName(Constants.SQLTABLES.TIPO, Constants.SQLCOLUMNS.IdTipo, user.Id, movimiento.get("Tipo"), db);
            //int idMes = General.getIdByName(Constants.SQLTABLES.MESES, Constants.SQLCOLUMNS.IdMes, user.Id, movimiento.get("IdMes"), db);
            int id = DataBase.getLastId(Constants.SQLTABLES.MOVIMIENTOS, Constants.SQLCOLUMNS.IdMovimiento, db) + 1;
            int IdDeuda =  -1;
            int Cuota =  movimiento.get("Cuotas").isEmpty()?-1: Integer.valueOf(movimiento.get("Cuotas"));
            int Cant = movimiento.get("Cant").isEmpty()?-1: Integer.valueOf(movimiento.get("Cant"));





            if (!movimiento.get("Deuda").isEmpty()) {
                if(Integer.parseInt(movimiento.get("Deuda"))<Integer.parseInt(movimiento.get("Monto"))) {
                    //float Monto = Integer.parseInt(movimiento.get("Monto")) -Integer.parseInt(movimiento.get("Deuda"));
                    query = "INSERT INTO " + Constants.SQLTABLES.MOVIMIENTOS + " VALUES(" + id + "," + mes + "," + año + "," + Integer.parseInt(movimiento.get("Monto")) + ",'" + movimiento.get("Descripcion") +
                            "'," + idCategoria + "," + idTipo + "," + Cuota + "," + Cant + "," + user.Id + ", '" + General.GetToday() + "')";
                    DataBase.Ejecutar(query, db);
                }
                IngresarDeuda(db, Integer.parseInt(movimiento.get("Deuda")),user.Id,id,Integer.parseInt(movimiento.get("Monto")));

            }
            else{
                if (!movimiento.get("Cuotas").isEmpty()) {
                    Monto =  Monto/Cuota;
                    IngresarCuotas(db, Monto, Cuota,id, mes,user.Id);
                }
                query = "INSERT INTO "+ Constants.SQLTABLES.MOVIMIENTOS+" VALUES(" + id + "," + mes + "," + año + "," + Integer.parseInt(movimiento.get("Monto")) + ",'" + movimiento.get("Descripcion") +
                        "'," + idCategoria + "," + idTipo + "," + Cuota + "," + Cant + "," + user.Id + ", '"+ General.GetToday()+"')";
                DataBase.Ejecutar(query, db);
            }

            if(Cant > 0){
                CategoriaBL.UpdateCantidad(db,idCategoria,Cant,idTipo);
            }
            /*mes,año,monto,descripcion,deuda,,categoria,tipo,cuota,cant,usuario*/

            return "Se guardo correctamente!";
        }catch (Exception ex){
            throw ex;
        }

    }
    private static void IngresarCuotas(SQLiteDatabase db,int monto,int numeroCuotas,int IdMovimiento,int mes, int idUsuario){
        int id = DataBase.getLastId(Constants.SQLTABLES.CUOTA,Constants.SQLCOLUMNS.IdCuota,db)+1;
       // int cuota = monto / numeroCuotas;
        String query = "INSERT INTO "+Constants.SQLTABLES.CUOTA+" VALUES("+id+","+mes+", "+IdMovimiento+", "+monto+", "+numeroCuotas+", "+1+","+idUsuario+")";
        DataBase.Ejecutar(query,db);
    }
    private static void IngresarDeuda(SQLiteDatabase db,int monto,int idUsuario,int IdMovimiento,int MontoTotal){
        int id = DataBase.getLastId(Constants.SQLTABLES.DEUDA,Constants.SQLCOLUMNS.IdDeuda,db)+1;
        String query= "INSERT INTO "+Constants.SQLTABLES.DEUDA+ " VALUES("+id+","+monto+","+idUsuario+","+IdMovimiento+","+MontoTotal+")";
        DataBase.Ejecutar(query,db);
    }
    public static   Map<String,List<Movimiento>>  GetByMes(SQLiteDatabase db) {
        Usuario user = Usuario.getUsuario();
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        Movimiento mov;
        int IdCategoriaAnt;
        Map<String,List<Movimiento>> listas = new HashMap<>();
        List<Movimiento> listMovimientosTodos = new ArrayList<>();
        List<Movimiento> listMovimientosCompra = new ArrayList<>();
        List<Movimiento> listMovimientosVenta = new ArrayList<>();
        List<Movimiento> listMovimientosIngresos = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.SQLTABLES.MOVIMIENTOS + " WHERE IdUsuario=" + user.Id + " and IdMes=" + mes + " ORDER BY IdCategoria";
        Cursor c = DataBase.Traer(query, db);
        while (c.moveToNext()) {
            mov = new Movimiento();
            mov.IdMovimiento = c.getInt(0);
            mov.IdMes = c.getInt(1);
            mov.Año = c.getInt(2);
            mov.Monto = c.getFloat(3);
            mov.Descripcion = c.getString(4);
            mov.IdCategoria = c.getInt(5);
            mov.IdTipo = c.getInt(6);
            mov.Cuota = c.getInt(7);
            mov.Cant = c.getInt(8);
            mov.IdUsuario = c.getInt(9);
            listMovimientosTodos.add(mov);




        }
        List<Deuda> listDeudas = GetAllDeudas(db);
        List<Cuota> listCuotas = GetAllCuotas(db);
        int indexCuotas;
        int indexDeuda;
        for (Movimiento item : listMovimientosTodos) {


             indexCuotas = General.GetIndexCuota(listCuotas,item.IdMovimiento);
            indexDeuda = General.GetIndexDeuda(listDeudas,item.IdMovimiento);
            if (indexCuotas > Constants.FinLista) {
                item.Monto = listCuotas.get(indexCuotas).Monto;
            }
            if (indexDeuda > Constants.FinLista) {
                item.Monto = listDeudas.get(indexDeuda).Monto;
            }
            switch (item.IdTipo) {
                case Constants.Compra:

                    listMovimientosCompra.add(item);
                        break;
                        case Constants.Venta:
                            listMovimientosVenta.add(item);
                            break;
                        case Constants.Prestamo:
                            listMovimientosCompra.add(item);
                            break;
                        case Constants.Devolucion:
                            listMovimientosVenta.add(item);
                            break;
                        case Constants.Ingreso:
                            listMovimientosIngresos.add(item);
                            break;
             }


            }
        listas.put("Compra",listMovimientosCompra);
        listas.put("Venta",listMovimientosVenta);
        listas.put("Ingresos",listMovimientosIngresos);
            return listas;

    }
    public static Cuota GetCuotaByIdMovimiento(SQLiteDatabase db, int Id){
        Cuota cuota=null;
        String query = "SELECT * FROM CUOTA WHERE IdMovimiento="+Id;
        Cursor c = DataBase.Traer(query,db);
        while (c.moveToNext()){
            cuota =  new Cuota();
            cuota.IdCuota = c.getInt(0);
            cuota.mes = c.getInt(1);
            cuota.IdMovimiento =c.getInt(2);
            cuota.Monto = c.getInt(3);
            cuota.CantCuotas = c.getInt(4);
            cuota.CuotaActual = c.getInt(5);

        }
        return cuota;
    }
    public static float GetTotales(List<Movimiento> list){
       float listNumCompra = 0;
        for (Movimiento mov:list) {
           listNumCompra+= mov.Monto;

        }
        return listNumCompra;
    }
    public static List<Cuota> GetAllCuotas(SQLiteDatabase db){
        Usuario user = Usuario.getUsuario();
        List<Cuota> listCuotas = new ArrayList<>();
        Cuota cuota=null;
        String query = "SELECT * FROM CUOTA WHERE IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        while (c.moveToNext()){
            cuota= new Cuota();
            cuota.IdCuota = c.getInt(0);
            cuota.mes = c.getInt(1);
            cuota.IdMovimiento =c.getInt(2);
            cuota.Monto = c.getInt(3);
            cuota.CantCuotas = c.getInt(4);
            cuota.CuotaActual = c.getInt(5);
            listCuotas.add(cuota);
        }

        return listCuotas;
    }
    public static Movimiento GetMovimientoById(SQLiteDatabase db,int id){
        try {
            Movimiento mov = null;
            String query = "SELECT * FROM MOVIMIENTOS WHERE IdMovimiento=" + id;
            Cursor c = DataBase.Traer(query, db);
            if (c.moveToNext()) {
                mov = new Movimiento();
                mov.IdMovimiento = c.getInt(0);
                mov.IdMes = c.getInt(1);
                mov.Año = c.getInt(2);
                mov.Monto = c.getFloat(3);
                mov.Descripcion = c.getString(4);
                mov.IdCategoria = c.getInt(5);
                mov.IdTipo = c.getInt(6);
                mov.Cuota = c.getInt(7);
                mov.Cant = c.getInt(8);
                mov.IdUsuario = c.getInt(9);
                mov.Fecha = c.getString(10);
            }
            return mov;
        }catch(Exception ex){
            throw ex;
        }
    }
    public static List<Deuda> GetAllDeudas(SQLiteDatabase db){
        Usuario user = Usuario.getUsuario();
        List<Deuda> listDeudas = new ArrayList<>();
        Deuda deuda=null;
        String query = "SELECT * FROM DEUDA WHERE IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        while (c.moveToNext()){
            deuda = new Deuda();
            deuda.IdDeuda = c.getInt(0);
            deuda.Monto = c.getFloat(1);
            deuda.IdMovimiento = c.getInt(3);
            deuda.MontoTotal = c.getInt(4);
            listDeudas.add(deuda);
        }

        return listDeudas;
    }
    public static void UpdateDeuda(SQLiteDatabase db, int id, float valor){
        String query = "UPDATE DEUDA SET Monto="+valor+" WHERE IdDeuda="+id;
        DataBase.Ejecutar(query,db);
    }
    public static void UpdateTotalDeuda(SQLiteDatabase db, int id, float valor){
        String query = "UPDATE DEUDA SET MontoTotal="+valor+" WHERE IdDeuda="+id;
        DataBase.Ejecutar(query,db);
    }
    public static void UpdateTotalCuota(SQLiteDatabase db,int id, int valor){
        String query = "UPDATE CUOTA SET Monto="+valor+" WHERE IdCuota="+id;
        DataBase.Ejecutar(query,db);
    }
    public static void DeleteCuota(SQLiteDatabase db,int id){
        String query = "DELETE FROM CUOTA WHERE IdCuota="+id;
        DataBase.Ejecutar(query,db);
    }
    public static void DeleteDeuda(SQLiteDatabase db,int id){
        String query = "DELETE FROM DEUDA WHERE IdDeuda="+id;
        DataBase.Ejecutar(query,db);
    }
    public static void UpdateMovimiento(SQLiteDatabase db,Map<String,String> movimiento){
        try {
            String query = "UPDATE MOVIMIENTOS SET Monto=" + movimiento.get("Monto") +
                    ",Descripcion='" + movimiento.get("Descripcion") +
                    "',IdTipo=" + movimiento.get("Tipo") +
                    ",IdCategoria=" + movimiento.get("Categoria") +
                    " WHERE IdMovimiento=" + movimiento.get("IdMovimiento");
            DataBase.Ejecutar(query, db);
        }catch(Exception ex){
            throw ex;
        }
    }
    public static List<Movimiento> GetMovimientosByMes(SQLiteDatabase db,int mes,int año){
        Usuario user = Usuario.getUsuario();
        List<Movimiento> listMovmientos = new ArrayList<>();
        List<Deuda> listDeudas = GetAllDeudas(db);
        List<Cuota> listCuotas = GetAllCuotas(db);

        Movimiento mov;
        String query = "SELECT * FROM MOVIMIENTOS WHERE IdUsuario="+user.Id+" AND IdMes="+mes+" AND Ano="+año;
        Cursor c = DataBase.Traer(query,db);
        int indexCuotas;
        int indexDeuda;
        while(c.moveToNext()){
            mov = new Movimiento();
            mov.IdMovimiento = c.getInt(0);
            mov.IdMes = c.getInt(1);
            mov.Año = c.getInt(2);
             indexCuotas = General.GetIndexCuota(listCuotas, mov.IdMovimiento);
             indexDeuda = General.GetIndexDeuda(listDeudas, mov.IdMovimiento);
            if (indexCuotas > Constants.FinLista) {
                mov.Monto = listCuotas.get(indexCuotas).Monto;
            }else if(indexDeuda > Constants.FinLista) {
                mov.Monto = listDeudas.get(indexDeuda).Monto;
            }else {
                mov.Monto = c.getFloat(3);
            }
            mov.Descripcion = c.getString(4);
            mov.IdCategoria = c.getInt(5);
            mov.IdTipo = c.getInt(6);
            mov.Cuota = c.getInt(7);
            mov.Cant = c.getInt(8);
            mov.IdUsuario = c.getInt(9);
            mov.Fecha = c.getString(10);
            listMovmientos.add(mov);
             indexCuotas = listCuotas.size()-1;
             indexDeuda = listDeudas.size()-1;
        }
        return listMovmientos;

    }
    public static void ActualizarCuotas(SQLiteDatabase db){
        Usuario user = Usuario.getUsuario();
        int mes;
        String query ="SELECT * FROM MOVIMIENTOS WHERE IdUsuario="+user.Id+" ORDER BY IdMovimiento DESC LIMIT 1";
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            mes = c.getInt(1);
            Calendar cal = Calendar.getInstance();
            int mesActual = cal.get(Calendar.MONTH);
            if(mes!=mesActual){
             List<Cuota> listCuotas = GetAllCuotas(db);
                for (int i = 0; i< listCuotas.size();i++) {
                    listCuotas.get(i).CuotaActual++;
                    if( listCuotas.get(i).CuotaActual> listCuotas.get(i).CantCuotas) {
                        DeleteCuota(db, listCuotas.get(i).IdCuota);
                    }else {
                        query = "UPDATE CUOTA SET IdMes="+mesActual+", Actuales=" +  listCuotas.get(i).CuotaActual + " WHERE IdCuota=" +  listCuotas.get(i).IdCuota;
                        DataBase.Ejecutar(query, db);
                        Movimiento mov = GetMovimientoById(db, listCuotas.get(i).IdMovimiento);
                        Map<String, String> Ingresos = new HashMap<>();
                        Ingresos.put("Categoria", CategoriaBL.getCategoriaById(mov.IdCategoria,db).nombre);
                        Ingresos.put("Tipo", CategoriaBL.getTipoById(mov.IdTipo,db).Descripcion);
                        Ingresos.put("Descripcion", mov.Descripcion);
                        Ingresos.put("Monto", String.valueOf( listCuotas.get(i).Monto));
                        Ingresos.put("IdUsuario", String.valueOf(user.Id));
                        Ingresos.put("Checked", String.valueOf(false));
                        Ingresos.put("Deuda", "");
                        Ingresos.put("Cant", "");
                        Ingresos.put("Cuotas", "");
                        IngresarMovimiento(db,Ingresos);
                    }
                }
            }
        }

    }
    public static Map<String,List<Movimiento>>  GetMovimientosProcesados(SQLiteDatabase db,int mes){
        Usuario user = Usuario.getUsuario();
        Movimiento mov;
        int IdCategoriaAnt;
        List<Deuda> listDeudas = GetAllDeudas(db);
        List<Cuota> listCuotas = GetAllCuotas(db);
        Map<String,List<Movimiento>> listas = new HashMap<>();
        List<Movimiento> listMovimientosTodos = new ArrayList<>();
        List<Movimiento> listMovimientosCompra = new ArrayList<>();
        List<Movimiento> listMovimientosVenta = new ArrayList<>();
        List<Movimiento> listMovimientosIngresos = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.SQLTABLES.MOVIMIENTOS + " WHERE IdUsuario=" + user.Id + " and IdMes=" + mes + " ORDER BY IdCategoria";
        Cursor c = DataBase.Traer(query, db);
        while (c.moveToNext()) {
            mov = new Movimiento();
            mov.IdMovimiento = c.getInt(0);
            mov.IdMes = c.getInt(1);
            mov.Año = c.getInt(2);
            mov.Monto = c.getFloat(3);
            mov.Descripcion = c.getString(4);
            mov.IdCategoria = c.getInt(5);
            mov.IdTipo = c.getInt(6);
            mov.Cuota = c.getInt(7);
            mov.Cant = c.getInt(8);
            mov.IdUsuario = c.getInt(9);
            listMovimientosTodos.add(mov);




        }
        int indexCuotas =0;
        int indexDeuda=0;
        for (Movimiento item : listMovimientosTodos) {
            indexCuotas =0;

            while (indexCuotas < listCuotas.size() && listCuotas.get(indexCuotas).IdMovimiento != item.IdMovimiento){
                indexCuotas++;
            }
            while (indexDeuda < listDeudas.size() && listDeudas.get(indexDeuda).IdMovimiento != item.IdMovimiento){
                indexDeuda++;
            }
            if (indexCuotas < listCuotas.size()) {
                item.Monto = listCuotas.get(indexCuotas).Monto;
            }
            if (indexDeuda < listDeudas.size()) {
                item.Monto = listDeudas.get(indexDeuda).Monto;
            }
            switch (item.IdTipo) {
                case Constants.Compra:

                    listMovimientosCompra.add(item);
                    break;
                case Constants.Venta:
                    listMovimientosVenta.add(item);
                    break;
                case Constants.Prestamo:
                    listMovimientosCompra.add(item);
                    break;
                case Constants.Devolucion:
                    listMovimientosVenta.add(item);
                    break;
                case Constants.Ingreso:
                    listMovimientosIngresos.add(item);
                    break;
            }


        }
        listas.put("Compra",listMovimientosCompra);
        listas.put("Venta",listMovimientosVenta);
        listas.put("Ingresos",listMovimientosIngresos);
        return listas;
    }
    public static String GetCategoriaByName(SQLiteDatabase db, String name){
        Usuario user = Usuario.getUsuario();
        String index ="0";
        String query = "SELECT * from CATEGORIA WHERE Descripcion= '"+ name.toLowerCase() +"' AND IdUsuario="+user.Id;
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            index = c.getString(0);
        }



        return index;
    }
    public static String GetTipoByName(SQLiteDatabase db, String name){
        Usuario user = Usuario.getUsuario();
        String index = "0";
        String query = "SELECT * from TIPO WHERE Descripcion= '"+ name+"'";
        Cursor c = DataBase.Traer(query,db);
        if(c.moveToNext()){
            index = c.getString(0);
        }



        return index;
    }
    public static void DeleteMovimientoById(SQLiteDatabase db,int id){
        String query = "DELETE FROM MOVIMIENTOS WHERE IdMovimiento="+id;
        DataBase.Ejecutar(query,db);
    }
    public static  Map<String,List<Movimiento>> GetAllMvimientosSorted(SQLiteDatabase db){
        Usuario user = Usuario.getUsuario();
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        Movimiento mov;
        int IdCategoriaAnt;
        Map<String,List<Movimiento>> listas = new HashMap<>();
        List<Movimiento> listMovimientosTodos = new ArrayList<>();
        List<Movimiento> listMovimientosCompra = new ArrayList<>();
        List<Movimiento> listMovimientosVenta = new ArrayList<>();
        List<Movimiento> listMovimientosIngresos = new ArrayList<>();
        String query = "SELECT * FROM " + Constants.SQLTABLES.MOVIMIENTOS + " WHERE IdUsuario=" + user.Id + " ORDER BY Ano,IdMes DESC";
        Cursor c = DataBase.Traer(query, db);
        while (c.moveToNext()) {
            mov = new Movimiento();
            mov.IdMovimiento = c.getInt(0);
            mov.IdMes = c.getInt(1);
            mov.Año = c.getInt(2);
            mov.Monto = c.getFloat(3);
            mov.Descripcion = c.getString(4);
            mov.IdCategoria = c.getInt(5);
            mov.IdTipo = c.getInt(6);
            mov.Cuota = c.getInt(7);
            mov.Cant = c.getInt(8);
            mov.IdUsuario = c.getInt(9);
            listMovimientosTodos.add(mov);




        }
        List<Deuda> listDeudas = GetAllDeudas(db);
        List<Cuota> listCuotas = GetAllCuotas(db);
        int indexCuotas;
        int indexDeuda;
        for (Movimiento item : listMovimientosTodos) {


            indexCuotas = General.GetIndexCuota(listCuotas,item.IdMovimiento);
            indexDeuda = General.GetIndexDeuda(listDeudas,item.IdMovimiento);
            if (indexCuotas > Constants.FinLista) {
                item.Monto = listCuotas.get(indexCuotas).Monto;
            }
            if (indexDeuda > Constants.FinLista) {
                item.Monto = listDeudas.get(indexDeuda).Monto;
            }
            switch (item.IdTipo) {
                case Constants.Compra:

                    listMovimientosCompra.add(item);
                    break;
                case Constants.Venta:
                    listMovimientosVenta.add(item);
                    break;
                case Constants.Prestamo:
                    listMovimientosCompra.add(item);
                    break;
                case Constants.Devolucion:
                    listMovimientosVenta.add(item);
                    break;
                case Constants.Ingreso:
                    listMovimientosIngresos.add(item);
                    break;
            }


        }
        listas.put("Compra",listMovimientosCompra);
        listas.put("Venta",listMovimientosVenta);
        listas.put("Ingresos",listMovimientosIngresos);
        return listas;
    }
    public static List<Totales> SortTotalesByMes(SQLiteDatabase db, Map<String,List<Movimiento>> list){
        int tipoAnt;
        int añoAnt;
        int mesAnt;
        int indexCuotas;
        int indexDeuda;
        Totales total;
        int index=0;
        List<Deuda> listDeudas = GetAllDeudas(db);
        List<Cuota> listCuotas = GetAllCuotas(db);
        List<Totales> listTotales = new ArrayList<>();
        List<Movimiento> listIngresos = list.get("Ingresos");
        List<Movimiento> listVenta = list.get("Venta");
        List<Movimiento> listCompra = list.get("Compra");

        while(index < listCompra.size()){
            total = new Totales();
            total.Año=listCompra.get(index).Año;
            añoAnt = listCompra.get(index).Año;
            while(index< listCompra.size() && añoAnt == listCompra.get(index).Año){
                mesAnt = listCompra.get(index).IdMes;
                total.Mes = listCompra.get(index).IdMes;
                while(index< listCompra.size() && añoAnt == listCompra.get(index).Año && mesAnt ==listCompra.get(index).IdMes){
                        indexCuotas = General.GetIndexCuota(listCuotas,listCompra.get(index).IdMovimiento);
                        indexDeuda = General.GetIndexDeuda(listDeudas,listCompra.get(index).IdMovimiento);
                        if (indexCuotas > Constants.FinLista) {
                            listCompra.get(index).Monto = listCuotas.get(indexCuotas).Monto;
                        }
                        if (indexDeuda > Constants.FinLista) {
                            listCompra.get(index).Monto = listDeudas.get(indexDeuda).Monto;
                        }

                            total.Compra +=  listCompra.get(index).Monto;
                        index ++;

                }
                listTotales.add(total);
            }
        }
        index = 0;
        while(index < listVenta.size()){

            añoAnt = listVenta.get(index).Año;
            while(index< listVenta.size() && añoAnt == listVenta.get(index).Año){
                mesAnt = listVenta.get(index).IdMes;
                int indexTotal = GetIndexTotal(listTotales,listVenta.get(index).Año,listVenta.get(index).IdMes);
                if(indexTotal == Constants.FinLista){
                    total = new Totales();
                    total.Año = listVenta.get(index).Año;
                    total.Mes = listVenta.get(index).IdMes;
                }else{
                    total = listTotales.get(indexTotal);
                }
                while(index< listVenta.size() && añoAnt == listVenta.get(index).Año && mesAnt == listVenta.get(index).IdMes){
                    indexCuotas = General.GetIndexCuota(listCuotas,listVenta.get(index).IdMovimiento);
                    indexDeuda = General.GetIndexDeuda(listDeudas,listVenta.get(index).IdMovimiento);
                    if (indexCuotas > Constants.FinLista) {
                        listVenta.get(index).Monto = listCuotas.get(indexCuotas).Monto;
                    }
                    if (indexDeuda > Constants.FinLista) {
                        listVenta.get(index).Monto = listDeudas.get(indexDeuda).Monto;
                    }

                    total.Venta +=  listVenta.get(index).Monto;
                    index ++;


                }
                if(indexTotal == Constants.FinLista){
                    listTotales.add(total);
                }
            }
        }
        index = 0;
        while(index < listIngresos.size()){

            añoAnt = listIngresos.get(index).Año;
            while(index< listIngresos.size() && añoAnt == listIngresos.get(index).Año){
                mesAnt = listIngresos.get(index).IdMes;
                int indexTotal = GetIndexTotal(listTotales,listIngresos.get(index).Año,listIngresos.get(index).IdMes);
                if(indexTotal == Constants.FinLista){
                    total = new Totales();
                    total.Año = listIngresos.get(index).Año;
                    total.Mes = listIngresos.get(index).IdMes;
                }else{
                    total = listTotales.get(indexTotal);
                }
                while(index< listIngresos.size() && añoAnt == listIngresos.get(index).Año && mesAnt == listIngresos.get(index).IdMes){
                    indexCuotas = General.GetIndexCuota(listCuotas,listIngresos.get(index).IdMovimiento);
                    indexDeuda = General.GetIndexDeuda(listDeudas,listIngresos.get(index).IdMovimiento);
                    if (indexCuotas > Constants.FinLista) {
                        listIngresos.get(index).Monto = listCuotas.get(indexCuotas).Monto;
                    }
                    if (indexDeuda > Constants.FinLista) {
                        listIngresos.get(index).Monto = listDeudas.get(indexDeuda).Monto;
                    }

                    total.Ingreso +=  listIngresos.get(index).Monto;
                    index ++;


                }
                if(indexTotal == Constants.FinLista){
                    listTotales.add(total);
                }
            }
        }

    return listTotales;
    }
    private static int GetIndexTotal(List<Totales> list,int año, int mes){
        int index = list.size()-1;
        while(index > Constants.FinLista && año != list.get(index).Año && mes != list.get(index).Mes){
            index --;
        }
        return index;
    }
    public static List<Movimiento> GetMovimientosByCategoriia(List<Movimiento> list,int cat){
        List<Movimiento> listCat = new ArrayList<>();
        for(Movimiento item: list){
            if(item.IdCategoria == cat){
                listCat.add(item);
            }
        }
        return listCat;
    }
    public static int CountCant(List<Movimiento> list){
        int count =0;
        for(Movimiento item: list){
            if(item.Cant > 0){
                count++;
            }
        }
        return count;
    }

}
