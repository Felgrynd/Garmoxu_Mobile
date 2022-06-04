package pruebas.com.pfc.ui.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Base64;

import pruebas.com.pfc.InicioSesion;
import pruebas.com.pfc.MainActivity;
import pruebas.com.pfc.MenuMain;
import pruebas.com.pfc.PlatosDeCategorias;
import pruebas.com.pfc.R;
import pruebas.com.pfc.VerCategorias;
import pruebas.com.pfc.VerPlatos;
//import pruebas.com.pfc.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private Button btnBuscarIdPlato, btnCategorias, btnVerTodosPlatos, btnBuscarIdPlatoEt;
    private TextInputEditText etBuscarIdPlato;
    private TextInputLayout til;

    private RequestQueue requestQueue;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        btnBuscarIdPlato = v.findViewById(R.id.btnBuscarIdPlato);
        btnCategorias = v.findViewById(R.id.btnCategorias);
        btnVerTodosPlatos = v.findViewById(R.id.btnVerTodosPlatos);
        btnBuscarIdPlatoEt = v.findViewById(R.id.btnBuscarIdPlatoEt);
        etBuscarIdPlato = v.findViewById(R.id.etBuscarIdPlato);
        til = v.findViewById(R.id.til);

        btnBuscarIdPlato.setOnClickListener(buscarIdPlato());
        btnCategorias.setOnClickListener(intentCategorias());
        btnVerTodosPlatos.setOnClickListener(verTodosLosPlatos());
        btnBuscarIdPlatoEt.setOnClickListener(buscarIdPlatoEt());

        requestQueue = Volley.newRequestQueue(getActivity());

        return v;
    }

    private View.OnClickListener buscarIdPlatoEt(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        MainActivity.DOMAIN + "buscar_platoid.php?IdPlatoComida="+etBuscarIdPlato.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(JSONObject response) {
                                //Bitmap bitmap = null;
                                try {
                                    JSONArray jsonData = response.getJSONArray("data");
                                    for (int i = 0; i<jsonData.length(); i++){
                                        if(jsonData.getJSONObject(i).getString("IdPlatoComida").equals(etBuscarIdPlato.getText().toString())){
                                            Intent intent = new Intent().setClass(getActivity(), VerPlatos.class);
                                            intent.putExtra("idPlato", jsonData.getJSONObject(i).getString("IdPlatoComida"));
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getActivity(), "No se ha encontrado el plato", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "onResponse: \n" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "No Se ha encontrado el plato", Toast.LENGTH_SHORT).show();
                    }
                }
                );
                requestQueue.add(jsonObjectRequest);
            }
        };
    }

    private View.OnClickListener buscarIdPlato(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCategorias.getVisibility() == View.VISIBLE){
                    btnCategorias.setVisibility(View.GONE);
                    btnVerTodosPlatos.setVisibility(View.GONE);
                    btnBuscarIdPlatoEt.setVisibility(View.VISIBLE);
                    til.setVisibility(View.VISIBLE);
                }else{
                    btnCategorias.setVisibility(View.VISIBLE);
                    btnVerTodosPlatos.setVisibility(View.VISIBLE);
                    btnBuscarIdPlatoEt.setVisibility(View.GONE);
                    til.setVisibility(View.GONE);
                }
            }
        };
    }

    private View.OnClickListener intentCategorias(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(VerCategorias.class, false);
            }
        };
    }

    private View.OnClickListener verTodosLosPlatos(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(PlatosDeCategorias.class, true);
            }
        };
    }

    private void startIntent(Class c, boolean verTodosLosPlatos){
        Intent intent = new Intent().setClass(getActivity(), c); //VerCategorias.class
        intent.putExtra("esConsulta", true);
        intent.putExtra("verTodosPlatos", verTodosLosPlatos);

        startActivity(intent);
    }

}