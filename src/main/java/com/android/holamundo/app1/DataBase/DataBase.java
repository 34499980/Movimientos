package com.android.holamundo.app1.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.ArrayList;
import java.util.List;


public class DataBase extends SQLiteOpenHelper {
    private static SQLiteDatabase db = null;
    private static DataBase b;
    private static Context context = null;
        
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // this.context = context;
        String query ="";
        //Crear consulta de tabla usuario
        query = StringTableUser();
        //Crea tabla usuario
        Ejecutar(query,db);
        //Crear consulta para primera row de tabla usuario
       // query =FirstUsuarioOnCreate();
        //crea primera row de la tabla de usuario
       // Ejecutar(query,db);
        //Crear consulta de tabla de categorias.
        query = StringTableCategoria();
        //Crear tabla categorias
        Ejecutar(query,db);
        query = StringTableTipo();
        //Crear tabla tipo
        Ejecutar(query,db);
        query = StringTableMeses();
        //Crear tabla meses
        Ejecutar(query,db);
        query = StringTableDeuda();
        //Crear tabla deuda
        Ejecutar(query,db);
        query = StringTableTotales();
        //Crear tabla totales
        Ejecutar(query,db);
        query = StringCuotasPendientes();
        //Crear tabla pendientes
        Ejecutar(query,db);
        query = StringTableCuota();
        //Crear tabla cuota
        Ejecutar(query,db);
        query = StringTableMovimientos();
        //Crear tabla movimientos
        Ejecutar(query,db);
        //Creo tabla configuracion
        query=StringTableConfiguracionGeneral();
        Ejecutar(query,db);
        //Crear Tipos
       List<String> listTipos = IngresarTipos();
       for(int i = 0 ; i< listTipos.size();i++) {
           Ejecutar(listTipos.get(i), db);
       }


    }
    public static void onCreat(SQLiteDatabase db) {
        // this.context = context;
        String query ="";
        //Crear consulta de tabla usuario
        query = StringTableUser();
        //Crea tabla usuario
        Ejecutar(query,db);
        //Crear consulta para primera row de tabla usuario
        query =FirstUsuarioOnCreate();
        //crea primera row de la tabla de usuario
        Ejecutar(query,db);
        //Crear consulta de tabla de categorias.
        query = StringTableCategoria();
        //Crear tabla categorias
        Ejecutar(query,db);


    }
    private List<String> IngresarTipos(){
        List<String> list = new ArrayList<String>();
       list.add("INSERT INTO TIPO VALUES(0,'Compra')");

         list.add("INSERT INTO TIPO VALUES(1,'Venta')");

         list.add("INSERT INTO TIPO VALUES(2,'Prestamo')");
          list.add("INSERT INTO TIPO VALUES(3,'Devolucion')");
        list.add("INSERT INTO TIPO VALUES(4,'Ingreso')");
        return list;

    }

    private static String StringTableCategoria() {
    final String TABLE_NAME = "CATEGORIA";
    final String COLUMN_IdCategoria ="IdCategoria";
    final String COLUMN_DESCRIPCION = "Descripcion";
    final String COLUMN_IMAGEN = "Imagen";
    final String COLUMN_CANT = "Cant";
    final String COLUMN_IDUSUAIO = "IdUsuario";

    String SqlCreateCategoria ="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( "+ COLUMN_IdCategoria + " INTEGER NOT NULL, "+ COLUMN_DESCRIPCION + " TEXT NOT NULL," +
            COLUMN_IMAGEN+ " TEXT NULL,"+COLUMN_CANT+" INTEGER NULL, "+ COLUMN_IDUSUAIO+" INTEGER NOT NULL)";
    return SqlCreateCategoria;
    }

    private static String StringTableUser() {
     final String TABLE_NAME = "USUARIOS";
     final String COLUMN_IdUsuario = "IdUsuario";
     final String COLUMN_NOMBRE = "Nombre";
     final String COLUMN_MAIL = "Mail";
     final String COLUMN_FECHANACIMIENTO = "FechaNacimiento";
     final String COLUMN_CONTRASEÑA = "Contraseña";

    //  String SqlCreateUsuario = "DROP TABLE USUARIOS";
     String SqlCreateUsuario = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
             "( "+ COLUMN_IdUsuario +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_NOMBRE + " TEXT NOT NULL, " + COLUMN_CONTRASEÑA +
             "TEXT NULL NOT NULL, "  + COLUMN_MAIL + " TEXT NULL, " + COLUMN_FECHANACIMIENTO + " DATETIME NULL)";
        return SqlCreateUsuario;
    }
    private static String StringTableTipo(){
        final String TABLE_NAME = "TIPO";
        final String COLUMN_IDTIPO = "IdTipo";
        final String COLUMN_DESCRIPCION = "Descripcion";

        String SqlCreateTipo = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDTIPO +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_DESCRIPCION + " TEXT NOT NULL)";
        return SqlCreateTipo;
    }
    private static String StringTableMeses(){
        final String TABLE_NAME = "MES";
        final String COLUMN_IDMES = "IdMes";
        final String COLUMN_DESCRIPCION = "Descripcion";

        String SqlCreateTipo = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDMES +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_DESCRIPCION + " TEXT NOT NULL)";
        return SqlCreateTipo;
    }
    private static String StringTableDeuda(){
        final String TABLE_NAME = "DEUDA";
        final String COLUMN_IDDEUDA = "IdDeuda";
        final String COLUMN_MONTO = "Monto";
        final String COLUMN_IDUSUAIO = "IdUsuario";
        final String COLUMN_IDMOVIMIENTO = "IdMovimiento";
        final String COLUMN_MONTOTOTAL = "MontoTotal";

        String result = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDDEUDA +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_MONTO + " DECIMAL NOT NULL," + COLUMN_IDUSUAIO+" INTEGER NOT NULL, "+COLUMN_IDMOVIMIENTO+" INTEGER NOT NULL,"+COLUMN_MONTOTOTAL+" DECIMAL NOT NULL)";
        return result;
    }
    private static String StringTableTotales(){
        final String TABLE_NAME = "TOTALES";
        final String COLUMN_IDTOTALES = "IdTotales";
        final String COLUMN_IDMES = "IdMes";
        final String COLUMN_ANO = "Ano";
        final String COLUMN_INGRESO = "Ingerso";
        final String COLUMN_EGRESO = "Egreso";
        final String COLUMN_IDUSUAIO = "IdUsuario";

        String SqlCreateTotales = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDTOTALES +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_IDMES + " INTEGER NOT NULL, "+ COLUMN_ANO + " INTEGER NOT NULL, " +
                COLUMN_INGRESO + " DECIMAL NULL, "+ COLUMN_EGRESO + " DECIMAL NULL,"+ COLUMN_IDUSUAIO+" INTEGER NOT NULL)";
        return SqlCreateTotales;
    }
    private static String StringCuotasPendientes(){
        final String TABLE_NAME = "CUOTAS_PENDIENTES";
        final String COLUMN_IDPENDIENTE = "IdPendiente";
        final String COLUMN_IDMOVIMIENTO = "IdMovimiento";
        final String COLUMN_IDUSUAIO = "IdUsuario";

        String result = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDPENDIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_IDMOVIMIENTO + " INTEGER NOT NULL,"+ COLUMN_IDUSUAIO+" INTEGER NOT NULL)";
        return result;
    }
    private static String StringTableCuota(){
        final String TABLE_NAME = "CUOTA";
        final String COLUMN_IDCUOTA = "IdCuota";
        final String COLUMN_MONTO = "Monto";
        final String COLUMN_TOTALES = "Totales";
        final String COLUMN_ACTUALES = "Actuales";
        final String COLUMN_MES = "IdMes";
        final String COLUMN_ANO = "IdMovimiento";
        final String COLUMN_IDUSUAIO = "IdUsuario";

        String SqlCreateTotales = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDCUOTA +" INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_MES + " INTEGER NOT NULL, "+ COLUMN_ANO + " INTEGER NOT NULL, " +
                COLUMN_MONTO + " DECIMAL NOT NULL, "+ COLUMN_TOTALES + " INTEGER NULL, " + COLUMN_ACTUALES + " INTEGER NOT NULL,"+ COLUMN_IDUSUAIO+" INTEGER NOT NULL)";
        return SqlCreateTotales;
    }
    private static String StringTableMovimientos(){
        final String TABLE_NAME = "MOVIMIENTOS";
        final String COLUMN_IDMOVIMIENTO = "IdMovimiento";
        final String COLUMN_MONTO = "Monto";
        final String COLUMN_DESCRIPCION = "Descripcion";
        final String COLUMN_IDTIPO = "IdTipo";
        final String COLUMN_CATEGORIA = "IdCategoria";
        final String COLUMN_MES = "IdMes";
        final String COLUMN_ANO = "Ano";
        final String COLUMN_CUOTA = "Cuota";
        final String COLUMN_CANT = "Cant";
        final String COLUMN_IDUSUAIO = "IdUsuario";
        final String COLUMN_FECHA = "Fecha";

        String SqlCreateTotales = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDMOVIMIENTO +" INTEGER PRIMARY KEY, " +  COLUMN_MES + " INTEGER NOT NULL, "+ COLUMN_ANO + " INTEGER NOT NULL, " +
                COLUMN_MONTO + " DECIMAL NOT NULL, "+COLUMN_DESCRIPCION+" TEXT NOT NULL," + COLUMN_CATEGORIA + " INTEGER NOT NULL, "+
                COLUMN_IDTIPO + " INTEGER NOT NULL, " + COLUMN_CUOTA + " INTEGER DEFAULT 0,"+COLUMN_CANT+" INTEGER NULL, "+ COLUMN_IDUSUAIO+" INTEGER NOT NULL,"+COLUMN_FECHA+" TEXT NOT NULL)";
        return SqlCreateTotales;
    }
    private static String StringTableConfiguracionGeneral(){
        final String TABLE_NAME = "ConfiguracionGeneral";
        final String COLUMN_IDCONFIGURACION = "IdConfiguracion";
        final String COLUMN_Key = "Key";
        final String COLUMN_VALUE = "Value";
        final String COLUMN_IDUSUAIO = "IdUsuario";


        String SqlCreateTotales = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "( "+ COLUMN_IDCONFIGURACION +" INTEGER PRIMARY KEY, " +COLUMN_Key+" TEXT NOT NULL," +COLUMN_VALUE+" TEXT NOT NULL, "+ COLUMN_IDUSUAIO+" INTEGER NOT NULL)";
        return SqlCreateTotales;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
    public static SQLiteDatabase getConnection(SQLiteDatabase b){

        try
        {
            if(db == null) {
                db = b;
            }
            return db;
        }
        catch(Exception ex)
        {
            Toast.makeText(context, "No se pudo conectar a la base", Toast.LENGTH_LONG).show();
            return null;
        }
    }
    public static void Ejecutar(String consulta,SQLiteDatabase db) {
     try
     {
         db.execSQL(consulta);

     }catch(Exception ex) {
         throw ex;
     }
    }
    public static Cursor Traer(String consulta,SQLiteDatabase db) {
        try
        {
            Cursor c;
            c = db.rawQuery(consulta,null);
            return c;

        }catch(Exception ex) {
            throw ex;
        }
    }
    private static String FirstUsuarioOnCreate()
    {
        return"INSERT INTO USUARIOS VALUES(0,'primerusuario','asd',null,null)";
    }
    public static int getLastId(Constants.SQLTABLES tabla,Constants.SQLCOLUMNS column, SQLiteDatabase db){
        int id = -1;
        try {
            Cursor c;

            c = Traer("SELECT " + column + " FROM " + tabla + " order by " + column + " desc LIMIT 1", db);
            c.moveToNext();
            id = Integer.parseInt(c.getString(0));

            return id;
        }catch(Exception ex)
        {
            return id;
        }
    }
    public void setConnection(DataBase b,SQLiteDatabase db)
    {
        this.db = db;
        this.b = b;
    }

    public static SQLiteDatabase getDb()     {
        return db;
    }
}
