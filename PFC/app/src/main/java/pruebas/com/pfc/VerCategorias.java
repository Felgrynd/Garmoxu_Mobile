package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerCategorias extends AppCompatActivity {

    private EditText etBuscarCategorias;
    private LinearLayout llCategorias, llDynamic;
    private Button btnDynamic;

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
                    @Override
                    public void onResponse(JSONObject response){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 500);
                        params.weight = 1;
                        params.leftMargin = 5;
                        params.rightMargin = 5;
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                //Toast.makeText(getActivity(), jsonData.getJSONObject(i).getString("IdPedido")+jsonData.getJSONObject(i).getString("IdMesa"), Toast.LENGTH_SHORT).show();
                                if(i % 2 == 0){
                                    llDynamic = new LinearLayout(VerCategorias.this);
                                    llDynamic.setOrientation(LinearLayout.HORIZONTAL);
                                    llDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));
                                    llCategorias.addView(llDynamic);
                                }
                                btnDynamic = new Button(VerCategorias.this);
                                btnDynamic.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                btnDynamic.setId(Integer.parseInt(jsonData.getJSONObject(i).getString("IdCategoria")));
                                btnDynamic.setOnClickListener(dynamicOnClick(jsonData.getJSONObject(i).getString("IdCategoria")));
                                btnDynamic.setLayoutParams(params);

                                llDynamic.addView(btnDynamic);
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