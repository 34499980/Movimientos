package com.android.holamundo.app1.ConstantsController;

import android.graphics.Color;
import android.os.Build;

public class Constants {
    public static final String DATABASE_NAME = "Gastos";
    public static final int DATABASE_VERSION = 1;
    public static final int CantCategorias= 1;
    public static final int Compra= 0;
    public static final int Venta= 1;
    public static final int Prestamo= 2;
    public static final int Devolucion= 3;
    public static final int Ingreso= 4;
    public static final int FinLista =-1;
    public static final String  colorActionBar = "#212121" ;
    public enum SQLQUERY{
        InsertarUsuario
    }
    public enum SQLTABLES{
        USUARIOS,CATEGORIA,TIPO,MESES,ANO,CUOTA,DEUDA,CUOTAS_PENDIENTES,TOTALES,MOVIMIENTOS,ConfiguracionGeneral
    }
    public enum SQLCOLUMNS{
        IdUsuario,IdCategoria,IdMovimiento,IdMes,IdTipo,IdCuota,IdDeuda,IdCuotaPendiente,IdTotal,IdConfiguracion
    }


}
