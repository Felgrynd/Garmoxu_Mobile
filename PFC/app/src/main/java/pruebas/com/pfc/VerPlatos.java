package pruebas.com.pfc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Base64;

public class VerPlatos extends AppCompatActivity {

    private LinearLayout llDiponobilidad;
    private TextView tvIdPedido, tvNombre, tvPrecio, tvDescripcion, tvAlergenos;
    private ShapeableImageView ivImagenPlato;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_platos);

        llDiponobilidad = findViewById(R.id.llDiponobilidad);
        tvIdPedido = findViewById(R.id.tvIdPedido);
        tvNombre = findViewById(R.id.tvNombre);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        ivImagenPlato = findViewById(R.id.ivImagenPlato);
        tvAlergenos = findViewById(R.id.tvAlergenos);

        requestQueue = Volley.newRequestQueue(VerPlatos.this);

        generarDetallesPlatos();
    }

    private void generarDetallesPlatos() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                //MainActivity.DOMAIN + "categorias_platos.php?IdCategoria=0&EsOpc=3&IdPlatoComida=1",
                MainActivity.DOMAIN + "categorias_platos.php?IdCategoria=0&EsOpc=3&IdPlatoComida="+getIntent().getStringExtra("idPlato"),
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                if(jsonData.getJSONObject(i).getString("Disponibilidad").equals("0")) llDiponobilidad.setBackgroundColor(Color.RED);
                                tvIdPedido.setText(jsonData.getJSONObject(i).getString("IdPlatoComida"));
                                tvNombre.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                if(!jsonData.getJSONObject(i).getString("ImagenPlato").equals("")) {
                                    byte[] byteArray = Base64.getDecoder().decode(jsonData.getJSONObject(i).getString("ImagenPlato"));
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    ivImagenPlato.setImageBitmap(bitmap);
                                }
                                tvPrecio.setText(jsonData.getJSONObject(i).getString("PrecioConIVA"));
                                tvDescripcion.setText(jsonData.getJSONObject(i).getString("Descripcion"));
                                tvAlergenos.setText(jsonData.getJSONObject(i).getString("Alergenos"));
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

}