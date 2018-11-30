package com.android.holamundo.app1.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Cuota;
import com.android.holamundo.app1.Entities.Deuda;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.Usuario;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static android.support.v4.content.ContextCompat.startActivity;

public class General extends Activity {
    public static int getIdByName(Constants.SQLTABLES tabla, Constants.SQLCOLUMNS columna, int IdUsuario,String nombre, SQLiteDatabase db){
        String query="";
        int id = -1;
        if(tabla==Constants.SQLTABLES.TIPO) {
            query = "SELECT " + "*" + " FROM " + tabla + " WHERE Descripcion='" + nombre+"'";
        }else {
            query = "SELECT " + "*" + " FROM " + tabla + " WHERE Descripcion='" + nombre + "' AND IdUsuario=" + IdUsuario;
        }
        Cursor c =DataBase.Traer(query,db);
        if(c.moveToNext());
        {
        id = c.getInt(0);
        }

        return id;
    }
    public static List<Movimiento> SortMovimientos(List<Movimiento> list){
        boolean bFlag=true;
        Movimiento aux;
        int i=0;
        if(list.size() > 1) {
            while (bFlag) {
                bFlag = false;
                while (i < list.size()-1) {
                    if (list.get(i).Monto < list.get(i + 1).Monto) {
                        aux = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, aux);
                        bFlag = true;
                    }
                    i++;
                }
                i = 0;
            }
        }
        return list;
    }
    public static List<Movimiento> ActumByCategoria(SQLiteDatabase db,List<Movimiento> list){
        List<Deuda> listDeudas = MovimientosBL.GetAllDeudas(db);
        List<Movimiento> listAux = new ArrayList<>();
        Movimiento mov;
        int j=0;
        int categoriaAnt;
        int i = 0;
        int cantTotal=0;
        float montoAcum=0;
        while(i < list.size()){
            mov = new Movimiento();
            mov.IdCategoria = list.get(i).IdCategoria;
            mov.IdTipo = list.get(i).IdTipo;
            categoriaAnt = list.get(i).IdCategoria;
            while (i < list.size()&&categoriaAnt==list.get(i).IdCategoria){
             /*   while( j < listDeudas.size() && listDeudas.get(j).IdMovimiento != list.get(i).IdMovimiento) {
                    j++;
                }*/
               // if(j == listDeudas.size()) {
                    mov.Monto += list.get(i).Monto;
                    montoAcum += list.get(i).Monto;
               // }else{
              //      mov.Monto += listDeudas.get(i).Monto;
               //     montoAcum += listDeudas.get(i).Monto;
              //  }
                if(list.get(i).Cant != -1) {
                    mov.Cant += list.get(i).Cant;
                    cantTotal += list.get(i).Cant;
                }
                i++;
            }
            listAux.add(mov);
            montoAcum =0;
            cantTotal=0;

        }
        return listAux;
    }
    public static String GetToday(){
        String todayAsString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        return todayAsString;
    }
    public static int GetIndexDeuda(List<Deuda> listDeudas,int IdMovimiento)
    {
        int indexDeuda = listDeudas.size()-1;
        while (indexDeuda > Constants.FinLista && listDeudas.get(indexDeuda).IdMovimiento != IdMovimiento) {
            indexDeuda--;
        }
        return indexDeuda;
    }
    public static int GetIndexCuota(List<Cuota> listCuotas,int IdMovimiento){
        int indexCuota = listCuotas.size()-1;
        while (indexCuota > Constants.FinLista && listCuotas.get(indexCuota).IdMovimiento != IdMovimiento) {
            indexCuota--;
        }
        return indexCuota;
    }
    public static File CreateExcel(int año, int mes,Context context,String[][] table){
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = String.valueOf(año)+String.valueOf(mes)+"Movimientos.xls";
        File directory = new File(sd.getAbsolutePath());
        directory.mkdirs();
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            File file = new File(directory+"/Document", csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;

            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Movimientos", 0);
            // column and row
            // sheet.addCell(new Label(0, 0, "UserName"));
            //sheet.addCell(new Label(1, 0, "PhoneNumber"));

            int i=0;
            int j=0;
            while(j < table.length){
                sheet.addCell(new Label(0, j, table[j][0]));
                sheet.addCell(new Label(1, j, table[j][1]));
                sheet.addCell(new Label(2, j, table[j][2]));
                sheet.addCell(new Label(3, j, table[j][3]));
                sheet.addCell(new Label(4, j, table[j][4]));
                sheet.addCell(new Label(5, j, table[j][5]));
                sheet.addCell(new Label(6, j, table[j][6]));
                sheet.addCell(new Label(7, j, table[j][7]));
                j++;
            }
           /* for (String[] row:rows) {
                for(String cell: row) {
                    sheet.addCell(new Label(i, j, cell));
                    j++;
                }
                j=0;
                i++;
            }*/
            workbook.write();
            workbook.close();
          //  Toast.makeText(context,"Excel creado", Toast.LENGTH_SHORT).show();
            return file;
        }catch(WriteException ex) {
         return null;

        }catch(IOException ex){
            return null;

        }
    }
    public static void SendEmail(SQLiteDatabase db,File file,Context context){
        try{
            Usuario user = Usuario.getUsuario();
            String email;
            String subject;
            String message;
            String attachmentFile;
            Uri URI = Uri.parse(file.getAbsolutePath());

            email = UsuariosBL.getUsuarioById(db,user.Id).Mail;
            subject ="Back Up Movimientos";
            message = "Se envia adjunto el back up del mes.";
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(emailIntent,""));


        }catch (Exception ex){
            file.delete();
        }
    }
    public static void  EliminarArchivos(int año,int mes){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),año+mes+"Movimientos.xls" );
        if(file.exists()) {
            file.delete();
        }
    }

}
