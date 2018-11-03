package com.android.holamundo.app1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.holamundo.app1.Business.UsuariosBL;
import com.android.holamundo.app1.ConstantsController.Constants;
import com.android.holamundo.app1.DataBase.DataBase;
import com.android.holamundo.app1.Entities.Usuario;

import java.util.HashMap;
import java.util.Map;

public class HomeUser extends Fragment{
 public View onCreaView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
     return inflater.inflate(R.layout.activity_listar_categorias,container,false);
 }

}
