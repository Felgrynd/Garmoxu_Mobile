package pruebas.com.garmoxu_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pruebas.com.garmoxu_mobile.R;
import pruebas.com.garmoxu_mobile.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private ListView lvMesas;
    private Button btnRecargar;

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

        btnRecargar = (Button) v.findViewById(R.id.btnRecargar);
        btnRecargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "ASd", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}