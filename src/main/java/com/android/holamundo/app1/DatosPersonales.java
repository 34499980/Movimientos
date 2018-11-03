package com.android.holamundo.app1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCas;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.Entities.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DatosPersonales extends AppCompatActivity implements View.OnClickListener{
    Button btnAtras;
    Button btnFinalizar;
    EditText edtMail;
    DatePicker dtpFecha;
    SQLiteDatabase db;
    Usuario user = null;
    ActionBarDrawerToggle toggle;
    Switch switchCliente;
    GradientDrawable shape;
    TextView txtHeader ;
    TextView txtTable ;
    Button btnMenosHeader;
    Button btnMasHeader;
    Button btnMenosTable;
    Button btnMasTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(Constants.colorActionBar));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Button btnBorrar = findViewById(R.id.btnDelete);
        shape =  new GradientDrawable();
        shape.setCornerRadius( 15 );
        shape.setColor(Color.parseColor("#F44336"));
        btnBorrar.setTextColor(Color.WHITE);
        btnBorrar.setOnClickListener(this);
       // mView = getLayoutInflater().inflate(R.layout.activity_datos_personales, null);
         findViewById(R.id.btnDelete).setBackground(shape);
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

        try {
            db = openOrCreateDatabase("Gastos", MODE_PRIVATE, null);
            btnAtras = (Button) findViewById(R.id.btnCancelar);
            btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
            edtMail = (EditText) findViewById(R.id.edtMail);
            dtpFecha = (DatePicker) findViewById(R.id.dtpFecha);
            switchCliente = findViewById(R.id.switchCliente);
            String usabilidad = ConfiguracionGeneralBL.GetUsabilidad(db);
            shape =  new GradientDrawable();
            shape.setCornerRadius( 100 );
            shape.setColor(Color.parseColor("#ECEFF1"));


             txtHeader = findViewById(R.id.SizeHeader);
            txtHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            findViewById(R.id.SizeHeader).setBackground(shape);
            txtHeader.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            btnMasHeader = findViewById(R.id.btnMasHeader);

            findViewById(R.id.btnMasHeader).setBackground(shape);
            btnMasHeader.setOnClickListener(this);
             txtTable = findViewById(R.id.SizeTable);

            findViewById(R.id.SizeTable).setBackground(shape);
            txtTable.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
             btnMenosHeader = findViewById(R.id.btnMenosHeader);
            btnMenosHeader.setOnClickListener(this);
            findViewById(R.id.btnMenosHeader).setBackground(shape);



             btnMenosTable = findViewById(R.id.btnMenosTable);
            btnMenosTable.setOnClickListener(this);
            findViewById(R.id.btnMenosTable).setBackground(shape);

             btnMasTable = findViewById(R.id.btnMasTable);
            btnMasTable.setOnClickListener(this);
            findViewById(R.id.btnMasTable).setBackground(shape);

            txtHeader.setText(String.valueOf(ConfiguracionGeneralBL.GetTableHeaderSize(db)));
            txtTable.setText(String.valueOf(ConfiguracionGeneralBL.GetTableSize(db)));
            if(!usabilidad.equals("Categorias")){
                switchCliente.setChecked(true);
            }else{
                switchCliente.setChecked(false);
            }
            switchCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(!isChecked) {
                        ConfiguracionGeneralBL.SetUsabilidad(db,"Categorias");
                    } else {
                        ConfiguracionGeneralBL.SetUsabilidad(db,"Clientes");
                    }
                }
            });
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 10 );
            shape.setColor(Color.parseColor("#4CAF50"));
            btnFinalizar.setOnClickListener(this);
            btnFinalizar.setTextColor(Color.WHITE);
            findViewById(R.id.btnFinalizar).setBackground(shape);

            shape =  new GradientDrawable();
            shape.setCornerRadius( 10 );
            btnAtras.setTextColor(Color.WHITE);
            shape.setColor(Color.parseColor("#3f51b5"));
            findViewById(R.id.btnCancelar).setBackground(shape);
            btnAtras.setOnClickListener(this);

            user = Usuario.getUsuario();
            if (user.FechaNacimiento == null) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                dtpFecha.updateDate(year, month, Calendar.DAY_OF_MONTH);
            } else {
                int day = Integer.valueOf(user.FechaNacimiento.substring(user.FechaNacimiento.lastIndexOf("-")+1, user.FechaNacimiento.length()));
                int month = Integer.valueOf(user.FechaNacimiento.substring(user.FechaNacimiento.indexOf("-")+1, user.FechaNacimiento.lastIndexOf("-")));
                int year = Integer.valueOf(user.FechaNacimiento.substring(0, user.FechaNacimiento.indexOf("-")));
                dtpFecha.updateDate(year, month, day);
            }
            if (user.Mail != null) {
                edtMail.setText(user.Mail);
            }
        }catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        Usuario user = Usuario.getUsuario();
        switch (view.getId()) {
            case R.id.btnCancelar:

                if(user.Nombre == null) {
                    intent = new Intent(this, MainActivity.class);
                }else
                {
                    intent = new Intent(this, HomeNavigate.class);
                }
                startActivity(intent);
                finish();
                break;

            case R.id.btnFinalizar:
                if(!(edtMail.getText().toString().trim().isEmpty()))
                {
                    int index = edtMail.getText().toString().indexOf("@");
                    if(index < 1 || index == edtMail.getText().length()-1) {
                        Toast.makeText(getApplicationContext(), "formato de mail incorrecto", Toast.LENGTH_LONG).show();
                    }else {
                        user.Mail = edtMail.getText().toString().trim();
                        user.FechaNacimiento =  dtpFecha.getYear() +"-"+ dtpFecha.getMonth() +"-"+ dtpFecha.getDayOfMonth();
                        UsuariosBL.IngresarDatosPersonales(db);
                        intent = new Intent(this, HomeNavigate.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Ingrese los parametros correspondientes", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete");
                builder.setMessage("¿Desea borrar el usuario?");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Usuario user = Usuario.getUsuario();
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
            case R.id.btnMenosHeader:
                if(Integer.valueOf(txtHeader.getText().toString())>0){
                    ConfiguracionGeneralBL.UpdateTableHeaderSize(db,Integer.valueOf(txtHeader.getText().toString())-1);
                    txtHeader.setText(String.valueOf(ConfiguracionGeneralBL.GetTableHeaderSize(db)));
                }
                break;
            case R.id.btnMasHeader:
                if(Integer.valueOf(txtHeader.getText().toString())<20){
                    ConfiguracionGeneralBL.UpdateTableHeaderSize(db,Integer.valueOf(txtHeader.getText().toString())+1);
                    txtHeader.setText(String.valueOf(ConfiguracionGeneralBL.GetTableHeaderSize(db)));
                }
                break;
            case R.id.btnMenosTable:
                if(Integer.valueOf(txtTable.getText().toString())>0){
                    ConfiguracionGeneralBL.UpdateTableSize(db,Integer.valueOf(txtTable.getText().toString())-1);
                    txtTable.setText(String.valueOf(ConfiguracionGeneralBL.GetTableSize(db)));
                }
                break;
            case R.id.btnMasTable:
                if(Integer.valueOf(txtTable.getText().toString())<20){
                    ConfiguracionGeneralBL.UpdateTableSize(db,Integer.valueOf(txtTable.getText().toString())+1);
                    txtTable.setText(String.valueOf(ConfiguracionGeneralBL.GetTableSize(db)));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        Usuario user = Usuario.getUsuario();
                if(user.Nombre == null) {
                    intent = new Intent(this, MainActivity.class);
                }else
                {
                    intent = new Intent(this, HomeNavigate.class);
                }
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
