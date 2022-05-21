package pruebas.com.pfc.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import pruebas.com.pfc.DetallesPedidos;
import pruebas.com.pfc.MainActivity;
import pruebas.com.pfc.R;

public class HomeFragment extends Fragment {

    private EditText etBuscarMesaPropio;
    private LinearLayout llMesasPropias, llDynamic;
    private Button btnDynamic;

    private RequestQueue requestQueue;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        etBuscarMesaPropio = (EditText) v.findViewById(R.id.etBuscarMesaPropio);
        llMesasPropias = v.findViewById(R.id.llMesasPropias);

        requestQueue = Volley.newRequestQueue(getActivity());

        generarPedidosEnCurso();

        return v;
    }

    private void generarPedidosEnCurso(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.DOMAIN + "pedidos_local.php?NombreUsuario=" + getActivity().getIntent().getStringExtra("NombreUsuario")+"&TusPedidos=1",
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);
                        params.weight = 1;
                        params.leftMargin = 20;
                        params.rightMargin = 20;
                        try {
                            JSONArray jsonData = response.getJSONArray("data");
                            for (int i = 0; i<jsonData.length(); i++){
                                //Toast.makeText(getActivity(), jsonData.getJSONObject(i).getString("IdPedido")+jsonData.getJSONObject(i).getString("IdMesa"), Toast.LENGTH_SHORT).show();
                                if(i % 2 == 0){
                                    llDynamic = new LinearLayout(getActivity());
                                    llDynamic.setOrientation(LinearLayout.HORIZONTAL);
                                    llDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    llMesasPropias.addView(llDynamic);
                                }
                                btnDynamic = new Button(getActivity());
                                btnDynamic.setText(jsonData.getJSONObject(i).getString("IdMesa"));
                                btnDynamic.setId(Integer.parseInt(jsonData.getJSONObject(i).getString("IdMesa")));
                                btnDynamic.setTag(jsonData.getJSONObject(i).getString("IdPedido"));
                                //btnDynamic.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,200));
                                btnDynamic.setLayoutParams(params);
                                btnDynamic.setOnClickListener(dynamicOnClick());

                                llDynamic.addView(btnDynamic);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "onResponse: \n"+e.toString(), Toast.LENGTH_SHORT).show();
                            //etBuscarMesaPropio.setText(etBuscarMesaPropio.getText()+e.toString());
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                //Toast.makeText(getActivity(), "onErrorResponse: \n"+error.toString(), Toast.LENGTH_SHORT).show();
                //etBuscarMesaPropio.setText(etBuscarMesaPropio.getText()+error.toString());
                Toast.makeText(getActivity(), "No tienes pedidos asignados", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private View.OnClickListener dynamicOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), ""+view.getTag(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent().setClass(getActivity(), DetallesPedidos.class);
                intent.putExtra("idTag", view.getTag().toString()); //id del pedido
                intent.putExtra("idMesa", ""+view.getId());
                intent.putExtra("NombreUsuario", getActivity().getIntent().getStringExtra("NombreUsuario"));
                intent.putExtra("btnDer", "Guardar \nModificacion");
                intent.putExtra("btnIzq", "Borrar \nPedido");
                intent.putExtra("esNuevoPedido", false);
                intent.putExtra("tusPedidos", true);
                startActivity(intent);
            }
        };
    }

}