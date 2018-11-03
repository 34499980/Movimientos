package com.android.holamundo.app1.Entities;

public class Usuario {
    private static Usuario usuario;
    public int Id;
    public String Nombre;
    public String Mail;
    public String FechaNacimiento;
    public String Contraseña;


    public static Usuario getUsuario()
    {
        if (usuario == null)
        {
            usuario = new Usuario();
        }
        return  usuario;
    }
    public static void Destroy(){
        usuario = null;
    }

}
