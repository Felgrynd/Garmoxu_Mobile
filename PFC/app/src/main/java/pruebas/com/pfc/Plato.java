package pruebas.com.pfc;

import android.graphics.Bitmap;

import java.text.DecimalFormat;

public class Plato {

    public String getIdPlato() {
        return idPlato;
    }

    public double getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Bitmap getBitmap(){ return bitmap; }

    public double getTotal(){
        return getDoubleTwoDecimalFormat((precio * cantidad));
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    private double getDoubleTwoDecimalFormat(double d){
        return Double.parseDouble(decimalFormat.format(d));
    }

    private String idPlato;
    private double precio;
    private String nombre;
    private int cantidad;
    private Bitmap bitmap;

    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public Plato(String idPlato, String nombre, int cantidad, double precio, Bitmap bitmap){
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.bitmap = bitmap;
    }

}
