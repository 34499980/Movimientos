package com.android.holamundo.app1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Business.ConfiguracionGeneralBL;
import com.android.holamundo.app1.Business.UsuariosBL;

import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.HashMap;
import java.util.Map;


public class Registrarse extends AppCompatActivity implements  View.OnClickListener{
    //region controles
    TextView txtTitulo;
    EditText edtNombre;
    Button btnSiguiente,btnCrear,btnFinalizar;
    EditText edtContraseña, edtContraseña2;
    SQLiteDatabase db;
    DataBase b;
    GradientDrawable shape;
    boolean bCreado = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        shape =  new GradientDrawable();
        shape.setCornerRadius( 15 );
        shape.setColor(Color.parseColor("#3f51b5"));
        btnFinalizar.setTextColor(Color.WHITE);
        findViewById(R.id.btnFinalizar).setBackground(shape);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        shape =  new GradientDrawable();
        shape.setCornerRadius( 15 );
        shape.setColor(Color.parseColor("#FDD835"));
        btnCrear.setTextColor(Color.WHITE);
        findViewById(R.id.btnCrear).setBackground(shape);

        edtNombre = (EditText) findViewById(R.id.editNombre);
        txtTitulo = (TextView) findViewById(R.id.txtTitulos);

        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        shape =  new GradientDrawable();
        shape.setCornerRadius( 15 );
        shape.setColor(Color.parseColor("#4CAF50"));
        btnSiguiente.setTextColor(Color.WHITE);
        findViewById(R.id.btnSiguiente).setBackground(shape);

        edtContraseña = (EditText) findViewById(R.id.edtContraseña);
        edtContraseña2 = (EditText) findViewById(R.id.edtContraseña2);
        btnCrear.setOnClickListener(this);
        btnSiguiente.setOnClickListener(this);
        btnFinalizar.setOnClickListener(this);
        b = new DataBase(this, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
        db = b.getReadableDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        try {
            switch (view.getId()) {
                case R.id.btnCrear:
                    boolean espacio = edtNombre.getText().toString().contains(" ");
                    if(!espacio) {
                        if (!(edtNombre.getText().toString().isEmpty()) && !(edtContraseña.getText().toString().trim().isEmpty()) && !(edtContraseña2.getText().toString().trim().isEmpty())) {
                            if (edtContraseña.getText().toString().trim().equals(edtContraseña2.getText().toString().trim())) {
                                Map<String, Object> datos = new HashMap<String, Object>();

                                datos.put("Nombre", edtNombre.getText().toString().trim().toLowerCase());
                                datos.put("Contraseña", edtContraseña.getText().toString().trim());
                                if (UsuariosBL.InsertarUsuario(Constants.SQLQUERY.InsertarUsuario, datos, db, this) == true) {
                                    Toast.makeText(getApplicationContext(), "Usuario " + edtNombre.getText() + " creado", Toast.LENGTH_SHORT).show();
                                    bCreado = true;
                                    ConfiguracionGeneralBL.InsertarConfiguracionInicial(db);
                                    Usuario user = Usuario.getUsuario();
                                    user.Nombre =  edtNombre.getText().toString().trim().toLowerCase();
                                    user.Contraseña = edtContraseña.getText().toString().trim();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Debe ingresar un nombre de usuario y una contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El nombre no puede contener espacios.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSiguiente:
                    if (!(edtNombre.getText().toString().trim().isEmpty()) && !(edtContraseña.getText().toString().trim().isEmpty())) {
                        if(edtContraseña.getText().toString().trim().equals(edtContraseña2.getText().toString().trim())) {
                            if(bCreado) {
                                intent = new Intent(this, DatosPersonales.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Antes debe crear el usuario.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Debe ingresar un nombre de usuario y una contraseña", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnFinalizar:
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    finish();
                    break;

            }
        }catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Ha ocurrido el siguiente error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
