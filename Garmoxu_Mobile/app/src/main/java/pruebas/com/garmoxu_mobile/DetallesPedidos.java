package pruebas.com.garmoxu_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

import pruebas.com.garmoxu_mobile.Adaptador.Adaptador;

public class DetallesPedidos extends AppCompatActivity {

    private ListView lvPlatosPedidos;
    private Adaptador adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedidos);

        lvPlatosPedidos = (ListView) findViewById(R.id.lvPlatosPedidos);

        // RELLENADO Y CONFIGURACIÓN DEL ACTION BAR PERSONALIZADO

        // PRUEBAS PARA VER EL RELLENADO DEL LIST VIEW
        ArrayList<String> rellenadoListView = new ArrayList<>();

        rellenadoListView.add("prueba");

        adaptador = new Adaptador(getApplicationContext(), rellenadoListView);
        lvPlatosPedidos.setAdapter(adaptador);
    }
}