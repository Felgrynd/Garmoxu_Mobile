package pruebas.com.pfc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class CambiarPasswd extends AppCompatActivity {

    private EditText etActualPasswd, etNuevoPasswd, etRepetirPasswd;
    private Button btnCambiarPasswd;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_passwd);

        getSupportActionBar().hide();

        etActualPasswd = findViewById(R.id.etActualPasswd);
        etNuevoPasswd = findViewById(R.id.etNuevoPasswd);
        etRepetirPasswd = findViewById(R.id.etRepetirPasswd);
        btnCambiarPasswd = findViewById(R.id.btnCambiarPasswd);

        requestQueue = Volley.newRequestQueue(this);

        btnCambiarPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getPassEncrypt(etActualPasswd.getText().toString()).equals(getIntent().getStringExtra("Contraseña"))){
                    if(etNuevoPasswd.getText().toString().equals(etRepetirPasswd.getText().toString())){
                        cambiarPasswd();
                    }else
                        Toast.makeText(CambiarPasswd.this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(CambiarPasswd.this, "Contraseña Actual es incorrecta", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cambiarPasswd(){
        StringRequest stringRequest = new StringRequest(
            Request.Method.POST,
                MainActivity.DOMAIN +"cambiar_passwd.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(CambiarPasswd.this, "Se ha cambiado la contraseña correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CambiarPasswd.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                params.put("NombreUsuario", getIntent().getStringExtra("NombreUsuario"));
                params.put("Contraseña", getPassEncrypt(etNuevoPasswd.getText().toString()));
                params.put("RestablecerContraseña", "0");

                return params;
            }
        };
        requestQueue.add(stringRequest);
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
            Toast.makeText(this, "Error en la encriptacion: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return hexString.toString();
    }

}