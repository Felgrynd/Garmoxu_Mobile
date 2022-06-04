package pruebas.com.pfc;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetallesPedidos extends AppCompatActivity {

    private Button btnAddPlato, btnCancelarPedido, btnConfirmarPedido;
    private TextView tvPrecioTotal;
    private ListView lvPedidosPlatos;
    private Spinner spEstado, spIdMesa;

    private RequestQueue requestQueue;
    private double precioTotal;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatTime;
    private Date date;
    private ArrayList<String> mesasDisponibles;
    private LocalDateTime ldt;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static ArrayList<Plato> listaDePlatosDelPedido = new ArrayList<>();
    public static CustomAdapter customAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O) //addSpinnerMesaDisponibles()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedidos);

        //etPrueba = findViewById(R.id.etPrueba);

        btnAddPlato = findViewById(R.id.btnAddPlato);
        btnCancelarPedido = findViewById(R.id.btnCancelarPedido);
        btnConfirmarPedido = findViewById(R.id.btnConfirmarPedido);
        tvPrecioTotal = findViewById(R.id.tvPrecioTotal);
        lvPedidosPlatos = findViewById(R.id.lvPedidosPlatos);
        spEstado = findViewById(R.id.spEstado);
        spIdMesa = findViewById(R.id.spIdMesa);

        listaDePlatosDelPedido.clear();

        requestQueue = Volley.newRequestQueue(DetallesPedidos.this);
        formatDate = new SimpleDateFormat("yyyy-MM-dd");
        formatTime = new SimpleDateFormat("HH:mm:ss");
        mesasDisponibles = new ArrayList<>();

        getMesasDisponibles();

        btnCancelarPedido.setText(getIntent().getStringExtra("btnIzq"));
        btnConfirmarPedido.setText(getIntent().getStringExtra("btnDer"));
        if(!getIntent().getExtras().getBoolean("tusPedidos")) btnCancelarPedido.setVisibility(View.GONE);

        date = new Date();
        if(getIntent().getExtras().getBoolean("esNuevoPedido")) {
            setIdPedidoMax();
            mesasDisponibles.add("");
        }else {
            generarPlatosDelPedido();
            mesasDisponibles.add(getIntent().getStringExtra("idMesa"));
        }
        addSpinnerEstadoPedido();
        addSpinnerMesaDisponibles();

        ArrayAdapter<String> arrayAdapterMesasDisponibles = new ArrayAdapter<String>(DetallesPedidos.this, android.R.layout.simple_spinner_item, mesasDisponibles);
        spIdMesa.setAdapter(arrayAdapterMesasDisponibles);

        customAdapter = new CustomAdapter(this, R.layout.activity_custom_adapter, listaDePlatosDelPedido);
        lvPedidosPlatos.setAdapter(customAdapter);

        btnAddPlato.setOnClickListener(buscarPlato());
        customAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                //super.onChanged();
                calcularTotalPedido();
            }
        });

        btnConfirmarPedido.setOnClickListener(platosDelPedido(getIntent().getStringExtra("btnDer")));
        btnCancelarPedido.setOnClickListener(btnLeft(getIntent().getStringExtra("btnIzq"), getIntent().getExtras().getBoolean("esNuevoPedido")));

        spEstado.setOnItemSelectedListener(setListenerSpinnerTextWhite());
        spIdMesa.setOnItemSelectedListener(setListenerSpinnerTextWhite());
    }

    private AdapterView.OnItemSelectedListener setListenerSpinnerTextWhite(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O) //DateTimeFormatter.ofPattern
    private boolean mesaDisponibleRango2h(LocalDateTime now, String fecha, String hora){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ldt = LocalDateTime.parse(fecha + " " + hora, dtf);

        long minutes = ChronoUnit.MINUTES.between(ldt, now);

        if(minutes >= 120) return true;
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O) //LocalDateTime.now()
    private void addSpinnerMesaDisponibles(){
        ldt = LocalDateTime.now();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "reservas_mesas.php?FechaActual=" + formatDate.format(date).toString() + "&FechaPlus=" + "2022-05-30",//formatDate.format(LocalDateTime.from(date.toInstant()).plusDays(1)).toString(), //falta añadir cosas la fecha de hoy 'formatDate.format(date).toString()' y la fecha siguiente 'LocalDateTime.from(date.toInstant()).plusDays(1);'
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                    if (mesasDisponibles.contains(jsonData.getJSONObject(i).getString("IdMesa")))
                                        if (!mesaDisponibleRango2h(ldt, jsonData.getJSONObject(i).getString("Fecha"), jsonData.getJSONObject(i).getString("Hora")))
                                            mesasDisponibles.remove(jsonData.getJSONObject(i).getString("IdMesa"));
                            }
                        } catch (Exception e) {
                            //etPrueba.setText(etPrueba.getText() + "\nException2: "+e.getMessage().toString());
                            Toast.makeText(DetallesPedidos.this, "addSpinnerMesasDisponibles - onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){ //si salta aqui es porque no hay mesas reservadas durante el dia de hoy y el proximo
                //etPrueba.setText(etPrueba.getText() + "\nVolleyError: "+error.getMessage().toString());
                //Toast.makeText(DetallesPedidos.this, "addSpinnerMesaDisponibles: Actualmente no hay mesas disponibles", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void setEstadoMesa(String estado, String idMesa){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                MainActivity.DOMAIN +"set_estado_mesas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetallesPedidos.this, "setEstadoMesa correctamente", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Este error esta comentado porqque hay veces que salta aunque este bien y esos es porque en update no se ha hecho ningun cambio ya que es igual tanto el nuevo como el viejo valor
                        //Toast.makeText(DetallesPedidos.this, "setEstadoMesa onErrorResponse:"+error.toString(), Toast.LENGTH_SHORT).show();
                        //etPrueba.setText(error.getMessage().toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                params.put("IdMesa", idMesa);
                params.put("Estado", estado); //porque esta linea falla

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private View.OnClickListener btnLeft(String s, boolean esNuevoPedido){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s.equals("Cancelar \nPedido") && esNuevoPedido) {
                    Toast.makeText(DetallesPedidos.this, "Se ha cancelado el pedido", Toast.LENGTH_SHORT).show();
                    finish();
                }else borrarPedidoExistente();
            }
        };
    }

    private void borrarPedidoExistente(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                MainActivity.DOMAIN +"delete_pedidos_platos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetallesPedidos.this, "borrarPedidoExistente: Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetallesPedidos.this, "borrarPedidoExistente onErrorResponse: "+error.toString(), Toast.LENGTH_SHORT).show();
                        //etPrueba.setText(error.getMessage().toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                params.put("IdPedido", getIntent().getStringExtra("idTag"));
                params.put("NombreUsuario", getIntent().getStringExtra("NombreUsuario"));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void modificarPedido(boolean esNuevoPedido){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                MainActivity.DOMAIN +"modificar_add_pedido.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetallesPedidos.this, "Se ha modificado/añadido el pedido correctamente", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetallesPedidos.this, "modificarPedido onErrorResponse: "+error.toString(), Toast.LENGTH_SHORT).show();
                        //etPrueba.setText(error.getMessage().toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                params.put("NombreUsuario", getIntent().getStringExtra("NombreUsuario"));
                params.put("Estado", spEstado.getSelectedItem().toString());
                params.put("PrecioConIVA", ""+precioTotal);
                params.put("PrecioSinIVA", ""+(precioTotal/1.1));
                params.put("IdMesa", spIdMesa.getSelectedItem().toString());
                if(esNuevoPedido){
                    params.put("EsNuevo", "1");
                    params.put("Fecha", formatDate.format(date).toString());
                    params.put("Hora", formatTime.format(date).toString());
                    params.put("IdPedido", ""+(Integer.parseInt(spIdMesa.getTag().toString())+1));
                }else{
                    params.put("IdPedido", getIntent().getStringExtra("idTag"));
                    params.put("EsNuevo", "0");
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private View.OnClickListener platosDelPedido(String s){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listaDePlatosDelPedido.size()>0 && spIdMesa.getAdapter().getCount()>0){
                    setEstadoMesa("Ocupado", spIdMesa.getSelectedItem().toString());
                    if(!spIdMesa.getSelectedItem().toString().equals(getIntent().getStringExtra("idMesa")) && !getIntent().getExtras().getBoolean("esNuevoPedido"))
                        setEstadoMesa("Libre", getIntent().getStringExtra("idMesa"));

                    if(s.equals("Crear \nPedido")) {
                        if (!spIdMesa.getSelectedItem().toString().equals(""))
                            modificarPedido(true);
                        else
                            Toast.makeText(DetallesPedidos.this, "Debe seleccionar una mesa", Toast.LENGTH_SHORT).show();
                    }else {modificarPedido(false);}

                    if(!getIntent().getExtras().getBoolean("esNuevoPedido")){
                        addAndDeletePlatosPedidos(true);
                    }
                    addAndDeletePlatosPedidos(false);
                    setResult(10);
                    finish();
                }else
                    Toast.makeText(DetallesPedidos.this, "No hay platos asignados a esta Mesa o No hay mesa asignada al pedido", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void addAndDeletePlatosPedidos(boolean b){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                MainActivity.DOMAIN +"detalles_platos_pedido.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetallesPedidos.this, "Se ha añadido los platos correctamente al pedido", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(DetallesPedidos.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(DetallesPedidos.this, "Error en el metodo addAndDeletePlatosPedidos()", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();

                if(b){
                    params.put("EsDelete", "1");
                    params.put("IdPedido", getIntent().getStringExtra("idTag"));
                }else {
                    String valores = "";
                    params.put("EsDelete", "0");
                    for (int i = 0; i < listaDePlatosDelPedido.size(); i++) {
                        if(getIntent().getExtras().getBoolean("esNuevoPedido")) valores += "(" + (Integer.parseInt(spIdMesa.getTag().toString())+1) + ", '" + listaDePlatosDelPedido.get(i).getIdPlato() + "', " + listaDePlatosDelPedido.get(i).getCantidad() + ")";
                        else valores += "(" + getIntent().getStringExtra("idTag") + ", '" + listaDePlatosDelPedido.get(i).getIdPlato() + "', " + listaDePlatosDelPedido.get(i).getCantidad() + ")";

                        if (i < listaDePlatosDelPedido.size() - 1)  valores += ", ";
                    }
                    params.put("Valores", valores);
                    //Toast.makeText(DetallesPedidos.this, ""+valores, Toast.LENGTH_SHORT).show();
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void addSpinnerEstadoPedido(){
        String[] pedidoEstado = {"En Proceso", "Esperando Pago"};
        ArrayAdapter<String> arrayAdapterPedidoEstado = new ArrayAdapter<String>(DetallesPedidos.this, android.R.layout.simple_spinner_item, pedidoEstado);
        spEstado.setAdapter(arrayAdapterPedidoEstado);
    }

    private void getMesasDisponibles(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "estado_mesas.php",
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                mesasDisponibles.add(jsonData.getJSONObject(i).getString("IdMesa"));
                            }
                            //spIdMesa.setAdapter(arrayAdapterMesasDisponibles);
                        } catch (Exception e) {
                            Toast.makeText(DetallesPedidos.this, "addSpinnerMesasDisponibles - onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //Toast.makeText(DetallesPedidos.this, "addSpinnerMesasDisponibles - onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(DetallesPedidos.this, "getMesasDisponibles: Actualemte no hay mesas disponibles", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void setIdPedidoMax(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "getMaxIdPedido.php",
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            spIdMesa.setTag(jsonData.getJSONObject(0).getString("IdPedidoMax"));
                        } catch (Exception e) {
                            Toast.makeText(DetallesPedidos.this, "setIdPedidoMax - onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(DetallesPedidos.this, "setIdPedidoMax - onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void calcularTotalPedido(){
        precioTotal = 0;
        for (Plato p: listaDePlatosDelPedido) {
            precioTotal += p.getTotal();
        }
        precioTotal = getDoubleTwoDecimalFormat(precioTotal);
        tvPrecioTotal.setText("Precio Total: "+precioTotal+"€");
    }

    private void generarPlatosDelPedido(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "pedidos_platos.php?IdPedido=" + getIntent().getStringExtra("idTag"),
                null,
                new Response.Listener<JSONObject>(){
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response){
                        Bitmap bitmap = null;
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                if(!jsonData.getJSONObject(i).getString("ImagenPlato").equals("")) {
                                    byte[] byteArray = Base64.getDecoder().decode(jsonData.getJSONObject(i).getString("ImagenPlato"));
                                    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                }else bitmap = null;
                                listaDePlatosDelPedido.add(new Plato(jsonData.getJSONObject(i).getString("IdPlatoComida"),jsonData.getJSONObject(i).getString("Nombre"),Integer.parseInt(jsonData.getJSONObject(i).getString("Cantidad")),getDoubleTwoDecimalFormat(jsonData.getJSONObject(i).getString("Precio")), bitmap));
                                //Toast.makeText(DetallesPedidos.this, ""+getDoubleTwoDecimalFormat(jsonData.getJSONObject(i).getString("Precio")), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DetallesPedidos.this, "generarPlatosDelPedido onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        customAdapter.notifyDataSetChanged();
                        calcularTotalPedido();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //Toast.makeText(DetallesPedidos.this, "onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(DetallesPedidos.this, "No hay platos asginados a este pedido", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private double getDoubleTwoDecimalFormat(String s){
        return Double.parseDouble(decimalFormat.format(Double.parseDouble(s)));
    }

    private double getDoubleTwoDecimalFormat(double d){
        return Double.parseDouble(decimalFormat.format(d));
    }

    private View.OnClickListener buscarPlato(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent().setClass(DetallesPedidos.this, VerCategorias.class);
                intent.putExtra("esConsulta", false);
                intent.putExtra("verTodosPlatos", false);
                startActivity(intent);

            }
        };
    }

}