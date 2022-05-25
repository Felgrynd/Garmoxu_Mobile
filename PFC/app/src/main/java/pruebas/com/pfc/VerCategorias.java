package pruebas.com.pfc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Base64;

public class VerCategorias extends AppCompatActivity {

    private EditText etBuscarCategorias;
    private LinearLayout llCategorias, llDynamic;
    //private ImageButton ibtnDynamic;
    private CardView cvDynamic;
    private TextView tvDynamic;
    private ImageView ivDynamic;

    private RequestQueue requestQueue;
    //private boolean esConsulta;

    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_categorias);

        etBuscarCategorias = findViewById(R.id.etBuscarCategoria);
        llCategorias = findViewById(R.id.llCategorias);
        //esConsulta = getIntent().getExtras().getBoolean("esConsulta");

        instance = this;
        requestQueue = Volley.newRequestQueue(this);

        generarCategorias();
    }

    private void generarCategorias(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "categorias_platos.php?IdCategoria=0&EsOpc=0&EsPlato=0&IdPlatoComida=0",
                null,
                new Response.Listener<JSONObject>(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 500);
                        params.weight = 1;
                        params.topMargin = 20;
                        params.leftMargin = 20;
                        params.rightMargin = 20;
                        params.bottomMargin = 20;

                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                //Toast.makeText(getActivity(), jsonData.getJSONObject(i).getString("IdPedido")+jsonData.getJSONObject(i).getString("IdMesa"), Toast.LENGTH_SHORT).show();
                                if(i % 2 == 0){
                                    llDynamic = new LinearLayout(VerCategorias.this);
                                    llDynamic.setOrientation(LinearLayout.HORIZONTAL);
                                    llDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    llDynamic.setGravity(Gravity.CENTER);
                                    llCategorias.addView(llDynamic);
                                }
                                cvDynamic = new CardView(VerCategorias.this);
                                ivDynamic = new ImageView(VerCategorias.this);
                                tvDynamic = new TextView(VerCategorias.this);

                                //btnDynamic.setId(i);
                                tvDynamic.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                tvDynamic.setTag(jsonData.getJSONObject(i).getString("IdCategoria"));
                                tvDynamic.setGravity(Gravity.CENTER);
                                tvDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                tvDynamic.setTextColor(Color.BLACK);
                                tvDynamic.setTypeface(null, Typeface.BOLD);
                                tvDynamic.setTextSize(24);

                                ivDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                if(jsonData.getJSONObject(i).getString("ImagenCategoria").equals(""))
                                    ivDynamic.setImageResource(R.drawable.noimage);
                                else {
                                    byte[] byteArray = Base64.getDecoder().decode(jsonData.getJSONObject(i).getString("ImagenCategoria"));
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    ivDynamic.setImageBitmap(bitmap);
                                }
                                ivDynamic.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                cvDynamic.setRadius(80);
                                cvDynamic.setLayoutParams(params);
                                cvDynamic.addView(ivDynamic);
                                cvDynamic.addView(tvDynamic);
                                tvDynamic.bringToFront();

                                tvDynamic.setOnClickListener(dynamicOnClick(jsonData.getJSONObject(i).getString("IdCategoria")));

                                llDynamic.addView(cvDynamic);
                            }
                        } catch (Exception e) {
                            Toast.makeText(VerCategorias.this, "onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                            etBuscarCategorias.setText(etBuscarCategorias.getText()+e.toString());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(VerCategorias.this, "onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                etBuscarCategorias.setText(etBuscarCategorias.getText()+error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private View.OnClickListener dynamicOnClick(String id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent().setClass(VerCategorias.this, PlatosDeCategorias.class);
                intent.putExtra("idCategoria", id);
                intent.putExtra("esConsulta", getIntent().getExtras().getBoolean("esConsulta"));
                intent.putExtra("verTodosPlatos", getIntent().getExtras().getBoolean("verTodosPlatos"));
                startActivity(intent);
                /*
                if(!getIntent().getExtras().getBoolean("esConsulta"))
                    finish();
                 */
            }
        };
    }
}