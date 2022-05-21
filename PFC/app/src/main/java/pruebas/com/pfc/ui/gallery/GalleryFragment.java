package pruebas.com.pfc.ui.gallery;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import pruebas.com.pfc.InicioSesion;
import pruebas.com.pfc.MenuMain;
import pruebas.com.pfc.PlatosDeCategorias;
import pruebas.com.pfc.R;
import pruebas.com.pfc.VerCategorias;
import pruebas.com.pfc.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private Button btnBuscarIdPlato, btnCategorias, btnVerTodosPlatos;

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

        btnBuscarIdPlato.setOnClickListener(buscarIdPlato());
        btnCategorias.setOnClickListener(intentCategorias());
        btnVerTodosPlatos.setOnClickListener(verTodosLosPlatos());

        return v;
    }

//Falta por hacer
    private View.OnClickListener buscarIdPlato(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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