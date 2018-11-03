package com.android.holamundo.app1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;
import com.android.holamundo.app1.Entities.Usuarios;
import com.android.holamundo.app1.Helper.Components;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  //  Button btnRegistrarse ;
    SQLiteDatabase db;
    DataBase b;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Usuario.Destroy();
       // btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        //btnRegistrarse.setOnClickListener(this);

        b = new DataBase(this, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
        db = b.getReadableDatabase();
        b.setConnection(b,db);
        List<Usuarios> listUsuarios = UsuariosBL.getAllUsuarios(db);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //Creo boton para registrar usuario nuevo
        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
         button = Components.CreateButton(this,-1,"Registrarse",Color.WHITE,Color.parseColor("#388E3C"),Gravity.CENTER,700,150);
       // button.getBackground().setAlpha(0);
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 15 );

        shape.setColor(Color.parseColor("#4CAF50"));
        button.setBackground(shape);
        linearLayout.addView(button);

        //Creo botones de usuarios
        for (Usuarios user:listUsuarios) {
            button = Components.CreateButton(this,user.Id,user.Nombre,Color.WHITE,Color.parseColor("#0D47A1"),Gravity.CENTER,700,150);
             shape =  new GradientDrawable();
            shape.setCornerRadius( 15 );

            shape.setColor(Color.parseColor("#3f51b5"));
            button.setBackground(shape);
            linearLayout.addView(button);


        }



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case -1:
            Intent intent = new Intent(this, Registrarse.class);
            startActivity(intent);
            finish();
            break;
            default:
                int id = Integer.valueOf(view.getId());

                Usuario user =  UsuariosBL.getUsuarioById(db,id);

                Intent intent2 = new Intent(this, Login.class);
                startActivity(intent2);
                finish();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
