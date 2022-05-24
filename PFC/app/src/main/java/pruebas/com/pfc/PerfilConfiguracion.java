package pruebas.com.pfc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilConfiguracion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilConfiguracion extends Fragment {

    private Spinner spnIdiomas;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilConfiguracion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilConfiguracion.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilConfiguracion newInstance(String param1, String param2) {
        PerfilConfiguracion fragment = new PerfilConfiguracion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil_configuracion, container, false);
        View v = inflater.inflate(R.layout.fragment_perfil_configuracion, container, false);

        spnIdiomas = v.findViewById(R.id.spnIdiomas);

        generarIdiomasSpinner();
        //construir un metodo para detectar el idioma actual?

        return v;
    }

    private void generarIdiomasSpinner(){
        final String[] idiomas = {"Español", "Ingles"};
        ArrayAdapter<String> arrayAdapterIdiomas = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, idiomas);
        spnIdiomas.setAdapter(arrayAdapterIdiomas);
    }
}