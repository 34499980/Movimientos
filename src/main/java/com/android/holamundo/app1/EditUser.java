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
import android.widget.Button;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;

public class EditUser extends AppCompatActivity implements View.OnClickListener {
    Button btnDelete,btnSettings,btnVolver_Edit;
    Usuario user = null;
    SQLiteDatabase db;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
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



    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btnDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete");
                builder.setMessage("¿Desea borrar el usuario?");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UsuariosBL.DeleteUserByNombre(user.Nombre.toLowerCase(),db);
                        Usuario.Destroy();
                        Toast.makeText(getApplicationContext(), "Usuario eliminado", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

                break;
            case R.id.btnVolver_Edit:
                intent = new Intent(this, HomeNavigate.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnSettings:
                intent = new Intent(this, DatosPersonales.class);
                startActivity(intent);
                finish();
                break;
        }

    }
    private void CargarControles()
    {
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);
        btnVolver_Edit = (Button) findViewById(R.id.btnVolver_Edit);
        btnVolver_Edit.setOnClickListener(this);
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
