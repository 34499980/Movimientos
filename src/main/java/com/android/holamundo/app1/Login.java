package com.android.holamundo.app1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.holamundo.app1.Entities.Usuario;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView txtNombre;
    EditText edtContraseña;
    Button btnVolver,btnLogIn;
    Usuario user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = Usuario.getUsuario();
        CargarControloes();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnVolver:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogIn:
                if(!edtContraseña.getText().toString().equals(user.Contraseña))
                {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta!", Toast.LENGTH_LONG).show();
                }else{
                    intent = new Intent(this,HomeNavigate.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void CargarControloes()
    {
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 10 );

        shape.setColor(Color.parseColor("#0277BD"));
        edtContraseña = (EditText) findViewById(R.id.edtContraseña_Home);
        txtNombre = (TextView) findViewById(R.id.txtNombre);

        txtNombre.setText(user.Nombre);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setVisibility(View.INVISIBLE);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        findViewById(R.id.btnLogIn).setBackground(shape);
        btnLogIn.setTextColor(Color.WHITE);
        btnLogIn.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }
}
