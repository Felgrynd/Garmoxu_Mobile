package pruebas.com.garmoxu_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    private EditText etUser, etPass;
    private Button btnLogin;
    private ImageView iconoVisibilidadPass;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        iconoVisibilidadPass = findViewById(R.id.iconoVisibilidadPass);

        iconoVisibilidadPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }


    private boolean checkUser(String user, String pass) {

        //aqui añadimos el codigo de la cual verificara al bbdd si exite y coincide con la contraseña
        //se debe encriptar el user y el pass? es necesario un back-end?
        if (user.equalsIgnoreCase("admin")) return true;
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (checkUser(etUser.getText().toString(), etPass.getText().toString())) {
                    Intent mainIntent = new Intent().setClass(InicioSesion.this, MenuMain.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iconoVisibilidadPass:

                if (contador == 0) {
                    iconoVisibilidadPass.setImageResource(R.drawable.iconocontrasenavisible);
                    contador ++;
                } else {
                    iconoVisibilidadPass.setImageResource(R.drawable.iconocontrasenaoculta);
                    contador --;
                }
                break;
        }
    }
}