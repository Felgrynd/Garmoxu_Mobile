package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InicioSesion extends AppCompatActivity {

    boolean usuarioCorrecto;
    private EditText etUser, etPass;
    private Button btnLogin;

    private RequestQueue requestQueue;
    private final String urlDomain = "http://192.168.1.36/garmoxu/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarUsuario();
            }
        });

    }

    public void verificarUsuario(){
        String url = urlDomain + "verificar_user.php?NombreUsuario=" + etUser.getText().toString().trim() + "&Contraseña=" + etPass.getText().toString().trim();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        String txtId;

                        try {
                            if(getPassEncrypt().equals(response.getString("Contraseña"))){
                                Intent mainIntent = new Intent().setClass(InicioSesion.this, MenuMain.class);
                                startActivity(mainIntent);
                                finish();
                            }else Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(InicioSesion.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(InicioSesion.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

//>>falta implementar el codigo del codificacion
    private String getPassEncrypt(){

        return etPass.getText().toString();
    }

}