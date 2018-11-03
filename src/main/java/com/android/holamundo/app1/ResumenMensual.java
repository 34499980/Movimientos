package com.android.holamundo.app1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.Entities.Cuota;
import com.android.holamundo.app1.Entities.Deuda;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.TableDynamic;
import com.android.holamundo.app1.Entities.Tipo;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.Components;
import com.android.holamundo.app1.Helper.General;
import com.android.holamundo.app1.Helper.SpinnerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ResumenMensual extends AppCompatActivity {
    SQLiteDatabase db;
    ActionBarDrawerToggle toggle;
    Spinner spnTipo,spnCat;
    List<Categorias> listCategoria;
    List<Tipo> listTipos;
    TableLayout tblResumen;
    ImageView btnBuscar;
    TableDynamic tableDynamic;
    Spinner mesSpinner,añoSpinner;
    String[] header;
    String[] rows;
    List<Movimiento> listMovimientos;
    String[] arrayMes = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] arrayAños;
    View mView;
     List<Deuda> listDeudas;
     List<Cuota> listCuotas;
     Usuario user;
     int selectAño;
     int selectMes;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_mensual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!= null){
            toolbar.setTitle("");
            toolbar.setBackgroundColor(Color.parseColor(Constants.colorActionBar));
        }
        setSupportActionBar(toolbar);
        if (!checkPermission()) {
            openActivity();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                openActivity();
            }
        }
        user = Usuario.getUsuario();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()){
                    case R.id.btnLogOut:
                        Usuario.Destroy();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnCrearCat:
                        intent = new Intent(getApplicationContext(), ListarCategorias.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnIngresar:
                        intent = new Intent(getApplicationContext(), IngresarMovimiento.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnMes:
                        intent = new Intent(getApplicationContext(), ResumenMensual.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnAnuales:
                        intent = new Intent(getApplicationContext(), ResumenAnual.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnEditUser:
                        intent = new Intent(getApplicationContext(), DatosPersonales.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnCuotas:
                        intent = new Intent(getApplicationContext(), Cuotas.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.btnDeudas:
                        intent = new Intent(getApplicationContext(), Deudas.class);
                        startActivity(intent);
                        finish();
                        break;
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if(savedInstanceState == null)
            navigationView.setCheckedItem(R.id.drawer_layout);


        db = openOrCreateDatabase("Gastos",MODE_PRIVATE,null);

        Calendar cal = Calendar.getInstance();
        arrayAños = GetAños();
       CargarControles(cal.get(Calendar.MONTH),GetAñoIndex(String.valueOf(cal.get(Calendar.YEAR))));
       mesSpinner.setSelection(cal.get(Calendar.MONTH));
       añoSpinner.setSelection(GetAñoIndex(String.valueOf(cal.get(Calendar.YEAR))));

    }
    public void CargarControles(int mes,int año) {
        try{
            General.EliminarArchivos(año,mes);
            selectAño = año;
            selectMes = mes;
            Button btnEnviar = findViewById(R.id.btnEnviarMail);
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 15 );
            shape.setColor(Color.parseColor("#0091EA"));
            btnEnviar.setTextColor(Color.WHITE);
            findViewById(R.id.btnEnviarMail).setBackground(shape);
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        String[][] table;
                        int cant=MovimientosBL.CountCant(listMovimientos);
                      // if(cant>0) {
                             table = new String[listMovimientos.size() + 1][header.length + 3];
                      //  }else{
                      //     table = new String[listMovimientos.size() + 1][header.length + 1];
                     //  }
                        boolean bCabecera = false;
                        int indexCuotas;
                        int indexDeudas;
                        int i = 0;
                        int j =0;
                        while(j < listMovimientos.size()-1){

                                if (!bCabecera) {
                                    table[i][0] = ConfiguracionGeneralBL.GetUsabilidad(db);
                                    table[i][1] = "Descripcion";
                                    table[i][2] = "Tipo";
                                    table[i][3] = "Monto";
                                    table[i][4] = "Cant";
                                    table[i][5] = "Saldado";
                                    table[i][6] = "Cuotas";
                                    table[i][7] = "Fecha";
                                    bCabecera = true;
                                } else {
                                    indexCuotas = General.GetIndexCuota(listCuotas, listMovimientos.get(i).IdMovimiento);
                                    indexDeudas = General.GetIndexDeuda(listDeudas, listMovimientos.get(i).IdMovimiento);
                                    table[i][0] = CategoriaBL.getCategoriaById(listMovimientos.get(i).IdCategoria, db).nombre;
                                    table[i][1] = listMovimientos.get(i).Descripcion;
                                    table[i][2] = CategoriaBL.getTipoById(listMovimientos.get(i).IdTipo, db).Descripcion;
                                    table[i][3] = indexCuotas > Constants.FinLista ? String.valueOf(listCuotas.get(indexCuotas).Monto) : String.valueOf(listMovimientos.get(i).Monto);
                                    table[i][4] = listMovimientos.get(i).Cant != Constants.FinLista ? String.valueOf(listMovimientos.get(i).Cant):"";
                                    table[i][5] = indexDeudas > Constants.FinLista ? String.valueOf(listDeudas.get(indexDeudas).Monto) : "";
                                    table[i][6] = indexCuotas > Constants.FinLista ? String.valueOf(listCuotas.get(indexCuotas).CuotaActual + "/" + listCuotas.get(indexCuotas).CantCuotas) : "";
                                    table[i][7] = listMovimientos.get(i).Fecha;
                                    j++;
                               }


                            i++;

                        }

                       File file = General.CreateExcel(selectAño, selectMes, getApplicationContext(), table);
                       SendEmail(file);
                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(), "Error al generar el Excel", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        añoSpinner = findViewById(R.id.añoSpinner);
        mesSpinner = findViewById(R.id.mesSpinner);

        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableDynamic.removeAll();
                CargarControles(mesSpinner.getSelectedItemPosition(), añoSpinner.getSelectedItemPosition());
                mesSpinner.setSelection(selectMes);
                añoSpinner.setSelection(selectAño);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayMes);
        mesSpinner.setAdapter(adapter);
        //


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayAños);
        añoSpinner.setAdapter(adapter2);
        //

        tblResumen = findViewById(R.id.tblResumen);
        if (tableDynamic == null)
            tableDynamic = new TableDynamic(tblResumen, getApplicationContext());
         int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
         int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
        tableDynamic.setSize(headerSize,tableSize);
        listMovimientos = MovimientosBL.GetMovimientosByMes(db, selectMes, Integer.valueOf(arrayAños[selectAño]));
       if( MovimientosBL.CountCant(listMovimientos)>0) {
           header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db), "Descripcion", "Tipo", "Monto", "Cant", "Fecha"};
       }else{
           header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db), "Descripcion", "Tipo", "Monto", "Fecha"};
       }
        tableDynamic.addHeader(header);


        tableDynamic.setRows(listMovimientos.size());
        getRow(listMovimientos);
        tableDynamic.backgroundHeader(Color.parseColor("#0091EA"));
        tableDynamic.backgroundData(Color.parseColor("#BDBDBD"), Color.parseColor("#757575"));
         listDeudas = MovimientosBL.GetAllDeudas(db);
         listCuotas = MovimientosBL.GetAllCuotas(db);

        for (int i = 1; i < tblResumen.getChildCount(); i++) {

            final int index = i - 1;
            View child = tblResumen.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ResumenMensual.this);
                         mView = getLayoutInflater().inflate(R.layout.dialog, null);
                        TextView txtCategoria = mView.findViewById(R.id.dialogTxtCat);
                        txtCategoria.setText(ConfiguracionGeneralBL.GetUsabilidad(db));
                        spnCat = (Spinner) mView.findViewById(R.id.SpinnerdialogCat);
                        GradientDrawable shape =  new GradientDrawable();
                        shape.setCornerRadius( 10 );
                        Button btnCancelar =mView.findViewById(R.id.btnDialogCancelar);
                        shape.setColor(Color.parseColor("#3f51b5"));
                        mView.findViewById(R.id.btnDialogCancelar).setBackground(shape);
                        btnCancelar.setTextColor(Color.WHITE);
                        btnCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ResumenMensual.this, ResumenMensual.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        });
                        Button btnAceptar =mView.findViewById(R.id.btnDialogAceptar);
                        shape =  new GradientDrawable();
                        shape.setCornerRadius( 10 );
                        shape.setColor(Color.parseColor("#4CAF50"));
                        mView.findViewById(R.id.btnDialogAceptar).setBackground(shape);
                        btnAceptar.setTextColor(Color.WHITE);

                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final EditText edtCant = (EditText) mView.findViewById(R.id.dialogEdtCant);
                                final EditText edtMonto = (EditText) mView.findViewById(R.id.dialogEdtMonto);
                                final EditText edtDesc = (EditText) mView.findViewById(R.id.dialogEdtDesc);
                                Map<String, String> Ingresos = new HashMap<>();
                                Categorias cat = (Categorias) spnCat.getSelectedItem();
                                Ingresos.put("Categoria", MovimientosBL.GetCategoriaByName(db, cat.nombre));
                                Ingresos.put("Tipo", MovimientosBL.GetTipoByName(db, spnTipo.getSelectedItem().toString()));
                                Ingresos.put("Descripcion", edtDesc.getText().toString());
                                Ingresos.put("Monto", edtMonto.getText().toString());
                                Ingresos.put("Cant", edtCant.getText().toString());
                                Ingresos.put("IdMovimiento", String.valueOf(index));
                                MovimientosBL.UpdateMovimiento(db, Ingresos);

                                int indexCuotas = General.GetIndexCuota(listCuotas,listMovimientos.get(index).IdMovimiento);
                                int indexDeuda = General.GetIndexDeuda(listDeudas,listMovimientos.get(index).IdMovimiento);

                                if (indexCuotas >Constants.FinLista) {
                                    Movimiento mov = MovimientosBL.GetMovimientoById(db, listMovimientos.get(index).IdMovimiento);
                                    float Cuota = Float.valueOf(edtMonto.getText().toString()) / listCuotas.get(indexCuotas).CantCuotas;
                                    MovimientosBL.UpdateTotalCuota(db, listCuotas.get(indexCuotas).IdCuota, (int) Cuota);


                                } else if (indexDeuda >Constants.FinLista) {
                                    MovimientosBL.UpdateTotalDeuda(db, listDeudas.get(indexDeuda).IdDeuda, Float.valueOf(edtMonto.getText().toString()));
                                }
                                Intent intent = new Intent(ResumenMensual.this, ResumenMensual.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);

                                intent = new Intent(ResumenMensual.this, ResumenMensual.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        });




                        Button btnBorrar =mView.findViewById(R.id.btnDialogBorrar);
                        shape =  new GradientDrawable();
                        shape.setCornerRadius( 15 );
                        shape.setColor(Color.parseColor("#F44336"));
                        btnBorrar.setTextColor(Color.WHITE);
                        mView.findViewById(R.id.btnDialogBorrar).setBackground(shape);
                        btnBorrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int indexCuotas = General.GetIndexCuota(listCuotas,listMovimientos.get(index).IdMovimiento);
                                int indexDeuda = General.GetIndexDeuda(listDeudas,listMovimientos.get(index).IdMovimiento);
                                if (indexCuotas >Constants.FinLista) {
                                    Movimiento mov = MovimientosBL.GetMovimientoById(db, listMovimientos.get(index).IdMovimiento);
                                    MovimientosBL.DeleteCuota(db, listCuotas.get(indexCuotas).IdCuota);


                                } else if (indexDeuda > Constants.FinLista) {
                                    MovimientosBL.DeleteDeuda(db, listDeudas.get(indexDeuda).IdDeuda);

                                }
                                MovimientosBL.DeleteMovimientoById(db, index);
                                Intent intent = new Intent(ResumenMensual.this, ResumenMensual.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        });

                        CargarCategorias();
                        spnCat.setSelection(listMovimientos.get(index).IdCategoria);

                        final EditText edtDesc = (EditText) mView.findViewById(R.id.dialogEdtDesc);
                        edtDesc.setText(listMovimientos.get(index).Descripcion);

                        spnTipo = (Spinner) mView.findViewById(R.id.SpinnerdialogTipo);
                        CargarTipos();
                        spnTipo.setSelection(listMovimientos.get(index).IdTipo);

                        final EditText edtMonto = (EditText) mView.findViewById(R.id.dialogEdtMonto);

                        int indexCuotas = General.GetIndexCuota(listCuotas,listMovimientos.get(index).IdMovimiento);
                        int indexDeuda = General.GetIndexDeuda(listDeudas,listMovimientos.get(index).IdMovimiento);
                        if (indexCuotas > Constants.FinLista) {
                            Movimiento mov = MovimientosBL.GetMovimientoById(db, listMovimientos.get(index).IdMovimiento);
                            edtMonto.setText(String.valueOf(mov.Monto));

                        } else if (indexDeuda > Constants.FinLista) {
                            edtMonto.setText(String.valueOf(listDeudas.get(indexDeuda).MontoTotal));
                        } else {
                            edtMonto.setText(String.valueOf(listMovimientos.get(index).Monto));
                        }

                        final EditText edtCant = (EditText) mView.findViewById(R.id.dialogEdtCant);
                        edtCant.setText(String.valueOf(listMovimientos.get(index).Cant == -1 ? 0 : listMovimientos.get(index).Cant));


                        builder.setView(mView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

            }

        }



    }catch(Exception ex)
    {
        Toast.makeText(getApplicationContext(), "Hubo un error al cargar la pantalla", Toast.LENGTH_SHORT).show();

    }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeNavigate.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getRow(List<Movimiento> list){
        int count= MovimientosBL.CountCant(list);

        for (Movimiento item: list) {

            if(count > 0) {
                tableDynamic.addItemDeuda(new String[]{CategoriaBL.getCategoriaById(item.IdCategoria, db).nombre, item.Descripcion, CategoriaBL.getTipoById(item.IdTipo, db).Descripcion,
                        String.valueOf(item.Monto), String.valueOf(item.Cant > 0 ? item.Cant : 0), item.Fecha});
            }else{
                tableDynamic.addItemDeuda(new String[]{CategoriaBL.getCategoriaById(item.IdCategoria, db).nombre, item.Descripcion, CategoriaBL.getTipoById(item.IdTipo, db).Descripcion,
                        String.valueOf(item.Monto), item.Fecha});
            }
        }

    }
    private String[] GetAños(){
        int index = 0;
        arrayAños = new String[21];
        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        for (int i=año-10; i<=año;i++){
            arrayAños[index] = String.valueOf(i);
            index++;
        }
        for (int i=año+1; i<=año+10;i++){
            arrayAños[index] = String.valueOf(i);
            index++;
        }
        return arrayAños;

    }
    private int GetAñoIndex(String año){
        int index = 0;
        while(!arrayAños[index].equals(año)){
            index++;
        }
        return index;
    }
    public  void SendEmail(File file){
        try{
            String email;
            String subject;
            String message;
            String attachmentFile;
           // Uri URI = Uri.parse(file.getPath());
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(),file.getName() ));

            email = UsuariosBL.getUsuarioById(db,user.Id).Mail;
            subject ="Movimientos";
            message = "Se envia adjunto el back up del mes.";
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/excel");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
            if (uri != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent,""));


        }catch (Exception ex){
            file.delete();
        }
    }
   private void CargarTipos(){
       List<Tipo> listTipos = CategoriaBL.getTipos(db);
       List<String> listStringTipos = new ArrayList<String>();
       for(int i=0; i< listTipos.size();i++){
           listStringTipos.add(listTipos.get(i).Descripcion);
       }

       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listStringTipos);
       // adapter.setDropDownViewResource(R.layout.activity_ingresar_movimiento);
       spnTipo.setAdapter(adapter);
   }

    private void CargarCategorias(){
        try {
            listCategoria = CategoriaBL.getAllCategorias(db);
            List<String> listCat = new ArrayList<String>();
            for(int i= 0; i < listCategoria.size();i++){
                listCat.add(listCategoria.get(i).nombre);
            }
            SpinnerAdapter adapter = new SpinnerAdapter(this,listCategoria);
            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listCat);
            // adapter.setDropDownViewResource(R.layout.activity_ingresar_movimiento);
            spnCat.setAdapter(adapter);
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeNavigate.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
               // alertBuilder.setTitle(getString(R.string.permission_necessary));
               // alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_wrote_event);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ResumenMensual.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(ResumenMensual.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            openActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    openActivity();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openActivity() {
        //add your further process after giving permission or to download images from remote server.
    }
}
