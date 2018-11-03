package com.android.holamundo.app1;

import android.content.ClipData;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.TableDynamic;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.General;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomeNavigate extends AppCompatActivity  {
    SQLiteDatabase db;
    TextView txtVenta,txtCompra,txtIngreso,txtSaldo;
    float totalesCompras,totalesVenta, totalIngreso;
    TableLayout tblMes, tblTotales, tblByCat;
    TableDynamic tableDynamic, tableTotales, tableByCat;
    String[] headerTotales={"Ingreso","Compra","Venta","Saldo"};
    String[] header;
    String[] rows;
    ArrayList<String[]> tableRows = new ArrayList<>();
    ActionBarDrawerToggle toggle;
    Map<String,List<Movimiento>> listMovimientos;
    List<Movimiento> listSort;
    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_navigate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor(Constants.colorActionBar));
        setSupportActionBar(toolbar);
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






        try {

            listMovimientos = MovimientosBL.GetByMes(db);
            totalIngreso = MovimientosBL.GetTotales(listMovimientos.get("Ingresos"));
            totalesVenta = MovimientosBL.GetTotales(listMovimientos.get("Venta"));
            totalesCompras= MovimientosBL.GetTotales(listMovimientos.get("Compra"));
            MovimientosBL.ActualizarCuotas(db);

            CargarControles();
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "Problemas al loguearse", Toast.LENGTH_SHORT).show();

        }

    }
    private void CargarControles(){
       // txtCompra = findViewById(R.id.Compra);
     //   txtVenta = findViewById(R.id.Venta);
      //  txtIngreso = findViewById(R.id.Ingreso);

       // MenuItem categoria = findViewById(R.id.btnCrearCat);
      //  categoria.setTitle(ConfiguracionGeneralBL.GetUsabilidad(db));
        tblTotales = findViewById(R.id.tblTotales);
        tableTotales = new TableDynamic(tblTotales,getApplicationContext());
        int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
        int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
        tableTotales.setSize(headerSize,tableSize);
        tableTotales.addHeader(headerTotales);
        float[] list = new float[]{totalIngreso,totalesCompras,totalesVenta,totalIngreso-totalesCompras+totalesVenta};
        tableTotales.setRows(1);
        tableTotales.addData(getRow(list));
        tableTotales.backgroundHeader(Color.parseColor("#43A047"));
        tableTotales.backgroundData(Color.LTGRAY,Color.GRAY);
     //   txtCompra.setText("Compra: "+String.valueOf(totalesCompras));
      //  txtVenta.setText("Venta: "+String.valueOf(totalesVenta));
       // txtIngreso.setText("Ingreso: "+String.valueOf(totalIngreso));
       // txtSaldo.setText("Saldo: "+String.valueOf(totalIngreso-totalesCompras+totalesVenta));
        tblMes = findViewById(R.id.tblMes);
        tableDynamic = new TableDynamic(tblMes,getApplicationContext());
        tableDynamic.setSize(headerSize,tableSize);
        if(MovimientosBL.CountCant(listMovimientos.get("Venta"))+MovimientosBL.CountCant(listMovimientos.get("Compra"))+MovimientosBL.CountCant(listMovimientos.get("Ingresos"))>0) {
            header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db), "Monto", "Tipo", "Cant"};
        }else{
            header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db), "Monto", "Tipo"};
        }
        tableDynamic.addHeader(header);
        int rowsDetalles = listMovimientos.get("Venta").size()+listMovimientos.get("Compra").size()+listMovimientos.get("Ingresos").size();
        tableDynamic.setRows(rowsDetalles==0?1:rowsDetalles);
        //Acumulor por la misma categoria
        listSort = General.ActumByCategoria(db,listMovimientos.get("Ingresos"));
        //Ordeno de mayor a menor
        listSort = General.SortMovimientos(listSort);
        //Ingreso las rows
        getRow(listSort);
        //Acumulor por la misma categoria
        listSort = General.ActumByCategoria(db,listMovimientos.get("Compra"));
        //Ordeno de mayor a menor
        listSort = General.SortMovimientos(listSort);
        //Ingreso las rows
        getRow(listSort);
        //Acumulor por la misma categoria
        listSort = General.ActumByCategoria(db,listMovimientos.get("Venta"));
        //Ordeno de mayor a menor
        listSort = General.SortMovimientos(listSort);
        //Ingreso las rows
        getRow(listSort);

        tableDynamic.backgroundHeader(Color.parseColor("#0091EA"));
        tableDynamic.backgroundData(Color.parseColor("#BDBDBD"),Color.parseColor("#757575"));


        for (int i = 1; i < tblMes.getChildCount(); i++) {

            final int index = i - 1;
            View child = tblMes.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View child = tblMes.getChildAt(index+1);
                        TableRow row = (TableRow) child;
                        TextView textView =(TextView)row.getChildAt(0);
                        String cat = textView.getText().toString();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeNavigate.this);
                        mView = getLayoutInflater().inflate(R.layout.dialogcategoria, null);
                        tblByCat = mView.findViewById(R.id.tblResumenByCategoria);
                        tableByCat = new TableDynamic(tblByCat,getApplicationContext());
                        int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
                        int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
                        tableByCat.setSize(headerSize,tableSize);
                        List<Movimiento> listCompra = listMovimientos.get("Compra");
                        List<Movimiento> listVenta = listMovimientos.get("Venta");
                        List<Movimiento> listIngresos = listMovimientos.get("Ingresos");
                        List<Movimiento> listCompraByCat = MovimientosBL.GetMovimientosByCategoriia(listCompra,CategoriaBL.getCategoriaByName(cat,db).Id);
                        List<Movimiento> listVentaByCat = MovimientosBL.GetMovimientosByCategoriia(listVenta,CategoriaBL.getCategoriaByName(cat,db).Id);
                        List<Movimiento> listIngresosByCat = MovimientosBL.GetMovimientosByCategoriia(listIngresos,CategoriaBL.getCategoriaByName(cat,db).Id);
                        String[] headerDialog ;
                        if(MovimientosBL.CountCant(listCompraByCat)+MovimientosBL.CountCant(listVentaByCat)+MovimientosBL.CountCant(listIngresosByCat)>0)
                        {
                            headerDialog  = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db),"Descripcion","Monto","Tipo","Cant"};

                        }else{
                            headerDialog  = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db),"Descripcion","Monto","Tipo"};

                        }
                        tableByCat.addHeader(headerDialog);


                        int rowsDetalles = listCompraByCat.size()+listVentaByCat.size()+listIngresosByCat.size();
                        tableByCat.setRows(rowsDetalles==0?1:rowsDetalles);


                        getRowDialog(listIngresosByCat);
                        getRowDialog(listCompraByCat);
                        getRowDialog(listVentaByCat);
                        tableByCat.backgroundHeader(Color.parseColor("#0091EA"));
                        tableByCat.backgroundData(Color.LTGRAY,Color.GRAY);
                        Button btnAceptar =mView.findViewById(R.id.btnDialogCatAceptar);
                        GradientDrawable  shape =  new GradientDrawable();
                        shape.setCornerRadius( 10 );
                        shape.setColor(Color.parseColor("#4CAF50"));
                        mView.findViewById(R.id.btnDialogCatAceptar).setBackground(shape);
                        btnAceptar.setTextColor(Color.WHITE);
                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeNavigate.this, HomeNavigate.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }
                        });
                        builder.setView(mView);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });
            }
        }


    }
    private void getRowDialog(List<Movimiento> list){
        int categoriaAnt;
        int i = 0;
        int cantTotal=0;

        while(i < list.size()){

            if(MovimientosBL.CountCant(list)>0) {
                tableByCat.addItem(new String[]{CategoriaBL.getCategoriaById(list.get(i).IdCategoria, db).nombre, list.get(i).Descripcion, String.valueOf(list.get(i).Monto), CategoriaBL.getTipoById(list.get(i).IdTipo, db).Descripcion, String.valueOf(list.get(i).Cant < 0 ? 0 : list.get(i).Cant)});
            }else{
                tableByCat.addItem(new String[]{CategoriaBL.getCategoriaById(list.get(i).IdCategoria, db).nombre, list.get(i).Descripcion, String.valueOf(list.get(i).Monto), CategoriaBL.getTipoById(list.get(i).IdTipo, db).Descripcion});
            }
            i++;

        }


    }
    private void getRow(List<Movimiento> list){
        int categoriaAnt;
        int i = 0;
        int cantTotal=0;

        while(i < list.size()){
            cantTotal = MovimientosBL.CountCant(list);
            if(cantTotal > 0) {
                tableDynamic.addItem(new String[]{CategoriaBL.getCategoriaById(list.get(i).IdCategoria, db).nombre, String.valueOf(list.get(i).Monto), CategoriaBL.getTipoById(list.get(i).IdTipo, db).Descripcion, String.valueOf(list.get(i).Cant < 0 ? 0 : list.get(i).Cant)});
            }else{
                tableDynamic.addItem(new String[]{CategoriaBL.getCategoriaById(list.get(i).IdCategoria, db).nombre, String.valueOf(list.get(i).Monto), CategoriaBL.getTipoById(list.get(i).IdTipo, db).Descripcion});
            }
            i++;

        }


    }
    private String[] getRow(float[] list){
       // for (float item :list) {
          return  new String[]{String.valueOf(list[0]),String.valueOf(list[1]),String.valueOf(list[2]),String.valueOf(list[3])};
       // }
        //  rows.add(new String[]{"Sueldo","Ingreso","22800","0"});
       // return rows;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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



}
