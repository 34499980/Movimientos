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
import com.android.holamundo.app1.Entities.Cuota;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.TableDynamic;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cuotas extends AppCompatActivity {
    SQLiteDatabase db;
    ActionBarDrawerToggle toggle;
    TableLayout tblMes;
    TableDynamic tableDynamic;

    String[] header;
    String[] rows;
    ArrayList<String[]> tableRows = new ArrayList<>();
    List<Cuota> listCuotas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cuotas);
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
        header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db),"Descripcion","Monto","Cuotas","Fecha"};
        CargarControles();
    }
    private void CargarControles(){
        tblMes = findViewById(R.id.tblCuotas);
        tableDynamic = new TableDynamic(tblMes,getApplicationContext());
        int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
        int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
        tableDynamic.setSize(headerSize,tableSize);
        tableDynamic.addHeader(header);
        listCuotas = MovimientosBL.GetAllCuotas(db);
        tableDynamic.setRows(listCuotas.size());
        getRow(listCuotas);
        tableDynamic.backgroundHeader(Color.parseColor("#0091EA"));
        tableDynamic.backgroundData(Color.parseColor("#BDBDBD"),Color.parseColor("#757575"));

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
    private void getRow(List<Cuota> list){
        for (Cuota item: list) {
            Movimiento movimiento = MovimientosBL.GetMovimientoById(db,item.IdMovimiento);
            tableDynamic.addItem(new String[]{CategoriaBL.getCategoriaById(movimiento.IdCategoria,db).nombre,movimiento.Descripcion,String.valueOf(item.Monto)
            ,String.valueOf(item.CuotaActual+"/"+item.CantCuotas),String.valueOf(movimiento.Fecha)});
        }


    }


}
