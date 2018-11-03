package com.android.holamundo.app1.Entities;

public class Categoria {
    public int Id;
    public String Nombre;
    public long Imagen;
    public int Cant;
    private static Categoria categoria;

    private Categoria(){}

    public static Categoria getCategoria() {
        if(categoria==null){
            categoria = new Categoria();
        }
        return categoria;
    }
}
