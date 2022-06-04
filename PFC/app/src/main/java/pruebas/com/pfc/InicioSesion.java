package pruebas.com.pfc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Base64;

public class InicioSesion extends AppCompatActivity {

    private TextInputEditText etUser, etPass;
    private Button btnLogin;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        getSupportActionBar().hide();

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBarLoading progressBarLoading = new ProgressBarLoading(InicioSesion.this);
                progressBarLoading.startLoadingDialog();

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(InicioSesion.this); //R.style.AlertDialogTheme
                View v = LayoutInflater.from(InicioSesion.this).inflate(R.layout.dialog_load, findViewById(R.id.clDialogLoad));
                builder.setView(v);
                final AlertDialog alert = builder.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alert.show();
                */
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verificarUsuario();
                        progressBarLoading.dismissDialog();
                        //alert.dismiss();
                    }
                }, 2500);
            }
        });

    }

    private void verificarUsuario(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "verificar_user.php?NombreUsuario=" + etUser.getText().toString().trim() + "&Contraseña=" + getPassEncrypt(etPass.getText().toString().trim()),
                null,
                new Response.Listener<JSONObject>(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++) {
                                if (getPassEncrypt(etPass.getText().toString()).equals(jsonData.getJSONObject(i).getString("Contraseña"))) {
                                    Intent mainIntent = null;
                                    //mainIntent.putExtra("IdTipoUsuario", response.getString("IdTipoUsuario"));
                                    if (jsonData.getJSONObject(i).getString("RestablecerContraseña").equals("1")) {
                                        mainIntent = new Intent().setClass(InicioSesion.this, CambiarPasswd.class);
                                        mainIntent.putExtra("Contraseña", jsonData.getJSONObject(i).getString("Contraseña"));
                                        mainIntent.putExtra("NombreUsuario", jsonData.getJSONObject(i).getString("NombreUsuario"));
                                        startActivity(mainIntent);
                                    } else {
                                        mainIntent = new Intent().setClass(InicioSesion.this, MenuMain.class);
                                        mainIntent.putExtra("NombreEmpleado", jsonData.getJSONObject(i).getString("NombreEmpleado"));
                                        mainIntent.putExtra("NombreUsuario", jsonData.getJSONObject(i).getString("NombreUsuario"));
                                        byte[] byteArray = Base64.getDecoder().decode(jsonData.getJSONObject(i).getString("ImagenUsuario"));
                                        mainIntent.putExtra("ImagenUsuario", byteArray);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                } else
                                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(InicioSesion.this, "TryCatch: "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(InicioSesion.this, "onErrorResponse: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private String getPassEncrypt(String passwd){
        StringBuilder hexString = new StringBuilder();
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwd.getBytes("UTF-8"));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
        }catch(Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return hexString.toString();
    }

}