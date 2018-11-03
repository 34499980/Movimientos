package com.android.holamundo.app1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.TableDynamic;
import com.android.holamundo.app1.Entities.Totales;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.List;
import java.util.Map;

public class ResumenAnual extends AppCompatActivity {
    SQLiteDatabase db;
    ActionBarDrawerToggle toggle;
    List<Totales> listTotales;
    TableLayout tblResumen;

    TableDynamic tableDynamic;
    Map<String,List<Movimiento>> listasMovimientos;
    String[] header;
    String[] rows;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_anual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!= null){
            toolbar.setTitle("");
            toolbar.setBackgroundColor(Color.parseColor(Constants.colorActionBar));
        }
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
        header = new String[]{"Ingresos","Compra","Venta","Saldo","Fecha"};;
       CargarControles();
    }
    private void CargarControles(){
        tblResumen = findViewById(R.id.tblResumenAnual);
        if (tableDynamic == null)
            tableDynamic = new TableDynamic(tblResumen, getApplicationContext());
        int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
        int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
        tableDynamic.setSize(headerSize,tableSize);
        tableDynamic.addHeader(header);
        listasMovimientos = MovimientosBL.GetAllMvimientosSorted(db);
        listTotales = MovimientosBL.SortTotalesByMes(db,listasMovimientos);
        tableDynamic.setRows(listTotales.size());
        getRow(listTotales);
        tableDynamic.backgroundHeader(Color.parseColor("#0091EA"));
        tableDynamic.backgroundData(Color.parseColor("#BDBDBD"), Color.parseColor("#757575"));
    }
    private void getRow(List<Totales> list){
        for (Totales item: list) {


            tableDynamic.addItemDeuda(new String[]{String.valueOf(item.Ingreso),String.valueOf(item.Compra),String.valueOf(item.Venta),String.valueOf(item.Ingreso - item.Compra + item.Venta),String.valueOf(item.Mes+1+"/"+item.Año)});
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

}
