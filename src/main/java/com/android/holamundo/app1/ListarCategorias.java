package com.android.holamundo.app1;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.CategoriaBL;
import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Categoria;
import com.android.holamundo.app1.Entities.Categorias;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Helper.Components;
import com.android.holamundo.app1.Helper.ImageAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarCategorias extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener  {
    Button btnListCat;
    FloatingActionButton aCat;
    SQLiteDatabase db;
    GridView grdCategorias;
    ActionBarDrawerToggle toggle;
    int[]  icons = {R.drawable.cat_0,R.drawable.cat_1,R.drawable.cat_2,R.drawable.cat_3,R.drawable.cat_4,R.drawable.cat_5,R.drawable.cat_6,R.drawable.cat_7,R.drawable.cat_8,R.drawable.cat_9,R.drawable.cat_10,R.drawable.cat_11
            ,R.drawable.cat_12,R.drawable.cat_13,R.drawable.cat_14,R.drawable.cat_15,R.drawable.cat_16,R.drawable.cat_17};
    int[] icons2;
    public static EditText ed;
    ImageView imageView;
    public static String nombreViejo;
    List<Categorias> listCategorias;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categorias);
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
                }switch (item.getItemId()){
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
        aCat = (FloatingActionButton) findViewById(R.id.ACat);
        btnListCat = (Button) findViewById(R.id.btnVolverListCat);
        btnListCat.setOnClickListener(this);
        aCat.setOnClickListener(this);
        imageView = findViewById(R.id.imageDialog);
        btnListCat.setVisibility(View.INVISIBLE);
        listCategorias = CategoriaBL.getAllCategorias(db);
        int i = 0;
        icons2 = new int[listCategorias.size()];
        for (Categorias item: listCategorias) {
            icons2[i] = Integer.valueOf(item.Imagen);
            i++;
        }
        grdCategorias = (GridView) findViewById(R.id.grdCategorias);
        ImageAdapter adapter = new ImageAdapter(this, icons2, listCategorias);
        grdCategorias.setAdapter(adapter);
        setEditClick();
        setDeleteClick();

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.btnVolverListCat:
                intent = new Intent(this, HomeUser.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ACat:
                intent = new Intent(this, NuevaCategoria.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeNavigate.class);
        startActivity(intent);
        finish();
    }
    public void setEditClick(){
        grdCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nombreViejo = listCategorias.get(i).nombre;
                final EditText edit = Components.CreateEditText(ListarCategorias.this,nombreViejo);

                int imagen = (int)l;
                if(imageView != null) {
                    imageView.setImageResource(imagen);
                    onClickImagen(imageView);
                }
                // final ImageView imageView = Components.CreateImageView(ListarCategorias.this,i);
                ed = edit;
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("ImageView",imageView);
                map.put("EditText",edit);
                AlertDialog.Builder alerDialog = Components.CreateDialog(ListarCategorias.this,map,"Editar "+ ConfiguracionGeneralBL.GetUsabilidad(db));
                final Categoria categoria = Categoria.getCategoria();
                categoria.Nombre = nombreViejo;
                categoria.Imagen = imagen;

                alerDialog.setPositiveButton("Editar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                               if(!ed.getText().toString().trim().isEmpty()) {
                                   CategoriaBL.ActualizarPorNombre(ed.getText().toString(), nombreViejo, categoria.Imagen, db);
                               }else{
                                   CategoriaBL.ActualizarImagen(nombreViejo,categoria.Imagen, db);
                               }
                                Intent intent = new Intent(ListarCategorias.this, ListarCategorias.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(0, 0);


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
    }
    public void setDeleteClick()
    {
        grdCategorias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                nombreViejo = listCategorias.get(pos).nombre;
                AlertDialog.Builder alerDialog = Components.CreateDialog(ListarCategorias.this,null,"Borrar "+ConfiguracionGeneralBL.GetUsabilidad(db));

                alerDialog.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                try {
                                    CategoriaBL.BorrarCategoria(nombreViejo, db);
                                    Intent intent = new Intent(ListarCategorias.this, ListarCategorias.class);
                                    finish();
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

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


                return true;
            }
        });
    }
    public void onClickImagen(final ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //GridView gridEditCat = (GridView) findViewById(R.id.GridEditCat);

                GridView gridEditCat = new GridView(ListarCategorias.this);
                gridEditCat.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                gridEditCat.setNumColumns(GridView.AUTO_FIT);
                gridEditCat.setGravity(Gravity.CENTER);
                ImageAdapter adapter = new ImageAdapter(ListarCategorias.this, icons,null);
                gridEditCat.setAdapter(adapter);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("GridView",gridEditCat);
               final AlertDialog.Builder alerDialog = Components.CreateDialog(ListarCategorias.this,map,"");
               dialog = alerDialog.show();

                gridEditCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Categoria categoria = Categoria.getCategoria();
                        categoria.Imagen = l;
                        imageView.setImageResource((int)l);
                        dialog.dismiss();



                    }
                });

               // alerDialog.show();
            }
        });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = null;

        switch (item.getItemId()){
            case R.id.btnLogOut:
                Usuario.Destroy();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnCrearCat:
                intent = new Intent(this, ListarCategorias.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnIngresar:
                intent = new Intent(this, IngresarMovimiento.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnMes:
                break;
            //  case R.id.btnPorCategoria:
            //      break;
            case R.id.btnAnuales:
                break;
            case R.id.btnEditUser:
                intent = new Intent(this, DatosPersonales.class);
                startActivity(intent);
                finish();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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







