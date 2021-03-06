package pruebas.com.pfc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class PlatosDeCategorias extends AppCompatActivity {

    //private EditText etBuscarPlatosDeCategoria;
    private LinearLayout llPlatosDeCategorias, llDynamicHorizontal;
    //private ImageButton ibtnDynamic;
    private CardView cvDynamic;
    private TextView tvDynamic;
    private ImageView ivDynamic;
    private RelativeLayout rlDynamic;

    private RequestQueue requestQueue;
    private String urlDomain;
    //private String idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platos_de_categorias);

        //etBuscarPlatosDeCategoria = findViewById(R.id.etBuscarPlatosDeCategoria);
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
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(450, 450);
                        //params.weight = 1;
                        params.topMargin = 20;
                        params.leftMargin = 20;
                        params.rightMargin = 20;
                        params.bottomMargin = 20;

                        Bitmap bitmap = null;
                        byte[] byteArray = null;

                        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        //rlParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        //para Centrarlo en botton
                        rlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                //Toast.makeText(getActivity(), jsonData.getJSONObject(i).getString("IdPedido")+jsonData.getJSONObject(i).getString("IdMesa"), Toast.LENGTH_SHORT).show();
                                if(i % 2 == 0){
                                    llDynamicHorizontal = new LinearLayout(PlatosDeCategorias.this);
                                    llDynamicHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                                    llDynamicHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    llDynamicHorizontal.setGravity(Gravity.CENTER);
                                    llPlatosDeCategorias.addView(llDynamicHorizontal);
                                }
                                cvDynamic = new CardView(PlatosDeCategorias.this);
                                ivDynamic = new ImageView(PlatosDeCategorias.this);
                                tvDynamic = new TextView(PlatosDeCategorias.this);
                                rlDynamic = new RelativeLayout(PlatosDeCategorias.this);

                                rlDynamic.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                                //btnDynamic.setId(i);
                                tvDynamic.setText(jsonData.getJSONObject(i).getString("Nombre"));
                                tvDynamic.setTag(jsonData.getJSONObject(i).getString("IdPlatoComida"));
                                //tvDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                tvDynamic.setLayoutParams(rlParams);
                                tvDynamic.setTextColor(Color.BLACK);
                                tvDynamic.setTypeface(null, Typeface.BOLD);
                                tvDynamic.setTextSize(15);
                                tvDynamic.setBackgroundResource(R.drawable.text_view_background_a);
                                tvDynamic.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                                ivDynamic.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                                if(jsonData.getJSONObject(i).getString("ImagenPlato").equals(""))
                                    ivDynamic.setImageResource(R.drawable.noimage);
                                else {
                                    byteArray = Base64.getDecoder().decode(jsonData.getJSONObject(i).getString("ImagenPlato"));
                                    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    ivDynamic.setImageBitmap(bitmap);
                                }
                                ivDynamic.setScaleType(ImageView.ScaleType.FIT_XY);

                                cvDynamic.setRadius(30);
                                cvDynamic.setLayoutParams(params);

                                rlDynamic.addView(ivDynamic);
                                rlDynamic.addView(tvDynamic);
                                cvDynamic.addView(rlDynamic);
                                tvDynamic.bringToFront();

                                //btnDynamic.setBackgroundResource(R.drawable.custom_button_a);
                                if(getIntent().getExtras().getBoolean("esConsulta"))
                                    tvDynamic.setOnClickListener(dynamicOnClickConsultarPlato(jsonData.getJSONObject(i).getString("IdPlatoComida")));
                                else if(!getIntent().getExtras().getBoolean("esConsulta") && jsonData.getJSONObject(i).getString("Disponibilidad").equals("0")){
                                    tvDynamic.setOnClickListener(noDisponible());
                                }
                                else
                                    tvDynamic.setOnClickListener(dynamicOnClickAddPlato(jsonData.getJSONObject(i).getString("IdPlatoComida"), jsonData.getJSONObject(i).getString("PrecioConIVA"), jsonData.getJSONObject(i).getString("Nombre"), bitmap));

                                llDynamicHorizontal.addView(cvDynamic);
                            }
                        } catch (Exception e) {
                            Toast.makeText(PlatosDeCategorias.this, "onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                            //etBuscarPlatosDeCategoria.setText("onResponse: \n"+etBuscarPlatosDeCategoria.getText()+e.toString());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //Toast.makeText(PlatosDeCategorias.this, "onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(PlatosDeCategorias.this, "No hay platos asignados a esta categoria", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private View.OnClickListener noDisponible(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlatosDeCategorias.this, "Plato No Disponible", Toast.LENGTH_SHORT).show();
            }
        };
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

    private View.OnClickListener dynamicOnClickAddPlato(String idPlato, String precio, String nombre, Bitmap imagenPlato){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DetallesPedidos.arrayList.add(new Plato(idPlato, precio, ));
                showAlertDialogCantidad(idPlato, precio, nombre, imagenPlato);
            }
        };
    }

    private void showAlertDialogCantidad(String idPlato, String precio, String nombre, Bitmap imagenPlato){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlatosDeCategorias.this); //R.style.AlertDialogTheme
        View view = LayoutInflater.from(PlatosDeCategorias.this).inflate(R.layout.alert_dialog_cantidad_platos, findViewById(R.id.clAlertDialogCantidadContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.tvTitulo)).setText("Cantidad del plato");
        ((TextView) view.findViewById(R.id.tvMensaje)).setText("Indica una cantidad que quieras a??adir");
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
                            Toast.makeText(PlatosDeCategorias.this, "Se ha a??adido la siguiente cantidad: " + tvCantidad.getText() + ", al siguiente plato: " + nombre, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(contiene == false){
                        DetallesPedidos.listaDePlatosDelPedido.add(new Plato(idPlato, nombre, Integer.parseInt(tvCantidad.getText().toString()), Double.parseDouble(precio), imagenPlato));
                        Toast.makeText(PlatosDeCategorias.this, "Se ha a??adido correctamente el siguiente plato: " + nombre + " (Cantidad: " + tvCantidad.getText() + ")", Toast.LENGTH_SHORT).show();
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