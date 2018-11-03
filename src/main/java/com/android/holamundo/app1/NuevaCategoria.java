package com.android.holamundo.app1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.Components;
import com.android.holamundo.app1.Helper.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NuevaCategoria extends AppCompatActivity implements View.OnClickListener{
    GridView gridCat;
    ActionBarDrawerToggle toggle;
    int[]  icons = {R.drawable.cat_0,R.drawable.cat_1,R.drawable.cat_2,R.drawable.cat_3,R.drawable.cat_4,R.drawable.cat_5,R.drawable.cat_6,R.drawable.cat_7,R.drawable.cat_8,R.drawable.cat_9,R.drawable.cat_10,R.drawable.cat_11
    ,R.drawable.cat_12,R.drawable.cat_13,R.drawable.cat_14,R.drawable.cat_15,R.drawable.cat_16,R.drawable.cat_17};
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nueva_categoria);
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
            gridCat = (GridView) findViewById(R.id.GridCat);

            ImageAdapter adapter = new ImageAdapter(this, icons,null);
            gridCat.setAdapter(adapter);
            gridCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int id, long i) {
                    final long imagen = i;
                  final EditText edit = Components.CreateEditText(NuevaCategoria.this,"Ingrese "+ ConfiguracionGeneralBL.GetUsabilidad(db));

                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("EditText",edit);

                  AlertDialog.Builder alerDialog = Components.CreateDialog(NuevaCategoria.this,map,"Crear "+ ConfiguracionGeneralBL.GetUsabilidad(db));


                    alerDialog.setPositiveButton("Crear",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    try {
                                       if(CategoriaBL.IngresarCategoria(edit.getText().toString(),imagen, db)){

                                           Toast.makeText(getApplicationContext(),ConfiguracionGeneralBL.GetUsabilidad(db)+" " +edit.getText()+ " creada." , Toast.LENGTH_LONG).show();
                                       }else{
                                           Toast.makeText(getApplicationContext(), ConfiguracionGeneralBL.GetUsabilidad(db)+ " " +edit.getText().toString()+ " ya existe.", Toast.LENGTH_LONG).show();
                                       }

                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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

                    // closed

                    // Showing Alert Message
                    alerDialog.show();
                }


            });
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onClick(View view) {


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListarCategorias.class);
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
