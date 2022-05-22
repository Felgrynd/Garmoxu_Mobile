package pruebas.com.pfc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Plato> {

    private Context context;
    private int mResource;

    private Button btnClose, btnPlus, btnLess;
    private TextView tvTexto, tvNumb;
    private ImageView ivPedidoPlado;

    public CustomAdapter(Context context, int resource, ArrayList<Plato> objects) {
        super(context, resource,objects);
        this.context = context;
        this.mResource = resource;
    }

    public View getView(int pos, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);

        tvTexto = convertView.findViewById(R.id.tvText);
        tvNumb = convertView.findViewById(R.id.tvNumb);
        btnClose = convertView.findViewById(R.id.btnClose);
        btnPlus = convertView.findViewById(R.id.btnPlus);
        btnLess = convertView.findViewById(R.id.btnLess);
        ivPedidoPlado = convertView.findViewById(R.id.ivPedidoPlado);

        tvTexto.setText(getItem(pos).getIdPlato()+" - "+getItem(pos).getNombre());
        tvNumb.setText(getItem(pos).getCantidad()+"");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Se esta cerrando " + tvTexto.getText(), Toast.LENGTH_SHORT).show();
                DetallesPedidos.listaDePlatosDelPedido.remove(pos);
                DetallesPedidos.customAdapter.notifyDataSetChanged();
            }
        });

        btnPlus.setOnClickListener(cantidadPlusLess(pos, true));

        btnLess.setOnClickListener(cantidadPlusLess(pos, false));

        ivPedidoPlado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent().setClass(context, VerPlatos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idPlato", getItem(pos).getIdPlato());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private View.OnClickListener cantidadPlusLess(int pos, boolean operator){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(pos).setCantidad(setNumberSumarRestar(getItem(pos).getCantidad(), operator));
                DetallesPedidos.customAdapter.notifyDataSetChanged();
            }
        };
    }

    private int setNumberSumarRestar(int cantidad, boolean operator){
        if(operator) return (cantidad+1);
        else if(!operator && cantidad>1)return (cantidad-1);
        else return cantidad;
    }
}