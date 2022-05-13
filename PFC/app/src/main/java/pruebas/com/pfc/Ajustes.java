package pruebas.com.pfc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Ajustes extends Fragment {

    private Spinner spnIdiomas;
    private String idiomas[] = {"Castellano", "Inglés"};

    public Ajustes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spnIdiomas = (Spinner) getActivity().findViewById(R.id.spnIdiomas);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, idiomas);
        spnIdiomas.setAdapter(adaptador);
        spnIdiomas.setSelection(0, false);*/

        return inflater.inflate(R.layout.fragment_ajustes, container, false);
    }
}