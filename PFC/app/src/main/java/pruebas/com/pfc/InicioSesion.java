package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InicioSesion extends AppCompatActivity {

    private EditText etUser, etPass;
    private Button btnLogin, btnPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnPrueba = findViewById(R.id.btnPrueba);

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new Task().execute();
                Toast.makeText(InicioSesion.this, "a", Toast.LENGTH_SHORT).show();
                Connection connection = null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");

                    connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/garmoxu"//?useUnicode=true&" +
                            //"characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&"+ //JDBCCompliantTimezoneShift=true&useLegacyDateTimeCode=false&" +
                            //"serverTimezone=GMT"
                            ,"root", "root");

                }catch(Exception e){
                    etUser.setText(""+e.toString());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUser(etUser.getText().toString(), etPass.getText().toString())){
                    Intent mainIntent = new Intent().setClass(InicioSesion.this, MenuMain.class);
                    startActivity(mainIntent);
                    finish();
                }else
                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkUser(String user, String pass){

        //aqui añadimos el codigo de la cual verificara al bbdd si exite y coincide con la contraseña
        //se debe encriptar el user y el pass? es necesario un back-end?
        if(user.equalsIgnoreCase("admin")) return true;
        return false;
    }

}