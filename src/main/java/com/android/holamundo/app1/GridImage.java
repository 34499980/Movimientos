package com.android.holamundo.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GridImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_image);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ListarCategorias.class);
        startActivity(intent);
        finish();
    }
}
