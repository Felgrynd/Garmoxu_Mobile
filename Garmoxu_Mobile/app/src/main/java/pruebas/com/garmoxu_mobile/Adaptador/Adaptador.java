package pruebas.com.garmoxu_mobile.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pruebas.com.garmoxu_mobile.R;

public class Adaptador extends BaseAdapter {

    private Context miContexto;
    private ArrayList<String> miArrayList;

    public Adaptador(Context miContexto, ArrayList<String> miArrayList) {
        this.miContexto = miContexto;
        this.miArrayList = miArrayList;
    }

    @Override
    public int getCount() {
        // Debe devolver el numero de elementos que va a tener el listView
        // return 0; viene por defecto
        return miArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // Devuelve un objeto, habrá que hacer casting si queremos
        // que por ejemplo nos devuelva un objeto de tipo persona
        return miArrayList.get(position);
        // Dame elobjeto de la posición i
    }

    @Override
    public long getItemId(int position) {
        // Devuelve el codigo id de un objeto por posición
        // return miArrayList.get(position).getCodigo();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Método para ver la presentación
        // Devuelve una VISTA

        // Ahora tenemos que hacer la carga del sistema o INFLARLO
        LayoutInflater layoutInflater = LayoutInflater.from(miContexto);

        // Vamos a inflar una vista
        // (Lo que queremos inflar,   grupo de vistas, suele ser null)
        view = layoutInflater.inflate(R.layout.marco_listviewpedidos, null);

        // Tenemos que poner el view antes del find porque es de este contexto, si no no lo pilla
        EditText codigoPedido = (EditText) view.findViewById(R.id.etCodigoPedido);
        EditText cantidadPedido = (EditText) view.findViewById(R.id.etCantidadPedidos);
        EditText nombrePedido = (EditText) view.findViewById(R.id.etNombrePedido);

        // Una vez declarado falta hacer que ahora cargue los datos
        codigoPedido.setText(miArrayList.get(i));
        cantidadPedido.setText(miArrayList.get(i));
        nombrePedido.setText(miArrayList.get(i));

        // Devolvemos la vista que ya está cargada con los datos que ya le hemos puesto
        return view;
    }
}