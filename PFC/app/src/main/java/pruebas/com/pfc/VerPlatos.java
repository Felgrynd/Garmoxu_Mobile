package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerPlatos extends AppCompatActivity {

    private LinearLayout llDiponobilidad, llAlergenos;
    private TextView tvIdPedido, tvNombre, tvPrecio, tvDescripcion;
    private ImageView ivImagenPlato;


    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_platos);

        llDiponobilidad = findViewById(R.id.llDiponobilidad);
        llAlergenos = findViewById(R.id.llAlergenos);
        tvIdPedido = findViewById(R.id.tvIdPedido);
        tvNombre = findViewById(R.id.tvNombre);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        ivImagenPlato = findViewById(R.id.ivImagenPlato);

        requestQueue = Volley.newRequestQueue(VerPlatos.this);

        generarDetallesPlatos();
    }

    private void generarDetallesPlatos() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "categorias_platos.php?IdCategoria=0&EsOpc=3&IdPlatoComida="+getIntent().getStringExtra("idPlato"),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                if(jsonData.getJSONObject(i).getString("Disponibilidad").equals("0")) llDiponobilidad.setBackgroundColor(Color.RED);
                                tvIdPedido.setText(jsonData.getJSONObject(i).getString("IdPlatoComida"));
                                tvNombre.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                tvPrecio.setText(jsonData.getJSONObject(i).getString("PrecioConIVA"));
                                tvDescripcion.setText(jsonData.getJSONObject(i).getString("Descripcion"));
                                generarEtiquetasAlergenos(jsonData.getJSONObject(i).getString("Alergenos"));
                            }
                        } catch (Exception e) {
                            Toast.makeText(VerPlatos.this, "onResponse: \n" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerPlatos.this, "onErrorResponse: \n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void generarEtiquetasAlergenos(String alergenos){

    }

}