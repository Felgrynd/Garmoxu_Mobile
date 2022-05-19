package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class InicioSesion extends AppCompatActivity implements View.OnClickListener {

    boolean activaVisualContra = false;
    boolean usuarioCorrecto;
    private EditText etUser, etPass;
    private Button btnLogin;
    private ImageView iconoVisibilidadPass;

    private RequestQueue requestQueue;
    private final String urlDomain = "http://192.168.1.36/garmoxu/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        iconoVisibilidadPass = findViewById(R.id.iconoVisibilidadPass);

        requestQueue = Volley.newRequestQueue(this);

        iconoVisibilidadPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btnLogin:
                verificarUsuario();
                break;


            case R.id.iconoVisibilidadPass:

                if(activaVisualContra == false){

                    iconoVisibilidadPass.setImageResource(R.drawable.iconocontrasenaocultaoscuro);
                    activaVisualContra = true;
                    //  SE ENSEÑA CONTRASEÑA

                }
                else {
                    iconoVisibilidadPass.setImageResource(R.drawable.iconocontrasenavisibleoscuro);
                    activaVisualContra = false;
                }
        }
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
                                Intent mainIntent = null;
                                if(response.getString("RestablecerContraseña").equals(1)){
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Añadir el codigo de ir a la activity de modificar contraseña
                                    Toast.makeText(InicioSesion.this, "Cambiar a la ventana de modificacion de la contraseña", Toast.LENGTH_SHORT).show();

                                }else mainIntent = new Intent().setClass(InicioSesion.this, MenuMain.class);

                                mainIntent.putExtra("NombreUsuario", response.getString("NombreUsuario"));
                                mainIntent.putExtra("NombreEmpleado", response.getString("NombreEmpleado"));
                                //mainIntent.putExtra("ImagenUsuario", response.getString("ImagenUsuario"));
                                mainIntent.putExtra("IdTipoUsuario", response.getString("IdTipoUsuario"));
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