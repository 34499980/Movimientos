package com.android.holamundo.app1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Cuota;
import com.android.holamundo.app1.Entities.Deuda;
import com.android.holamundo.app1.Entities.Movimiento;
import com.android.holamundo.app1.Entities.TableDynamic;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.Components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Deudas extends AppCompatActivity {
    SQLiteDatabase db;
    ActionBarDrawerToggle toggle;
    TableLayout tblDeuda;
    TableDynamic tableDynamic;

    String[] header;
    String[] rows;
    ArrayList<String[]> tableRows = new ArrayList<>();
    List<Deuda> listDeudas = new ArrayList<>();
    static Context context;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context= this;

        setContentView(R.layout.activity_deudas);
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
        header = new String[]{ConfiguracionGeneralBL.GetUsabilidad(db),"Descripcion","Tipo","Deuda","Fecha"};
        CargarControles();
    }
    private void CargarControles(){
        tblDeuda = findViewById(R.id.tblDeudas);
        tableDynamic = new TableDynamic(tblDeuda,getApplicationContext());
        int headerSize =  ConfiguracionGeneralBL.GetTableHeaderSize(db);
        int tableSize =  ConfiguracionGeneralBL.GetTableSize(db);
        tableDynamic.setSize(headerSize,tableSize);
        tableDynamic.addHeader(header);
        listDeudas = MovimientosBL.GetAllDeudas(db);
        tableDynamic.setRows(listDeudas.size());
        getRow(listDeudas);
        tableDynamic.backgroundHeader(Color.parseColor("#0091EA"));
        tableDynamic.backgroundData(Color.parseColor("#BDBDBD"),Color.parseColor("#757575"));
        //tblDeuda.setOnClickListener(new View.OnClickListener() {

        for (int i=1; i<tblDeuda.getChildCount();i++) {

                final int index = i-1;
                View child = tblDeuda.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Movimiento movimiento = MovimientosBL.GetMovimientoById(db, listDeudas.get(index).IdMovimiento);
                              final TextView txtPago = Components.CreateText(Deudas.this,"Pago: ");
                            txtPago.setTextSize(20);
                              final TextView txtTotal = Components.CreateText(Deudas.this,"Total: ");
                            txtTotal.setTextSize(20);

                            //  final TextView txtTipo = Components.CreateText(Deudas.this,CategoriaBL.getTipoById(movimiento.IdTipo,db).Descripcion);
                            final TextView txtMonto = Components.CreateText(Deudas.this, String.valueOf(listDeudas.get(index).MontoTotal));
                            txtMonto.setTextSize(20);
                            final EditText edtMonto = Components.CreateEditText(Deudas.this, String.valueOf(listDeudas.get(index).Monto));
                            edtMonto.setInputType(InputType.TYPE_CLASS_NUMBER);
                            Map<String, Object> map = new LinkedHashMap<String, Object>();
                            List<TextView> listText = new ArrayList<>();
                            // listText.add(txtCat);
                           // listText.add(txtPago);
                            listText.add(txtTotal);
                            listText.add(txtMonto);


                            map.put("TextView",txtPago);
                            map.put("EditText", edtMonto);
                            map.put("listText", listText);
                            AlertDialog.Builder alerDialog = Components.CreateDialog(Deudas.this, map, "Saldo de deuda");
                            alerDialog.setPositiveButton("Editar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            float pago = edtMonto.getText().toString().isEmpty()? Float.valueOf(edtMonto.getHint().toString()) : Float.valueOf(edtMonto.getText().toString());
                                            float total = listDeudas.get(index).MontoTotal;
                                            if (!edtMonto.getText().toString().trim().isEmpty()) {

                                                if (pago == total) {
                                                    Map<String, String> map = new HashMap<String, String>();
                                                    map.put("IdMovimiento", String.valueOf(movimiento.IdMovimiento));
                                                    map.put("Categoria", String.valueOf(movimiento.IdCategoria));
                                                    map.put("Tipo", String.valueOf(movimiento.IdTipo));
                                                    map.put("Descripcion", movimiento.Descripcion);
                                                    map.put("Monto", String.valueOf(pago));
                                                    map.put("IdUsuario", String.valueOf(movimiento.IdUsuario));
                                                    map.put("Checked", String.valueOf(false));
                                                    map.put("Deuda", "");
                                                    map.put("Cant", "");
                                                    map.put("Cuotas", "");
                                                    // MovimientosBL.IngresarMovimiento(db,map);
                                                    MovimientosBL.UpdateMovimiento(db, map);
                                                    MovimientosBL.DeleteDeuda(db, listDeudas.get(index).IdDeuda);
                                                } else {
                                                    MovimientosBL.UpdateDeuda(db, listDeudas.get(index).IdDeuda, pago);
                                                }
                                                Intent intent = new Intent(Deudas.this, Deudas.class);
                                                finish();
                                                startActivity(intent);
                                                overridePendingTransition(0, 0);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                            // Setting Negative "NO" Button
                            alerDialog.setNegativeButton("Cancelar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to execute after dialog

                                            dialog.cancel();
                                        }
                                    });

                            alerDialog.show();
                        }
                    });
                }

        }

      //  });

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
    private void getRow(List<Deuda> list){
        for (Deuda item: list) {
            //Button button = Components.CreateButton(this,-1,"Saldar", Color.WHITE,Color.parseColor("#388E3C"), Gravity.CENTER,100,50);

            Movimiento movimiento = MovimientosBL.GetMovimientoById(db,item.IdMovimiento);
            float total = movimiento.Monto+item.Monto;
            tableDynamic.addItemDeuda(new String[]{CategoriaBL.getCategoriaById(movimiento.IdCategoria,db).nombre,movimiento.Descripcion,CategoriaBL.getTipoById(movimiento.IdTipo,db).Descripcion,
                    String.valueOf(item.Monto+"/"+item.MontoTotal),movimiento.Fecha});
        }


    }
    public static Context getContext(){

        return context;
    }


}
