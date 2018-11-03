package com.android.holamundo.app1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.MovimientosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.Entities.Tipo;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresarMovimiento extends AppCompatActivity  {
    Spinner catSpinner, tipoSpinner;
    EditText edtMonto, edtCant,edtDescripcion, edtDeuda, edtCuotas;
    CheckBox checkedAvanzado;
    SQLiteDatabase db;
    List<Categorias> listCategorias;
    ImageView btnOk;
    GridView gridMes;
    Usuario user=null;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_movimiento);
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
        user = Usuario.getUsuario();
        CargarControles();
        CargarCategorias();
        CargarTipos();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeNavigate.class);
        startActivity(intent);
        finish();
    }
    private void CargarControles(){
        final Categorias categoriaSelected = new Categorias();
        catSpinner =  findViewById(R.id.catSpinner);

        tipoSpinner = findViewById(R.id.tipoSpinner);
        edtMonto = findViewById(R.id.edtMonto);
        edtCant = findViewById(R.id.edtCantidad);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        checkedAvanzado = findViewById(R.id.chkAvanzado);
        edtDeuda = findViewById(R.id.edtDeuda);
        edtCuotas = findViewById(R.id.edtCuotas);

        edtCant.setVisibility(View.INVISIBLE);
        edtDeuda.setVisibility(View.INVISIBLE);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result="Debe escribir descripcion y monto!";
                if(!edtDescripcion.getText().toString().isEmpty() &&  !edtMonto.getText().toString().isEmpty()) {
                    Map<String, String> Ingresos = new HashMap<>();
                    Categorias cat = (Categorias)catSpinner.getSelectedItem();
                    Ingresos.put("Categoria", cat.nombre);
                    Ingresos.put("Tipo", tipoSpinner.getSelectedItem().toString());
                    Ingresos.put("Descripcion", edtDescripcion.getText().toString());
                    Ingresos.put("Monto", edtMonto.getText().toString());
                    Ingresos.put("IdUsuario", String.valueOf(user.Id));
                    Ingresos.put("Checked", String.valueOf(checkedAvanzado.isChecked()));
                    Ingresos.put("Deuda", edtDeuda.getText().toString());
                    Ingresos.put("Cant", edtCant.getText().toString());
                    Ingresos.put("Cuotas", edtCuotas.getText().toString());


                    result = MovimientosBL.IngresarMovimiento(db, Ingresos);
                    edtCuotas.setText("");
                    edtCant.setText("");
                    edtDescripcion.setText("");
                    edtDeuda.setText("");
                    edtMonto.setText("");
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
        checkedAvanzado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!checkedAvanzado.isChecked()){
                    edtCant.setVisibility(View.INVISIBLE);
                    edtDeuda.setVisibility(View.INVISIBLE);
                }else{
                    edtCant.setVisibility(View.VISIBLE);
                    edtDeuda.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    private void CargarTipos(){
        List<Tipo> listTipos = CategoriaBL.getTipos(db);
        List<String> listStringTipos = new ArrayList<String>();
        for(int i=0; i< listTipos.size();i++){
            listStringTipos.add(listTipos.get(i).Descripcion);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listStringTipos);
       // adapter.setDropDownViewResource(R.layout.activity_ingresar_movimiento);
         tipoSpinner.setAdapter(adapter);
    }

    private void CargarCategorias(){
        try {
            listCategorias = CategoriaBL.getAllCategorias(db);
            List<String> listCat = new ArrayList<String>();
            for(int i= 0; i < listCategorias.size();i++){
                listCat.add(listCategorias.get(i).nombre);
            }
            SpinnerAdapter adapter = new SpinnerAdapter(this,listCategorias);
           // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,listCat);
           // adapter.setDropDownViewResource(R.layout.activity_ingresar_movimiento);
            catSpinner.setAdapter(adapter);
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeNavigate.class);
            startActivity(intent);
            finish();
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
