package pruebas.com.pfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //public final static String DOMAIN = "http://192.168.117.9/garmoxu/";
    public final static String DOMAIN = "https://www.garmoxu.es/garmoxu/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //fija en estatico la orientacion del activity
        getSupportActionBar().hide(); //quita la barra del titulo

        //Es un metodo de splash screen para controlar el tiempo de carga antes de inicar los codigos indicados
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //carga la nueva Activity y cerra esta Activity
                Intent mainIntent = new Intent().setClass(MainActivity.this, InicioSesion.class);
                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2500);

    }
}