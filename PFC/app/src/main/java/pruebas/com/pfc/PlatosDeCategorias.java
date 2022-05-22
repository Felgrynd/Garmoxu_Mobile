package pruebas.com.pfc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PlatosDeCategorias extends AppCompatActivity {

    private EditText etBuscarPlatosDeCategoria;
    private LinearLayout llPlatosDeCategorias, llDynamic;
    private Button btnDynamic;

    private RequestQueue requestQueue;
    private String urlDomain;
    //private String idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platos_de_categorias);

        etBuscarPlatosDeCategoria = findViewById(R.id.etBuscarPlatosDeCategoria);
        llPlatosDeCategorias = findViewById(R.id.llPlatosDeCategorias);
        //idCategoria = getIntent().getStringExtra("idCategoria");

        if(getIntent().getExtras().getBoolean("verTodosPlatos"))
            urlDomain = MainActivity.DOMAIN + "categorias_platos.php?IdCategoria=0&EsOpc=1&IdPlatoComida=0";
        else
            urlDomain = MainActivity.DOMAIN + "categorias_platos.php?IdCategoria="+getIntent().getStringExtra("idCategoria")+"&EsOpc=2&IdPlatoComida=0";

        requestQueue = Volley.newRequestQueue(PlatosDeCategorias.this);

        generarPlatosDeCategorias();
    }

    private void generarPlatosDeCategorias(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlDomain,
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
                                    llDynamic = new LinearLayout(PlatosDeCategorias.this);
                                    llDynamic.setOrientation(LinearLayout.HORIZONTAL);
                                    llDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));
                                    llPlatosDeCategorias.addView(llDynamic);
                                }
                                btnDynamic = new Button(PlatosDeCategorias.this);
                                btnDynamic.setId(i);
                                btnDynamic.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                btnDynamic.setTag(jsonData.getJSONObject(i).getString("IdPlatoComida"));
                                btnDynamic.setLayoutParams(params);
                                if(getIntent().getExtras().getBoolean("esConsulta"))
                                    btnDynamic.setOnClickListener(dynamicOnClickConsultarPlato(jsonData.getJSONObject(i).getString("IdPlatoComida")));
                                else
                                    btnDynamic.setOnClickListener(dynamicOnClickAddPlato(jsonData.getJSONObject(i).getString("IdPlatoComida"), jsonData.getJSONObject(i).getString("PrecioConIVA"), jsonData.getJSONObject(i).getString("Nombre")));

                                llDynamic.addView(btnDynamic);
                            }
                        } catch (Exception e) {
                            Toast.makeText(PlatosDeCategorias.this, "onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                            etBuscarPlatosDeCategoria.setText(etBuscarPlatosDeCategoria.getText()+e.toString());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(PlatosDeCategorias.this, "onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                etBuscarPlatosDeCategoria.setText(etBuscarPlatosDeCategoria.getText()+error.toString());
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private View.OnClickListener dynamicOnClickConsultarPlato(String idPlato){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PlatosDeCategorias.this, ""+idPlato, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent().setClass(PlatosDeCategorias.this, VerPlatos.class);
                intent.putExtra("idPlato", idPlato);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener dynamicOnClickAddPlato(String idPlato, String precio, String nombre){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DetallesPedidos.arrayList.add(new Plato(idPlato, precio, ));
                showAlertDialogCantidad(idPlato, precio, nombre);

            }
        };
    }

    private void showAlertDialogCantidad(String idPlato, String precio, String nombre){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlatosDeCategorias.this); //R.style.AlertDialogTheme
        View view = LayoutInflater.from(PlatosDeCategorias.this).inflate(R.layout.alert_dialog_cantidad_platos, findViewById(R.id.clAlertDialogCantidadContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.tvTitulo)).setText("Cantidad del plato");
        ((TextView) view.findViewById(R.id.tvMensaje)).setText("Indica una cantidad que quieras añadir");
        TextView tvCantidad = view.findViewById(R.id.tvCantidad);

        final AlertDialog alert = builder.create();

        ((Button) view.findViewById(R.id.btnMas)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCantidad.setText(""+(Integer.parseInt(tvCantidad.getText().toString())+1));
                //int total = ;
            }
        });
        ((Button) view.findViewById(R.id.btnMenos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvCantidad.getText().toString())>1)
                tvCantidad.setText(""+(Integer.parseInt(tvCantidad.getText().toString())-1));
            }
        });
        ((Button) view.findViewById(R.id.btnConfirmar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean contiene = false;
                try{
                    for(int i = 0; i<DetallesPedidos.listaDePlatosDelPedido.size(); i++){
                        if(DetallesPedidos.listaDePlatosDelPedido.get(i).getIdPlato().equals(idPlato)){
                            contiene = true;
                            DetallesPedidos.listaDePlatosDelPedido.get(i).setCantidad(DetallesPedidos.listaDePlatosDelPedido.get(i).getCantidad() + (Integer.parseInt(tvCantidad.getText().toString())));
                            Toast.makeText(PlatosDeCategorias.this, "Se ha añadido la siguiente cantidad: " + tvCantidad.getText() + ", al siguiente plato: " + nombre, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(contiene == false){
                        DetallesPedidos.listaDePlatosDelPedido.add(new Plato(idPlato, nombre, Integer.parseInt(tvCantidad.getText().toString()), Double.parseDouble(precio)));
                        Toast.makeText(PlatosDeCategorias.this, "Se ha añadido correctamente el siguiente plato: " + nombre + " (Cantidad: " + tvCantidad.getText() + ")", Toast.LENGTH_SHORT).show();
                    }
                    DetallesPedidos.customAdapter.notifyDataSetChanged();
                    alert.dismiss();

                    VerCategorias.instance.finish();
                    finish();
                }catch (Exception ex){
                    Toast.makeText(PlatosDeCategorias.this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        ((Button) view.findViewById(R.id.btnCancelar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        if(alert.getWindow() != null)
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        alert.show();
    }
}